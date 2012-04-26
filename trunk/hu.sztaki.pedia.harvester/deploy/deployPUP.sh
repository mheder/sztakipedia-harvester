#*******************************************************************************
# Copyright 2012 Tamas Farkas, MTA SZTAKI, Hungary
#
#    Licensed under the Apache License, Version 2.0 (the "License");
#    you may not use this file except in compliance with the License.
#    You may obtain a copy of the License at
#
#        http://www.apache.org/licenses/LICENSE-2.0
#
#    Unless required by applicable law or agreed to in writing, software
#    distributed under the License is distributed on an "AS IS" BASIS,
#    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
#    See the License for the specific language governing permissions and
#    limitations under the License.
#*******************************************************************************
#!/bin/bash

if [ $# -lt 1 ] 
then
	echo "USAGE: $0 <hostname> [<username (default root) > <port (default 22) > <installbase (deafault:/home/apache-uima)>] ";
	exit 1;
fi

HOST=$1
USER=$2
PORT=$3
UHOME=$4
if [ $# -lt 2 ] 
then
USER="root"
fi
if [ $# -lt 3 ] 
then
PORT=22
fi

if [ $# -lt 4 ] 
then
UHOME=/home/apache-uima
fi

UIMA_HOME=$UHOME/apache-uima-as-2.3.1

if [ -f pedia_uima_harvester.tar.gz ] 
then
	rm pedia_uima_harvester.tar.gz
fi
chmod +x pedia_uima_harvester/*.sh
tar -czhf pedia_uima_harvester.tar.gz pedia_uima_harvester --exclude="\.svn"

ssh -p $PORT $USER@$HOST " mkdir -p $UHOME "
#scp -P $PORT uima-as-2.3.1-bin.tar.gz  $USER@$HOST:$UHOME/
#ssh -p $PORT $USER@$HOST " cd $UHOME; tar -xzf uima-as-2.3.1-bin.tar.gz; "
scp -P $PORT pedia_uima_harvester.tar.gz $USER@$HOST:$UIMA_HOME/
ssh -p $PORT $USER@$HOST " cd $UIMA_HOME; rm -r pedia_uima_harvester; tar -xzf pedia_uima_harvester.tar.gz; "
#ssh -p $PORT $USER@$HOST " cd $UIMA_HOME/pedia_uima_harvester; chmod +x  setUimaHome.sh; ./setUimaHome.sh $UIMA_HOME ";



