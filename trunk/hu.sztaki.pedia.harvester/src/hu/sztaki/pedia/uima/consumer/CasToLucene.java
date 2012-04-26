/*******************************************************************************
 * Copyright 2012 Tamas Farkas, MTA SZTAKI, Hungary
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 *******************************************************************************/
/**
 * 
 */
package hu.sztaki.pedia.uima.consumer;

import hu.sztaki.pedia.lucene.Indexer;
import hu.sztaki.pedia.lucene.util.EngPosChainIndexerUtil;
import hu.sztaki.pedia.lucene.util.HunPosChainIndexerUtil;
import hu.sztaki.pedia.lucene.util.HunStemChainIndexerUtil;
import hu.sztaki.pedia.lucene.util.IChainIndexerUtil;
import hu.sztaki.pedia.uima.engines.AbstractMultiSofaAnnotator;
import hu.sztaki.pedia.uima.util.LuceneUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.uima.Link;
import org.apache.uima.SentenceAnnotation;
import org.apache.uima.SourceDocumentInformation;
import org.apache.uima.TokenAnnotation;
import org.apache.uima.UimaContext;
import org.apache.uima.WikiTemplate;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.text.AnnotationIndex;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;
import org.apache.uima.resource.ResourceInitializationException;

/**
 * @author tfarkas
 * 
 */
public class CasToLucene extends AbstractMultiSofaAnnotator {
	// public static final String DOCUMENTINFO_SOFA_NAME_PARAM =
	// "DocumentSofaName";

	public static long totalTokensProcessed = 0;

	private Indexer indexer;
	private IChainIndexerUtil chainIndexerUtil;
	private String chainID;

	// private String docinfoSofaName;

	public static final String LUCENE_INDEX_DIR_PARAM = "LuceneIndexDir";
	public static final String LANG_CHAIN_ID_PARAM = "LangChainID";
	public static final String SKIPPED_WORD_PARAM = "SkippedWordPlaceHolder";
	public static final String UNIMPORTANT_POS_PARAM = "UnimportantPOSTags";

	public static final String HUNPOSCHAIN_ID = "hun_pos";
	public static final String HUNSTEMCHAIN_ID = "hun_stem";
	public static final String ENGPOSCHAIN_ID = "eng_pos";

	private Logger logger = Logger.getLogger(CasToLucene.class.getSimpleName());

