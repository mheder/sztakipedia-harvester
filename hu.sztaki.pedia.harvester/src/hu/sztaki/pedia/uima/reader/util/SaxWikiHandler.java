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
package hu.sztaki.pedia.uima.reader.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Arrays;
import java.util.concurrent.ArrayBlockingQueue;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class SaxWikiHandler extends DefaultHandler {

	protected int sleep = 50;
	protected boolean showStatus = true;
	protected StringBuilder textSB;
	protected StringBuilder titleB = new StringBuilder();
	protected int state = 0;
	protected int counter;
	protected String currentId;
	protected String currentTitle;
	protected String destinationDirectory = "pages";

	protected ArrayBlockingQueue<WikiArticle> queue;

	public boolean usingQueue = true;

	/**
	 * Creates a SaxWikiHandler object, using the provided
	 * {@link ArrayBlockingQueue} as an output source.
	 * 
	 * @param queue
	 *            ArrayBlockingQueue with WikiArticles for output
	 */
	public SaxWikiHandler(ArrayBlockingQueue<WikiArticle> queue) {
		this.queue = queue;
	}

	/**
	 * Creates a SaxWikiHandler object, using a folder named "pages" as an
	 * output source.
	 */
	public SaxWikiHandler() {
		usingQueue = false;
	}

	protected String replaces(String s) {
		s = s.replaceAll("&", "&amp;");
		s = s.replaceAll("\"", "&quot;");
		s = s.replaceAll("<", "&lt;");
		s = s.replaceAll(">", "&gt;");
		return s;
	}

	// private void write(String s) {
	// if (state == 5) {
	// textSB.append(s);
	// }
	// }

	protected void dumpArticleToQueue() {
		WikiArticle article = new WikiArticle();
		article.setId(currentId);
		article.setTitle(currentTitle);
		article.setText(textSB.toString());

		try {
			queue.put(article);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

	private void sendEOF() {
		EndOfFileToken eofToken = new EndOfFileToken();
		try {
			queue.put(eofToken);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

	protected void dumpArticleToFile() {
		try {
			StringBuilder articleSB = new StringBuilder();
			// calculate directories
			titleB.append(currentId);
			titleB.append(" ");
			titleB.append(currentTitle);
			titleB.append("\n");

			articleSB.append(currentId);
			articleSB.append(" ");
			articleSB.append(currentTitle);
			articleSB.append("\n");

			articleSB.append(textSB.toString());

			int currentIdi = Integer.parseInt(currentId);
			int x100K = currentIdi / 100000;
			int x1K = (currentIdi % 100000) / 1000;

			File tDir = new File(destinationDirectory + File.separator + (x100K * 100000)
					+ File.separator + ((x100K * 100000) + (x1K * 1000)));
			if (!tDir.canRead()) {
				tDir.mkdirs();
			}

			OutputStream fout = new FileOutputStream(tDir + File.separator + currentId + ".xml");
			OutputStream bout = new BufferedOutputStream(fout);
			OutputStreamWriter out = new OutputStreamWriter(bout, "UTF-8");

			out.write(articleSB.toString());
			out.close();
			counter++;
			if (showStatus) {
				if ((counter % 100) == 0) {
					System.out.print(counter + "-");
					if ((counter % 500) == 0) {
						System.out.println("-" + counter);

						OutputStream fout2 = new FileOutputStream("page-index.txt", true);
						OutputStream bout2 = new BufferedOutputStream(fout2);
						OutputStreamWriter out2 = new OutputStreamWriter(bout2, "UTF-8");
						out2.write(titleB.toString());
						out2.close();
						titleB = new StringBuilder();
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (sleep > 0) {
			try {
				Thread.sleep(sleep);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.xml.sax.helpers.DefaultHandler#characters(char[], int, int)
	 */
	@Override
	public void characters(char[] chs, int start, int length) throws SAXException {
		char[] charsToUse = Arrays.copyOfRange(chs, start, start + length);
		String s = new String(charsToUse);
		s = replaces(s);
		// write(s);
		if (state == 2) {
			currentTitle += s;
		}
		if (state == 4) {
			currentId += s;
		}
		if (state == 6) {
			textSB.append(s);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.xml.sax.helpers.DefaultHandler#ignorableWhitespace(char[], int,
	 * int)
	 */
	@Override
	public void ignorableWhitespace(char[] chs, int start, int length) throws SAXException {
		// char[] charsToUse = Arrays.copyOfRange(chs, start, start + length);
		// String s = new String(charsToUse);
		// write(s);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.xml.sax.helpers.DefaultHandler#startElement(java.lang.String,
	 * java.lang.String, java.lang.String, org.xml.sax.Attributes)
	 */
	@Override
	public void startElement(String uri, String localName, String qname, Attributes attrs)
			throws SAXException {
		if (localName.equals("page")) {
			textSB = new StringBuilder();
			state = 1;
		}
		if (state == 1 && localName.equals("title")) {
			currentTitle = new String();
			state = 2;
		}
		if (state == 3 && localName.equals("id")) {
			currentId = new String();
			state = 4;
		}
		if (state == 5 && localName.equals("text")) {
			state = 6;
		}
		// write("<" + qname);
		// for (int i = 0; i < attrs.getLength(); i++) {
		// write(" " + attrs.getQName(i) + "=\"" + attrs.getValue(i) + "\"");
		// }
		// write(">");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.xml.sax.helpers.DefaultHandler#endElement(java.lang.String,
	 * java.lang.String, java.lang.String)
	 */
	@Override
	public void endElement(String uri, String localName, String qname) throws SAXException {
		// write("</" + qname + ">");
		if (localName.equals("page")) {
			if (usingQueue) {
				dumpArticleToQueue();
			} else {
				dumpArticleToFile();
			}
		}
		if (localName.equals("text")) {
			state = 7;
		}
		if (localName.equals("id")) {
			state = 5;
		}
		if (localName.equals("title")) {
			state = 3;
		}
		if (localName.equals("mediawiki")) {
			sendEOF();
		}

	}

	/**
	 * @param sleep
	 *            the sleep to set
	 */
	public void setSleep(int sleep) {
		this.sleep = sleep;
	}

	/**
	 * @param statusOnConsole
	 *            the statusOnConsole to set
	 */
	public void setShowStatus(boolean statusOnConsole) {
		this.showStatus = statusOnConsole;
	}
}
