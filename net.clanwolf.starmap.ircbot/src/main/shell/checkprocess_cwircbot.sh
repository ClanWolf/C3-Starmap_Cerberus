#!/bin/sh
ps -fC java | grep CWIRCBot.jar || /usr/java/jdk-12.0.1/bin/java -jar /var/www/vhosts/clanwolf.net/httpdocs/apps/C3/server/CWIRCBot.jar
