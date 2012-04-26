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

import java.io.File;
import java.util.ArrayList;

import org.apache.uima.ArticleId;
import org.apache.uima.SourceDocumentInformation;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.text.AnnotationIndex;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;

public class ArcticleIdExtractor extends AbstractMultiSofaAnnotator {

	@Override
	public void process(JCas aJCas) throws AnalysisEngineProcessException {
		ArrayList<JCas> casList = getJCasList(aJCas);
		SourceDocumentInformation sdocInfo = null;
		for (JCas doc : casList) {
			AnnotationIndex<Annotation> sdiIndex = doc
					.getAnnotationIndex(SourceDocumentInformation.type);
			for (Annotation annotation : sdiIndex) {
				sdocInfo = (SourceDocumentInformation) annotation;
			}
			if (sdocInfo != null) {
				String uri = sdocInfo.getUri();
				String artcicleId = uri.substring(uri.lastIndexOf(File.separatorChar) + 1);
				artcicleId = artcicleId.trim();
				if (artcicleId.endsWith(".xml")) {
					artcicleId = artcicleId.substring(0, artcicleId.length() - 4);
				}
				ArticleId id = new ArticleId(doc);
				id.setId(artcicleId);
				id.addToIndexes();
			}
		}
	}

}
