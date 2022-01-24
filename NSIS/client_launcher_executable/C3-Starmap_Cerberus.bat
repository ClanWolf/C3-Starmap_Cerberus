::[Bat To Exe Converter]
::
::YAwzoRdxOk+EWAjk
::fBw5plQjdCyDJGmW+0UiKRYUag2OOXiGJeZRy+Ty6uSTnk5OGus8d+8=
::YAwzuBVtJxjWCl3EqQJgSA==
::ZR4luwNxJguZRRnk
::Yhs/ulQjdF+5
::cxAkpRVqdFKZSzk=
::cBs/ulQjdF+5
::ZR41oxFsdFKZSDk=
::eBoioBt6dFKZSDk=
::cRo6pxp7LAbNWATEpCI=
::egkzugNsPRvcWATEpCI=
::dAsiuh18IRvcCxnZtBJQ
::cRYluBh/LU+EWAnk
::YxY4rhs+aU+JeA==
::cxY6rQJ7JhzQF1fEqQJQ
::ZQ05rAF9IBncCkqN+0xwdVs0
::ZQ05rAF9IAHYFVzEqQJQ
::eg0/rx1wNQPfEVWB+kM9LVsJDGQ=
::fBEirQZwNQPfEVWB+kM9LVsJDGQ=
::cRolqwZ3JBvQF1fEqQJQ
::dhA7uBVwLU+EWDk=
::YQ03rBFzNR3SWATElA==
::dhAmsQZ3MwfNWATElA==
::ZQ0/vhVqMQ3MEVWAtB9wSA==
::Zg8zqx1/OA3MEVWAtB9wSA==
::dhA7pRFwIByZRRnk
::Zh4grVQjdCyDJHrXyFIiJxFRTxCRAEjpS4YI6fr27vq4gUgPVuctbZzn9IenE9wQ5kzhe4IS2ntPlsgECQlkehO4akExsWsi
::YB416Ek+ZG8=
::
::
::978f952a14a936cc963da21a135fa983
@echo off
set JLINK_VM_OPTIONS=-Dprism.maxvram=3G -Dio.netty.leakDetectionLevel=advanced
set DIR=%~dp0
"%DIR%\java" %JLINK_VM_OPTIONS% -m net.clanwolf.starmap.client/net.clanwolf.starmap.client.gui.MainFrame %*
