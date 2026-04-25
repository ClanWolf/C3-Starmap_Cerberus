#!/bin/sh
ps -fC java | grep C3-Bot-Discord.jar || /opt/jdk-26/bin/java -jar /var/www/vhosts/clanwolf.net/c3.clanwolf.net/server/C3-Bot-Discord.jar ; exit
exit
