#!/bin/sh
ps -fC java | grep C3-Bot-Discord.jar || /opt/jdk-19.0.2/bin/java -jar /var/www/vhosts/clanwolf.net/httpdocs/apps/C3/server/C3-Bot-Discord.jar ; exit
exit
