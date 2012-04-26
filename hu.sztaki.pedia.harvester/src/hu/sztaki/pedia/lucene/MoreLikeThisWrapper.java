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

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.similar.MoreLikeThis;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.NIOFSDirectory;

public class MoreLikeThisWrapper {
	private Directory directory;
	private IndexSearcher searcher;
	private IndexReader ir;
	private MoreLikeThis mlt;

	private int minTermFreq = 2;
	private int minWordLen = 4;
	private float percentTermsToMatch = 0.5f;

	public void setMinTerm(int minTerm) {
		this.minTermFreq = minTerm;
	}

	public MoreLikeThisWrapper(String indexDir, Analyzer analyzer) throws IOException {
		init(indexDir, analyzer);
	}

	public MoreLikeThisWrapper(String indexDir, Analyzer analyzer, int minTermFreq, int minWordLen)
			throws IOException {
		this.minTermFreq = minTermFreq;
		this.minWordLen = minWordLen;
		init(indexDir, analyzer);
	}

	private void init(String indexDir, Analyzer analyzer) throws IOException {
		directory = NIOFSDirectory.open(new File(indexDir));
		searcher = new IndexSearcher(directory, true);
		ir = IndexReader.open(directory);
		mlt = new MoreLikeThis(ir);
		mlt.setMinTermFreq(minTermFreq);
		mlt.setMinWordLen(minWordLen);
		mlt.setAnalyzer(analyzer);
		mlt.setMaxQueryTerms(50);
	}

	public List<Document> getMLTDocuments(String text, String fieldName, int topN)
			throws IOException {
		String[] mlFields = { fieldName };
		mlt.setFieldNames(mlFields);
		List<Document> docs = new ArrayList<Document>();
		StringReader sr = new StringReader(text);
		// Query mltQuery = mlt.like(sr, fieldName);
		BooleanQuery bq = (BooleanQuery) mlt.like(sr, fieldName);
		BooleanClause[] clauses = bq.getClauses();
		// make at least half the terms match
		bq.setMinimumNumberShouldMatch((int) (clauses.length * percentTermsToMatch));
		System.out.println("Q:" + bq.toString(fieldName));
		TopDocs tdocs = searcher.search(bq, topN);
		for (ScoreDoc scoreDoc : tdocs.scoreDocs) {
			docs.add(searcher.doc(scoreDoc.doc));
		}
		return docs;

	}

}
