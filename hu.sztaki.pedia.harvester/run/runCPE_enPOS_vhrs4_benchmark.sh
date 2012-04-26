#!/bin/bash
if [ "$UIMA_HOME" = "" ]
then
  echo UIMA_HOME environment variable is not set
  exit 1
fi

#if [ $# -lt 1 ]
#  then echo " Usage: $0 <thread count>"
#       exit 1;
#fi;
#THREADCOUNT=$1

WD=$UIMA_HOME/pedia_uima_harvester 
CLASSPATH=$WD/pedia.uima.harvester_lib:$WD
#export CLASSPATH

JAVA=java
INPUTDIR=/wikidata/WikidumpEnglish
INPUTFILE=enwiki-20120403-pages-articles.xml
#INPUTFILE=enwikitest2.xml

for THREADCOUNT in `seq 1 8`; do
	echo -n "Running CPE for $THREADCOUNT ... "
	LUCENEINDEXDIR=/wikidata/niif-wikidata-mp/uimacpe/indexes/benchmark_en_POS-$INPUTFILE-tc$THREADCOUNT
	XCASOUTPUTDIR=/wikidata/niif-wikidata-mp/uimacpe/output/benchmark_en_xcas-$INPUTFILE-tc$THREADCOUNT
	LOGFILE=benchmark_cpe-enPOS-$INPUTFILE-`date +%d.%m.%y-%H%M`_tc$THREADCOUNT.log
	LOGDIR=/wikidata/niif-wikidata-mp/uimacpe/logs

	if [ -e $LUCENEINDEXDIR ]
	then 
		rm -r $LUCENEINDEXDIR
	fi
	if [ -e $XCASOUTPUTDIR ]
	then 
		rm -r $XCASOUTPUTDIR
	fi
	mkdir -p $LUCENEINDEXDIR;
	mkdir -p $XCASOUTPUTDIR;

	sed "s#__INPUTFILE#$INPUTDIR/$INPUTFILE#; s#__LUCENEINDEXDIR#$LUCENEINDEXDIR#; s#__XCASOUTPUTDIR#$XCASOUTPUTDIR#" $WD/descriptors/CPE/en_WdCR_ParserStemmerIndexer_CPE_benchmark.xml  > $WD/descriptors/CPE/cpe_current.xml

	sed -i "s/<casProcessors casPoolSize=\"[0-9]*\" processingUnitThreadCount=\"[0-9]*\">/<casProcessors casPoolSize=\"$THREADCOUNT\" processingUnitThreadCount=\"$THREADCOUNT\">/" $WD/descriptors/CPE/cpe_current.xml

	#sed -i "s/processingUnitThreadCount=\"[0-9]*\"/processingUnitThreadCount=\"$THREADCOUNT\"/" $WD/descriptors/CPE/cpe_current.xml

	$JAVA -Xmx16g -cp $CLASSPATH -jar pedia.uima.harvester.jar $WD/descriptors/CPE/cpe_current.xml 2>&1 > $LOGDIR/$LOGFILE
	echo "OK"
done

