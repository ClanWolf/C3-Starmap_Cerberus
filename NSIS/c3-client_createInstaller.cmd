@ECHO OFF
ECHO .
ECHO Will run command NSICreator to add needed files to the NSIS config file
ECHO .
C:
CD \
CD C:\C3\projects\C3-Starmap_Cerberus

REM SET VERSION=7.3.21
FOR /f "delims== tokens=1,2" %%G in (C:\C3\projects\C3-Starmap_Cerberus\net.clanwolf.starmap.client\target\classes\version.number) do set %%G=%%H
ECHO Found version: %VERSION%
REM PAUSE

ECHO Copying launcher exe
COPY C:\c3\projects\C3-Starmap_Cerberus\NSIS\client_launcher_executable\C3-Starmap_Cerberus.exe_ C:\C3\projects\C3-Starmap_Cerberus\net.clanwolf.starmap.client\target\jlink-image\bin\C3-Starmap_Cerberus.exe
REM PAUSE

ECHO Copying TTS samples from cache into project
COPY %UserProfile%\.ClanWolf.net_C3\cache\voice\de\*.* C:\C3\projects\C3-Starmap_Cerberus\net.clanwolf.starmap.client\src\main\resources\sound\voice\de /Y /V
COPY %UserProfile%\.ClanWolf.net_C3\cache\voice\en\*.* C:\C3\projects\C3-Starmap_Cerberus\net.clanwolf.starmap.client\src\main\resources\sound\voice\en /Y /V
REM PAUSE

REM 20 is set in NRICreator.java
REM !!!
REM ATTENTION:
REM "c3_client_createInstaller.cmd" might need to be changed manually for the first run!
REM !!!

IF EXIST "C:\Program Files\Java\jdk-20\bin\java.exe" (
  ECHO Java found
  "C:\Program Files\Java\jdk-20\bin\java.exe" -jar C:\C3\projects\C3-Starmap_Cerberus\net.clanwolf.starmap.client.packager\target\net.clanwolf.starmap.client.packager-%VERSION%.jar
) ELSE (
  ECHO Java NOT found
  GOTO END
)

ECHO Add JVM-OPTIONS to start file (copy template)
COPY C:\C3\projects\C3-Starmap_Cerberus\NSIS\templates\start.bat_template C:\C3\projects\C3-Starmap_Cerberus\net.clanwolf.starmap.client\target\jlink-image\bin\C3-Starmap_Cerberus.bat

ECHO *******************
ECHO *******************
ECHO *******************
REM PAUSE

REM ###############################################
REM PAUSE
REM ###############################################

SET PATH=%PATH%;C:\Program Files (x86)\NSIS\Bin;
ECHO %PATH%
ECHO .
ECHO Will run command: makensis.exe /V4 /INPUTCHARSET utf8 /OUTPUTCHARSET utf8 c3-client.nsi
ECHO .
C:
CD \
CD C3\projects\C3-Starmap_Cerberus\NSIS

DEL C3-Client-*.* /F /Q

makensis.exe /V4 /INPUTCHARSET utf8 /OUTPUTCHARSET utf8 c3-client.nsi

certutil -hashfile c:\c3\projects\C3-Starmap_Cerberus\NSIS\C3-Client-%VERSION%_install.exe MD5  | find /i /v "md5" | find /i /v "certutil" > c:\c3\projects\C3-Starmap_Cerberus\NSIS\C3-Client-%VERSION%_install.md5checksum
certutil -hashfile c:\c3\projects\C3-Starmap_Cerberus\NSIS\C3-Client-%VERSION%_install.exe SHA512 | find /i /v "sha512" | find /i /v "certutil" > c:\c3\projects\C3-Starmap_Cerberus\NSIS\C3-Client-%VERSION%_install.sha512checksum

ECHO *******************
ECHO *******************
ECHO *******************
"C:\Program Files (x86)\WinSCP\winscp.com" /ini=nul /script=C:\C3\projects\C3-Starmap_Cerberus\NSIS\scripts\upload_manual.script

:REQUEST_SERVER
ECHO ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
ECHO Upload Server build? (y/n)
SET /p chc1=
IF '%chc1%'=='y' GOTO UPLOADSERVER
IF '%chc1%'=='n' GOTO REQUEST_INSTALLER
GOTO REQUEST_SERVER

:UPLOADSERVER
REM Upload server
"C:\Program Files (x86)\WinSCP\winscp.com" /ini=nul /script=C:\C3\projects\C3-Starmap_Cerberus\NSIS\scripts\upload_server.script

