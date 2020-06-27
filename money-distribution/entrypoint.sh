#!/bin/sh

java --version
./consul agent -retry-join consul-server-1 -data-dir /var/lib/consul &
sleep 5s
java -Dspring.profiles.active=docker -jar app.jar

