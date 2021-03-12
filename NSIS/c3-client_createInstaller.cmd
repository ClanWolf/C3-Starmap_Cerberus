@ECHO OFF
ECHO .
ECHO .
ECHO ###############################################
ECHO #
ECHO # !!! ATTENTION !!!
ECHO #
ECHO # Consider changing the version number
ECHO # (in 'NSICreator.java')
ECHO #
ECHO ###############################################
ECHO .
ECHO .

ECHO .
ECHO Will run command NSICreator to add needed files to the NSIS config file
ECHO .
C:
CD \
CD C:\C3\projects\C3-Starmap_Cerberus

IF EXIST "C:\Program Files\Java\jdk-15\bin\java.exe" (
  ECHO Java found
  "C:\Program Files\Java\jdk-15\bin\java.exe" -jar C:\C3\projects\C3-Starmap_Cerberus\net.clanwolf.starmap.client.packager\target\net.clanwolf.starmap.client.packager-5.2.0.jar
) ELSE (
  ECHO Java NOT found
  GOTO END
)

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
makensis.exe /V4 /INPUTCHARSET utf8 /OUTPUTCHARSET utf8 c3-client.nsi

certutil -hashfile c:\c3\projects\C3-Starmap_Cerberus\NSIS\C3-Client-5.2.0_install.exe MD5  | find /i /v "md5" | find /i /v "certutil" > c:\c3\projects\C3-Starmap_Cerberus\NSIS\C3-Client-5.2.0_install.md5checksum
certutil -hashfile c:\c3\projects\C3-Starmap_Cerberus\NSIS\C3-Client-5.2.0_install.exe SHA512 | find /i /v "sha512" | find /i /v "certutil" > c:\c3\projects\C3-Starmap_Cerberus\NSIS\C3-Client-5.2.0_install.sha512checksum

ECHO *******************
ECHO *******************
ECHO *******************
"C:\Program Files (x86)\WinSCP\winscp.com" /ini=nul /script=C:\C3\projects\C3-Starmap_Cerberus\NSIS\scripts\upload_manual.script

:REQUEST_INSTALLER
ECHO Upload Installer? (y/n)
SET /p chc=
IF '%chc%'=='y' GOTO UPLOADINSTALLER
IF '%chc%'=='n' GOTO REQUEST_SERVER
GOTO REQUEST_INSTALLER

:UPLOADINSTALLER
REM Upload installer
"C:\Program Files (x86)\WinSCP\winscp.com" /ini=nul /script=C:\C3\projects\C3-Starmap_Cerberus\NSIS\scripts\upload_installer.script

:REQUEST_SERVER
ECHO Upload Server? (y/n)
SET /p chc1=
IF '%chc1%'=='y' GOTO UPLOADSERVER
IF '%chc1%'=='n' GOTO REQUEST_IRCBOT
GOTO REQUEST_SERVER

:UPLOADSERVER
REM Upload server
"C:\Program Files (x86)\WinSCP\winscp.com" /ini=nul /script=C:\C3\projects\C3-Starmap_Cerberus\NSIS\scripts\upload_server.script

:REQUEST_IRCBOT
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