:REQUEST_INSTALLER
ECHO ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
ECHO Upload Client installer? (y/n)
SET /p chc2=
IF '%chc2%'=='y' GOTO UPLOADINSTALLER
IF '%chc2%'=='n' GOTO REQUEST_IRCBOT
GOTO REQUEST_INSTALLER

:UPLOADINSTALLER
REM Sign installer
ECHO ####################################################################################
ECHO #
ECHO # Signing the installer executable
ECHO #
ECHO ####################################################################################

FOR /F "delims=" %%f IN (C:\c3\projects\C3-Starmap_Cerberus\NSIS\setstorepw.cmd) DO %%f
REM ECHO C:\C3\tools\Windows10-SignTool\signtool.exe sign /f C:\C3\certificate\c3_certificate.pfx /p %STOREPW% /fd SHA256 C:\c3\projects\C3-Starmap_Cerberus\NSIS\C3-Client-%VERSION%_install.exe
C:\C3\tools\Windows10-SignTool\signtool.exe sign /f C:\C3\certificate\c3_certificate.pfx /p %STOREPW% /fd SHA256 C:\c3\projects\C3-Starmap_Cerberus\NSIS\C3-Client-%VERSION%_install.exe

REM Upload installer
"C:\Program Files (x86)\WinSCP\winscp.com" /ini=nul /script=C:\C3\projects\C3-Starmap_Cerberus\NSIS\scripts\upload_installer.script
START "" "https://www.clanwolf.net/apps/C3/server/php/insert_new_version.php"

:REQUEST_IRCBOT
ECHO ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
ECHO Upload IRCBot? (y/n)
SET /p chc3=
IF '%chc3%'=='y' GOTO UPLOADIRCBOT
IF '%chc3%'=='n' GOTO REQUEST_DISCORDBOT
GOTO REQUEST_IRCBOT

:UPLOADIRCBOT
REM Upload IRCBot
"C:\Program Files (x86)\WinSCP\winscp.com" /ini=nul /script=C:\C3\projects\C3-Starmap_Cerberus\NSIS\scripts\upload_ircbot.script

:REQUEST_DISCORDBOT
ECHO ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
ECHO Upload DiscordBot? (y/n)
SET /p chc3=
IF '%chc3%'=='y' GOTO UPLOADDISCORDBOT
IF '%chc3%'=='n' GOTO REQUEST_TS3BOT
GOTO REQUEST_DISCORDBOT

:UPLOADDISCORDBOT
REM Upload DiscordBot
"C:\Program Files (x86)\WinSCP\winscp.com" /ini=nul /script=C:\C3\projects\C3-Starmap_Cerberus\NSIS\scripts\upload_discordbot.script

:REQUEST_TS3BOT
ECHO ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
ECHO Upload TS3Bot? (y/n)
SET /p chc4=
IF '%chc4%'=='y' GOTO UPLOADTS3BOT
IF '%chc4%'=='n' GOTO END
GOTO REQUEST_TS3BOT

:UPLOADTS3BOT
REM Upload TS3Bot
"C:\Program Files (x86)\WinSCP\winscp.com" /ini=nul /script=C:\C3\projects\C3-Starmap_Cerberus\NSIS\scripts\upload_ts3bot.script


ECHO .
ECHO .
ECHO .
ECHO Waiting for some time before removing flag request
timeout /t 1 /nobreak >nul
ECHO 7
timeout /t 1 /nobreak >nul
ECHO 6
timeout /t 1 /nobreak >nul
ECHO 5
timeout /t 1 /nobreak >nul
ECHO 4
timeout /t 1 /nobreak >nul
ECHO 3
timeout /t 1 /nobreak >nul
ECHO 2
timeout /t 1 /nobreak >nul
ECHO 1
timeout /t 1 /nobreak >nul
ECHO 0


:REQUEST_REMOVE_BOT_FLAGS
ECHO ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
ECHO Remove flags to restart BOTs (did they have time enough to shut down)? (y/n)
SET /p chc5=
IF '%chc5%'=='y' GOTO REMOVE_BOT_FLAGS
IF '%chc5%'=='n' GOTO END
GOTO REQUEST_REMOVE_BOT_FLAGS

:REMOVE_BOT_FLAGS
REM Remove flags so that the bots can startup again
"C:\Program Files (x86)\WinSCP\winscp.com" /ini=nul /script=C:\C3\projects\C3-Starmap_Cerberus\NSIS\scripts\remove_bot_flags.script

REM ###############################################
REM PAUSE
REM ###############################################

:END
