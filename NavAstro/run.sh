#!bash

WS=/D/git/github/NavAstro/NavAstro
[ ! -d $WS ] && {
	WS=/e/WorkSpaces/ws/GitHub/NavAstro/NavAstro
	JAVA_HOME=/e/bin/Java/DefautJdk11
	GRADLE_HOME=/E/bin/gradle/gradle-7.5.1
	PATH=${JAVA_HOME}:${JAVA_HOME}/bin:${GRADLE_HOME}:${GRADLE_HOME}/bin:${PATH}
}
LOG_DIR=${WS}/logs
JAR_PATH=${WS}/app/build/libs
JAR_NAME=NavAstroAppBigJar-0.1.0-all.jar
LOGBACK=${JAR_PATH}/logback.xml
cd $WS

gradle fatJar

# gradle test -i

[ ! -d ${LOG_DIR} ] && mkdir -p ${LOG_DIR}

echo logs: ${LOG_DIR}/tests.log
cp ${WS}/app/src/main/resources/logback.xml ${LOGBACK}

JAVA_OPT="${JAVA_OPT} -Dlogback.configurationFile=${LOGBACK}"
JAVA_OPT="${JAVA_OPT} -DLOG_DIR=${LOG_DIR}"
java ${JAVA_OPT} -jar ${JAR_PATH}/${JAR_NAME}
