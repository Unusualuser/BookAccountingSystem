FROM postgres:14
ENV POSTGRES_USER postgres
ENV POSTGRES_PASSWORD postgres
ENV POSTGRES_DB book_accounting_system
COPY /scripts/createTablesSequencesAndTriggers.sql /docker-entrypoint-initdb.d/
COPY /scripts/insertAndUpdate.sql /docker-entrypoint-initdb.d/
EXPOSE 5432