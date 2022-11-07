package ru.example.testneed;

import org.h2.api.Trigger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AfterInsertBookTrigger implements Trigger {
    @Override
    public void init(Connection conn, String schemaName, String triggerName, String tableName, boolean before, int type) throws SQLException {
        Trigger.super.init(conn, schemaName, triggerName, tableName, before, type);
    }

    @Override
    public void fire(Connection conn, Object[] oldRow, Object[] newRow) throws SQLException {
        try (PreparedStatement preparedStatement = conn.prepareStatement(
                "INSERT INTO public.book_storage(book_storage_id, book_id, quantity) " +
                "VALUES(nextval('book_storage_book_storage_id_seq'), ?, 0)")) {
            preparedStatement.setObject(1, newRow[0]);
            preparedStatement.executeUpdate();
        }
    }

    @Override
    public void close() throws SQLException {
        Trigger.super.close();
    }

    @Override
    public void remove() throws SQLException {
        Trigger.super.remove();
    }
}
