package ru.gb.file.gb_cloud;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import ru.gb.file.gb_cloud.dto.AuthRequest;
import ru.gb.file.gb_cloud.dto.RegRequest;

public class PrimaryController implements Initializable {
    @FXML
    public VBox RegPanel;
    @FXML
    public VBox AuthPanel;
    @FXML
    public TextField AuthLogin;
    @FXML
    public PasswordField AuthPassword;
    @FXML
    public Label LoginNo;
    @FXML
    public Label RegNo;
    @FXML
    public TextField RegLogin;
    @FXML
    public PasswordField RegPassword;
    @FXML
    public PasswordField RegPasswordCopy;
    @FXML
    public Label PassSame;

    public Connect getConnect() {
        return connect;
    }

    private Connect connect;

    @FXML
    public void loginOk() throws IOException {
       App.setRoot("secondary");
    }


    @FXML
    public void OpenAuthPanel(ActionEvent actionEvent) {
        RegPanel.setVisible(false);
        RegPanel.setManaged(false);
        AuthPanel.setVisible(true);
        AuthPanel.setManaged(true);

    }

    @FXML
    public void OpenRegPanel(ActionEvent actionEvent) {
        AuthPanel.setVisible(false);
        AuthPanel.setManaged(false);
        RegPanel.setVisible(true);
        RegPanel.setManaged(true);
    }
// авторизация
@FXML
    public void clickBtnGo(ActionEvent actionEvent) {
        AuthRequest authRequest = new AuthRequest(AuthLogin.getText().trim(), AuthPassword.getText().trim());
        connect.getChannel().writeAndFlush(authRequest);
    }

    public void loginNo(){
            LoginNo.setVisible(true);
            LoginNo.setManaged(true);
    }

    public void regNo(){
        PassSame.setVisible(false);
        PassSame.setManaged(false);
        RegNo.setVisible(true);
        RegNo.setManaged(true);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ControllerRegistry.register(this);
        connect = new Connect();
    }

    public void ClickBtnReg(ActionEvent actionEvent) {
        if(!RegPassword.getText().trim().equals(RegPasswordCopy.getText().trim())){
            RegNo.setVisible(false);
            RegNo.setManaged(false);
            PassSame.setVisible(true);
            PassSame.setManaged(true);
        } else {
            RegRequest regRequest = new RegRequest(RegLogin.getText().trim(), RegPassword.getText().trim());
            connect.getChannel().writeAndFlush(regRequest);
        }
    }
}
