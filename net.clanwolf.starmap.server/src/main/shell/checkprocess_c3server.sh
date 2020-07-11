#!/bin/sh
ps -fC java | grep C3-Server- || /usr/java/jdk-12.0.1/bin/java -jar /var/www/vhosts/clanwolf.net/httpdocs/apps/C3/server/C3-Server-5.1.7.jar
