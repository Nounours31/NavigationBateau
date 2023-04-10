#bash
set +x

export JAVA_HOME=/C/paps/Programs/Java/java17
export GRADLE_HOME=/C/paps/Programs/gradle-8.0.2
export PATH=${GRADLE_HOME}:${GRADLE_HOME}/bin:${JAVA_HOME}:${JAVA_HOME}/bin:${PATH}
export JAVA_FX_HOME=/C/paps/Programs/Java/javaFX
export PATH_TO_FX=${JAVA_FX_HOME}/javafx-sdk-17.0.6/lib
export PATH_TO_FXMOD=${JAVA_FX_HOME}/javafx-jmods-17.0.6

gradle.bat