#!/usr/bin/env bash
nohup java -Xmx4G -Dfile.encoding=utf-8 -jar dbpedia-spotlight-0.7.1.jar it http://localhost:4445/  > logIT.log &
