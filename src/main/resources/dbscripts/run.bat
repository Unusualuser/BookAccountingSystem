psql -U postgres -a -f .\scripts\createDatabase.sql
psql -U postgres -d book_accounting_system -a -f .\scripts\createTables.sql
psql -U postgres -d book_accounting_system -a -f .\scripts\insert.sql