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
package hu.sztaki.pedia.uima.engines;

import hu.sztaki.pedia.lucene.IndexFieldNames;
import hu.sztaki.pedia.lucene.Searcher;
import hu.sztaki.pedia.lucene.util.EngPosChainIndexerUtil;
import hu.sztaki.pedia.lucene.util.HunPosChainIndexerUtil;
import hu.sztaki.pedia.lucene.util.HunStemChainIndexerUtil;
import hu.sztaki.pedia.lucene.util.IChainIndexerUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.document.Document;
import org.apache.uima.SentenceAnnotation;
import org.apache.uima.SourceDocumentInformation;
import org.apache.uima.TokenAnnotation;
import org.apache.uima.UimaContext;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.text.AnnotationIndex;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;
import org.apache.uima.resource.ResourceInitializationException;

public class CategoryFromMLTEngine extends AbstractMultiSofaAnnotator {
	public static final String LUCENE_INDEX_DIR_PARAM = "LuceneIndexDir";
	public static final String LANG_CHAIN_ID_PARAM = "LangChainID";
	public static final String MLT_MIN_TERM_FREQ_PARAM = "MLTMinTermFreq";
	public static final String MLT_MIN_WORD_LEN_PARAM = "MLTMinWordLength";
	public static final String SKIPPED_WORD_PARAM = "SkippedWordPlaceHolder";
	public static final String UNIMPORTANT_POS_PARAM = "UnimportantPOSTags";

	public static final String HUNPOSCHAIN_ID = "hun_pos";
	public static final String HUNSTEMCHAIN_ID = "hun_stem";
	public static final String ENGPOSCHAIN_ID = "eng_pos";

	private IChainIndexerUtil indexerUtil;
	private Searcher searcher;

	@Override
	public void initialize(UimaContext aContext) throws ResourceInitializationException {
		super.initialize(aContext);
		String utilName = (String) aContext.getConfigParameterValue(LANG_CHAIN_ID_PARAM);
		String indexDir = (String) aContext.getConfigParameterValue(LUCENE_INDEX_DIR_PARAM);
		String skippedWordPLaceHolder = (String) aContext
				.getConfigParameterValue(SKIPPED_WORD_PARAM);
		String[] unimportantPOSTags = (String[]) aContext
				.getConfigParameterValue(UNIMPORTANT_POS_PARAM);
		Integer minTermFreq = (Integer) aContext.getConfigParameterValue(MLT_MIN_TERM_FREQ_PARAM);
		Integer minWordLen = (Integer) aContext.getConfigParameterValue(MLT_MIN_WORD_LEN_PARAM);
		if (HUNPOSCHAIN_ID.startsWith(utilName)) {
			indexerUtil = new HunPosChainIndexerUtil(skippedWordPLaceHolder);
		} else if (HUNSTEMCHAIN_ID.startsWith(utilName)) {
			indexerUtil = new HunStemChainIndexerUtil();
		} else if (ENGPOSCHAIN_ID.startsWith(utilName)) {
			indexerUtil = new EngPosChainIndexerUtil(skippedWordPLaceHolder, unimportantPOSTags);
		} else
			throw new RuntimeException("Invalid indexer util name!");
		try {
			searcher = new Searcher(indexDir, indexerUtil.getSentenceAnalyzer(),
					minTermFreq.intValue(), minWordLen.intValue());
		} catch (IOException e) {
			throw new ResourceInitializationException(e.getMessage(), null);
		}
	}

	@Override
	public void process(JCas aJCas) throws AnalysisEngineProcessException {
		ArrayList<JCas> casList = getJCasList(aJCas);
		SourceDocumentInformation sdocInfo = null;
		for (JCas doc : casList) {

			AnnotationIndex<Annotation> sdiIndex = doc
					.getAnnotationIndex(SourceDocumentInformation.type);
			for (Annotation annotation : sdiIndex) {
				sdocInfo = (SourceDocumentInformation) annotation;
			}
			// String articleId = LuceneUtil.getIdFromURI(sdocInfo.getUri());
			AnnotationIndex<Annotation> sentenceIndex = doc
					.getAnnotationIndex(SentenceAnnotation.type);

			AnnotationIndex<Annotation> tokenIndex = doc.getAnnotationIndex(TokenAnnotation.type);

			List<String> sentencesList = indexerUtil.getSentences(sentenceIndex, tokenIndex);
			StringBuilder sb = new StringBuilder();
			for (String sentence : sentencesList) {
				// System.out.println("Sentence:"+sentence+": ");
				sb.append(sentence);
			}
			List<Document> docs;
			try {
				System.out.println("Article:" + sb.toString());
				docs = searcher.searchMLTDocuments(sb.toString(),
						IndexFieldNames.SENTENCES_FIELD_NAME, 5);
				for (Document document : docs) {
					// if
					// (articleId.equals(document.get(Indexer.ID_FIELD_NAME))){
					// continue;
					// }
					System.out.println(document.get(IndexFieldNames.ID_FIELD_NAME));
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
