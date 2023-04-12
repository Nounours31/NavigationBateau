set SERVER=VAPE

if "%SERVER%" == "VAPE" (
    set JAVA_HOME=E:\bin\Java\jdk17\jdk-17.0.5+8
    set GRADLE_HOME=E:\bin\gradle\gradle-8.0.2
    set JAVAFX_HOME=E:\bin\Java\javaFX-pourJava17
)
if "%SERVER%" == "TITI" (
    set JAVA_HOME=C:\paps\Programs\Java\java17
    set GRADLE_HOME=C:\paps\Programs\gradle-8.0.2
    set JAVAFX_HOME=C:\paps\Programs\Java\javaFX
)
set PATH=%GRADLE_HOME%;%GRADLE_HOME%/bin;%JAVA_HOME%;%JAVA_HOME%/bin;%PATH%
set PATH_TO_FX=%JAVAFX_HOME%\javafx-sdk-17.0.6\lib"
set PATH_TO_FXJMOD=%JAVAFX_HOME%\javafx-jmods-17.0.6

gradle