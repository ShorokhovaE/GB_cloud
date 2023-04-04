package ru.gb.dto;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DBConnect {

    private static Connection connection;
    private static Statement stm;

    public static Statement getStm() {
        return stm;
    }

    public static Connection getConnection() {
        return connection;
    }

    public static void connect() throws Exception {

        Class.forName("org.sqlite.JDBC");
        connection = DriverManager.getConnection("jdbc:sqlite:main.db");
        stm = connection.createStatement();
        System.out.println("БД подключена");
    }

    public static void disconnect() {
        try {
            stm.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
