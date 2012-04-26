#!/bin/bash
if [ "$UIMA_HOME" = "" ]
then
  echo UIMA_HOME environment variable is not set
  exit 1
fi

if [ $# -lt 2 ]
  then echo "You must specify the input queue name and the input data directory path, and an optional Java home path. \n Usage: $0 <input queue name>  <path to data directory> [Java home]"
       exit 1;
fi;

QUEUENAME=$1
INPUTDIR=$2
JAVA_HOME=$3

if [ "$JAVA_HOME" != "" ]; then
	export JAVA_HOME
fi


CLASSPATH=$UIMA_HOME/pedia_uima_harvester:$UIMA_HOME/pedia_uima_harvester/pedia.uima.harvester_lib
export CLASSPATH

sed "s#__INPUTDIRECTORY#$INPUTDIR#" $UIMA_HOME/pedia_uima_harvester/descriptors/CAS/FileSystemCollectionReader.xml > $UIMA_HOME/pedia_uima_harvester/descriptors/CAS/FSCR_tmp.xml

$UIMA_HOME/bin/runRemoteAsyncAE.sh tcp://jmsbroker.sztaki.hu:61616 $QUEUENAME -c $UIMA_HOME/pedia_uima_harvester/descriptors/CAS/FSCR_tmp.xml -log -p 10 #-o ~/Dev/UIMA/as_temp/out
