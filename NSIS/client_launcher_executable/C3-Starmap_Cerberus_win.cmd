@echo off
set JLINK_VM_OPTIONS=-Dprism.maxvram=3G -Dio.netty.leakDetectionLevel=advanced
set DIR=%~dp0
"%DIR%\java" %JLINK_VM_OPTIONS% -m net.clanwolf.starmap.client/net.clanwolf.starmap.client.gui.MainFrame %*