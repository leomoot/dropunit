#!/bin/sh

docker-compose -f ./_docker/application/docker-compose-it.yml stop

mvn clean package || exit

docker-compose -f ./_docker/application/docker-compose-it.yml up -d --build

sleep 3
while ! curl --silent http://127.0.0.1:8080/
do
    sleep 1
done

mvn test -Dtest=*IT -DfailIfNoTests=false
mvn test -Dtest=*ITslow -DfailIfNoTests=false

docker-compose -f ./_docker/application/docker-compose-it.yml stop

exit 1
