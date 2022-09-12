SETLOCAL
SET mvn_home=C:\Files\Java\apache-maven-3.6.0
SET project_home=C:\Files\Java\BookAccountingSystem

cd "%project_home%"
"%mvn_home%\bin\mvn" tomcat7:deploy
ENDLOCAL