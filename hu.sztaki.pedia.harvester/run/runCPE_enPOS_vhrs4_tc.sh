#!/bin/bash
if [ "$UIMA_HOME" = "" ]
then
  echo UIMA_HOME environment variable is not set
  exit 1
fi

if [ $# -lt 1 ]
  then echo " Usage: $0 <thread count>"
       exit 1;
fi;
THREADCOUNT=$1

WD=$UIMA_HOME/pedia_uima_harvester 
CLASSPATH=$WD/pedia.uima.harvester_lib:$WD
#export CLASSPATH

JAVA=java
INPUTDIR=/wikidata/WikidumpEnglish
#INPUTFILE=enwiki-20120403-pages-articles.xml
INPUTFILE=enwikitest2.xml
LUCENEINDEXDIR=/wikidata/niif-wikidata-mp/uimacpe/indexes/en_POS-$INPUTFILE-tc$THREADCOUNT
XCASOUTPUTDIR=/wikidata/niif-wikidata-mp/uimacpe/output/en_xcas-$INPUTFILE-tc$THREADCOUNT
LOGFILE=cpe-enPOS-$INPUTFILE-`date +%d.%m.%y-%H%M`_tc$THREADCOUNT.log
LOGDIR=/wikidata/niif-wikidata-mp/uimacpe/logs/cpe/en

mkdir -p $LUCENEINDEXDIR;
mkdir -p $XCASOUTPUTDIR;
mkdir -p $LOGDIR
sed "s#__INPUTFILE#$INPUTDIR/$INPUTFILE#; s#__LUCENEINDEXDIR#$LUCENEINDEXDIR#; s#__XCASOUTPUTDIR#$XCASOUTPUTDIR#" $WD/descriptors/CPE/en_WdCR_ParserStemmerIndexer_CPE.xml  > $WD/descriptors/CPE/cpe_current.xml

sed -i "s/<casProcessors casPoolSize=\"[0-9]*\" processingUnitThreadCount=\"[0-9]*\">/<casProcessors casPoolSize=\"$THREADCOUNT\" processingUnitThreadCount=\"$THREADCOUNT\">/" $WD/descriptors/CPE/cpe_current.xml

#sed -i "s/processingUnitThreadCount=\"[0-9]*\"/processingUnitThreadCount=\"$THREADCOUNT\"/" $WD/descriptors/CPE/cpe_current.xml

$JAVA -Xmx16g -cp $CLASSPATH -jar pedia.uima.harvester.jar $WD/descriptors/CPE/cpe_current.xml 2>&1 > $LOGDIR/$LOGFILE

