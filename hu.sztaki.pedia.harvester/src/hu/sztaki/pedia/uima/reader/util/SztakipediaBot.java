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
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.concurrent.ArrayBlockingQueue;

import javax.security.auth.login.FailedLoginException;

import org.apache.log4j.Logger;
import org.jibble.pircbot.IrcException;
import org.jibble.pircbot.NickAlreadyInUseException;
import org.jibble.pircbot.PircBot;

public class SztakipediaBot extends PircBot {
	private ReaderOutputModes outputMode;
	public static Logger logger = Logger.getLogger(SztakipediaBot.class);
	private Wiki wikiAPI;
	private String ircChannel;
	private String language;
	private String applicationName;
	private HTTPWriter httpWriter;
	protected ArrayBlockingQueue<WikiArticle> queue;
	private WikiArticleFilter articleFilter = null;

	/**
	 * Creates IRC Bot, output is written into a Log
	 * 
	 * @param ircChannel
	 * @param domainUrl
	 * @param articleFilter
	 * @param applicationName
	 * @param language
	 * @param apiUser
	 *            MediaWiki API username (can be null)
	 * @param apiPassword
	 *            MediaWiki API password (can be null)
	 */
	public SztakipediaBot(String ircChannel, String domainUrl, WikiArticleFilter articleFilter,
			String applicationName, String language, String apiUser, String apiPassword) {
		outputMode = ReaderOutputModes.LOG;
		this.articleFilter = articleFilter;
		this.ircChannel = ircChannel;
		this.applicationName = applicationName;
		this.language = language;
		initializeBot(domainUrl, apiUser, apiPassword);
	}

	/**
	 * Creates IRC Bot, output is written into a Queue
	 * 
	 * @param ircChannel
	 * @param domainUrl
	 * @param queue
	 * @param articleFilter
	 * @param applicationName
	 * @param language
	 * @param apiUser
	 *            MediaWiki API username (can be null)
	 * @param apiPassword
	 *            MediaWiki API password (can be null)
	 */
	public SztakipediaBot(String ircChannel, String domainUrl,
			ArrayBlockingQueue<WikiArticle> queue, WikiArticleFilter articleFilter,
			String applicationName, String language, String apiUser, String apiPassword) {
		// setVerbose(true);
		outputMode = ReaderOutputModes.QUEUE;
		this.queue = queue;
		this.articleFilter = articleFilter;
		this.ircChannel = ircChannel;
		this.applicationName = applicationName;
		this.language = language;
		initializeBot(domainUrl, apiUser, apiPassword);
	}

	/**
	 * Creates IRC Bot, output is written into a HTTP request sent to a host and
	 * port
	 * 
	 * @param ircChannel
	 * @param domainUrl
	 * @param destinationHost
	 * @param destinationPort
	 * @param articleFilter
	 * @param applicationName
	 * @param language
	 * @param apiUser
	 *            MediaWiki API username (can be null)
	 * @param apiPassword
	 *            MediaWiki API password (can be null)
	 * @throws MalformedURLException
	 */
	public SztakipediaBot(String ircChannel, String domainUrl, String destinationHost,
			Integer destinationPort, WikiArticleFilter articleFilter, String applicationName,
			String language, String apiUser, String apiPassword) throws MalformedURLException {
		// setVerbose(true);
		outputMode = ReaderOutputModes.HTTP;
		this.httpWriter = new HTTPWriter(destinationHost, destinationPort);
		this.articleFilter = articleFilter;
		this.ircChannel = ircChannel;
		this.applicationName = applicationName;
		this.language = language;
		initializeBot(domainUrl, apiUser, apiPassword);
	}

	@SuppressWarnings("deprecation")
	private void initializeBot(String domainUrl, String apiUser, String apiPassword) {
		setLogin(apiUser + applicationName);
		wikiAPI = new Wiki(domainUrl);
		wikiAPI.setLogLevel(java.util.logging.Level.WARNING);
		// setVerbose(true);
		setName("sztakipediabot" + applicationName);
		try {
			connect("irc.wikimedia.org");
			if (apiUser != null && apiPassword != null) {
				wikiAPI.login(apiUser, apiPassword.toCharArray());
			}
		} catch (NickAlreadyInUseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (IrcException e) {
			e.printStackTrace();
		} catch (FailedLoginException e) {
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
		System.out.println(channel + ": " + message);
		String title = parseTitleFromMessage(message);
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

	private void writeOut(WikiArticle article) {
		switch (outputMode) {
		case QUEUE:
			logger.debug("Queue util: " + queue.size() + " and " + queue.remainingCapacity()
					+ " remaining");
			try {
				queue.put(article);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		case LOG:
			logger.info(article);
			break;
		case HTTP:
			httpWriter.writeArticle(article);
			break;
		default:
			break;
		}
	}

	public static void main(String[] args) throws Exception {

		// Now start our bot up.
		SztakipediaBot bot = new SztakipediaBot("#en.wikipedia", "en.wikipedia.org", null, "TEST",
				"en", "ExampleWikiAPIUser", "ExampleWikiAPIPassword");
		bot.start();
	}
}
