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
INPUTDIR=/wikidata/WikidumpHungarian
INPUTFILE=huwiki-20120405-pages-articles.xml
#INPUTFILE=enwikitest2.xml
LUCENEINDEXDIR=/wikidata/niif-wikidata-mp/uimacpe/indexes/hunStem-$INPUTFILE-tc$THREADCOUNT
XCASOUTPUTDIR=/wikidata/niif-wikidata-mp/uimacpe/output/hun_xcas-$INPUTFILE-tc$THREADCOUNT
LOGFILE=cpe-hunStem-$INPUTFILE-`date +%d.%m.%y-%H%M`_tc$THREADCOUNT.log
LOGDIR=/wikidata/niif-wikidata-mp/uimacpe/logs

mkdir -p $LUCENEINDEXDIR;
mkdir -p $XCASOUTPUTDIR;

sed "s#__INPUTFILE#$INPUTDIR/$INPUTFILE#; s#__LUCENEINDEXDIR#$LUCENEINDEXDIR#; s#__XCASOUTPUTDIR#$XCASOUTPUTDIR#" $WD/descriptors/CPE/hun_WdCR_ParserStemmerIndexer_CPE.xml  > $WD/descriptors/CPE/cpe_current.xml

sed -i "s/<casProcessors casPoolSize=\"[0-9]*\" processingUnitThreadCount=\"[0-9]*\">/<casProcessors casPoolSize=\"$THREADCOUNT\" processingUnitThreadCount=\"$THREADCOUNT\">/" $WD/descriptors/CPE/cpe_current.xml

#sed -i "s/processingUnitThreadCount=\"[0-9]*\"/processingUnitThreadCount=\"$THREADCOUNT\"/" $WD/descriptors/CPE/cpe_current.xml

$JAVA -Xmx16g -cp $CLASSPATH -jar pedia.uima.harvester.jar $WD/descriptors/CPE/cpe_current.xml 2>&1 > $LOGDIR/$LOGFILE

