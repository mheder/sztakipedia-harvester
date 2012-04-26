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
INPUTDIR=/wikidata/WikidumpHungarian/raw_sorted
LUCENEINDEXDIR=/wikidata/WikidumpHungarian/indexes/hunpos
POSOUTPUTDIR=/wikidata/WikidumpHungarian/POSout

mkdir -p $LUCENEINDEXDIR;
mkdir -p $POSOUTPUTDIR;

i=1; 
j=0;
for j in `ls $INPUTDIR/$i`; do
        echo "Processig: $i - $j "
        sed "s#__INPUTDIR#$INPUTDIR/$i/$j#; s#__LUCENEINDEXDIR#$LUCENEINDEXDIR#; s#__POSOUTPUTDIR#$POSOUTPUTDIR#" $WD/descriptors/CPE/hunPosIndexer_CPE.xml  > $WD/descriptors/CPE/cpe_current.xml
        $JAVA -Xmx8g -cp $CLASSPATH -jar pedia.uima.harvester.jar $WD/descriptors/CPE/cpe_current.xml ### 2>&1 > $WD/output$i-$j.log
done;
