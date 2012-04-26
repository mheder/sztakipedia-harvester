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
package hu.sztaki.pedia.uima.reader.util;

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.logging.Level;

import org.apache.log4j.Logger;
import org.jibble.pircbot.IrcException;
import org.jibble.pircbot.NickAlreadyInUseException;
import org.jibble.pircbot.PircBot;

public class SztakipediaBot extends PircBot {
	// private String ircChannel;
	public static Logger logger = Logger.getLogger(SztakipediaBot.class);
	private Wiki wikiAPI;
	private String ircChannel;
	protected ArrayBlockingQueue<WikiArticle> queue;
	private WikiDumpArticleFilter articleFilter;

	@SuppressWarnings("deprecation")
	public SztakipediaBot(String ircChannel, String domainUrl,
			ArrayBlockingQueue<WikiArticle> queue, WikiDumpArticleFilter articleFilter) {
		// setVerbose(true);
		// setLogin("husztakipediabot");
		this.queue = queue;
		this.articleFilter = articleFilter;
		this.ircChannel = ircChannel;
		wikiAPI = new Wiki(domainUrl);
		wikiAPI.setLogLevel(Level.WARNING);

		// this.ircChannel = ircChannel;
		setName("sztakipediabot");
		try {
			connect("irc.wikimedia.org");
		} catch (NickAlreadyInUseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (IrcException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param ircChannel
	 *            channel
	 */
	public void start() {
		joinChannel(ircChannel);
	}

	@Override
	protected void onMessage(String channel, String sender, String login, String hostname,
			String message) {
		// System.out.println(channel + ": " + message);
		String title = parseTitleFromMessage(message);
		try {
			WikiArticle article = new WikiArticle();
			article.setTitle(title);
			article.setText("");
			boolean validArticleByTitle = articleFilter.isValidArticle(article);
			logger.debug("Queue util: " + queue.size() + " and " + queue.remainingCapacity()
					+ " remaining");
			if (validArticleByTitle) {
				// debug
				String text = wikiAPI.getPageText(title);
				article.setText(text);
				if (articleFilter.isNotRedirect(article)) {
					HashMap<String, Object> pageinfo = wikiAPI.getPageInfo(title);
					Long id = (Long) pageinfo.get("pageid");
					article.setId(id.toString());

					queue.put(article);
					logger.info("ACCEPTED: " + article.getTitle() + "(ID:" + article.getId() + ")");
				} else {
					// debug
					logger.info("DENIED: " + article.getTitle() + "(" + article.getId() + ")");
				}
			} else {
				// debug
				logger.info("DENIED: " + article.getTitle() + "(" + article.getId() + ")");
			}
			logger.debug("EDIT: " + article.getTitle() + " (" + article.getId() + ")");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Parses messages given with the below format, seraches for Title of the
	 * article field. Message format: 14[[07Title of the article14]]4 10
	 * 02http://hu.wikipedia.org/w/index.php?diff=11549228&oldid=11549206 5*
	 * 03EditorUserName 5* (+57) -diffsize 10 Editors comments 
	 * 
	 * @param message
	 * @return article title
	 */
	private String parseTitleFromMessage(String message) {
		StringBuilder titleB = new StringBuilder();
		int state = 0;
		for (int i = 0; i < message.length(); i++) {
			char c = message.charAt(i);
			if (c == 3) {
				state++;
			}
			switch (state) {
			case 0:
			case 1:
				break;
			case 2:
				if (c == '7') {
					state++;
				}
				break;
			case 3:
				titleB.append(c);
				break;
			default:
				break;
			}

		}
		return titleB.toString();

	}

	// public static void main(String[] args) throws Exception {
	//
	// // Now start our bot up.
	// SztakipediaBot bot = new SztakipediaBot("#hu.wikipedia",
	// "hu.wikipedia.org");
	//
	// }
}
