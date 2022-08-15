#!bash

WS=/D/git/github/NavAstro/NavAstro
LOG_DIR=${WS}/logs
JAR_PATH=${WS}/app/build/libs
JAR_NAME=NavAstroAppBigJar-0.1.0-all.jar
LOGBACK=${JAR_PATH}/logback.xml
cd $WS

gradle fatJar
gradle test -i
