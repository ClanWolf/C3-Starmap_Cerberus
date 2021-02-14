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

REM ###############################################
REM PAUSE
REM ###############################################

:END
PAUSE
