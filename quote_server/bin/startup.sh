#!/bin/bash
export JAVA_HOME=/opt/sumscope/software/java
export JVM_OPS="-Xms256m -Xmx512m"
export APP_OPS="-Dapplication.zookeeper.connect=172.16.87.2:2181 -Dapplication.zookeeper.path=/com/sumscope/bab/store/dev -Dport=8730"

nohup ${JAVA_HOME}/bin/java ${JVM_OPS} ${APP_OPS} -jar bab_store.jar >/dev/null &
