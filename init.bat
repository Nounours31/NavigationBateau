SERVER=TITI
if "%COMPUTERNAME%" == "LP5-PFS-DSY" (
	SERVER="LP5-PFS-DSY"
)

if "%COMPUTERNAME%" == "ALPH2DSY" (
	SERVER="ALPH2DSY"
)

if "%SERVER%" == "LP5-PFS-DSY" (
	echo "PC portable"
    set GRADLE_HOME=E:\bin\gradle\gradle-8.0.2
    #set JAVA_HOME=E:\bin\Java\jdk17\jdk-17.0.5+8
	set JAVA_HOME=E:\bin\Java\jdk11\jdk-11.0.20.1+1
	set CATALINA_HOME=E:\bin\tomee\apache-tomee-plus-8.0.1

)
if "%SERVER%" == "ALPH2DSY" (
	echo "PC Fixe"
	#set JAVA_HOME=E:\bin\Java\jdk17\jdk-17.0.5+8
	set JAVA_HOME=E:\bin\Java\jdk11\jdk-11.0.20.1+1
	set GRADLE_HOME=E:\bin\gradle\gradle-8.0.2-bin\gradle-8.0.2
	set CATALINA_HOME=E:\bin\tomee\apache-tomee-plus-8.0.1
)

if "%SERVER%" == "TITI" (
	echo "PC titi"
    set JAVA_HOME=D:\bin\java\jdk-17.0.7+7
    set GRADLE_HOME=D:\bin\gradle\gradle-8.2.1
	
)
set PATH=%GRADLE_HOME%;%GRADLE_HOME%/bin;%JAVA_HOME%;%JAVA_HOME%/bin;%PATH%

echo "remove SLF4J jdk14"
echo rm %CATALINA_HOME%\lib\slf4j-api-1.7.21.jar
echo rm %CATALINA_HOME%\lib\slf4j-jdk14-1.7.21.jar
echo "a la place slf4j-api-2.0.7 + logback ..."

echo "WARNING CP logbacl"
echo cp E:\WorkSpaces\WS\Github\GradleWebApp\Projet\lib\src\main\resources\logback.xml %CATALINA_HOME%\lib

gradle