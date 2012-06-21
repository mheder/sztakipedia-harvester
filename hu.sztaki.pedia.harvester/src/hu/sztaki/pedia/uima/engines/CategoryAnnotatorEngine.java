package hu.sztaki.pedia.uima.engines;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.uima.UimaContext;
import org.apache.uima.WikiArticleAnnotation;
import org.apache.uima.WikiCategoryAnnotation;
import org.apache.uima.WikiLinkAnnotation;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.text.AnnotationIndex;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;
import org.apache.uima.resource.ResourceInitializationException;

public class CategoryAnnotatorEngine extends AbstractMultiSofaAnnotator {
	public static final String CATEGORYPREFIXES_NAME_PARAM = "CategoryPrefixesWithLanguage";

	private Map<String, String> categoryPrefixes = new HashMap<String, String>();

	@Override
	public void initialize(UimaContext aContext) throws ResourceInitializationException {
		super.initialize(aContext);
		// a prefix is lang code + : + prefix, e.g: hu:Kategória, or en:Category
		String[] prefixes = (String[]) aContext
				.getConfigParameterValue(CATEGORYPREFIXES_NAME_PARAM);
		for (String langprefix : prefixes) {
			int delim = langprefix.indexOf(":");
			categoryPrefixes.put(langprefix.substring(0, delim), langprefix.substring(delim + 1));
		}
	}

	@Override
	public void process(JCas aJCas) throws AnalysisEngineProcessException {
		ArrayList<JCas> casList = getJCasList(aJCas);
		for (JCas jCas : casList) {
			WikiArticleAnnotation article = (WikiArticleAnnotation) jCas
					.getAnnotationIndex(WikiArticleAnnotation.type).iterator().get();
			AnnotationIndex<Annotation> linkIndex = jCas
					.getAnnotationIndex(WikiLinkAnnotation.type);
			List<Annotation> toRemove = new ArrayList<Annotation>();
			for (Annotation annotation : linkIndex) {
				WikiLinkAnnotation link = (WikiLinkAnnotation) annotation;
				if (categoryPrefixes.containsKey(article.getLang()) && link.getTitle() != null) {
					String catPrefix = categoryPrefixes.get(article.getLang());
					if (link.getTitle().startsWith(catPrefix + ":")) {
						WikiCategoryAnnotation category = new WikiCategoryAnnotation(jCas);
						category.setName(link.getTitle());
						category.addToIndexes();
						toRemove.add(annotation);
					}
				}
			}
			// Remove category links from LinkAnnotations,
			// because they are not real links
			for (Annotation annotation : toRemove) {
				annotation.removeFromIndexes();
			}
		}

	}

}
