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
import java.util.Iterator;

import org.apache.uima.UimaContext;
import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.cas.CASException;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;

public abstract class AbstractMultiSofaAnnotator extends JCasAnnotator_ImplBase {

	public static final String SOFANAME_PARAM = "SofaNames";
	protected JCas cas = null;

	protected String[] sofaNames;

	@Override
	public void initialize(UimaContext aContext)
			throws ResourceInitializationException {
		super.initialize(aContext);
		this.sofaNames = (String[]) aContext
				.getConfigParameterValue(SOFANAME_PARAM);
	}

	protected ArrayList<JCas> getJCasList(JCas aCas) {
		ArrayList<JCas> casList = new ArrayList<JCas>();
		// check if sofa names are available
		if (this.sofaNames != null && this.sofaNames.length > 0) {

			// get sofa names
			for (String sofaName : sofaNames) {
				Iterator<JCas> it;
				try {
					it = aCas.getViewIterator(sofaName);
					while (it.hasNext()) {
						// add sofas to the cas List to process
						casList.add(it.next());
					}
				} catch (CASException e) {
					e.printStackTrace();
				}
			}
		} else {
			// use default sofa for the processing
			casList.add(aCas);
		}
		return casList;
	}

}
