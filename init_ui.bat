@echo off
set SERVER=TITI

if "%COMPUTERNAME%" == "LP5-PFS-DSY" (
	set SERVER="LP5-PFS-DSY"
)

if "%SERVER%" == "LP5-PFS-DSY" (
	echo "PC portable"
)
if "%SERVER%" == "TITI" (
	echo "PC titi"
)
set PATH=%PATH%

e:
cd E:\WorkSpaces\WS\GitHub\Navigation\1.0.0\Eclipse\ProjetUI\V1.0.0

call code .
call npx webpack --watch
