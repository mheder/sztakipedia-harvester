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
import hu.sztaki.sztakipediaparser.wiki.tags.ParagraphTag;
import hu.sztaki.sztakipediaparser.wiki.tags.TemplateTag;
import hu.sztaki.sztakipediaparser.wiki.visitor.html.PlainTextContentWriter;

import org.apache.uima.ParagraphAnnotation;
import org.apache.uima.WikiLinkAnnotation;
import org.apache.uima.WikiTemplateAnnotation;
import org.apache.uima.jcas.JCas;

public class UimaTagVisitor extends PlainTextContentWriter {

	private JCas doc;

	public UimaTagVisitor(StringBuilder b) {
		this.out = b;
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
		int beginPos = out.length() - 2;
		super.visit(tag);
		int endPos = out.length() - 2;
		if (tag.getAttributes().containsKey("href")) {
			String href = tag.getAttributes().get("href");
			String title = tag.getAttributes().get("title");
			WikiLinkAnnotation link = new WikiLinkAnnotation(doc);
			link.setHref(href);
			link.setTitle(title);
			link.setBegin(beginPos);
			link.setEnd(endPos);
			link.addToIndexes();
		}

	}

	@Override
	public void visit(ParagraphTag tag) {
		out.append("\n");
		int beginPos = out.length() - 2;
		visitChildren(tag);
		int endPos = out.length() - 2;
		ParagraphAnnotation paragraph = new ParagraphAnnotation(doc);
		paragraph.setBegin(beginPos);
		paragraph.setEnd(endPos);
		paragraph.addToIndexes();
	};
}
