package ru.gb.file.gb_cloud.dto;

import ru.gb.file.gb_cloud.PrimaryController;

import java.io.File;
import java.nio.file.Files;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RegRequest implements BasicRequest{

    private String login;
    private String password;

    @Override
    public String getType() {
        return "reg";
    }

    public RegRequest(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    //регистрация
    public boolean registration() {

        try {
            ResultSet rs = DBConnect.getStm().executeQuery("SELECT * FROM clients;");
            while (rs.next()) {
                if (rs.getString("login").equals(login)){
                    return false;
                }
            }
            addInDB(login, password);
            createDirectory(login); //создали директорию
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    //метод для добавления учетки в БД
    public void addInDB(String login, String password) throws SQLException {
        PreparedStatement psInsert =
                DBConnect.getConnection().prepareStatement("INSERT INTO clients (login, password) VALUES ( ? , ? );");
        psInsert.setString(1, login);
        psInsert.setString(2, password);
        psInsert.executeUpdate();
    }

    //создали директорию
    public void createDirectory(String login){
        new File("src/main/clients.directory/", login).mkdirs();
    }
}
