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
package hu.sztaki.pedia.uima.util;

import java.io.File;

public class LuceneUtil {

	public static String getIdFromURI(String uri) {
		String result = uri.substring(uri.lastIndexOf(File.separatorChar) + 1);
		result = result.trim();
		if (result.endsWith(".xml")) {
			result = result.substring(0, result.length() - 4);
		}

		return result;
	}
}
