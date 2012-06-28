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

import hu.sztaki.pedia.lucene.Searcher;
import hu.sztaki.pedia.lucene.util.IndexerHelperUtil;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.apache.lucene.analysis.KeywordAnalyzer;
import org.apache.uima.UimaContext;
import org.apache.uima.WikiArticleAnnotation;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;

public class ArticleRevisionCASFilter extends AbstractMultiSofaAnnotator {
	public static Logger logger = Logger.getLogger(ArticleRevisionCASFilter.class);
	public static final String LUCENE_INDEX_DIR_PARAM = "LuceneIndexDir";
	private Searcher searcher = null;
	private String indexDir;

	@Override
	public void initialize(UimaContext aContext) throws ResourceInitializationException {
		super.initialize(aContext);
		indexDir = (String) aContext.getConfigParameterValue(LUCENE_INDEX_DIR_PARAM);

	}

	@Override
	public void process(JCas aJCas) throws AnalysisEngineProcessException {
		// initialize the searcher here, because index has to be initialized by
		// the writer first
		if (searcher == null) {
			try {
				searcher = new Searcher(indexDir, new KeywordAnalyzer());
			} catch (IOException e) {
				logger.error("Searcher could not been initialized!", e);
				throw new AnalysisEngineProcessException(e);
			}
		}
		ArrayList<JCas> casList = getJCasList(aJCas);
		boolean isAlreadyStored = false;

		for (JCas doc : casList) {
			WikiArticleAnnotation articleAnnotation = (WikiArticleAnnotation) doc
					.getAnnotationIndex(WikiArticleAnnotation.type).iterator().get();
			if (IndexerHelperUtil.isNewerVersionStored(articleAnnotation, searcher)) {
				isAlreadyStored = true;
				logger.info("Article " + articleAnnotation.getTitle() + " (ID:"
						+ articleAnnotation.getId() + ",Rev:" + articleAnnotation.getRevision()
						+ ") filtered before AE processing, already stored.");
			}
		}

		if (isAlreadyStored) {
			aJCas.release();
		}

	}
}
