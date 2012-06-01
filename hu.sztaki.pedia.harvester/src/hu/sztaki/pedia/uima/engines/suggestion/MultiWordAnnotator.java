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
package hu.sztaki.pedia.uima.engines.suggestion;

import hu.sztaki.pedia.uima.engines.AbstractMultiSofaAnnotator;
import hu.sztaki.pedia.uima.util.PennTags;

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

public class MultiWordAnnotator extends AbstractMultiSofaAnnotator {
	public static String TAGSET_PARAM_NAME = "TagSetName";

	private String tagSetName;

	@Override
	public void initialize(UimaContext aContext) throws ResourceInitializationException {
		super.initialize(aContext);
		tagSetName = (String) aContext.getConfigParameterValue(TAGSET_PARAM_NAME);
	}

	private boolean byJustesonKatzFilter(String posTag1, String posTag2) {
		boolean toReturn = false;
		String pos1, pos2;
		if (tagSetName.equals("MSD")) {
			pos1 = posTag1.substring(0, 1);
			pos2 = posTag2.substring(0, 1);
		} else if (tagSetName.equals("Penn")) {
			pos1 = pennToMSDAbbrev(posTag1);
			pos2 = pennToMSDAbbrev(posTag2);
		} else {
			pos1 = "";
			pos2 = "";
		}
		if (pos1.equals("A") && pos2.equals("N")) {
			toReturn = true;
		}
		if (pos1.equals("N") && pos2.equals("N")) {
			toReturn = true;
		}
		return toReturn;
	}

	private String pennToMSDAbbrev(String posTag) {
		String toReturn = "X";
		PennTags category = null;
		for (PennTags pennTag : PennTags.values()) {
			if (posTag.startsWith(pennTag.abbreviation())) {
				category = pennTag;
			}
		}
		if (category != null) {
			switch (category) {
			case ARTICLE:
				toReturn = "T";
				break;
			case BE:
			case WERE:
			case WAS:
			case BEING:
			case AM:
			case BEEN:
			case ARE_ART:
			case IS:
			case DO:
			case DID:
			case DOES:
			case VERB_BASE_FORM:
			case VERB_PAST_TENSE:
			case VERB_PRESENT_PARTICIPLE_GERUND:
			case VERB_PAST_PARTICIPLE:
			case VERB_3RD_SINGULAR_PRESENT:
				toReturn = "V";
				break;
			case SINGULAR_OR_MASS_NOUN:
			case POSSESSIVE_SINGULAR_NOUN:
			case PLURAL_NOUN:
			case POSSESSIVE_PLURAL_NOUN:
			case PROPER_NOUN_OR_PART_OF_NAME_PHRASE:
			case POSSESSIVE_PROPER_NOUN:
			case PLURAL_PROPER_NOUN:
			case POSSESSIVE_PLURAL_PROPER_NOUN:
			case ADVERBIAL_NOUN:
			case PLURAL_ADVERBIAL_NOUN:
				toReturn = "N";
				break;
			case ADJECTIVE:
			case COMPARATIVE_ADJECTIVE:
			case SEMANTICALLY_SUPERLATIVE_ADJECTIVE:
			case MORPHOLOGICALLY_SUPERLATIVE_ADJECTIVE:
				toReturn = "A";
				break;
			case NOMINAL_PRONOUN:
			case POSSESSIVE_NOMINAL_PRONOUN:
			case POSSESSIVE_PERSONAL_PRONOUN:
			case SECOND_POSSESSIVE_PRONOUN:
			case SINGULAR_REFLEXIVE_INTENSIVE_PERSONAL_PRONOUN:
			case PLURAL_REFLEXIVE_INTENSIVE_PERSONAL_PRONOUN:
			case OBJECTIVE_PERSONAL_PRONOUN:
			case THIRD_SINGULAR_NOMINATIVE_PRONOUN:
			case OTHER_NOMINATIVE_PERSONAL_PRONOUN:
			case POSSESSIVE_WH_PRONOUN:
			case OBJECTIVE_WH_PRONOUN:
			case NOMINATIVE_WH_PRONOUN:
				toReturn = "P";
				break;
			case ADVERB:
			case COMPARATIVE_ADVERB:
			case SUPERLATIVE_ADVERB:
			case NOMINAL_ADVERB:
			case ADVERB_PARTICLE:
				toReturn = "R";
				break;
			case ORDINAL_NUMERAL:
			case CARDINAL_NUMERAL:
				toReturn = "M";
				break;
			case COORDINATING_CONJUNCTION:
			case SUBORDINATING_CONJUNCTION:
			case DETERMINER_DOUBLE_CONJUNCTION:
				toReturn = "C";
				break;
			case INTERJECTION_EXCLAMATION:
				toReturn = "I";
				break;
			default:
				toReturn = "X";
				break;
			}
		}
		return toReturn;
	}

	@Override
	public void process(JCas aJCas) throws AnalysisEngineProcessException {
		ArrayList<JCas> casList = getJCasList(aJCas);
		for (JCas doc : casList) {
			AnnotationIndex<Annotation> sentenceIndex = null;
			sentenceIndex = doc.getAnnotationIndex(SentenceAnnotation.type);

			FSIterator<Annotation> sentenceIterator = sentenceIndex.iterator();

			AnnotationIndex<Annotation> tokenIndex = doc.getAnnotationIndex(TokenAnnotation.type);

			FSIterator<Annotation> tokenIterator = null;

			SentenceAnnotation sentence = null;
			TokenAnnotation token1, token2 = null;

			while (sentenceIterator.hasNext()) {
				sentence = (SentenceAnnotation) sentenceIterator.next();
				// csak a magyar mondatokra fusson le az elemzes
				// if ("hu".equals(sentence.getLang())) {
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
					if (byJustesonKatzFilter(token1.getPosTag(), token2.getPosTag())) {
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
			// }
		}
	}

}
