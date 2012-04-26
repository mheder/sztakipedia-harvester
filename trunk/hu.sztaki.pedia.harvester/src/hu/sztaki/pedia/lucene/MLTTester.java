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

import hu.sztaki.pedia.lucene.util.HunPosChainIndexerUtil;
import hu.sztaki.pedia.lucene.util.HunStemChainIndexerUtil;
import hu.sztaki.pedia.lucene.util.IChainIndexerUtil;

import java.io.IOException;
import java.util.List;

import org.apache.lucene.document.Document;

public class MLTTester {
	private IChainIndexerUtil indexerUtil;
	private MoreLikeThisWrapper mltw;

	public MLTTester(String utilName, String indexDir) throws IOException {
		if ("hun_pos".startsWith(utilName)) {
			indexerUtil = new HunPosChainIndexerUtil(null);
		} else if ("hun_stem".startsWith(utilName)) {
			indexerUtil = new HunStemChainIndexerUtil();
		} else
			throw new RuntimeException("Invalid indexer util name!");
		mltw = new MoreLikeThisWrapper(indexDir, indexerUtil.getSentenceAnalyzer());
	}

	public static void main(String[] args) {
		String indexDir = "/home/tfarkas/Dev/UIMA/batchprocess/indexes/hunstem";
		String text = "Lucius Cornelius Scipio Barbatus (Kr. e. 4. század – Kr. e. 3. század) római politikus, hadvezér, a patrícius származású Cornelia gens tagja volt. Két fia, Cnaeus Asina és Lucius egyaránt elérte a consuli rangot. "
				+ "Apját Cnaeusnak hívták, akiről közelebbit nem tudunk. Kr. e. 298-ban Cnaeus Fulvius Maximus Centumalus kollégájaként consuli magistraturát viselt, és Volterrae mellett legyőzte az etruszkokat a III. szamnisz háború keretében. Kr. e. 297-ben, majd Kr. e. 295-ben ismét Quintus Fabius Maximus Rullianus és Publius Decius Mus consulok alatt szolgált: először az előbbi legatusaként a szamniszok ellen, majd propraetorként a nagy offenzívában a kelták, a szamniszok és etruszkok ellen. Kr. e. 293-ban Lucius Papirius Cursor alatt szolgált a döntő hadjáratban."
				+ "Az eddig ismertetett életút Livius történeti munkájából ismeretes, de ellentmond neki a Scipiók síremlékében talált epitáfium-felirat, amely egy szót sem ejt etruriai győzelmekről, ellenben apuliai és samniumi hódításokat emleget. Ezen tettek valószínűleg Kr. e. 297-hez kötődnek."
				+ "Hasonló nevű rokonaihoz lásd: Lucius Cornelius Scipio.";
		try {
			MLTTester tester = new MLTTester("hun_stem", indexDir);
			List<Document> docs = tester.mltw.getMLTDocuments(text, Indexer.SENTENCES_FIELD_NAME,
					10);
			for (Document document : docs) {
				System.out.println(document.get(Indexer.ID_FIELD_NAME));
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
