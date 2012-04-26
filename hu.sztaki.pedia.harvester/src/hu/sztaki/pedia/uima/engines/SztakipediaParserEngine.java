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

import hu.sztaki.pedia.uima.parser.IUimaWikiInterpreter;
import hu.sztaki.pedia.uima.parser.UimaWikiInterpreter;
import hu.sztaki.sztakipediaparser.wiki.parser.Parser;

import java.io.IOException;
import java.net.MalformedURLException;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;

import org.apache.uima.SourceDocumentInformation;
import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.CASException;
import org.apache.uima.cas.text.AnnotationIndex;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;

public class SztakipediaParserEngine extends JCasAnnotator_ImplBase {
	public static final String INPUT_SOFANAME_PARAM = "InputSofaName";
	public static final String OUTPUT_SOFANAME_PARAM = "OutputSofaName";
	private Parser parser;
	private IUimaWikiInterpreter interpreter;
	private String inputSofaName, outputSofaName;
	private String localeName;
	public static final String LOCALEABBREV_PARAM = "LocaleAbbrev";

	@Override
	public void initialize(org.apache.uima.UimaContext aContext)
			throws org.apache.uima.resource.ResourceInitializationException {
		super.initialize(aContext);
		try {
			inputSofaName = (String) aContext.getConfigParameterValue(INPUT_SOFANAME_PARAM);
			outputSofaName = (String) aContext.getConfigParameterValue(OUTPUT_SOFANAME_PARAM);
			localeName = (String) aContext.getConfigParameterValue(LOCALEABBREV_PARAM);
			String rootURL = "http://" + localeName + ".wikipedia.org/wiki/";
			String apiURL = "http://" + localeName + ".wikipedia.org/w/api.php";
			String mediaUrl = "http://upload.wikimedia.org/wikipedia/commons/";
			interpreter = new UimaWikiInterpreter(new Locale(localeName), rootURL, apiURL, mediaUrl);
			parser = new Parser(interpreter);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	};

	@Override
	public void process(JCas aCas) throws AnalysisEngineProcessException {
		try {
			JCas doc = aCas.getView(inputSofaName);
			JCas newdoc = aCas.createView(outputSofaName);
			interpreter.setDocument(newdoc);
			String wikitext = doc.getDocumentText();
			// do the parsing
			String plaintext = parser.parse(wikitext);
			// set parsed String as the new views text
			newdoc.setSofaDataString(plaintext, "text/plain");
			// copy SourceDocumentInformation annotations to new document
			copySourceDocumentAnnotationFromRawToParsed(doc, newdoc);
			// clear interpreter
			interpreter.clearDocument();

		} catch (CASException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Copies all SourceDocumentInformation annotations from source CAS
	 * annotation indexes to target CAS annotation indexes.
	 * 
	 * @param sourceCAS
	 *            JCas to copy from
	 * @param targetCAS
	 *            JCas to copy to
	 */
	private void copySourceDocumentAnnotationFromRawToParsed(JCas sourceCAS, JCas targetCAS) {
		AnnotationIndex<Annotation> sourceDocumentAnnotations = sourceCAS
				.getAnnotationIndex(SourceDocumentInformation.type);
		for (Annotation annotation : sourceDocumentAnnotations) {
			SourceDocumentInformation sdiAnnotation = (SourceDocumentInformation) annotation;
			SourceDocumentInformation newSDIAnnotation = new SourceDocumentInformation(targetCAS);
			newSDIAnnotation.setUri(sdiAnnotation.getUri());
			newSDIAnnotation.setOffsetInSource(sdiAnnotation.getOffsetInSource());
			newSDIAnnotation.setDocumentSize(sdiAnnotation.getDocumentSize());
			newSDIAnnotation.setLastSegment(sdiAnnotation.getLastSegment());
			newSDIAnnotation.addToIndexes();
		}

	}

}
