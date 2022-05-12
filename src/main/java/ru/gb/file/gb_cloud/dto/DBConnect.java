package ru.gb.file.gb_cloud.dto;

import java.sql.*;

public class DBConnect {

    private static Connection connection;   // чтобы подключиться
    private static Statement stm;           // чтобы делать запросы

    public static Statement getStm() {
        return stm;
    }

    public static Connection getConnection() {
        return connection;
    }

    //подключение к БД
    public static void connect() throws Exception {

        Class.forName("org.sqlite.JDBC");   //загрузка драйвера
        connection = DriverManager.getConnection("jdbc:sqlite:main.db");    //подключение драйвера
        stm = connection.createStatement(); // после получения connection, можем получить stm, чтобы делать запросы
        System.out.println("БД подключена");
    }

    //Отключение от БД
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
