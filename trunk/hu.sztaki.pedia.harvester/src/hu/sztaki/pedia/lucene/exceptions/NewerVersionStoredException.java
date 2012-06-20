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
package hu.sztaki.pedia.lucene.exceptions;

import org.apache.uima.WikiArticleAnnotation;

public class NewerVersionStoredException extends Exception {
	private WikiArticleAnnotation article;
	/**
	 * 
	 */
	private static final long serialVersionUID = -4240304332517978580L;

	public NewerVersionStoredException(WikiArticleAnnotation article) {
		this.article = article;
	}

	@Override
	public String getMessage() {
		return "Article:" + article.getTitle() + " (ID:" + article.getId() + ",Rev:"
				+ article.getRevision() + ") already stored with newer revision!";
	}
}
