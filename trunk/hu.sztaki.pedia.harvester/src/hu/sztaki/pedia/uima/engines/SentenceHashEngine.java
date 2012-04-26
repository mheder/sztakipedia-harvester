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

import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import org.apache.commons.codec.binary.Hex;
import org.apache.uima.SentenceAnnotation;
import org.apache.uima.UimaContext;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.FSIterator;
import org.apache.uima.cas.text.AnnotationIndex;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;
import org.apache.uima.resource.ResourceInitializationException;

public class SentenceHashEngine extends AbstractMultiSofaAnnotator {

	private MessageDigest messageDigest;

	@Override
	public void initialize(UimaContext aContext)
			throws ResourceInitializationException {
		super.initialize(aContext);
		try {
			messageDigest = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void process(JCas jCas) throws AnalysisEngineProcessException {
		ArrayList<JCas> casList = getJCasList(jCas);

		for (JCas doc : casList) {
			AnnotationIndex<Annotation> sentenceIndex = null;
			sentenceIndex = doc.getAnnotationIndex(SentenceAnnotation.type);

			FSIterator<Annotation> sentenceIterator = null;
			sentenceIterator = sentenceIndex.iterator();

			SentenceAnnotation sentence = null;

			while (sentenceIterator.hasNext()) {
				sentence = (SentenceAnnotation) sentenceIterator.next();

				messageDigest.reset();
				messageDigest.update(sentence.getCoveredText().getBytes(
						Charset.forName("UTF8")));
				final byte[] resultByte = messageDigest.digest();
				sentence.setHash(new String(Hex.encodeHex(resultByte)));
			}
		}
	}
}