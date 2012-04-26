#!/bin/bash

if [ "$UIMA_HOME" = "" ]
then
  echo UIMA_HOME environment variable is not set
  exit 1
fi

if [ $# -lt 3 ]
  then echo "You must specify the descriptor, the parameters placeholder name, and the value to be replaced.  Usage: $0 <file-path-of-deployment-descriptor> <param name> <value> [ JAVA_HOME ] "
       exit 1;
fi;
if [ ! -f $1 ]
  then echo "File '"$1"' does not exist";
       exit 1;
fi;

DDESCRIPTOR=$1
PARAM=$2
PARAMVALUE=$3
JAVA_HOME=$4;
export JAVA_HOME


PHOME=$UIMA_HOME/pedia_uima_harvester
CLASSPATH=$PHOME/pedia.uima.harvester_lib:$PHOME
export CLASSPATH

AEDESCRIPTOR=`cat $DDESCRIPTOR | grep import | sed "s/^.*\"\.\.\///;s/\"\/>//"`

TMP_DDESCRIPTOR=`echo $DDESCRIPTOR | sed "s/\.xml/\_tmp\.xml/"`;
TMP_AEDESCRIPTOR=`echo $AEDESCRIPTOR | sed "s/\.xml/\_tmp\.xml/"`;

sed "s#$PARAM#$PARAMVALUE#" descriptors/$AEDESCRIPTOR > descriptors/$TMP_AEDESCRIPTOR
sed "s#$AEDESCRIPTOR#$TMP_AEDESCRIPTOR#" $DDESCRIPTOR > $TMP_DDESCRIPTOR

"$UIMA_HOME/bin/runUimaClass.sh" org.apache.uima.adapter.jms.service.UIMA_Service -saxonURL "file:$UIMA_HOME/saxon/saxon8.jar" -xslt "$UIMA_HOME/bin/dd2spring.xsl" -dd $TMP_DDESCRIPTOR

