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

PAUSE

makensis.exe c3-client.nsi

PAUSE
