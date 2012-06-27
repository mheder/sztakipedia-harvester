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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WikiArticleFilter {
	private List<String> nonArticleTitles;
	private List<String> redirects;

	/**
	 * 
	 * @param nonArticleTitlesWords
	 * @param redirects
	 */
	public WikiArticleFilter(String[] nonArticleTitlesWords, String[] redirects) {
		this.redirects = new ArrayList<String>();
		this.nonArticleTitles = new ArrayList<String>();
		for (String title : nonArticleTitlesWords) {
			this.nonArticleTitles.add(title);
		}
		for (String redirect : redirects) {
			this.redirects.add(redirect);
		}

	}

	/**
	 * 
	 * @param nonArticleTitlesWords
	 * @param redirects
	 */
	public WikiArticleFilter(List<String> nonArticleTitlesWords, List<String> redirects) {
		this.redirects = redirects;
		this.nonArticleTitles = nonArticleTitlesWords;
	}

	/**
	 * 
	 * @param nonArticleTitles
	 * @param redirects
	 * @throws IOException
	 */
	public WikiArticleFilter(File nonArticleTitlesFile, File redirectsFile) throws IOException {
		this.redirects = readFile(redirectsFile);
		this.nonArticleTitles = readFile(nonArticleTitlesFile);
	}

	public List<String> readFile(File aFile) throws IOException {
		List<String> toReturn = new ArrayList<String>();

		// FileReader always assumes default encoding is OK!
		BufferedReader input = new BufferedReader(new FileReader(aFile));
		try {
			String line = null;

			while ((line = input.readLine()) != null) {
				toReturn.add(line);
			}
		} finally {
			input.close();
		}

		return toReturn;
	}

	public boolean isNotRedirect(WikiArticle article) {
		boolean result = true;
		for (String redirect : redirects) {
			if (matchRegex(article.getText(), redirect)) {
				result = false;
				break;
			}
		}
		return result;
	}

	public boolean isValidArticle(WikiArticle article) {
		boolean result = true;

		result = isNotRedirect(article);

		for (String invalidTitle : nonArticleTitles) {
			if (matchRegex(article.getTitle(), invalidTitle)) {
				result = false;
				break;
			}
		}
		return result;
	}

	private boolean matchRegex(String input, String regex) {
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(input);
		return m.find();
	}

}
