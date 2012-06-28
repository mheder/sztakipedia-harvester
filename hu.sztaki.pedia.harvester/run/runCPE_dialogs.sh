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
INPUTDIR=$UIMA_HOME/data/input
LUCENEINDEXDIR=$UIMA_HOME/data/lucene/index

mkdir -p $LUCENEINDEXDIR

echo "Processig: $INPUTDIR "
sed "s#__INPUTDIRECTORY#$INPUTDIR#; s#__LUCENEINDEXDIR#$LUCENEINDEXDIR#" $WD/descriptors/CPE/engPosIndexer_CPE.xml > $WD/descriptors/CPE/cpe_current.xml
$JAVA -Xmx5g -cp $CLASSPATH -jar pedia.uima.harvester.jar $WD/descriptors/CPE/cpe_current.xml ### 2>&1 > $WD/output$i-$j.log