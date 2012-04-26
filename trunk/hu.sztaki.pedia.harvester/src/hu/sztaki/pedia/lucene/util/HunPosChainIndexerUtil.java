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
package hu.sztaki.pedia.lucene.util;

import hu.sztaki.pedia.lucene.Indexer;
import hu.sztaki.pedia.uima.util.MSDCategory;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.KeywordAnalyzer;
import org.apache.lucene.analysis.PerFieldAnalyzerWrapper;
import org.apache.lucene.analysis.StopAnalyzer;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.util.Version;
import org.apache.uima.SentenceAnnotation;
import org.apache.uima.TokenAnnotation;
import org.apache.uima.cas.FSIterator;
import org.apache.uima.cas.text.AnnotationIndex;
import org.apache.uima.jcas.tcas.Annotation;

public class HunPosChainIndexerUtil implements IChainIndexerUtil {

	public static String STOP_DELIMITER = "skippedworddelimitertoken";
	public static final String SPACE = " ";

	private PerFieldAnalyzerWrapper pfaWrapper;
	private Analyzer sentenceAnalyzer;

	public HunPosChainIndexerUtil(String delimiter) {
		if (delimiter != null) {
			STOP_DELIMITER = delimiter;
		}
		Set<String> stopDelimiters = new HashSet<String>();
		stopDelimiters.add(STOP_DELIMITER);

		// per field different analyzer, the default is Keyword
		pfaWrapper = new PerFieldAnalyzerWrapper(new KeywordAnalyzer());
		sentenceAnalyzer = new StopAnalyzer(Version.LUCENE_34, stopDelimiters);
		pfaWrapper.addAnalyzer(Indexer.SENTENCES_FIELD_NAME, sentenceAnalyzer);
	}

	@Override
	public Set<String> getUniqLemmas(AnnotationIndex<Annotation> tokenIndex) {
		Set<String> uniqLemmas = new HashSet<String>();
		for (Annotation annotation : tokenIndex) {
			TokenAnnotation ta = (TokenAnnotation) annotation;
			// for collecting unique lemmas
			if (isValidWordToken(ta.getPosTag())) {
				uniqLemmas.add(ta.getLemma());
			}
		}
		return uniqLemmas;

	}

	@Override
	public List<String> getSentences(AnnotationIndex<Annotation> sentenceIndex,
			AnnotationIndex<Annotation> tokenIndex) {
		List<String> sentencesList = new ArrayList<String>();
		for (Annotation annotation : sentenceIndex) {
			SentenceAnnotation sa = (SentenceAnnotation) annotation;
			FSIterator<Annotation> tokenIterator = tokenIndex.subiterator(sa);
			StringBuilder sb = new StringBuilder();
			while (tokenIterator.hasNext()) {
				TokenAnnotation ta = (TokenAnnotation) tokenIterator.next();
				if (sb.length() > 0) {
					sb.append(SPACE);
				}
				if (isImportantWordByCategory(ta.getPosTag())) {
					sb.append(ta.getCoveredText());
				} else {
					sb.append(STOP_DELIMITER);
				}
			}
			sentencesList.add(sb.toString());
		}
		return sentencesList;
	}

	@Override
	public IndexWriterConfig getIndexWriterConfig() {
		return new IndexWriterConfig(Version.LUCENE_34, pfaWrapper);
	}

	protected boolean isImportantWordByCategory(String posTag) {
		boolean result;
		try {
			MSDCategory category = null;
			for (MSDCategory msdcat : MSDCategory.values()) {
				if (posTag.startsWith(msdcat.abbreviation())) {
					category = msdcat;
				}
			}
			switch (category) {
			case NOUN:
			case VERB:
			case ADVERB:
			case ADJECTIVE:
			case RESIDUAL:
				result = true;
				break;
			default:
				result = false;
				break;
			}
		} catch (Exception e) {
			result = false;
		}
		return result;
	}

	protected boolean isValidWordToken(String posTag) {
		boolean result = false;
		if (posTag != null) {
			for (MSDCategory msdcat : MSDCategory.values()) {
				if (!result && posTag.startsWith(msdcat.abbreviation())) {
					result = true;
				}
			}
		}
		return result;
	}

	@Override
	public Analyzer getSentenceAnalyzer() {
		return sentenceAnalyzer;
	}
}
