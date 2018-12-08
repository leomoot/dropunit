#!/bin/bash

sleep 1
while ! curl --silent http://172.20.0.11:9080/
do
    sleep 1
done
echo "Simulator is running!"

echo "Starting the Engine under test..."
java -jar target/engine-under-test.jar server config/config-it.yml
