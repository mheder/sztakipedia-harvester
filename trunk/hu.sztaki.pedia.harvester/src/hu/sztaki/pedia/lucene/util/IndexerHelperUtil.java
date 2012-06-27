package hu.sztaki.pedia.lucene.util;

import hu.sztaki.pedia.lucene.IndexFieldNames;
import hu.sztaki.pedia.lucene.Searcher;
import hu.sztaki.pedia.lucene.exceptions.BadQueryException;

import java.io.IOException;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.lucene.document.Document;
import org.apache.uima.WikiArticleAnnotation;

public class IndexerHelperUtil {
	public static Logger logger = Logger.getLogger(IndexerHelperUtil.class);

	/**
	 * Creates a unique ID for articles by concatenating the article's DB id
	 * from the Wiki with the application's name where it comes from
	 * 
	 * @param wikiArticleAnnotation
	 * @return the generated ID for the current article
	 */
	public static String getArticleID(WikiArticleAnnotation wikiArticleAnnotation) {
		return "" + wikiArticleAnnotation.getApplication() + "_" + wikiArticleAnnotation.getId();
	}

	/**
	 * Checks whether the given Article is stored, and if stored checks the
	 * revision version to be newer then the stored.
	 * 
	 * @param id
	 * @param revisionID
	 * @return
	 */
	public static boolean isNewerVersionStored(WikiArticleAnnotation wikiArticleAnnotation,
			Searcher searcher) {
		boolean isStored = false;
		try {
			List<Document> result = searcher.simpleSearch(IndexFieldNames.ID_FIELD_NAME,
					IndexerHelperUtil.getArticleID(wikiArticleAnnotation), 1);
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

}
