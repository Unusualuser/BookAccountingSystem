SETLOCAL
SET postgres_home=C:\Program Files\PostgreSQL\14
SET postgres_username=postgres

"%postgres_home%\bin\psql" -U %postgres_username% -a -f .\scripts\createDatabase.sql
"%postgres_home%\bin\psql" -U %postgres_username% -d book_accounting_system -a -f .\scripts\createTablesSequencesAndTriggers.sql
"%postgres_home%\bin\psql" -U %postgres_username% -d book_accounting_system -a -f .\scripts\insertAndUpdate.sql
ENDLOCAL