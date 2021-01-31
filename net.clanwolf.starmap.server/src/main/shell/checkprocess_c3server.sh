#!/bin/sh
ps -fC java | grep C3-Server- || /opt/jdk-15.0.1/bin/java -jar /var/www/vhosts/clanwolf.net/httpdocs/apps/C3/server/C3-Server-5.2.0.jar ; exit
exit
