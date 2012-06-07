package hu.sztaki.pedia.lucene;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.NIOFSDirectory;
import org.apache.lucene.util.Version;

public class Searcher {

	private Analyzer analyzer;
	private Directory indexDirectory;
	private IndexSearcher searcher = null;
	private Map<String, QueryParser> parsers = new HashMap<String, QueryParser>();

	public Searcher(String indexDirPath, Analyzer analyzer) {
		File indexDirFile = new File(indexDirPath);
		if (!indexDirFile.exists()) {
			indexDirFile.mkdirs();
		}
		this.analyzer = analyzer;
		try {
			indexDirectory = NIOFSDirectory.open(indexDirFile);
			searcher = new IndexSearcher(indexDirectory, true);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private Query createQuery(String fieldName, String qText) throws BadQueryException {
		QueryParser qp;
		if (parsers.containsKey(fieldName)) {
			qp = parsers.get(fieldName);
		} else {
			qp = new QueryParser(Version.LUCENE_34, fieldName, analyzer);
			parsers.put(fieldName, qp);
		}
		try {
			return qp.parse(qText);
		} catch (ParseException e) {
			throw new BadQueryException();
		}
	}

	/**
	 * Searches for a given text in the field determined by fieldname, returns
	 * the top n results.
	 * 
	 * @param fieldName
	 *            name of field to search in
	 * @param text
	 *            query text
	 * @param n
	 *            number of results returned
	 * @return
	 * @throws BadQueryException
	 *             when query is unparseable
	 * @throws IOException
	 *             when IO errors happen
	 */
	public TopDocs simpleSearch(String fieldName, String text, int n) throws BadQueryException,
			IOException {
		Query q = createQuery(fieldName, text);
		return searcher.search(q, n);
	}
}
