package ru.gb.dto;

import java.io.File;
import java.security.SecureRandom;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.regex.Pattern;


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

    public boolean registration() {

        try {
            ResultSet rs = DBConnect.getStm().executeQuery("SELECT * FROM clients;");
            while (rs.next()) {
                if (rs.getString("login").equals(login)){
                    return false;
                }
            }
            hashPassword();
            addInDB(login, password);
            createDirectory(login);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    public void addInDB(String login, String password) throws SQLException {
        PreparedStatement psInsert =
                DBConnect.getConnection().prepareStatement("INSERT INTO clients (login, password) VALUES ( ? , ? );");
        psInsert.setString(1, login);
        psInsert.setString(2, password);
        psInsert.executeUpdate();
    }


    public void createDirectory(String login){
        new File("client-dir/", login).mkdirs();
    }

    public void hashPassword(){
        PBKBF2HashPassword hashPassword = new PBKBF2HashPassword(10);
        this.password = hashPassword.hash(password);
    }
}
