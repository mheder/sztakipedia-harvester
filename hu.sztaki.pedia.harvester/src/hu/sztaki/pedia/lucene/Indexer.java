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
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.NIOFSDirectory;

public class Indexer {
	public static final String SENTENCES_FIELD_NAME = "SENTENCES";
	public static final String TOKENS_FIELD_NAME = "TOKENS";
	public static final String LINKS_FIELD_NAME = "LINKS";
	public static final String CATEGORY_FIELD_NAME = "CATEGORIES";
	public static final String TEMPLATES_FIELD_NAME = "TEMPLATES";
	public static final String REVISION_FIELD_NAME = "REVISION_ID";
	public static final String ID_FIELD_NAME = "ARTICLE_ID";

	private static final int COMMIT_THRESHOLD = 1000;
	private static int countToCommit = 0;
	private static volatile boolean writerInitialized = false;

	private static volatile Indexer indexerInstance = new Indexer();

	private IndexWriter writer;
	private Searcher searcher;
	private Directory indexDirectory;
	private Logger logger = Logger.getLogger("Lucene Indexer");

	public static Indexer getInstance() {
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

	// /**
	// * Creates an Indexer instance with analysers provided in
	// IndexWriterConfig
	// * outside
	// *
	// * @param indexDirPath
	// * @param iwc
	// * @throws IOException
	// */
	// private Indexer(String indexDirPath, IndexWriterConfig iwc) throws
	// IOException {
	// Directory dir = initIndexDir(indexDirPath);
	// writer = new IndexWriter(dir, iwc);
	// }

	private Indexer() {
	}

	// /**
	// * Default Indexer constructor, creates analyzers for custom hunPosChain
	// sentences.
	// * @param indexDirPath
	// * @throws IOException
	// */
	// @Deprecated
	// public Indexer(String indexDirPath) throws IOException {
	//
	// Directory dir = initIndexDir(indexDirPath);
	// // analyzers
	// Set<String> stopDelimiters = new HashSet<String>();
	// stopDelimiters.add(STOP_DELIMITER);
	//
	// // per field different analyzer, the default is Keyword
	// PerFieldAnalyzerWrapper pfaWrapper = new PerFieldAnalyzerWrapper(
	// new KeywordAnalyzer());
	// pfaWrapper.addAnalyzer(SENTENCES_FIELD_NAME, new StopAnalyzer(
	// Version.LUCENE_34, stopDelimiters));
	//
	// IndexWriterConfig iwc = new IndexWriterConfig(Version.LUCENE_34,
	// pfaWrapper);
	// writer = new IndexWriter(dir, iwc);
	// }

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

	private void indexSentences(List<String> sentences, Document doc) {
		for (String sentence : sentences) {
			// doc.add(new Field(SENTENCES_FIELD_NAME, sentence, Field.Store.NO,
			// Field.Index.ANALYZED, Field.TermVector.WITH_POSITIONS_OFFSETS));
			doc.add(new Field(SENTENCES_FIELD_NAME, sentence, Field.Store.YES,
					Field.Index.ANALYZED, Field.TermVector.WITH_POSITIONS_OFFSETS));
		}
	}

	private void indexLemmas(Set<String> tokens, Document doc) {
		Iterator<String> it = tokens.iterator();
		while (it.hasNext()) {
			String lemma = it.next();
			if (lemma != null) {
				// doc.add(new Field(TOKENS_FIELD_NAME, lemma, Field.Store.NO,
				// Field.Index.ANALYZED));
				doc.add(new Field(TOKENS_FIELD_NAME, lemma, Field.Store.YES, Field.Index.ANALYZED));
			}
		}
	}

	public void index(String articleID, List<String> sentences, Set<String> tokens)
			throws CorruptIndexException, IOException, IndexWriterNotInitializedException {
		if (isWriterInitialized()) {
			Document doc = new Document();
			doc.add(new Field(ID_FIELD_NAME, articleID, Field.Store.YES, Field.Index.NOT_ANALYZED));
			indexSentences(sentences, doc);
			indexLemmas(tokens, doc);
			writer.updateDocument(new Term(ID_FIELD_NAME, articleID), doc);
			countToCommit++;
			if (countToCommit == COMMIT_THRESHOLD) {
				countToCommit = 0;
				commit();
			}
		} else
			throw new IndexWriterNotInitializedException();
	}

	public void index(String articleID, List<String> sentences, Set<String> tokens,
			Set<String> links, Set<String> templates) throws CorruptIndexException, IOException {
		Document doc = new Document();
		doc.add(new Field(ID_FIELD_NAME, articleID, Field.Store.YES, Field.Index.NOT_ANALYZED));
		indexSentences(sentences, doc);
		indexLemmas(tokens, doc);
		indexLinks(links, doc);
		indexTemplates(templates, doc);
		writer.updateDocument(new Term(ID_FIELD_NAME, articleID), doc);
		countToCommit++;
		if (countToCommit == COMMIT_THRESHOLD) {
			countToCommit = 0;
			commit();
		}
	}

	private void indexTemplates(Set<String> templates, Document doc) {
		for (String template : templates) {
			doc.add(new Field(TEMPLATES_FIELD_NAME, template, Field.Store.YES, Field.Index.ANALYZED));
		}
	}

	private void indexLinks(Set<String> links, Document doc) {
		for (String link : links) {
			doc.add(new Field(LINKS_FIELD_NAME, link, Field.Store.YES, Field.Index.ANALYZED));
		}
	}

	public static boolean isWriterInitialized() {
		return writerInitialized;
	}

	public static synchronized void setWriterInitialized(boolean writerInitialized) {
		Indexer.writerInitialized = writerInitialized;
	}

}
