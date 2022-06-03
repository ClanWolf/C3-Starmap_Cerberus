#!/bin/sh
#test -f /var/www/vhosts/clanwolf.net/httpdocs/apps/C3/server/C3-Server_shutdown.flag && exit
#test -f /var/www/vhosts/clanwolf.net/httpdocs/apps/C3/server/uploading.jar.filepart && exit
#ps -fC java | grep C3-Server- || /opt/jdk-18.0.1.1/bin/java -jar /var/www/vhosts/clanwolf.net/httpdocs/apps/C3/server/C3-Server-7.0.26.jar ; exit
#exit
if [ -f /var/www/vhosts/clanwolf.net/httpdocs/apps/C3/server/C3-Server_shutdown.flag ]
then
    exit 0
else
    ps -fC java | grep C3-Server- || /opt/jdk-18.0.1.1/bin/java -jar /var/www/vhosts/clanwolf.net/httpdocs/apps/C3/server/C3-Server-7.0.26.jar
    exit 0
fi
