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

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.uima.SentenceAnnotation;
import org.apache.uima.SourceDocumentInformation;
import org.apache.uima.TokenAnnotation;
import org.apache.uima.UimaContext;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.FSIterator;
import org.apache.uima.cas.text.AnnotationIndex;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;
import org.apache.uima.resource.ResourceInitializationException;
import org.knallgrau.utils.textcat.TextCategorizer;

public class LanguageRecognizer extends AbstractMultiSofaAnnotator {

	private TextCategorizer langGuesser;
	private HashMap<String, String> langNameToISOCode;
	private HashMap<String, Integer> docLanguages;

	@Override
	public void initialize(UimaContext aContext)
			throws ResourceInitializationException {
		super.initialize(aContext);
		this.langGuesser = new TextCategorizer();
		// to map language names to ISO-639 2 letter format
		langNameToISOCode = new HashMap<String, String>();
		langNameToISOCode.put("hungarian", "hu");
		langNameToISOCode.put("english", "en");
		langNameToISOCode.put("slovakian", "sk");
		langNameToISOCode.put("german", "de");
		langNameToISOCode.put("french", "fr");
		langNameToISOCode.put("spanish", "es");
		langNameToISOCode.put("italian", "it");
		langNameToISOCode.put("swedish", "sv");
		langNameToISOCode.put("polish", "pl");
		langNameToISOCode.put("dutch", "nl");
		langNameToISOCode.put("norwegian", "no");
		langNameToISOCode.put("finnish", "fi");
		langNameToISOCode.put("albanian", "sq");
		langNameToISOCode.put("danish", "da");
		langNameToISOCode.put("slovenian", "sl");
	}

	@Override
	public void process(JCas jCas) throws AnalysisEngineProcessException {
		ArrayList<JCas> casList = getJCasList(jCas);

		for (JCas doc : casList) {
			AnnotationIndex<Annotation> sentenceIndex = null;
			sentenceIndex = doc.getAnnotationIndex(SentenceAnnotation.type);

			docLanguages = new HashMap<String, Integer>();

			AnnotationIndex<Annotation> tokenIndex = null;
			tokenIndex = doc.getAnnotationIndex(TokenAnnotation.type);
			FSIterator<Annotation> tokenIterator = null;

			FSIterator<Annotation> sentenceIterator = null;
			sentenceIterator = sentenceIndex.iterator();

			SentenceAnnotation sentence = null;

			while (sentenceIterator.hasNext()) {
				sentence = (SentenceAnnotation) sentenceIterator.next();
				String lang = langGuesser.categorize(sentence.getCoveredText());
				lang = langNameToISOCode.get(lang);
				// count different language sentences
				if (docLanguages.containsKey(lang)) {
					Integer curr = docLanguages.get(lang) + 1;
					docLanguages.put(lang, curr + 1);
				} else {
					docLanguages.put(lang, 1);
				}
				sentence.setLang(lang);
				tokenIterator = tokenIndex.subiterator(sentence);
				while (tokenIterator.hasNext()) {
					TokenAnnotation token = (TokenAnnotation) tokenIterator
							.next();
					token.setLang(lang);
				}
			}
			int max = 0;
			String docLang = "";

			// decide document's language
			for (String lang : docLanguages.keySet()) {
				if (docLanguages.get(lang) > max) {
					max = docLanguages.get(lang);
					docLang = lang;
				}
			}
			// set doc language
			doc.setDocumentLanguage(docLang);

			// set to SourceDocumentInformation's attribute for indexing
			AnnotationIndex<Annotation> sdiIndex = doc
					.getAnnotationIndex(SourceDocumentInformation.type);
			for (Annotation annotation : sdiIndex) {
				SourceDocumentInformation sdi = (SourceDocumentInformation) annotation;
				sdi.setLang(docLang);
			}

		}

	}

}
