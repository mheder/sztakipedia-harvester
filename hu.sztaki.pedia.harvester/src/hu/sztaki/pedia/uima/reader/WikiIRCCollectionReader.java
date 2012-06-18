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
package hu.sztaki.pedia.uima.reader;

import hu.sztaki.pedia.uima.reader.util.SztakipediaBot;
import hu.sztaki.pedia.uima.reader.util.WikiArticle;
import hu.sztaki.pedia.uima.reader.util.WikiDumpArticleFilter;

import java.io.IOException;
import java.util.concurrent.ArrayBlockingQueue;

import org.apache.log4j.Logger;
import org.apache.uima.SourceDocumentInformation;
import org.apache.uima.WikiArticleAnnotation;
import org.apache.uima.cas.CAS;
import org.apache.uima.cas.CASException;
import org.apache.uima.collection.CollectionException;
import org.apache.uima.collection.CollectionReader_ImplBase;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.DocumentAnnotation;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.util.Progress;

public class WikiIRCCollectionReader extends CollectionReader_ImplBase {
	/**
	 * Name of configuration parameter that must be set to the path of a
	 * directory containing input files.
	 */
	public static final String PARAM_INPUTDOMAIN = "InputDomain";

	/**
	 * Name of optional configuration parameter that contains the language of
	 * the documents in the input directory. If specified this information will
	 * be added to the CAS.
	 */
	public static final String PARAM_LANGUAGE = "Language";

	/**
	 * Name of optional configuration parameter that contains the Sofa to place
	 * read content
	 */
	public static final String PARAM_OUTPUTSOFA = "OutputSofa";
	public static final String PARAM_REDIRECT = "RedirectPage";
	public static final String PARAM_NONARTICLETITLES = "NonArticleTitles";
	public static final String PARAM_APPNAME = "ApplicationName";
	public static final String PARAM_APIUSERNAME = "WikiAPIUser";
	public static final String PARAM_APIPASSNAME = "WikiAPIPassword";

	public static Logger logger = Logger.getLogger(WikiIRCCollectionReader.class);

	private ArrayBlockingQueue<WikiArticle> articlesQueue;
	private final int queueDepth = 500;

	private String mLanguage;

	private String wikiAPIUserName;
	private String wikiAPIPassword;

	private String applicationName;

	private String mSofaName;

	private Thread botThread;

	private SztakipediaBot sztakipediaBot;

	private WikiDumpArticleFilter articleFilter;

	@Override
	public void initialize() throws ResourceInitializationException {
		mLanguage = (String) getConfigParameterValue(PARAM_LANGUAGE);
		wikiAPIUserName = (String) getConfigParameterValue(PARAM_APIUSERNAME);
		wikiAPIPassword = (String) getConfigParameterValue(PARAM_APIPASSNAME);
		mSofaName = (String) getConfigParameterValue(PARAM_OUTPUTSOFA);
		applicationName = (String) getConfigParameterValue(PARAM_APPNAME);
		String redirectPage = (String) getConfigParameterValue(PARAM_REDIRECT);
		String[] nonArticleTitles = (String[]) getConfigParameterValue(PARAM_NONARTICLETITLES);
		articleFilter = new WikiDumpArticleFilter(nonArticleTitles, redirectPage);

		String domain = ((String) getConfigParameterValue(PARAM_INPUTDOMAIN));
		final String domainUrl = domain + ".org";
		final String ircChannel = "#" + domain;

		articlesQueue = new ArrayBlockingQueue<WikiArticle>(queueDepth);

		botThread = new Thread(new Runnable() {
			@Override
			public void run() {
				sztakipediaBot = new SztakipediaBot(ircChannel, domainUrl, articlesQueue,
						articleFilter, applicationName, mLanguage, wikiAPIUserName, wikiAPIPassword);
				sztakipediaBot.start();
			}
		});

		botThread.start();

	}

	@Override
	public void getNext(CAS aCAS) throws IOException, CollectionException {
		WikiArticle article = null;
		try {
			// boolean validArticle = false;
			// while (!validArticle) {
			// article = articlesQueue.take();
			// validArticle = articleFilter.isValidArticle(article);
			// logger.debug("Queue util: " + articlesQueue.size() + " and "
			// + articlesQueue.remainingCapacity() + " remaining");
			// if (validArticle) {
			// // debug
			//
			// logger.info("ACCEPTED: " + article.getTitle() + "(ID:" +
			// article.getId() + ")");
			// } else {
			// // debug
			// logger.info("DENIED: " + article.getTitle() + "(" +
			// article.getId() + ")");
			//
			// }
			// }
			article = articlesQueue.take();
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}

		JCas jcas;
		JCas doc2 = null;
		try {
			jcas = aCAS.getJCas();
		} catch (CASException e) {
			throw new CollectionException(e);
		}

		if (mSofaName == null) {
			jcas.setDocumentText(article.getText());
		} else {
			try {
				doc2 = jcas.createView(mSofaName);
				doc2.setSofaDataString(article.getText(), "text/plain");
			} catch (CASException e) {
				e.printStackTrace();
			}

		}

		// set language if it was explicitly specified as a configuration
		// parameter
		if (mLanguage != null) {
			((DocumentAnnotation) jcas.getDocumentAnnotationFs()).setLanguage(mLanguage);
		}

		// Also store location of source document in CAS. This information is
		// critical
		// if CAS Consumers will need to know where the original document
		// contents are located.
		// For example, the Semantic Search CAS Indexer writes this information
		// into the
		// search index that it creates, which allows applications that use the
		// search index to
		// locate the documents that satisfy their semantic queries.

		SourceDocumentInformation srcDocInfo;
		WikiArticleAnnotation wikiArticleAnnotation;
		if (doc2 != null) {
			srcDocInfo = new SourceDocumentInformation(doc2);
			wikiArticleAnnotation = new WikiArticleAnnotation(doc2);
		} else {
			srcDocInfo = new SourceDocumentInformation(jcas);
			wikiArticleAnnotation = new WikiArticleAnnotation(jcas);
		}
		// create WikiArticleAnnotation, and copy all the metadata.
		wikiArticleAnnotation.setId(article.getId());
		wikiArticleAnnotation.setTitle(article.getTitle());
		wikiArticleAnnotation.setLang(article.getLanguage());
		wikiArticleAnnotation.setApplication(article.getApplication());
		wikiArticleAnnotation.setRevision(article.getRevision());
		wikiArticleAnnotation.addToIndexes();

		// create the SourceDocumentInformation
		srcDocInfo.setUri(article.getId() + ".xml");
		srcDocInfo.setOffsetInSource(0);
		srcDocInfo.setDocumentSize(article.getText().length());
		srcDocInfo.setLastSegment(false);
		srcDocInfo.addToIndexes();
		// if (doc2 != null){
		// srcDocInfo.addToIndexes(doc2);
		// }

	}

	@Override
	public boolean hasNext() throws IOException, CollectionException {
		// always true, this is endless processing :)
		return true;

	}

	@Override
	public Progress[] getProgress() {
		return null;
	}

	@Override
	public void close() throws IOException {
		botThread.interrupt();
	}

}