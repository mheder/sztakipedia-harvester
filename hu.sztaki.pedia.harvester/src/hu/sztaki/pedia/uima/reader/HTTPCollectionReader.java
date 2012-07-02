package hu.sztaki.pedia.uima.reader;

import hu.sztaki.pedia.uima.reader.util.HTTPReadHandler;
import hu.sztaki.pedia.uima.reader.util.WikiArticle;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
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
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandler;

public class HTTPCollectionReader extends CollectionReader_ImplBase {
	/**
	 * Name of optional configuration parameter that contains the Sofa to place
	 * read content
	 */
	public static final String PARAM_OUTPUTSOFA = "OutputSofa";
	public static final String PARAM_PORTS = "Ports";
	public static final String PARAM_MAXFORMSIZE = "MaxRequestSize";
	public static final String PARAM_QUEUESIZE = "QueueSize";

	private ArrayBlockingQueue<WikiArticle> articlesQueue;
	private Integer queueSize;
	private String mSofaName;
	private Integer[] ports;
	private Integer maxFormSize;
	private Map<Integer, Server> servers = new HashMap<Integer, Server>();
	public static Logger logger = Logger.getLogger(HTTPCollectionReader.class);

	// private long totalArticleCount;
	private long docsProcessed = 0;

	@Override
	public void initialize() throws ResourceInitializationException {
		mSofaName = (String) getConfigParameterValue(PARAM_OUTPUTSOFA);
		ports = (Integer[]) getConfigParameterValue(PARAM_PORTS);
		maxFormSize = (Integer) getConfigParameterValue(PARAM_MAXFORMSIZE);
		queueSize = (Integer) getConfigParameterValue(PARAM_QUEUESIZE);
		articlesQueue = new ArrayBlockingQueue<WikiArticle>(queueSize);
		for (Integer port : ports) {
			Server server = new Server(port);
			ContextHandler context = new ContextHandler();
			context.setContextPath("/");
			context.setResourceBase(".");
			if (maxFormSize != null) {
				context.setMaxFormContentSize(maxFormSize);
			}
			context.setClassLoader(Thread.currentThread().getContextClassLoader());
			context.setHandler(new HTTPReadHandler(articlesQueue));
			server.setHandler(context);
			// server.setAttribute("org.eclipse.jetty.Request.maxFormContentSize",
			// maxFormSize);
			servers.put(port, server);
			try {
				logger.info("Jetty server is about to start on port: " + port + ", maxFormSize: "
						+ maxFormSize);
				server.start();
				// server.join();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Override
	public void getNext(CAS aCAS) throws IOException, CollectionException {
		WikiArticle article = null;
		try {
			article = articlesQueue.take();
			docsProcessed++;
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
		String text = article.getText();
		if (mSofaName == null) {
			jcas.setDocumentText(article.getText());
		} else {
			try {
				doc2 = jcas.createView(mSofaName);
				doc2.setSofaDataString(text, "text/plain");
			} catch (CASException e) {
				e.printStackTrace();
			}

		}

		// set language if it was explicitly specified as a configuration
		// parameter
		if (article.getLanguage() != null) {
			((DocumentAnnotation) jcas.getDocumentAnnotationFs())
					.setLanguage(article.getLanguage());
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
		// always true, this is for endless processing :)
		return true;
	}

	@Override
	public Progress[] getProgress() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void close() throws IOException {
		// TODO Auto-generated method stub

	}

}
