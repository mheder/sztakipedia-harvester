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

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.Explanation;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

public class Explainer {
	public static void main(String[] args) throws Exception {
		if (args.length != 2) {
			System.err.println("Usage: Explainer <index dir> <query>");
			System.exit(1);
		}
		String indexDir = args[0];
		String queryExpression = args[1];
		Directory directory = FSDirectory.open(new File(indexDir));

		QueryParser parser = new QueryParser(Version.LUCENE_34, "words", new StandardAnalyzer(
				Version.LUCENE_34));
		Query query = parser.parse(queryExpression);
		System.out.println("Query: " + queryExpression);
		IndexSearcher searcher = new IndexSearcher(directory, true);
		TopDocs topDocs = searcher.search(query, 1);
		for (ScoreDoc match : topDocs.scoreDocs) {
			Explanation explanation = searcher.explain(query, match.doc);
			System.out.println("----------");
			Document doc = searcher.doc(match.doc);
			System.out.println("D:" + doc.get("docID"));
			System.out.println("Lang:" + doc.get("lang"));
			System.out.println(explanation.toString());
			// Explanation[] explanations = explanation.getDetails();
			// for (Explanation ex : explanations) {
			// System.out.println(ex.getDescription()+" = "+ex.getValue());
			// }

		}
		searcher.close();
		directory.close();
	}
}
