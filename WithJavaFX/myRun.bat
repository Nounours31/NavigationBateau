set JAVA_HOME=E:\bin\Java\jdk17\jdk-17.0.5+8
set GRADLE_HOME=E:\bin\gradle\gradle-7.6
set PATH=%GRADLE_HOME%;%GRADLE_HOME%/bin;%JAVA_HOME%;%JAVA_HOME%/bin;%PATH%
set PATH_TO_FX="E:\bin\Java\javaFX-pourJava17\javafx-sdk-17.0.6\lib"
set PATH_TO_FX_MODS="E:\bin\Java\javaFX-pourJava17\javafx-jmods-17.0.6"

java -jar E:\WorkSpaces\WS\GitHub\NavAstro\WithJavaFX\JavaProject\app\build\libs\app.jar --module-path "%PATH_TO_FX%;%PATH_TO_FX_MODS%;mods"