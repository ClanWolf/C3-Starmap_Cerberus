@ECHO OFF
FOR /R "%~1" %%A IN (*.*) DO (
    >> "%~2" ECHO.File "%%~A"
)
