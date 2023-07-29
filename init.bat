set SERVER=VAPE

if "%SERVER%" == "VAPE" (
    set JAVA_HOME=E:\bin\Java\jdk17\jdk-17.0.5+8
    set GRADLE_HOME=E:\bin\gradle\gradle-8.0.2
)
if "%SERVER%" == "TITI" (
    set JAVA_HOME=D:\bin\java\jdk-17.0.7+7
    set GRADLE_HOME=D:\bin\gradle\gradle-8.2.1
)
set PATH=%GRADLE_HOME%;%GRADLE_HOME%/bin;%JAVA_HOME%;%JAVA_HOME%/bin;%PATH%

gradle