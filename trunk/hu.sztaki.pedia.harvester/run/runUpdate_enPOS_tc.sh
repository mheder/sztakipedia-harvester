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
INPUTDOMAIN=en.wikipedia
LUCENEINDEXDIR=/wikidata/niif-wikidata-mp/uimacpe/indexes/en_POS-enwiki-20120403
XCASOUTPUTDIR=/wikidata/niif-wikidata-mp/uimacpe/output/en_xcas-enwiki-20120403-pages-articles.xml-tc6
LOGFILE=update-enPOS-$INPUTDOMAIN-`date +%d.%m.%y-%H%M`_tc$THREADCOUNT.log
LOGDIR=/wikidata/niif-wikidata-mp/uimacpe/logs/update/en

mkdir -p $LUCENEINDEXDIR;
mkdir -p $XCASOUTPUTDIR;
mkdir -p $LOGDIR
sed "s#__LUCENEINDEXDIR#$LUCENEINDEXDIR#; s#__XCASOUTPUTDIR#$XCASOUTPUTDIR#" $WD/descriptors/CPE/en_WIRCCR_ParserStemmerIndexer_CPE.xml  > $WD/descriptors/CPE/cpe_current.xml

sed -i "s/processingUnitThreadCount=\"[0-9]*\"/processingUnitThreadCount=\"$THREADCOUNT\"/" $WD/descriptors/CPE/cpe_current.xml

$JAVA -Xmx16g -cp $CLASSPATH -jar pedia.uima.harvester.jar $WD/descriptors/CPE/cpe_current.xml 2>&1 > $LOGDIR/$LOGFILE
