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
package hu.sztaki.pedia.uima.consumer.util;

import java.util.Map;

import org.apache.uima.cas.text.AnnotationIndex;
import org.apache.uima.jcas.tcas.Annotation;

public interface IExternalProcessor {
	/**
	 * Initialize the processor with commmand line like argv array.
	 * 
	 * @param argv
	 */
	public void initialize(String[] argv);

	/**
	 * Process the annotations, and the raw text.
	 * 
	 * @param annotationIndexMap
	 *            AnnotationIndexes are mapped by the Annotation.type value
	 * @param sofaNamesToText
	 *            raw text is mapped by the Sofa name
	 */
	public void processAnnotations(Map<Integer, AnnotationIndex<Annotation>> annotationIndexMap,
			Map<String, String> sofaTextToName);

}
