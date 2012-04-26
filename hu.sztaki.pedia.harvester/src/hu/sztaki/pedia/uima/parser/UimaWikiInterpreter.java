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
package hu.sztaki.pedia.uima.parser;

import hu.sztaki.sztakipediaparser.wiki.converter.DefaultWikiInterpreter;
import hu.sztaki.sztakipediaparser.wiki.tags.Tag;

import java.io.IOException;
import java.net.MalformedURLException;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;

import org.apache.uima.jcas.JCas;

public class UimaWikiInterpreter extends DefaultWikiInterpreter implements
		IUimaWikiInterpreter {
	
	JCas doc;

	public UimaWikiInterpreter() throws MalformedURLException, IOException,
			NoSuchAlgorithmException {
		super();
	}

	public UimaWikiInterpreter(Locale locale) throws MalformedURLException,
			IOException, NoSuchAlgorithmException {
		super(locale);
	}

	public UimaWikiInterpreter(Locale locale, String rootURL, String apiURL,
			String mediaUrl) throws MalformedURLException, IOException,
			NoSuchAlgorithmException {
		super(locale, rootURL, apiURL, mediaUrl);
	}
	
	@Override
	public void render(StringBuilder b, boolean visitRoot) {
		UimaTagVisitor v = new UimaTagVisitor();
		v.setDoc(doc);
		if (visitRoot) {
			v.dispatchVisit(tagtree.getTreeRoot());
		} else {
			for (Tag c : tagtree.getTreeRoot().getChildren()) {
				v.dispatchVisit(c);
			}
		}
		b.append(v.getContent());
	}

	@Override
	public void setDocument(JCas doc) {
		this.doc = doc;
		
	}

	@Override
	public JCas clearDocument() {
		JCas ret = doc;
		doc=null;
		return ret;
	}

}
