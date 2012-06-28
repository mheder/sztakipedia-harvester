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
package hu.sztaki.pedia.uima.reader.worker;

import hu.sztaki.pedia.uima.reader.util.ReceiveSendCounter;
import hu.sztaki.pedia.uima.reader.util.Wiki;
import hu.sztaki.pedia.uima.reader.util.WikiArticle;
import hu.sztaki.pedia.uima.reader.util.WikiArticleFilter;

import java.io.IOException;
import java.util.HashMap;

import org.apache.log4j.Logger;

public abstract class IRCBotBackgroundWorker extends Thread {
	public static Logger logger = Logger.getLogger("BackgroundWorkerThread");
	protected Wiki wikiAPI;
	protected WikiArticleFilter articleFilter;
	protected String title, language, applicationName;

	public IRCBotBackgroundWorker(Wiki wikiAPI, WikiArticleFilter articleFilter, String title,
			String applicationName, String language) {
		this.articleFilter = articleFilter;
		this.title = title;
		this.applicationName = applicationName;
		this.language = language;
		this.wikiAPI = wikiAPI;
	}

	@Override
	public void run() {
		ReceiveSendCounter counter = ReceiveSendCounter.getInstance();
		counter.threadStarted();
		try {
			WikiArticle article = new WikiArticle();
			article.setTitle(title);
			article.setText("");
			boolean validArticleByTitle = true;
			if (articleFilter != null) {
				validArticleByTitle = articleFilter.isValidArticle(article);
			}
			if (validArticleByTitle) {
				// debug
				String text = wikiAPI.getPageText(title);
				article.setText(text);
				boolean notRedirect = true;
				if (articleFilter != null) {
					notRedirect = articleFilter.isValidArticle(article);
				}
				if (notRedirect) {
					HashMap<String, Object> pageinfo = wikiAPI.getPageInfo(title);
					Long id = (Long) pageinfo.get("pageid");
					Long lastRevId = (Long) pageinfo.get("lastrevid");
					article.setId(id);
					article.setApplication(applicationName);
					article.setLanguage(language);
					article.setRevision(lastRevId);
					writeOut(article);
					counter.countSent();
					logger.info("ACCEPTED: " + article.getTitle() + "(ID:" + article.getId() + ")");
				} else {
					counter.countFiltered();
					logger.info("FILTERED: " + article.getTitle() + "(" + article.getId() + ")");
				}
			} else {
				counter.countFiltered();
				logger.info("FILTERED: " + article.getTitle() + "(" + article.getId() + ")");
			}
			// logger.debug("EDIT: " + article.getTitle() + " (" +
			// article.getId() + ")");
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			counter.threadFinished();
		}
	}

	protected abstract void writeOut(WikiArticle article);

}
