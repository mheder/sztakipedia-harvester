package hu.sztaki.pedia.uima.reader.util;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

public class HTTPReadHandler extends AbstractHandler {
	private ArrayBlockingQueue<WikiArticle> queue = null;

	public HTTPReadHandler(ArrayBlockingQueue<WikiArticle> queue) {
		this.queue = queue;
	}

	@Override
	public void handle(String target, Request baseRequest, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {

		WikiArticle article = assembleArticle(request.getParameterMap());
		if (queue != null) {
			try {
				queue.put(article);
				response.getWriter().println(
						"Article " + article.getTitle() + "(Rev:" + article.getRevision()
								+ "), accepted. Queue util:" + queue.size() + " items, "
								+ queue.remainingCapacity() + " remaining.");
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		response.setContentType("text/html;charset=utf-8");
		response.setStatus(HttpServletResponse.SC_OK);
		baseRequest.setHandled(true);
		// System.out.println("AttributeNames:");
		// Enumeration attrNames = request.getAttributeNames();
		// while (attrNames.hasMoreElements()) {
		// System.out.println(attrNames.nextElement());
		// }
		// System.out.println("ParamNames:");
		// Enumeration paramNames = request.getParameterNames();
		// while (paramNames.hasMoreElements()) {
		// System.out.println(paramNames.nextElement());
		// }

	}

	private WikiArticle assembleArticle(Map<String, String[]> parameters) {
		WikiArticle article = new WikiArticle();
		article.setId(new Long(parameters.get("articleid")[0]));
		article.setApplication(parameters.get("applicationName")[0]);
		article.setText(parameters.get("text")[0]);
		article.setLanguage(parameters.get("language")[0]);
		article.setTitle(parameters.get("title")[0]);
		article.setRevision(new Long(parameters.get("revision")[0]));
		return article;
	}

}
