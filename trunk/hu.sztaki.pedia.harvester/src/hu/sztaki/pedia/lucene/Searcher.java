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
package hu.sztaki.pedia.lucene;

import hu.sztaki.pedia.lucene.exceptions.BadQueryException;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.similar.MoreLikeThis;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.NIOFSDirectory;
import org.apache.lucene.util.Version;

public class Searcher {

	private Analyzer analyzer;
	private Directory indexDirectory;
	private IndexSearcher searcher = null;
	private Map<String, QueryParser> parsers = new HashMap<String, QueryParser>();
	private MoreLikeThis moreLikeThis;
	private IndexReader indexReader;
	private File indexDirFile;

	private int minTermFreq = 2;
	private int minWordLength = 4;
	private float percentTermsToMatch = 0.5f;

	public void setMinTerm(int minTerm) {
		this.minTermFreq = minTerm;
	}

	/**
	 * initialize the MLT with parameters
	 */
	private void initMoreLikeThis() {
		moreLikeThis = new MoreLikeThis(indexReader);
		moreLikeThis.setMinTermFreq(minTermFreq);
		moreLikeThis.setMinWordLen(minWordLength);
		moreLikeThis.setAnalyzer(analyzer);
		moreLikeThis.setMaxQueryTerms(50);
	}

	/**
	 * 
	 * @param indexDirPath
	 * @param analyzer
	 * @param minTermFreq
	 * @param minWordLength
	 * @throws IOException
	 */
	public Searcher(String indexDirPath, Analyzer analyzer, int minTermFreq, int minWordLength)
			throws IOException {
		this(indexDirPath, analyzer);
		this.minTermFreq = minTermFreq;
		this.minWordLength = minWordLength;
		initMoreLikeThis();
	}

	/**
	 * 
	 * @param indexDirPath
	 * @param analyzer
	 * @throws IOException
	 */
	public Searcher(String indexDirPath, Analyzer analyzer) throws IOException {
		indexDirFile = new File(indexDirPath);
		if (!indexDirFile.exists()) {
			indexDirFile.mkdirs();
		}
		this.analyzer = analyzer;
		indexDirectory = NIOFSDirectory.open(indexDirFile);
		searcher = new IndexSearcher(indexDirectory, true);
		indexReader = IndexReader.open(indexDirectory);
		initMoreLikeThis();
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

	private List<Document> getDocuments(TopDocs topDocs) throws CorruptIndexException, IOException {
		List<Document> results = new ArrayList<Document>();
		for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
			results.add(searcher.doc(scoreDoc.doc));
		}
		return results;
	}

	/**
	 * Searches for a given text in the field determined by fieldname, returns
	 * the top n results.
	 * 
	 * @param fieldName
	 *            name of field to search in
	 * @param text
	 *            query text
	 * @param topN
	 *            number of results returned
	 * @return list of Lucene Documents matching
	 * @throws BadQueryException
	 *             when query is unparseable
	 * @throws IOException
	 *             when IO errors happen
	 */
	public List<Document> simpleSearch(String fieldName, String text, int topN)
			throws BadQueryException, IOException {
		Query q = createQuery(fieldName, text);
		return getDocuments(searcher.search(q, topN));
	}

	/**
	 * Searches for similar documents based on the given field and returns the
	 * top N documents (MoreLikeThis)
	 * 
	 * @param fieldName
	 * @param text
	 * @param topN
	 * @return list of similar Lucene Documents matching
	 * @throws IOException
	 */
	public List<Document> searchMLTDocuments(String fieldName, String text, int topN)
			throws IOException, CorruptIndexException {
		String[] mlFields = { fieldName };
		moreLikeThis.setFieldNames(mlFields);
		StringReader stringReader = new StringReader(text);
		BooleanQuery bq = (BooleanQuery) moreLikeThis.like(stringReader, fieldName);
		BooleanClause[] clauses = bq.getClauses();
		// make at least half the terms match
		bq.setMinimumNumberShouldMatch((int) (clauses.length * percentTermsToMatch));
		return getDocuments(searcher.search(bq, topN));
	}

}
