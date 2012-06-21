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

import hu.sztaki.pedia.lucene.IndexFieldNames;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.KeywordAnalyzer;
import org.apache.lucene.analysis.PerFieldAnalyzerWrapper;
import org.apache.lucene.analysis.hu.HungarianAnalyzer;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.util.Version;
import org.apache.uima.SentenceAnnotation;
import org.apache.uima.TokenAnnotation;
import org.apache.uima.cas.text.AnnotationIndex;
import org.apache.uima.jcas.tcas.Annotation;

public class HunStemChainIndexerUtil implements IChainIndexerUtil {
	private PerFieldAnalyzerWrapper pfaWrapper;
	private Analyzer sentenceAnalyzer;

	public HunStemChainIndexerUtil() {
		// per field different analyzer, the default is Keyword
		pfaWrapper = new PerFieldAnalyzerWrapper(new KeywordAnalyzer());
		sentenceAnalyzer = new HungarianAnalyzer(Version.LUCENE_34);
		pfaWrapper.addAnalyzer(IndexFieldNames.SENTENCES_FIELD_NAME, sentenceAnalyzer);
	}

	@Override
	public IndexWriterConfig getIndexWriterConfig() {
		return new IndexWriterConfig(Version.LUCENE_34, pfaWrapper);
	}

	@Override
	public Set<String> getUniqLemmas(AnnotationIndex<Annotation> tokenIndex) {
		Set<String> uniqLemmas = new HashSet<String>();
		for (Annotation annotation : tokenIndex) {
			TokenAnnotation ta = (TokenAnnotation) annotation;
			// for collecting unique lemmas
			uniqLemmas.add(ta.getLemma());
		}
		return uniqLemmas;
	}

	@Override
	public List<String> getSentences(AnnotationIndex<Annotation> sentenceIndex,
			AnnotationIndex<Annotation> tokenIndex) {
		List<String> sentencesList = new ArrayList<String>();
		for (Annotation annotation : sentenceIndex) {
			SentenceAnnotation sa = (SentenceAnnotation) annotation;
			sentencesList.add(sa.getCoveredText());
		}
		return sentencesList;
	}

	@Override
	public Analyzer getSentenceAnalyzer() {
		return sentenceAnalyzer;
	}

}
