#!/bin/bash

if [ "$UIMA_HOME" = "" ]
then
  echo UIMA_HOME environment variable is not set
  exit 1
fi

if [ $# -lt 1 ]
  then echo "You must specify one or more deployment descriptors.  Usage: $0 file-path-of-deployment-descriptor [ JAVA_HOME ] "
       exit 1;
fi;
if [ ! -f $1 ]
  then echo "File '"$1"' does not exist";
       exit 1;
fi;

JAVA_HOME=$2;
export JAVA_HOME

PHOME=$UIMA_HOME/pedia_uima_processor
CLASSPATH=$PHOME/pedia.uima.processor_lib:$PHOME
export CLASSPATH
#echo $CLASSPATH
"$UIMA_HOME/bin/runUimaClass.sh" org.apache.uima.adapter.jms.service.UIMA_Service -saxonURL "file:$UIMA_HOME/saxon/saxon8.jar" -xslt "$UIMA_HOME/bin/dd2spring.xsl" -dd $1

