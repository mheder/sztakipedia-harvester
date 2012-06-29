#!/bin/bash
if [ "$UIMA_HOME" = "" ]
then
  echo UIMA_HOME environment variable is not set
  exit 1
fi

if [ $# -lt 2 ]
  then echo " Usage: $0 <workroot> <thread count>"
       exit 1;
fi;
WORKROOT=$1
THREADCOUNT=$2

WD=$UIMA_HOME/pedia_uima_harvester 
CLASSPATH=$WD/*:$WD/pedia.uima.harvester_lib/*
#export CLASSPATH
echo $CLASSPATH

JAVA=java

LOGFILE=cpe-HTTP-External-`date +%d.%m.%y-%H%M`_tc$THREADCOUNT.log
LOGDIR=$WORKROOT/logs/cpe/en

mkdir -p $LOGDIR

cp $WD/descriptors/CPE/HTTPCR_parser_wst_category_externalConsumer_CPE.xml $WD/descriptors/CPE/cpe_current.xml
sed -i "s/<casProcessors casPoolSize=\"[0-9]*\" processingUnitThreadCount=\"[0-9]*\">/<casProcessors casPoolSize=\"$THREADCOUNT\" processingUnitThreadCount=\"$THREADCOUNT\">/" $WD/descriptors/CPE/cpe_current.xml


$JAVA -Xmx16g -cp $CLASSPATH hu.sztaki.pedia.uima.RunCPE $WD/descriptors/CPE/cpe_current.xml 2>&1 > $LOGDIR/$LOGFILE