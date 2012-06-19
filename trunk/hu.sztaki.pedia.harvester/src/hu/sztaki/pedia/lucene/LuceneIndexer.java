package hu.sztaki.pedia.lucene;

import hu.sztaki.pedia.lucene.exceptions.BadQueryException;
import hu.sztaki.pedia.lucene.exceptions.NewerVersionStoredException;
import hu.sztaki.pedia.lucene.util.IChainIndexerUtil;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.NIOFSDirectory;
import org.apache.uima.WikiArticleAnnotation;
import org.apache.uima.cas.text.AnnotationIndex;
import org.apache.uima.jcas.tcas.Annotation;

public class LuceneIndexer {
	private static final int COMMIT_THRESHOLD = 1000;
	private static int countToCommit = 0;
	private static volatile boolean writerInitialized = false;

	private static volatile LuceneIndexer indexerInstance = new LuceneIndexer();
	private IndexWriter writer;
	private Searcher searcher;
	private Directory indexDirectory;
	private IChainIndexerUtil chainIndexerUtil;
	private Logger logger = Logger.getLogger(LuceneIndexer.class);

	public static LuceneIndexer getInstance() {
		return indexerInstance;
	}

	public synchronized void initialize(String indexDirPath, IChainIndexerUtil chainIndexerUtil)
			throws IOException {
		// directory
		this.chainIndexerUtil = chainIndexerUtil;
		File indexDirFile = new File(indexDirPath);
		if (!indexDirFile.exists()) {
			indexDirFile.mkdirs();
		}
		indexDirectory = NIOFSDirectory.open(indexDirFile);
		if (writer != null) {
			close();
		}

		writer = new IndexWriter(indexDirectory, chainIndexerUtil.getIndexWriterConfig());
		searcher = new Searcher(indexDirPath, chainIndexerUtil.getIndexWriterConfig().getAnalyzer());
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

	public void indexAnnotations(Map<Integer, AnnotationIndex<Annotation>> annotationIndexMap)
			throws NewerVersionStoredException {
		WikiArticleAnnotation wikiArticleAnnotation = (WikiArticleAnnotation) annotationIndexMap
				.get(WikiArticleAnnotation.type).iterator().get();
		if (isNewerVersionStored(wikiArticleAnnotation.getId(), wikiArticleAnnotation.getRevision())) {
			throw new NewerVersionStoredException();
		}
		// TODO finish indexing

	}

	private boolean isNewerVersionStored(long id, long revisionID) {
		boolean isStored = false;
		try {
			TopDocs result = searcher.simpleSearch(IndexFieldNames.ID_FIELD_NAME,
					Long.toString(id), 1);
			if (result.scoreDocs.length == 1) {
				Document doc = searcher.getDocument(result.scoreDocs[0].doc);
				long revFound = Long.parseLong(doc.get(IndexFieldNames.REVISION_FIELD_NAME));
				if (revFound >= revisionID) {
					isStored = true;
				}
			}
		} catch (BadQueryException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return isStored;
	}

}
