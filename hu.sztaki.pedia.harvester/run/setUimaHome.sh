#!/bin/bash
if [ $# -lt 1 ]
then 
	echo "Usage $0 <UIMA_HOME> ";
	exit 1
fi
UHOME=$1

if [ "$UIMA_HOME" = "" ]
then
#	echo "UIMA_HOME=/home/apache-uima/apache-uima-as-2.3.1" >> ~/.profile
	echo "UIMA_HOME=$UHOME" >> ~/.bash_profile
	echo "export UIMA_HOME" >> ~/.bash_profile
	#. ~/.bashrc
fi
