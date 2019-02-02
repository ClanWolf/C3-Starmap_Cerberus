::[Bat To Exe Converter]
::
::YAwzoRdxOk+EWAjk
::fBw5plQjdCyDJGyX8VAjFBhcTgWQKG62OpwY7enL/eWNp04JR94cK8L41r2LLvQs2k3rcJgkzkZJisgwARpRcC65axw7vHxBs3e5NJTSugzuKg==
::YAwzuBVtJxjWCl3EqQJgSA==
::ZR4luwNxJguZRRnk
::Yhs/ulQjdF+5
::cxAkpRVqdFKZSjk=
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
::Zh4grVQjdCyDJGyX8VAjFBhcTgWQKG62OpwY7enL/eWNp04JR94cK8L41r2LLvQs2k3rcJgkzkZ0q+I/MAlNcC2vaw0hriBHrmHl
::YB416Ek+ZG8=
::
::
::978f952a14a936cc963da21a135fa983
::[Bat To Exe Converter]
::
::YAwzoRdxOk+EWAjk
::fBw5plQjdCyDJGyX8VAjFBhcTgWQKG62OpwY7enL/eWNp04JR94cK8L41r2LLvQs2k3rcJgkzkZJisgwARpRcC65axw7vHxBs3e5PsSTvUHoSUfp
::YAwzuBVtJxjWCl3EqQJgSA==
::ZR4luwNxJguZRRnk
::Yhs/ulQjdF+5
::cxAkpRVqdFKZSjk=
::cBs/ulQjdF+5
::ZR41oxFsdFKZSDk=
::eBoioBt6dFKZSDk=
::cRo6pxp7LAbNWATEpCI=
::egkzugNsPRvcWATEpCI=
::dAsiuh18IRvcCxnZtBJQ
::cRYluBh/LU+EWAnk
::YxY4rhs+aU+IeA==
::cxY6rQJ7JhzQF1fEqQJQ
::ZQ05rAF9IBncCkqN+0xwdVs0
::ZQ05rAF9IAHYFVzEqQITe1t3QA2HMn/6NaEd+vz+/Yo=
::eg0/rx1wNQPfEVWB+kM9LVsJDGQ=
::fBEirQZwNQPfEVWB+kM9LVsJDGQ=
::cRolqwZ3JBvQF1fEqQJQ
::dhA7uBVwLU+EWHqI9UwHJxdSAgqHKAs=
::YQ03rBFzNR3SWATElA==
::dhAmsQZ3MwfNWATElA==
::ZQ0/vhVqMQ3MEVWAtB9wSA==
::Zg8zqx1/OA3MEVWAtB9wSA==
::dhA7pRFwIByZRRnk
::Zh4grVQjdCyDJGyX8VAjFBhcTgWQKG62OpwY7enL/eWNp04JR94cK8L41r2LLvQs2k3rcJgkzkZ0q+I/MAlNcC2vaw0hriBHrmHl
::YB416Ek+ZG8=
::
::
::978f952a14a936cc963da21a135fa983
@ECHO OFF
CLS
ECHO C3-Client
ECHO ------------------------------------

REM Find out java version
REM --------------------------------------------------------------------------------------------------------------------
SETLOCAL
SET "_JAVACMD=%JAVACMD%"
IF "%_JAVACMD"=="" (
	IF NOT "%JAVA_HOME%"=="" (
		IF EXIST "%JAVA_HOME%\bin\java.exe" SET "_JAVACMD=%JAVA_HOME%\bin\java.exe"
	)
)
IF "%_JAVACMD%"=="" SET _JAVACMD=java
SET JAVA_VERSION=0
FOR /f "tokens=3" %%g IN ('%_JAVACMD% -Xms32M -Xmx32M -version 2^>^&1 ^| findstr /i "version"') DO (
	SET JAVA_VERSION=%%g
)
SET JAVA_VERSION=%JAVA_VERSION:"=%
FOR /f "delims=.-_ tokens=1-2" %%v IN ("%JAVA_VERSION%") DO (
	IF /I "%%v" EQU "1" (
		SET JAVA_VERSION=%%w
	) ELSE (
		SET JAVA_VERSION=%%v
	)
)
ECHO Detected Java-Version: %JAVA_VERSION%

REM Find out C3 version
REM --------------------------------------------------------------------------------------------------------------------
FOR /r %%i IN (C3-Client*.jar) DO SET CLNAME=%%~nxi
ECHO Detected C3-Client: %CLNAME%

REM Run C3 Client
REM --------------------------------------------------------------------------------------------------------------------
IF /I "%JAVA_VERSION%" EQU "10" (
	ECHO 10
	ECHO Starting...
	START javaw -jar %CLNAME%
	GOTO END
)
IF /I "%JAVA_VERSION%" EQU "11" (
	ECHO 11
	ECHO Starting...
	START javaw -jar %CLNAME%
	GOTO END
)
ECHO ---
ECHO --- Install Java Runtime-Environment 10 or higher.
ECHO ---

REM End
REM --------------------------------------------------------------------------------------------------------------------
:END
ENDLOCAL
PAUSE
