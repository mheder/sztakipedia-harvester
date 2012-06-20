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
import hu.sztaki.pedia.lucene.exceptions.NewerVersionStoredException;
import hu.sztaki.pedia.lucene.util.IChainIndexerUtil;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.NIOFSDirectory;
import org.apache.uima.SentenceAnnotation;
import org.apache.uima.TokenAnnotation;
import org.apache.uima.WikiArticleAnnotation;
import org.apache.uima.WikiLinkAnnotation;
import org.apache.uima.WikiTemplateAnnotation;
import org.apache.uima.cas.text.AnnotationIndex;
import org.apache.uima.jcas.tcas.Annotation;

public class LuceneIndexer {
	private final int COMMIT_THRESHOLD = 100;
	private int countToCommit = 0;
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

	/**
	 * Private constructor, as indexer should be singleton.
	 */
	private LuceneIndexer() {
	}

	/**
	 * Commits the indexes, the changes can be seen fron this point
	 * 
	 * @throws CorruptIndexException
	 * @throws IOException
	 */
	public void commit() throws CorruptIndexException, IOException {
		logger.info("Commit");
		writer.commit();
	}

	/**
	 * Closes the IndexWriter
	 * 
	 * @throws IOException
	 */
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

	/**
	 * Stores the annotations given in a map in a Lucene index, specified by the
	 * Sztakipedia application
	 * 
	 * @param annotationIndexMap
	 *            Annotation indexes mapped by the type code of the Annotation
	 *            type
	 * @throws NewerVersionStoredException
	 *             if the document is already stored with the same, or newer
	 *             revision number
	 */
	public void indexAnnotations(Map<Integer, AnnotationIndex<Annotation>> annotationIndexMap)
			throws NewerVersionStoredException {
		WikiArticleAnnotation wikiArticleAnnotation = (WikiArticleAnnotation) annotationIndexMap
				.get(WikiArticleAnnotation.type).iterator().get();
		if (isNewerVersionStored(wikiArticleAnnotation)) {
			throw new NewerVersionStoredException(wikiArticleAnnotation);
		}
		Document doc = new Document();
		indexArticleMetadata(wikiArticleAnnotation, doc);
		indexSentences(annotationIndexMap.get(SentenceAnnotation.type),
				annotationIndexMap.get(TokenAnnotation.type), doc);
		indexLemmas(annotationIndexMap.get(TokenAnnotation.type), doc);
		indexLinks(annotationIndexMap.get(WikiLinkAnnotation.type), doc);
		indexTemplates(annotationIndexMap.get(WikiTemplateAnnotation.type), doc);
		try {
			writer.updateDocument(
					new Term(IndexFieldNames.ID_FIELD_NAME, Long.toString(wikiArticleAnnotation
							.getId())), doc);

			countToCommit++;
			if (countToCommit == COMMIT_THRESHOLD) {
				countToCommit = 0;
				commit();
			}
		} catch (CorruptIndexException e) {
			logger.error(e.getMessage());
		} catch (IOException e) {
			logger.error(e.getMessage());
		}

	}

	/**
	 * Writes the TEMPLATES fields of the stored document.
	 * 
	 * @param templateIndex
	 * @param doc
	 */
	private void indexTemplates(AnnotationIndex<Annotation> templateIndex, Document doc) {
		for (Annotation annotation : templateIndex) {
			WikiTemplateAnnotation wikiTemplate = (WikiTemplateAnnotation) annotation;
			doc.add(new Field(IndexFieldNames.TEMPLATES_FIELD_NAME, wikiTemplate.getName(),
					Field.Store.YES, Field.Index.ANALYZED));
		}
	}

	/**
	 * Writes the LINKS fields of the stored document.
	 * 
	 * @param linkIndex
	 * @param doc
	 */
	private void indexLinks(AnnotationIndex<Annotation> linkIndex, Document doc) {
		for (Annotation annotation : linkIndex) {
			WikiLinkAnnotation wikiLink = (WikiLinkAnnotation) annotation;
			doc.add(new Field(IndexFieldNames.LINKS_FIELD_NAME, wikiLink.getHref(),
					Field.Store.YES, Field.Index.ANALYZED));
		}
	}

