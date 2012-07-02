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

import hu.sztaki.pedia.uima.reader.util.EndOfFileToken;
import hu.sztaki.pedia.uima.reader.util.SaxWikiHandler;
import hu.sztaki.pedia.uima.reader.util.WikiArticle;
import hu.sztaki.pedia.uima.reader.util.WikiArticleFilter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
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
import org.apache.uima.resource.ResourceConfigurationException;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.util.Progress;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

public class WikiDumpCollectionReader extends CollectionReader_ImplBase {
	/**
	 * Name of configuration parameter that must be set to the path of a
	 * directory containing input files.
	 */
	public static final String PARAM_INPUTFILE = "InputFile";

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

	public static final String PARAM_REDIRECT = "RedirectPages";
	public static final String PARAM_NONARTICLETITLES = "NonArticleTitles";
	public static final String PARAM_APPNAME = "ApplicationName";

	public static Logger logger = Logger.getLogger(WikiDumpCollectionReader.class);

	private ArrayBlockingQueue<WikiArticle> articlesQueue;
	private final int queueDepth = 500;

	private String mLanguage;

	private String mSofaName;

	private Thread saxParserThread;

	private String applicationName;

	private WikiArticleFilter articleFilter;

	// private long totalArticleCount;
	private long docsProcessed = 0;

	@Override
	public void initialize() throws ResourceInitializationException {
		final File inputFile = new File(((String) getConfigParameterValue(PARAM_INPUTFILE)).trim());
		mLanguage = (String) getConfigParameterValue(PARAM_LANGUAGE);
		mSofaName = (String) getConfigParameterValue(PARAM_OUTPUTSOFA);
		applicationName = (String) getConfigParameterValue(PARAM_APPNAME);
		String[] redirectPage = (String[]) getConfigParameterValue(PARAM_REDIRECT);
		String[] nonArticleTitles = (String[]) getConfigParameterValue(PARAM_NONARTICLETITLES);
		articleFilter = new WikiArticleFilter(nonArticleTitles, redirectPage);

		// if input directory does not exist or is not a directory, throw
		// exception
		if (!inputFile.exists() || !inputFile.isFile()) {
			throw new ResourceInitializationException(
					ResourceConfigurationException.DIRECTORY_NOT_FOUND, new Object[] {
							PARAM_INPUTFILE, this.getMetaData().getName(), inputFile.getPath() });
		}

		// counting <page> and </page> tags to find out how many articles to
		// process
		// try {
		// FileInputStream fisAC = new FileInputStream(inputFile);
		// InputStreamReader isrAC = new InputStreamReader(fisAC, "UTF-8");
		// XMLReader xmlArticleCountReader = XMLReaderFactory.createXMLReader();
		// SaxWikiArticleCounter saxWikiArticleCounter = new
		// SaxWikiArticleCounter();
		// xmlArticleCountReader.setContentHandler(saxWikiArticleCounter);
		// xmlArticleCountReader.setErrorHandler(saxWikiArticleCounter);
		// // start the parsing
		// logger.info("Counting articles ...");
		// long counterStarted = System.currentTimeMillis();
		// xmlArticleCountReader.parse(new InputSource(isrAC));
		// totalArticleCount = saxWikiArticleCounter.getEnd();
		// long counterFinished = System.currentTimeMillis();
		// logger.debug("XML parsed in: " + (counterFinished - counterStarted));
		// logger.debug("# of <page>: " + saxWikiArticleCounter.getStart() +
		// " # of </page>: "
		// + saxWikiArticleCounter.getEnd());
		// logger.info("Total number of articles to process: " +
		// totalArticleCount);
		//
		// } catch (FileNotFoundException e) {
		// e.printStackTrace();
		// } catch (UnsupportedEncodingException e) {
		// e.printStackTrace();
		// } catch (SAXException e) {
		// e.printStackTrace();
		// } catch (IOException e) {
		// e.printStackTrace();
		// }

		articlesQueue = new ArrayBlockingQueue<WikiArticle>(queueDepth);

		saxParserThread = new Thread(new Runnable() {
			@Override
			public void run() {
				FileInputStream fis;
				InputStreamReader isr;
				try {
					fis = new FileInputStream(inputFile);
					isr = new InputStreamReader(fis, "UTF-8");
					XMLReader xmlReader = XMLReaderFactory.createXMLReader();
					SaxWikiHandler saxWikiHandler = new SaxWikiHandler(articlesQueue,
							applicationName, mLanguage, null);
					xmlReader.setContentHandler(saxWikiHandler);
					xmlReader.setErrorHandler(saxWikiHandler);
					// start the parsing
					xmlReader.parse(new InputSource(isr));

				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				} catch (SAXException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		});
		logger.info("Starting reading articles from XML");
		saxParserThread.start();

	}

	@Override
	public void getNext(CAS aCAS) throws IOException, CollectionException {
		WikiArticle article = null;
		try {
			boolean validArticle = false;
			while (!validArticle) {
				article = articlesQueue.take();
				docsProcessed++;
				validArticle = articleFilter.isValidArticle(article);
				logger.debug("Queue util: " + articlesQueue.size() + " and "
						+ articlesQueue.remainingCapacity() + " remaining");
				if (validArticle) {
					// debug

					logger.info(docsProcessed + " ACCEPTED: " + article.getTitle() + "("
							+ article.getId() + ")");
				} else {
					// debug
					logger.info(docsProcessed + " DENIED: " + article.getTitle() + "("
							+ article.getId() + ")");

				}
			}
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
				// TODO Auto-generated catch block
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
		// return !articlesQueue.isEmpty();
		// return docsProcessed < totalArticleCount;
		boolean hasNext = true;
		WikiArticle article = articlesQueue.peek();
		if (article != null && article instanceof EndOfFileToken) {
			hasNext = false;
		}
		return hasNext;
	}

	@Override
	public Progress[] getProgress() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void close() throws IOException {
		saxParserThread.interrupt();
	}

}
