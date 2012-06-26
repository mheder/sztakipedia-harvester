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

import hu.sztaki.pedia.uima.reader.util.Wiki;
import hu.sztaki.pedia.uima.reader.util.WikiArticle;
import hu.sztaki.pedia.uima.reader.util.WikiArticleFilter;

import java.util.concurrent.ArrayBlockingQueue;

public class QueueIRCBotBackgroundWorker extends IRCBotBackgroundWorker {
	protected ArrayBlockingQueue<WikiArticle> queue;

	public QueueIRCBotBackgroundWorker(Wiki wikiAPI, WikiArticleFilter articleFilter, String title,
			String applicationName, String language, ArrayBlockingQueue<WikiArticle> queue) {
		super(wikiAPI, articleFilter, title, applicationName, language);
		this.queue = queue;
	}

	@Override
	protected void writeOut(WikiArticle article) {
		logger.debug("Queue util: " + queue.size() + " and " + queue.remainingCapacity()
				+ " remaining");
		try {
			queue.put(article);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
