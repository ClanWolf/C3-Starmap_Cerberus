@ECHO OFF
ECHO .
ECHO .
ECHO ###############################################
ECHO #
ECHO # !!! ATTENTION !!!
ECHO #
ECHO # Before building a new installer,
ECHO # create a new c3-client.nsi
ECHO # with the packager module:
ECHO # -- net.clanwolf.starmap.client.packager --
ECHO #
ECHO # 1. Consider changing the version number
ECHO #    (in 'NSICreator.java')
ECHO # 2. Run 'c3-client_createInstaller.cmd'
ECHO #
ECHO ###############################################
ECHO .
ECHO .

SET PATH=%PATH%;C:\Program Files (x86)\NSIS\Bin;
ECHO %PATH%
ECHO .
ECHO Will run command: makensis.exe /V4 /INPUTCHARSET utf8 /OUTPUTCHARSET utf8 c3-client.nsi
ECHO .
PAUSE
makensis.exe /V4 /INPUTCHARSET utf8 /OUTPUTCHARSET utf8 c3-client.nsi
PAUSE
