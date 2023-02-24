#! /bin/bash
PID=$(ps -ef | grep api-gateway-1.0.0.jar | grep -v grep | awk '{ print $2 }')
if [ -z "$PID" ]
then
    echo Application is already stopped
else
    echo kill $PID
    kill -9 $PID
fi
