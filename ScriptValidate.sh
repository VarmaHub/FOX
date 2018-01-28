#!/bin/sh
export MAVEN_OPTS="-Xmx6G -Dlog4j.configuration=file:data/fox/log4j.properties"

nohup mvn -e exec:java -Dexec.mainClass="org.aksw.fox.ui.FoxCLI" -Dexec.args="-len -avalidate -iinput/Wikiner/aij-wikiner-it-wp3.bz2" > validate.log &

