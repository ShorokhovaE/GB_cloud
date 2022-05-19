package ru.gb.dto;

import java.sql.ResultSet;
import java.sql.SQLException;

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


    @Override
    public String getType() {
        return "auth";
    }


//    //проверка наличия учетной записи
    public boolean checkLoginAndPassword() {
        try {
            PBKBF2HashPassword hashPassword = new PBKBF2HashPassword(10);
            ResultSet rs = DBConnect.getStm().executeQuery("SELECT * FROM clients;");
            while (rs.next()) {
                if ( rs.getString("login").equals(login) && hashPassword.checkPassword(password, rs.getString("password"))){
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
