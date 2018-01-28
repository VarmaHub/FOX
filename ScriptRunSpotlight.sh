#!/bin/sh
export MAVEN_OPTS="-Xmx6G -Dlog4j.configuration=file:data/fox/log4j.properties"

cd spotlight

#./runDE.sh
# ./runEN.sh
#./runES.sh
#./runNL.sh
#./runFR.sh
./runIT.sh

cd ..
