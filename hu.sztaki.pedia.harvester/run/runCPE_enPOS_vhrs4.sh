#!/bin/bash
if [ "$UIMA_HOME" = "" ]
then
  echo UIMA_HOME environment variable is not set
  exit 1
fi

#if [ $# -lt 2 ]
#  then echo " an optional Java home path. \n Usage: $0 <input queue name>  <path to data directory> [Java home]"
#       exit 1;
#fi;
WD=$UIMA_HOME/pedia_uima_harvester 
CLASSPATH=$WD/pedia.uima.harvester_lib:$WD
#export CLASSPATH

JAVA=java
INPUTFILE=/wikidata/WikidumpEnglish/enwikitest.xml
LUCENEINDEXDIR=/wikidata/niif-wikidata-mp/uimacpe/indexes/en_POS
XCASOUTPUTDIR=/wikidata/niif-wikidata-mp/uimacpe/output/en_xcas

THREADCOUNT=1

mkdir -p $LUCENEINDEXDIR;
mkdir -p $XCASOUTPUTDIR;

sed "s#__INPUTFILE#$INPUTFILE#; s#__LUCENEINDEXDIR#$LUCENEINDEXDIR#; s#__XCASOUTPUTDIR#$XCASOUTPUTDIR#" $WD/descriptors/CPE/en_WdCR_ParserStemmerIndexer_CPE.xml  > $WD/descriptors/CPE/cpe_current.xml

sed -i "s/processingUnitThreadCount=\"[0-9]*\"/processingUnitThreadCount=\"$THREADCOUNT\"/" $WD/descriptors/CPE/cpe_current.xml

$JAVA -Xmx16g -cp $CLASSPATH -jar pedia.uima.harvester.jar $WD/descriptors/CPE/cpe_current.xml 2>&1 > /wikidata/niif-wikidata-mp/uimacpe/output-enPOS-`date +%d.%m.%y-%H%M`.log
