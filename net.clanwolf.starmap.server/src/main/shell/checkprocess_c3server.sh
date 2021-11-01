#!/bin/sh
test -f /var/www/vhosts/clanwolf.net/httpdocs/apps/C3/server/C3-Server_shutdown.flag && exit
ps -fC java | grep C3-Server- || /opt/jdk-17/bin/java -jar /var/www/vhosts/clanwolf.net/httpdocs/apps/C3/server/C3-Server-5.7.3.jar ; exit
exit
