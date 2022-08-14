#!bash

WS=/D/git/github/NavAstro/NavAstro
LOG_DIR=${WS}/logs
JAR_PATH=${WS}/app/build/libs
JAR_NAME=NavAstroAppBigJar-0.1.0-all.jar
LOGBACK=${JAR_PATH}/logback.xml
cd $WS

gradle fatJar


[ ! -d ${LOG_DIR} ] && mkdir -p ${LOG_DIR}

echo logs: ${LOG_DIR}/tests.log
cp /D/git/github/NavAstro/NavAstro/app/src/main/resources/logback.xml ${LOGBACK}

JAVA_OPT="${JAVA_OPT} -Dlogback.configurationFile=${LOGBACK}"
JAVA_OPT="${JAVA_OPT} -DLOG_DIR=${LOG_DIR}"
java ${JAVA_OPT} -jar ${JAR_PATH}/${JAR_NAME}