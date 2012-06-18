package hu.sztaki.pedia.lucene;

import java.io.File;
import java.io.IOException;

import org.apache.log4j.Logger;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.NIOFSDirectory;

public class LuceneIndexer {
	private static final int COMMIT_THRESHOLD = 1000;
	private static int countToCommit = 0;
	private static volatile boolean writerInitialized = false;

	private static volatile LuceneIndexer indexerInstance = new LuceneIndexer();
	private IndexWriter writer;
	private Searcher searcher;
	private Directory indexDirectory;
	private Logger logger = Logger.getLogger(LuceneIndexer.class);

	public static LuceneIndexer getInstance() {
		return indexerInstance;
	}

	public synchronized void initialize(String indexDirPath, IndexWriterConfig iwc)
			throws IOException {
		// directory
		File indexDirFile = new File(indexDirPath);
		if (!indexDirFile.exists()) {
			indexDirFile.mkdirs();
		}
		indexDirectory = NIOFSDirectory.open(indexDirFile);
		if (writer != null) {
			close();
		}

		writer = new IndexWriter(indexDirectory, iwc);
		searcher = new Searcher(indexDirPath, iwc.getAnalyzer());
		setWriterInitialized(true);
	}

	private LuceneIndexer() {
	}

	public void commit() throws CorruptIndexException, IOException {
		logger.info("Commit");
		writer.commit();
	}

	public void close() throws IOException {
		// Speed and performance issues, not what it suggests, will be renamed
		// anyway in newer versions
		// writer.optimize();
		commit();
		writer.close();
		setWriterInitialized(false);
	}

	public static boolean isWriterInitialized() {
		return writerInitialized;
	}

	public static synchronized void setWriterInitialized(boolean writerInitialized) {
		LuceneIndexer.writerInitialized = writerInitialized;
	}

}
