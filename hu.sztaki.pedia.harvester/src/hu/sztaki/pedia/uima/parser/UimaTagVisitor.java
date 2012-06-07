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

import hu.sztaki.sztakipediaparser.wiki.tags.AnchorTag;
import hu.sztaki.sztakipediaparser.wiki.tags.TemplateTag;
import hu.sztaki.sztakipediaparser.wiki.visitor.html.PlainTextContentWriter;

import org.apache.uima.WikiLinkAnnotation;
import org.apache.uima.WikiTemplateAnnotation;
import org.apache.uima.jcas.JCas;

public class UimaTagVisitor extends PlainTextContentWriter {

	private JCas doc;

	public UimaTagVisitor() {
		out = new StringBuilder();
	}

	/**
	 * 
	 * @return the string representing the articles text
	 */
	public String getContent() {
		return out.toString();
	}

	public void setDoc(JCas doc) {
		this.doc = doc;
	}

	public JCas getDoc() {
		return doc;
	}

	@Override
	public void visit(TemplateTag tag) {
		WikiTemplateAnnotation wt = new WikiTemplateAnnotation(doc);
		wt.setName(tag.getName());
		wt.addToIndexes();
	}

	@Override
	public void visit(AnchorTag tag) {
		if (tag.getAttributes().containsKey("href")) {
			WikiLinkAnnotation link = new WikiLinkAnnotation(doc);
			link.setHref(tag.getAttributes().get("href"));
			link.setTitle(tag.getAttributes().get("title"));
			link.addToIndexes();
		}
		super.visit(tag);
	}
}
