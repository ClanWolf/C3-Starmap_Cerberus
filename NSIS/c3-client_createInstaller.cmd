@ECHO OFF
ECHO .
ECHO Will run command NSICreator to add needed files to the NSIS config file
ECHO .
C:
CD \
CD C:\C3\projects\C3-Starmap_Cerberus

REM SET VERSION=7.1.37
FOR /f "delims== tokens=1,2" %%G in (C:\C3\projects\C3-Starmap_Cerberus\net.clanwolf.starmap.client\target\classes\version.number) do set %%G=%%H
ECHO Found version: %VERSION%
REM PAUSE

ECHO Copying launcher exe
COPY C:\c3\projects\C3-Starmap_Cerberus\NSIS\client_launcher_executable\C3-Starmap_Cerberus.exe_ C:\C3\projects\C3-Starmap_Cerberus\net.clanwolf.starmap.client\target\jlink-image\bin\C3-Starmap_Cerberus.exe
REM PAUSE

REM 18.0.2 is set in NRICreator.java

IF EXIST "C:\Program Files\Java\jdk-18.0.2\bin\java.exe" (
  ECHO Java found
  "C:\Program Files\Java\jdk-18.0.2\bin\java.exe" -jar C:\C3\projects\C3-Starmap_Cerberus\net.clanwolf.starmap.client.packager\target\net.clanwolf.starmap.client.packager-%VERSION%.jar
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
SET /p chc=
IF '%chc%'=='y' GOTO UPLOADINSTALLER
IF '%chc%'=='n' GOTO REQUEST_IRCBOT
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
SET /p chc2=
IF '%chc2%'=='y' GOTO UPLOADIRCBOT
IF '%chc2%'=='n' GOTO END
GOTO REQUEST_IRCBOT

:UPLOADIRCBOT
REM Upload server
"C:\Program Files (x86)\WinSCP\winscp.com" /ini=nul /script=C:\C3\projects\C3-Starmap_Cerberus\NSIS\scripts\upload_ircbot.script

REM ###############################################
REM PAUSE
REM ###############################################

:END
