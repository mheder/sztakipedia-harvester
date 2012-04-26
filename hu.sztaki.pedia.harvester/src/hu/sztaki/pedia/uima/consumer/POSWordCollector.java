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

import hu.sztaki.pedia.uima.engines.AbstractMultiSofaAnnotator;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.apache.uima.TokenAnnotation;
import org.apache.uima.UimaContext;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.text.AnnotationIndex;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;
import org.apache.uima.resource.ResourceInitializationException;

public class POSWordCollector extends AbstractMultiSofaAnnotator {

	public static final String OUTPUT_DIR_PARAM = "POSOutputDir";
	public static final String FILE_PREFIX_PARAM = "FilePrefix";
	public static final String TAGSET_PARAM = "TagSet";
	public static final String VERB_POS_PARAM = "VerbPOSTags";
	public static final String NOUN_POS_PARAM = "NounPOSTags";
	public static final String ADJECTIVE_POS_PARAM = "AdjectivePOSTags";

	private Set<String> verbPOSTags, nounPOSTags, adjectivePOSTags;
	private File verbFile, nounFile, adjectiveFile;
	private String tagSet;

	@Override
	public void initialize(UimaContext aContext) throws ResourceInitializationException {
		super.initialize(aContext);
		verbPOSTags = new HashSet<String>();
		nounPOSTags = new HashSet<String>();
		adjectivePOSTags = new HashSet<String>();
		tagSet = (String) aContext.getConfigParameterValue(TAGSET_PARAM);
		if (tagSet == null) {
			throw new ResourceInitializationException();
		}
		String[] verbPOS = (String[]) aContext.getConfigParameterValue(tagSet + VERB_POS_PARAM);
		for (String posTag : verbPOS) {
			verbPOSTags.add(posTag);
		}
		String[] nounPOS = (String[]) aContext.getConfigParameterValue(tagSet + NOUN_POS_PARAM);
		for (String posTag : nounPOS) {
			nounPOSTags.add(posTag);
		}
		String[] adjectivePOS = (String[]) aContext.getConfigParameterValue(tagSet
				+ ADJECTIVE_POS_PARAM);
		for (String posTag : adjectivePOS) {
			adjectivePOSTags.add(posTag);
		}
		String outPutDir = (String) aContext.getConfigParameterValue(OUTPUT_DIR_PARAM);
		String filePrefix = (String) aContext.getConfigParameterValue(FILE_PREFIX_PARAM);
		if (filePrefix == null) {
			filePrefix = "";
		}
		verbFile = new File("" + outPutDir + File.separator + filePrefix + "verbs");
		nounFile = new File("" + outPutDir + File.separator + filePrefix + "nouns");
		adjectiveFile = new File("" + outPutDir + File.separator + filePrefix + "adjectives");
	}

	@Override
	public void process(JCas aJCas) throws AnalysisEngineProcessException {
		BufferedWriter verbFw = null;
		BufferedWriter nounFw = null;
		BufferedWriter adjectiveFw = null;
		try {
			verbFw = new BufferedWriter(new FileWriter(verbFile, true));
			nounFw = new BufferedWriter(new FileWriter(nounFile, true));
			adjectiveFw = new BufferedWriter(new FileWriter(adjectiveFile, true));

			ArrayList<JCas> casList = getJCasList(aJCas);
			for (JCas doc : casList) {
				AnnotationIndex<Annotation> tokenIndex = doc
						.getAnnotationIndex(TokenAnnotation.type);
				Set<String> verbs = new HashSet<String>();
				Set<String> nouns = new HashSet<String>();
				Set<String> adjectives = new HashSet<String>();
				for (Annotation annotation : tokenIndex) {
					TokenAnnotation token = (TokenAnnotation) annotation;
					String posTag = token.getPosTag();
					// String word = token.getCoveredText();
					String word = token.getLemma();
					if (tagSet.startsWith("MSD")) {
						if (posTag != null && posTag.length() > 0) {
							posTag = posTag.substring(0, 1);
						}
					}
					if (verbPOSTags.contains(posTag)) {
						verbs.add(word);
					} else if (nounPOSTags.contains(posTag)) {
						nouns.add(word);
					} else if (adjectivePOSTags.contains(posTag)) {
						adjectives.add(word);
					}
				}
				writeToFile(verbs, verbFw);
				writeToFile(adjectives, adjectiveFw);
				writeToFile(nouns, nounFw);
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (verbFw != null) {
				try {
					verbFw.close();
				} catch (IOException e) {
				}
			}
			if (nounFw != null) {
				try {
					nounFw.close();
				} catch (IOException e) {
				}
			}
			if (adjectiveFw != null) {
				try {
					adjectiveFw.close();
				} catch (IOException e) {
				}
			}
		}

	}

	private void writeToFile(Set<String> words, BufferedWriter writer) throws IOException {
		for (String word : words) {
			writer.write(word);
			writer.newLine();
		}
	}

}
