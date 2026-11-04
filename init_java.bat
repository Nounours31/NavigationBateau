SERVER=TITI

if "%COMPUTERNAME%" == "LP5-PFS-DSY" (
	SERVER="LP5-PFS-DSY"
)

if "%SERVER%" == "LP5-PFS-DSY" (
	echo "PC portable"
    set JAVA_HOME=E:\bin\Java\jdk17\jdk-17.0.5+8
    set GRADLE_HOME=E:\bin\gradle\gradle-8.0.2
)
if "%SERVER%" == "TITI" (
	echo "PC titi"
    set JAVA_HOME=D:\bin\java\jdk-17.0.7+7
    set GRADLE_HOME=D:\bin\gradle\gradle-8.2.1
)
set PATH=%GRADLE_HOME%;%GRADLE_HOME%/bin;%JAVA_HOME%;%JAVA_HOME%/bin;%PATH%

e:
cd E:\WorkSpaces\WS\GitHub\Navigation\NavigationBateau\ProjetJava\projet

gradle