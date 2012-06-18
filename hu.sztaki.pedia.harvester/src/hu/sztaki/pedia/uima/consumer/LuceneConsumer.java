package hu.sztaki.pedia.uima.consumer;

import hu.sztaki.pedia.lucene.LuceneIndexer;
import hu.sztaki.pedia.uima.engines.AbstractMultiSofaAnnotator;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.jcas.JCas;

public class LuceneConsumer extends AbstractMultiSofaAnnotator {
	private Logger logger = Logger.getLogger(LuceneConsumer.class);
	private LuceneIndexer indexer;
	public static long totalTokensProcessed = 0;

	@Override
	public void process(JCas aJCas) throws AnalysisEngineProcessException {
		// TODO Auto-generated method stub

	}

	@Override
	public void destroy() {
		try {
			logger.info("CasToLucene total # of tokens processed:" + totalTokensProcessed);
			logger.info("destroy");
			indexer.close();
		} catch (IOException e) {
			logger.error("Couldnt save indexes", e);
		}
	}

	@Override
	public void batchProcessComplete() throws AnalysisEngineProcessException {
		logger.info("batchProcessComplete");
		try {
			indexer.commit();
		} catch (CorruptIndexException e) {
			logger.error("batchProcessComplete commit failed", e);
		} catch (IOException e) {
			logger.error("batchProcessComplete commit failed", e);
		}
	}

	@Override
	public void collectionProcessComplete() throws AnalysisEngineProcessException {
		logger.info("collectionProcessComplete");
		try {
			indexer.commit();
		} catch (CorruptIndexException e) {
			logger.error("collectionProcessComplete commit failed", e);
		} catch (IOException e) {
			logger.error("collectionProcessComplete commit failed", e);
		}

	}

}
