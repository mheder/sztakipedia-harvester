#!/bin/bash

if [ $# -lt 1 ] 
then
	echo "USAGE: $0 <release-num>"
	exit 1;
fi

REL=$1

mv pedia_uima_harvester/run_httpcr_pwstcat_extproc.sh .
rm pedia_uima_harvester/*.sh
mv run_httpcr_pwstcat_extproc.sh pedia_uima_harvester
chmod +x pedia_uima_harvester/*.sh

tar czvf pedia_uima_harvester_rel-${REL}.tar.gz pedia_uima_harvester
