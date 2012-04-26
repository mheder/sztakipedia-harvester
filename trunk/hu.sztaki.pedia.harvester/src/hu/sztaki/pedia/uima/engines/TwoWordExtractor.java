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

import hu.sztaki.pedia.uima.util.MSDCategory;

import java.util.ArrayList;

import org.apache.uima.SentenceAnnotation;
import org.apache.uima.TokenAnnotation;
import org.apache.uima.TwoWordAnnotation;
import org.apache.uima.UimaContext;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.FSIterator;
import org.apache.uima.cas.text.AnnotationIndex;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;
import org.apache.uima.resource.ResourceInitializationException;

public class TwoWordExtractor extends AbstractMultiSofaAnnotator {
	
	@Override
	public void initialize(UimaContext aContext)
			throws ResourceInitializationException {
		super.initialize(aContext);
	}

	private boolean isImportantWordByCategory(String posTag) {
		boolean result;
		try {
			MSDCategory category = null;
			for (MSDCategory msdcat : MSDCategory.values()) {
				if (posTag.startsWith(msdcat.abbreviation())) {
					category = msdcat;
				}
			}
			switch (category) {
			case NOUN:
			case VERB:
			case ADVERB:
			case ADJECTIVE:
			case RESIDUAL:
				result = true;
				break;
			default:
				result = false;
				break;
			}
		} catch (Exception e) {
			result = false;
		}
		return result;
	}

	@Override
	public void process(JCas jCas) throws AnalysisEngineProcessException {
		ArrayList<JCas> casList = getJCasList(jCas);

		for (JCas doc : casList) {
			AnnotationIndex<Annotation> sentenceIndex = null;
			sentenceIndex = doc.getAnnotationIndex(SentenceAnnotation.type);

			FSIterator<Annotation> sentenceIterator = sentenceIndex.iterator();

			AnnotationIndex<Annotation> tokenIndex = doc
					.getAnnotationIndex(TokenAnnotation.type);

			FSIterator<Annotation> tokenIterator = null;

			SentenceAnnotation sentence = null;
			TokenAnnotation token1, token2 = null;

			while (sentenceIterator.hasNext()) {
				sentence = (SentenceAnnotation) sentenceIterator.next();
				// csak a magyar mondatokra fusson le az elemzes
				if ("hu".equals(sentence.getLang())) {
					tokenIterator = tokenIndex.subiterator(sentence);
					if (tokenIterator.hasNext()) {
						token1 = (TokenAnnotation) tokenIterator.next();
					} else {
						continue;
					}
					if (tokenIterator.hasNext()) {
						token2 = (TokenAnnotation) tokenIterator.next();
					} else {
						continue;
					}

					while (tokenIterator.hasNext()) {
						if (isImportantWordByCategory(token1.getPosTag())
								&& isImportantWordByCategory(token2.getPosTag())) {
							TwoWordAnnotation tw = new TwoWordAnnotation(doc);
							tw.setBegin(token1.getBegin());
							tw.setEnd(token2.getEnd());
							// System.out.println("TW:"+tw.getCoveredText());
							tw.addToIndexes();
						}
						token1 = token2;
						token2 = (TokenAnnotation) tokenIterator.next();
					}
				}
			}
		}
	}

}