	/**
	 * Writes the SENTENCES fields of the stored document.
	 * 
	 * @param sentenceIndex
	 * @param tokenIndex
	 * @param doc
	 */
	private void indexSentences(AnnotationIndex<Annotation> sentenceIndex,
			AnnotationIndex<Annotation> tokenIndex, Document doc) {
		List<String> sentences = chainIndexerUtil.getSentences(sentenceIndex, sentenceIndex);
		for (String sentence : sentences) {
			// doc.add(new Field(SENTENCES_FIELD_NAME, sentence, Field.Store.NO,
			// Field.Index.ANALYZED, Field.TermVector.WITH_POSITIONS_OFFSETS));
			doc.add(new Field(IndexFieldNames.SENTENCES_FIELD_NAME, sentence, Field.Store.YES,
					Field.Index.ANALYZED, Field.TermVector.WITH_POSITIONS_OFFSETS));
		}
	}

	/**
	 * Adds Wiki article metadata to the Lucene Document, such as the article
	 * ID, revision, language, title, and the application (e.g en.wikipedia.org,
	 * or mywiki.example.org... )
	 * 
	 * @param wikiArticleAnnotation
	 * @param doc
	 */
	private void indexArticleMetadata(WikiArticleAnnotation wikiArticleAnnotation, Document doc) {
		doc.add(new Field(IndexFieldNames.ID_FIELD_NAME, getArticleID(wikiArticleAnnotation),
				Field.Store.YES, Field.Index.NOT_ANALYZED));
		doc.add(new Field(IndexFieldNames.REVISION_FIELD_NAME, Long.toString(wikiArticleAnnotation
				.getRevision()), Field.Store.YES, Field.Index.NOT_ANALYZED));
		doc.add(new Field(IndexFieldNames.APP_FIELD_NAME, wikiArticleAnnotation.getApplication(),
				Field.Store.YES, Field.Index.ANALYZED));
		doc.add(new Field(IndexFieldNames.LANG_FIELD_NAME, wikiArticleAnnotation.getLang(),
				Field.Store.YES, Field.Index.ANALYZED));
		doc.add(new Field(IndexFieldNames.TITLE_FIELD_NAME, wikiArticleAnnotation.getTitle(),
				Field.Store.YES, Field.Index.ANALYZED));
	}

	/**
	 * Writes the TOKENS fields of the stored document.
	 * 
	 * @param tokenIndex
	 * @param doc
	 */
	private void indexLemmas(AnnotationIndex<Annotation> tokenIndex, Document doc) {
		Set<String> tokens = chainIndexerUtil.getUniqLemmas(tokenIndex);
		Iterator<String> it = tokens.iterator();
		while (it.hasNext()) {
			String lemma = it.next();
			if (lemma != null) {
				// doc.add(new Field(TOKENS_FIELD_NAME, lemma, Field.Store.NO,
				// Field.Index.ANALYZED));
				doc.add(new Field(IndexFieldNames.TOKENS_FIELD_NAME, lemma, Field.Store.YES,
						Field.Index.ANALYZED));
			}
		}
	}

	/**
	 * Checks whether the given Article is stored, and if stored checks the
	 * revision version to be newer then the stored.
	 * 
	 * @param id
	 * @param revisionID
	 * @return
	 */
	private boolean isNewerVersionStored(WikiArticleAnnotation wikiArticleAnnotation) {
		boolean isStored = false;
		try {
			List<Document> result = searcher.simpleSearch(IndexFieldNames.ID_FIELD_NAME,
					getArticleID(wikiArticleAnnotation), 1);
			if (result.size() == 1) {
				Document doc = result.get(0);
				long revFound = Long.parseLong(doc.get(IndexFieldNames.REVISION_FIELD_NAME));
				if (revFound >= wikiArticleAnnotation.getRevision()) {
					isStored = true;
				}
			}
		} catch (BadQueryException e) {
			// e.printStackTrace();
			logger.error(e.getMessage());
		} catch (IOException e) {
			// e.printStackTrace();
			logger.error(e.getMessage());
		}
		return isStored;
	}

	/**
	 * Creates a unique ID for articles by concatenating the article's DB id
	 * from the Wiki with the application's name where it comes from
	 * 
	 * @param wikiArticleAnnotation
	 * @return the generated ID for the current article
	 */
	private String getArticleID(WikiArticleAnnotation wikiArticleAnnotation) {
		return "" + wikiArticleAnnotation.getApplication() + "_" + wikiArticleAnnotation.getId();
	}

}
