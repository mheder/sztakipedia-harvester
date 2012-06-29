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

import hu.sztaki.pedia.uima.consumer.util.IExternalProcessor;
import hu.sztaki.pedia.uima.engines.AbstractMultiSofaAnnotator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.uima.SentenceAnnotation;
import org.apache.uima.SourceDocumentInformation;
import org.apache.uima.TokenAnnotation;
import org.apache.uima.UimaContext;
import org.apache.uima.WikiArticleAnnotation;
import org.apache.uima.WikiCategoryAnnotation;
import org.apache.uima.WikiLinkAnnotation;
import org.apache.uima.WikiTemplateAnnotation;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.CASException;
import org.apache.uima.cas.text.AnnotationIndex;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;
import org.apache.uima.resource.ResourceInitializationException;

public class ExternalConsumer extends AbstractMultiSofaAnnotator {
	public static final String ANNOTATIONSOFANAME_PARAM = "AnnotationSofaName";
	public static final String EXTERNALPROCESSORCLASSNAME_PARAM = "ExternalProcessorClassName";
	public static final String EXTERNALPROCESSORPARAMS_PARAM = "ExternalProcessorParams";

	private Logger logger = Logger.getLogger(ExternalConsumer.class);
	private String annotatedSofaName;

	private IExternalProcessor externalProcessor;

	public void initialize(UimaContext aContext) throws ResourceInitializationException {
		super.initialize(aContext);
		annotatedSofaName = (String) aContext.getConfigParameterValue(ANNOTATIONSOFANAME_PARAM);
		String[] externalProcessorParams = (String[]) aContext
				.getConfigParameterValue(EXTERNALPROCESSORPARAMS_PARAM);
		String externalProcessorClassName = (String) aContext
				.getConfigParameterValue(EXTERNALPROCESSORCLASSNAME_PARAM);
		try {
			// loading the external Class
			Class classDef = Class.forName(externalProcessorClassName);
			Object object = classDef.newInstance();
			externalProcessor = (IExternalProcessor) object;
			if (externalProcessorParams != null) {
				externalProcessor.initialize(externalProcessorParams);
			}
			if (externalProcessor == null) {
				logger.error("Instantiating ExternalProcessor class: "+externalProcessorClassName+"failed");
				System.exit(1);
			}
		} catch (ClassNotFoundException e) {
			logger.error("Error while intantiating class:" + externalProcessorClassName, e);
		} catch (InstantiationException e) {
			logger.error("Error while intantiating class:" + externalProcessorClassName, e);
		} catch (IllegalAccessException e) {
			logger.error("Error while intantiating class:" + externalProcessorClassName, e);
		}
	}

	@Override
	public void process(JCas aJCas) throws AnalysisEngineProcessException {
		ArrayList<JCas> casList = getJCasList(aJCas);
		Map<String, String> sofaTextToName = new HashMap<String, String>();
		Map<Integer, AnnotationIndex<Annotation>> annotationIndexMap = new HashMap<Integer, AnnotationIndex<Annotation>>();
		for (JCas doc : casList) {
			// put texts to a map accessed by the view names (SOFA)
			logger.debug("Sofaname:" + doc.getViewName());
			sofaTextToName.put(doc.getViewName(), doc.getSofaDataString());
		}
		try {
			JCas annotationCAS = aJCas.getView(annotatedSofaName);
			// put all annotation indexes into a map,
			// accessed by the Annotation.type
			annotationIndexMap.put(WikiArticleAnnotation.type,
					annotationCAS.getAnnotationIndex(WikiArticleAnnotation.type));
			annotationIndexMap.put(SourceDocumentInformation.type,
					annotationCAS.getAnnotationIndex(SourceDocumentInformation.type));
			annotationIndexMap.put(TokenAnnotation.type,
					annotationCAS.getAnnotationIndex(TokenAnnotation.type));
			annotationIndexMap.put(SentenceAnnotation.type,
					annotationCAS.getAnnotationIndex(SentenceAnnotation.type));
			annotationIndexMap.put(WikiLinkAnnotation.type,
					annotationCAS.getAnnotationIndex(WikiLinkAnnotation.type));
			annotationIndexMap.put(WikiTemplateAnnotation.type,
					annotationCAS.getAnnotationIndex(WikiTemplateAnnotation.type));
			annotationIndexMap.put(WikiCategoryAnnotation.type,
					annotationCAS.getAnnotationIndex(WikiCategoryAnnotation.type));
			// process the annotations
			externalProcessor.processAnnotations(annotationIndexMap, sofaTextToName);
		} catch (CASException e) {
			logger.error("Error loading view:" + annotatedSofaName, e);
		}

	}
}
