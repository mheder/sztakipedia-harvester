package hu.sztaki.pedia.uima.engines;

import hunposchain.HunPosChain;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.uima.SentenceAnnotation;
import org.apache.uima.TokenAnnotation;
import org.apache.uima.UimaContext;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.FSIterator;
import org.apache.uima.cas.text.AnnotationIndex;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;
import org.apache.uima.resource.ResourceInitializationException;

public class HunPosEngine extends AbstractMultiSofaAnnotator {

	private HunPosChain hpc;
	private Logger logger = Logger.getLogger("HunPosEngine");

	@Override
	public void initialize(UimaContext aContext) throws ResourceInitializationException {
		super.initialize(aContext);
		this.hpc = new HunPosChain();
		logger.debug("initialize");

	}

	@Override
	public void process(JCas jCas) throws AnalysisEngineProcessException {
		ArrayList<JCas> casList = getJCasList(jCas);

		for (JCas doc : casList) {
			logger.debug("process");
			AnnotationIndex<Annotation> sentenceIndex = null;
			sentenceIndex = doc.getAnnotationIndex(SentenceAnnotation.type);

			FSIterator<Annotation> sentenceIterator = null;
			sentenceIterator = sentenceIndex.iterator();

			AnnotationIndex<Annotation> tokenIndex = null;
			tokenIndex = doc.getAnnotationIndex(TokenAnnotation.type);

			FSIterator<Annotation> tokenIterator = null;

			SentenceAnnotation sentence = null;
			TokenAnnotation token = null;
			List<String> sentenceTokens = null;
			String lemma;
			String posHun;

			while (sentenceIterator.hasNext()) {
				sentence = (SentenceAnnotation) sentenceIterator.next();
				// csak a magyar mondatokra fusson le az elemzes
				String sentenceLang = sentence.getLang();
				if ("hu".equals(sentenceLang) || sentenceLang == null) {
					tokenIterator = tokenIndex.subiterator(sentence);
					sentenceTokens = new LinkedList<String>();
					while (tokenIterator.hasNext()) {
						token = (TokenAnnotation) tokenIterator.next();
						sentenceTokens.add(token.getCoveredText().trim());
					}

					tokenIterator.moveToFirst();

					while (tokenIterator.hasNext()) {
						for (String s : hpc.analyseSentence(sentenceTokens)) {
							token = (TokenAnnotation) tokenIterator.next();
							lemma = s.substring(0, s.lastIndexOf("/"));
							posHun = s.substring(s.lastIndexOf("/") + 1, s.length());
							token.setLemma(lemma);
							token.setPosTag(posHun);
						}
					}
				}
			}
		}
	}
}
