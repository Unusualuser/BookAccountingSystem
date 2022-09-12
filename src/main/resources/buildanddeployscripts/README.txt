Приложение работает по адресу: http://localhost:8080/book-accounting-system


Перед запуском проекта необходимо:
    - запустить скрипт по созданию бд (предварительно прочитав README.txt, лежащий в директории dbscripts)
    - изменить атрибуты hibernate.database.username
    и hibernate.database.password в файле application.properties на необходимые

Перед выполнением скрипта buildProject.bat необходимо:
    - изменить значение переменной mvn_home(путь до корня maven)
    - изменить значение переменной project_home(путь до корня проекта)

Перед выполнением скриптов deployProject.bat и undeployProject.bat необходимо:
    1) в файл $CATALINA_HOME\conf\tomcat-users.xml добавить следующие строки (CATALINA_HOME - директория с Apache Tomcat):
        <role rolename="manager-gui"/>
        <role rolename="manager-script"/>
        <user username="admin" password="password" roles="manager-gui, manager-script"/>
    2) в файл ${user.home}/.m2/settings.xml либо в ${maven_home}/conf/settings.xml (user_home - директория пользователя,
        maven_home - корневая директория maven) в теге <servers>
        добавить следующие строки:
            <server>
                <id>TomcatServer</id>
                <username>admin</username>
                <password>password</password>
            </server>
    3) установить системные переменные CATALINA_HOME и JAVA_HOME (если они еще не установлены) через командную строку:
        1) Пример: SETX CATALINA_HOME "C:\Files\Java\apache-tomcat-8.5.81"
        2) Пример: SETX JAVA_HOME "C:\Program Files\Java\jdk-11.0.13"
    4) перезагрузить компьютер (если переменные не были установлены до этого)
    5) запустить tomcat из командной строки: %CATALINA_HOME%\bin\startup.bat
    6) изменить значение переменной mvn_home(путь до корня maven)
    7) изменить значение переменной project_home(путь до корня проекта)

Для сборки приложения необходимо запустить из консоли buildProject.bat

Для деплоя приложения необходимо запустить из консоли deployProject.bat