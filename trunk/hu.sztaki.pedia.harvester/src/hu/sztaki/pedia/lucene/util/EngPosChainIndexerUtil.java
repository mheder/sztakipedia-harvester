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
package hu.sztaki.pedia.lucene.util;

import hu.sztaki.pedia.uima.util.PennTags;

import java.util.HashSet;
import java.util.Set;

import org.apache.uima.TokenAnnotation;
import org.apache.uima.cas.text.AnnotationIndex;
import org.apache.uima.jcas.tcas.Annotation;

public class EngPosChainIndexerUtil extends HunPosChainIndexerUtil {

	private Set<String> unimportantPOSTags;

	public EngPosChainIndexerUtil(String delimiter, String[] unimportantPOSTags) {
		super(delimiter);
		this.unimportantPOSTags = new HashSet<String>();
		for (String posTag : unimportantPOSTags) {
			this.unimportantPOSTags.add(posTag);
		}

	}

	@Override
	protected boolean isImportantWordByCategory(String posTag) {
		boolean result = true;
		if (unimportantPOSTags.contains(posTag.toUpperCase())) {
			result = false;
		}
		return result;
	}

	@Override
	public Set<String> getUniqLemmas(AnnotationIndex<Annotation> tokenIndex) {
		Set<String> uniqLemmas = new HashSet<String>();
		for (Annotation annotation : tokenIndex) {
			TokenAnnotation ta = (TokenAnnotation) annotation;
			// for collecting unique lemmas
			if (isValidWordToken(ta.getPosTag())) {
				uniqLemmas.add(ta.getCoveredText());
			}
		}
		return uniqLemmas;

	}

	// @Override
	// protected boolean isImportantWordByCategory(String posTag) {
	// boolean result;
	// try {
	// PennTags category = null;
	// for (PennTags penncat : PennTags.values()) {
	// if (posTag.toUpperCase().equals(penncat.abbreviation())) {
	// category = penncat;
	// }
	// }
	// switch (category) {
	// case CARDINAL_NUMERAL:
	// case ADJECTIVE:
	// case COMPARATIVE_ADJECTIVE:
	// case SEMANTICALLY_SUPERLATIVE_ADJECTIVE:
	// case MORPHOLOGICALLY_SUPERLATIVE_ADJECTIVE:
	// case SINGULAR_OR_MASS_NOUN:
	// case POSSESSIVE_SINGULAR_NOUN:
	// case PLURAL_NOUN:
	// case POSSESSIVE_PLURAL_NOUN:
	// case PROPER_NOUN_OR_PART_OF_NAME_PHRASE:
	// case POSSESSIVE_PROPER_NOUN:
	// case PLURAL_PROPER_NOUN:
	// case POSSESSIVE_PLURAL_PROPER_NOUN:
	// case ADVERBIAL_NOUN:
	// case PLURAL_ADVERBIAL_NOUN:
	// case ORDINAL_NUMERAL:
	// case POSSESSIVE_NOMINAL_PRONOUN:
	// case POSSESSIVE_PERSONAL_PRONOUN:
	// case SECOND_POSSESSIVE_PRONOUN:
	// case SINGULAR_REFLEXIVE_INTENSIVE_PERSONAL_PRONOUN:
	// case PLURAL_REFLEXIVE_INTENSIVE_PERSONAL_PRONOUN:
	// case OBJECTIVE_PERSONAL_PRONOUN:
	// case THIRD_SINGULAR_NOMINATIVE_PRONOUN:
	// case OTHER_NOMINATIVE_PERSONAL_PRONOUN:
	// case QUALIFIER:
	// case POST_QUALIFIER:
	// case NOMINAL_PRONOUN:
	// case ADVERB:
	// case VERB_BASE_FORM:
	// case VERB_PAST_TENSE:
	// case VERB_PRESENT_PARTICIPLE_GERUND:
	// case VERB_PAST_PARTICIPLE:
	// case VERB_3RD_SINGULAR_PRESENT:
	// case DO:
	// case DID:
	// case DOES:
	// result = true;
	// break;
	// default:
	// result = false;
	// break;
	// }
	// } catch (Exception e) {
	// result = false;
	// }
	// return result;
	// }

	@Override
	protected boolean isValidWordToken(String posTag) {
		boolean result = false;
		if (posTag != null) {
			for (PennTags penncat : PennTags.values()) {
				if (!result
						&& posTag.toLowerCase().startsWith(penncat.abbreviation().toLowerCase())) {
					result = true;
				}
			}
		}
		return result;
	}

}