	@Override
	public void initialize(UimaContext aContext) throws ResourceInitializationException {
		super.initialize(aContext);
		String indexDir = "";
		indexDir = (String) aContext.getConfigParameterValue(LUCENE_INDEX_DIR_PARAM);
		chainID = (String) aContext.getConfigParameterValue(LANG_CHAIN_ID_PARAM);
		String skippedWordPLaceHolder = (String) aContext
				.getConfigParameterValue(SKIPPED_WORD_PARAM);

		String[] unimportantPOSTags = (String[]) aContext
				.getConfigParameterValue(UNIMPORTANT_POS_PARAM);
		// docinfoSofaName = (String) aContext
		// .getConfigParameterValue(DOCUMENTINFO_SOFA_NAME_PARAM);
		if (chainID == null) {
			throw new ResourceInitializationException(
					ResourceInitializationException.CONFIG_SETTING_ABSENT, null);
		}
		try {
			if (HUNPOSCHAIN_ID.startsWith(chainID)) {
				chainIndexerUtil = new HunPosChainIndexerUtil(skippedWordPLaceHolder);
			} else if (HUNSTEMCHAIN_ID.startsWith(chainID)) {
				chainIndexerUtil = new HunStemChainIndexerUtil();
			} else if (ENGPOSCHAIN_ID.startsWith(chainID)) {
				chainIndexerUtil = new EngPosChainIndexerUtil(skippedWordPLaceHolder,
						unimportantPOSTags);
			} else
				throw new ResourceInitializationException();
			IndexWriterConfig iwc = chainIndexerUtil.getIndexWriterConfig();
			indexer = new Indexer(indexDir, iwc);
		} catch (IOException e) {
			logger.error("Couldnt open " + indexDir, e);
			e.printStackTrace();
		}
	};

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.apache.uima.collection.base_cpm.CasObjectProcessor#processCas(org
	 * .apache.uima.cas.CAS)
	 */
	@Override
	public void process(JCas jCas) throws AnalysisEngineProcessException {
		SourceDocumentInformation sdocInfo = null;
		// try {
		// JCas docInfo = jCas.getView(docinfoSofaName);
		// AnnotationIndex<Annotation> sdiIndex =
		// jCas.getAnnotationIndex(SourceDocumentInformation.type);
		// for (Annotation annotation : sdiIndex) {
		// sdocInfo = (SourceDocumentInformation) annotation;
		// }
		// } catch (CASException e1) {
		// // TODO Auto-generated catch block
		// e1.printStackTrace();
		// }

		ArrayList<JCas> casList = getJCasList(jCas);
		for (JCas doc : casList) {

			AnnotationIndex<Annotation> sdiIndex = doc
					.getAnnotationIndex(SourceDocumentInformation.type);
			for (Annotation annotation : sdiIndex) {
				sdocInfo = (SourceDocumentInformation) annotation;
			}

			AnnotationIndex<Annotation> sentenceIndex = doc
					.getAnnotationIndex(SentenceAnnotation.type);

			long tokensProcessed = 0;
			AnnotationIndex<Annotation> tokenIndex = doc.getAnnotationIndex(TokenAnnotation.type);
			for (Annotation annotation : tokenIndex) {
				tokensProcessed++;
			}
			logger.info("# of tokens processed:" + tokensProcessed);
			totalTokensProcessed += tokensProcessed;
			Set<String> uniqLemmas = chainIndexerUtil.getUniqLemmas(tokenIndex);
			List<String> sentencesList = chainIndexerUtil.getSentences(sentenceIndex, tokenIndex);

			AnnotationIndex<Annotation> templateIndex = doc.getAnnotationIndex(WikiTemplate.type);
			AnnotationIndex<Annotation> linkIndex = doc.getAnnotationIndex(Link.type);

			// System.out.println("Doc: "+getIdFromURI(sdocInfo.getUri()));
			Set<String> linkList = new HashSet<String>();
			for (Annotation annotation : linkIndex) {
				Link link = (Link) annotation;
				// System.out.println("Link name: "+link.getHref());
				linkList.add(link.getHref());
			}
			Set<String> templateList = new HashSet<String>();
			for (Annotation annotation : templateIndex) {
				WikiTemplate tmpl = (WikiTemplate) annotation;
				// System.out.println("Template name: "+tmpl.getName());
				templateList.add(tmpl.getName());
			}

			try {

				indexer.index(LuceneUtil.getIdFromURI(sdocInfo.getUri()), sentencesList,
						uniqLemmas, linkList, templateList);
			} catch (CorruptIndexException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				logger.error("Couldnt save to indexes doc:" + sdocInfo.getUri(), e);
				e.printStackTrace();
			}
		}

	}

	@Override
	public void destroy() {
		try {
			logger.info("CasToLucene total # of tokens processed:" + totalTokensProcessed);
			logger.info("destroy");
			indexer.close();
		} catch (IOException e) {
			logger.error("Couldnt save indexes", e);
		}
	}

	@Override
	public void batchProcessComplete() throws AnalysisEngineProcessException {
		logger.info("batchProcessComplete");
		try {
			indexer.commit();
		} catch (CorruptIndexException e) {
			logger.error("batchProcessComplete commit failed", e);
		} catch (IOException e) {
			logger.error("batchProcessComplete commit failed", e);
		}
	}

	@Override
	public void collectionProcessComplete() throws AnalysisEngineProcessException {
		logger.info("collectionProcessComplete");
		try {
			indexer.commit();
		} catch (CorruptIndexException e) {
			logger.error("collectionProcessComplete commit failed", e);
		} catch (IOException e) {
			logger.error("collectionProcessComplete commit failed", e);
		}

	}

}
