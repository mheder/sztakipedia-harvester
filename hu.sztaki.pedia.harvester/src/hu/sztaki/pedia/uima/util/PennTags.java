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
package hu.sztaki.pedia.uima.util;

/*
 * Penn Treebank used in the Brown Corpus
 */
public enum PennTags {

	PRE_QUALIFIER("ABL"), 
	PRE_QUANTIFIER("ABN"), 
	PRE_QUANTIFIER2("ABX"), 
	POST_DETERMINER("AP"), 
	ARTICLE("AT"), 
	BE("BE"), 
	WERE("BED"), 
	WAS("BEDZ"), 
	BEING("BEG"), 
	AM("BEM"), 
	BEEN("BEN"), 
	ARE_ART("BER"), 
	IS("BEZ"), 
	COORDINATING_CONJUNCTION("CC"), 
	CARDINAL_NUMERAL("CD"), 
	SUBORDINATING_CONJUNCTION("CS"), 
	DO("DO"), 
	DID("DOD"), 
	DOES("DOZ"), 
	SINGULAR_DETERMINER("DT"), 
	SINGULAR_OR_PLURAL_DETERMINER_QUANTIFIER("DTI"), 
	PLURAL_DETERMINER("DTS"), 
	DETERMINER_DOUBLE_CONJUNCTION("DTX"), 
	EXISTENTIL_THERE("EX"), 
	FOREIGN_WORD("FW"), 
	WORD_OCCURRING_IN_HEADLINE("HL"), 
	HAVE("HV"), 
	HAD("HVD"), 
	HAVING("HVG"), 
	HAD_PP("HVN"), 
	HAS("HVZ"), 
	PREPOSITION("IN"), 
	ADJECTIVE("JJ"), 
	COMPARATIVE_ADJECTIVE("JJR"), 
	SEMANTICALLY_SUPERLATIVE_ADJECTIVE("JJS"), 
	MORPHOLOGICALLY_SUPERLATIVE_ADJECTIVE("JJT"), 
	MODAL_AUXILIARY("MD"), 
	CITED_WORD("NC"), 
	SINGULAR_OR_MASS_NOUN("NN"), 
	POSSESSIVE_SINGULAR_NOUN("NN$"), 
	PLURAL_NOUN("NNS"), 
	POSSESSIVE_PLURAL_NOUN("NNS$"), 
	PROPER_NOUN_OR_PART_OF_NAME_PHRASE("NP"), 
	POSSESSIVE_PROPER_NOUN("NP$"), 
	PLURAL_PROPER_NOUN("NPS"), 
	POSSESSIVE_PLURAL_PROPER_NOUN("NPS$"), 
	ADVERBIAL_NOUN("NR"), 
	PLURAL_ADVERBIAL_NOUN("NRS"), 
	ORDINAL_NUMERAL("OD"), 
	NOMINAL_PRONOUN("PN"), 
	POSSESSIVE_NOMINAL_PRONOUN("PN$"), 
	POSSESSIVE_PERSONAL_PRONOUN("PP$"), 
	SECOND_POSSESSIVE_PRONOUN("PP$$"), 
	SINGULAR_REFLEXIVE_INTENSIVE_PERSONAL_PRONOUN("PPL"), 
	PLURAL_REFLEXIVE_INTENSIVE_PERSONAL_PRONOUN("PPLS"), 
	OBJECTIVE_PERSONAL_PRONOUN("PPO"), 
	THIRD_SINGULAR_NOMINATIVE_PRONOUN("PPS"), 
	OTHER_NOMINATIVE_PERSONAL_PRONOUN("PPSS"), 
	QUALIFIER("QL"), 
	POST_QUALIFIER("QLP"), 
	ADVERB("RB"), 
	COMPARATIVE_ADVERB("RBR"), 
	SUPERLATIVE_ADVERB("RBT"), 
	NOMINAL_ADVERB("RN"), 
	ADVERB_PARTICLE("RP"), 
	WORD_OCCURRING_IN_TITLE("TL"), 
	INFINITIVE_MARKER_TO("TO"), 
	INTERJECTION_EXCLAMATION("UH"), 
	VERB_BASE_FORM("VB"), 
	VERB_PAST_TENSE("VBD"), 
	VERB_PRESENT_PARTICIPLE_GERUND("VBG"), 
	VERB_PAST_PARTICIPLE("VBN"), 
	VERB_3RD_SINGULAR_PRESENT("VBZ"), 
	WH_DETERMINER("WDT"), 
	POSSESSIVE_WH_PRONOUN("WP$"), 
	OBJECTIVE_WH_PRONOUN("WPO"), 
	NOMINATIVE_WH_PRONOUN("WPS"), 
	WH_QUALIFIER("WQL"), 
	WH_ADVERB("WRB");

	private String abbreviation;

	public String abbreviation() {
		return abbreviation;
	}

	private PennTags(String abbreviation) {
		this.abbreviation = abbreviation;
	}
}
