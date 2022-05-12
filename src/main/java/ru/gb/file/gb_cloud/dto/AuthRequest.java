package ru.gb.file.gb_cloud.dto;

import java.sql.*;

public class AuthRequest implements BasicRequest {

    private String login;
    private String password;

    public AuthRequest(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String getType() {
        return "auth";
    }


//    //проверка наличия учетной записи
    public boolean checkLoginAndPassword() {
        try {
            ResultSet rs = DBConnect.getStm().executeQuery("SELECT * FROM clients;");
            while (rs.next()) {
                if (rs.getString("login").equals(login) && rs.getString("password").equals(password)) {
                    return true;
                }
            }
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

}
