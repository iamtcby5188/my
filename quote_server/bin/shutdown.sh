#!/bin/bash

PID=`ps -ef | grep "\-Dport=8730 \-jar bab_store.jar" | grep -v grep | awk '{print $2}'`
 echo "Killing..." + ${PID}
   kill ${PID}
 echo "Done!"

