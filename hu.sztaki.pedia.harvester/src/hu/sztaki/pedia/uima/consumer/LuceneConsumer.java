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
package hu.sztaki.pedia.uima.consumer;

import hu.sztaki.pedia.lucene.Indexer;
import hu.sztaki.pedia.lucene.LuceneIndexer;
import hu.sztaki.pedia.lucene.exceptions.NewerVersionStoredException;
import hu.sztaki.pedia.lucene.util.EngPosChainIndexerUtil;
import hu.sztaki.pedia.lucene.util.HunPosChainIndexerUtil;
import hu.sztaki.pedia.lucene.util.HunStemChainIndexerUtil;
import hu.sztaki.pedia.lucene.util.IChainIndexerUtil;
import hu.sztaki.pedia.uima.engines.AbstractMultiSofaAnnotator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.uima.SentenceAnnotation;
import org.apache.uima.SourceDocumentInformation;
import org.apache.uima.TokenAnnotation;
import org.apache.uima.UimaContext;
import org.apache.uima.WikiArticleAnnotation;
import org.apache.uima.WikiCategoryAnnotation;
import org.apache.uima.WikiLinkAnnotation;
import org.apache.uima.WikiTemplateAnnotation;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.text.AnnotationIndex;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;
import org.apache.uima.resource.ResourceInitializationException;

public class LuceneConsumer extends AbstractMultiSofaAnnotator {
	private Logger logger = Logger.getLogger(LuceneConsumer.class);
	private LuceneIndexer indexer;
	// private String chainID;
	public static long totalTokensProcessed = 0;

	public static final String LUCENE_INDEX_DIR_PARAM = "LuceneIndexDir";
	public static final String LANG_CHAIN_ID_PARAM = "LangChainID";
	public static final String SKIPPED_WORD_PARAM = "SkippedWordPlaceHolder";
	public static final String UNIMPORTANT_POS_PARAM = "UnimportantPOSTags";

	public static final String HUNPOSCHAIN_ID = "hun_pos";
	public static final String HUNSTEMCHAIN_ID = "hun_stem";
	public static final String ENGPOSCHAIN_ID = "eng_pos";

	@Override
	public void initialize(UimaContext aContext) throws ResourceInitializationException {
		super.initialize(aContext);
		String indexDir = (String) aContext.getConfigParameterValue(LUCENE_INDEX_DIR_PARAM);
		String chainID = (String) aContext.getConfigParameterValue(LANG_CHAIN_ID_PARAM);
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
			IChainIndexerUtil chainIndexerUtil;
			if (HUNPOSCHAIN_ID.startsWith(chainID)) {
				chainIndexerUtil = new HunPosChainIndexerUtil(skippedWordPLaceHolder);
			} else if (HUNSTEMCHAIN_ID.startsWith(chainID)) {
				chainIndexerUtil = new HunStemChainIndexerUtil();
			} else if (ENGPOSCHAIN_ID.startsWith(chainID)) {
				chainIndexerUtil = new EngPosChainIndexerUtil(skippedWordPLaceHolder,
						unimportantPOSTags);
			} else
				throw new ResourceInitializationException();
			indexer = LuceneIndexer.getInstance();
			if (!Indexer.isWriterInitialized()) {
				indexer.initialize(indexDir, chainIndexerUtil);
			}
		} catch (IOException e) {
			logger.error("Could not open " + indexDir, e);
			e.printStackTrace();
		}
	};

	@Override
	public void process(JCas aJCas) throws AnalysisEngineProcessException {
		ArrayList<JCas> casList = getJCasList(aJCas);
		try {
			for (JCas doc : casList) {
				// put all annotation indexes into a map
				Map<Integer, AnnotationIndex<Annotation>> annotationIndexMap = new HashMap<Integer, AnnotationIndex<Annotation>>();
				annotationIndexMap.put(WikiArticleAnnotation.type,
						doc.getAnnotationIndex(WikiArticleAnnotation.type));
				annotationIndexMap.put(SourceDocumentInformation.type,
						doc.getAnnotationIndex(SourceDocumentInformation.type));
				annotationIndexMap.put(TokenAnnotation.type,
						doc.getAnnotationIndex(TokenAnnotation.type));
				annotationIndexMap.put(SentenceAnnotation.type,
						doc.getAnnotationIndex(SentenceAnnotation.type));
				annotationIndexMap.put(WikiLinkAnnotation.type,
						doc.getAnnotationIndex(WikiLinkAnnotation.type));
				annotationIndexMap.put(WikiTemplateAnnotation.type,
						doc.getAnnotationIndex(WikiTemplateAnnotation.type));
				annotationIndexMap.put(WikiCategoryAnnotation.type,
						doc.getAnnotationIndex(WikiCategoryAnnotation.type));
				// store the annotations in indexes, or exit if newer version
				// already stored
				indexer.indexAnnotations(annotationIndexMap);
			}
		} catch (NewerVersionStoredException e) {
			logger.info(e.getMessage());
			aJCas.release();
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
