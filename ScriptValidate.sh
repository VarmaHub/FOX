#!/bin/sh
export MAVEN_OPTS="-Xmx6G -Dlog4j.configuration=file:data/fox/log4j.properties"

nohup mvn exec:java -Dexec.mainClass="org.aksw.fox.ui.FoxCLI" -Dexec.args="-lit -avalidate -iinput/Wikiner/aij-wikiner-it-wp3.bz2" > validate.log &

