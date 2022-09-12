Для успешного выполнения bat-скрипта, необходимо:
    - запустить postgresql server:
        Пуск -> Выполнить -> введите services.msc -> PostgreSQl server -> запустить
        ИЛИ
        Запустить pgAdmin
    - изменить в run.bat путь до Postgres`а
    - изменить в run.bat, имя пользователя постгрес на то, которое у вас,
        а так же в файлах: createDatabase у атрибута "OWNER", createTablesSequencesAndTriggers
        у каждого создания последовательности у атрибута "OWNER TO"
    - запустить bat-скрипт из консоли в директории dbscripts
        и ввести пароль от пользователя postgres`а необходимое число раз