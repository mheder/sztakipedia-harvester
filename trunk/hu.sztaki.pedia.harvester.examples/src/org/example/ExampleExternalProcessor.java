package org.example;

import hu.sztaki.pedia.uima.consumer.util.IExternalProcessor;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.uima.SentenceAnnotation;
import org.apache.uima.TokenAnnotation;
import org.apache.uima.WikiArticleAnnotation;
import org.apache.uima.WikiCategoryAnnotation;
import org.apache.uima.WikiLinkAnnotation;
import org.apache.uima.cas.text.AnnotationIndex;
import org.apache.uima.jcas.tcas.Annotation;

public class ExampleExternalProcessor implements IExternalProcessor {

	@Override
	public void initialize(String[] argv) {
		System.out.println("Init params:");
		for (String arg : argv) {
			System.out.println(arg);
		}
	}

	@Override
	public void processAnnotations(Map<Integer, AnnotationIndex<Annotation>> annotationIndexMap,
			Map<String, String> sofaTextToName) {
		System.out.println("View names:");
		for (String sofaName : sofaTextToName.keySet()) {
			System.out.print(sofaName + ", ");
		}
		String sofaCoveredText = sofaTextToName.get("parsed");

		StringBuilder sb = new StringBuilder();
		AnnotationIndex<Annotation> articleIndex = annotationIndexMap
				.get(WikiArticleAnnotation.type);
		WikiArticleAnnotation article = (WikiArticleAnnotation) articleIndex.iterator().get();
		System.out.println("\nProcessed article:" + article.getId() + ", " + article.getTitle()
				+ ", " + article.getRevision() + ", " + article.getApplication() + ", "
				+ article.getLang());
		if (sofaCoveredText != null) {
			System.out.println("Total text length: " + sofaCoveredText.length());
		}
		sb.append("\n Links found:");
		for (Annotation annotation : annotationIndexMap.get(WikiLinkAnnotation.type)) {
			WikiLinkAnnotation linkAnnotation = (WikiLinkAnnotation) annotation;
			sb.append("\nTitle:" + linkAnnotation.getTitle());
			sb.append(", Surface form: " + linkAnnotation.getCoveredText());
			sb.append(", URL: " + linkAnnotation.getHref());
		}
		sb.append("\n Categories found:\n");
		for (Annotation annotation : annotationIndexMap.get(WikiCategoryAnnotation.type)) {
			WikiCategoryAnnotation categoryAnnotation = (WikiCategoryAnnotation) annotation;
			sb.append(categoryAnnotation.getName() + "; ");
		}
		sb.append("\n Sentences found:");
		for (Annotation annotation : annotationIndexMap.get(SentenceAnnotation.type)) {
			SentenceAnnotation sentenceAnnotation = (SentenceAnnotation) annotation;
			sb.append("\nFrom:" + sentenceAnnotation.getBegin() + " to: "
					+ sentenceAnnotation.getEnd());
			String text = sentenceAnnotation.getCoveredText();
			if (text.length() <= 40) {
				sb.append(" text: " + text.trim());
			} else {
				sb.append(" text: " + text.substring(0, 40).trim() + " ...");
			}
		}
		sb.append("\n Unique tokens found:\n");
		Set<String> uniqTokens = new HashSet<String>();
		for (Annotation annotation : annotationIndexMap.get(TokenAnnotation.type)) {
			TokenAnnotation tokenAnnotation = (TokenAnnotation) annotation;
			uniqTokens.add(tokenAnnotation.getCoveredText());
		}
		for (String token : uniqTokens) {
			sb.append(token + "; ");
		}

		System.out.println(sb.toString());

	}
}
