#!/bin/bash

HOST=$1
USER=$2

UHOME=/home/apache-uima
UIMA_HOME=$UHOME/apache-uima-as-2.3.1

ssh $USER@$HOST " cd $UIMA_HOME/; chmod +x pedia_uima_harvester/setUimaHome.sh; pedia_uima_harvester/setUimaHome.sh; cd bin; chmod +x startBroker.sh; ./startBroker.sh & ";
