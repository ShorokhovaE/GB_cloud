package ru.gb.client;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import ru.gb.dto.AuthRequest;
import ru.gb.dto.RegRequest;
//import ru.gb.file.gb_cloud.dto.AuthRequest;
//import ru.gb.file.gb_cloud.dto.RegRequest;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class PrimaryController implements Initializable {
    @FXML
    public VBox RegPanel, AuthPanel;
    @FXML
    public TextField AuthLogin;
    @FXML
    public PasswordField AuthPassword;
    @FXML
    public Label LoginNo;
    @FXML
    public Label PassSame, RegNo, RegOk, EmptyFieldReg;
    @FXML
    public TextField RegLogin;
    @FXML
    public PasswordField RegPassword, RegPasswordCopy;
    private static Stage stage;
    private Connect connect;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ControllerRegistry.register(this);
        connect = new Connect();

        Platform.runLater(() -> {
            stage = (Stage) AuthPanel.getScene().getWindow();
            stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent windowEvent) {
                    stage.close();
                    Connect.getEventLoopGroup().shutdownGracefully();
                }
            });
        });
    }

    public Connect getConnect() {
        return connect;
    }

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

    @FXML
    public void clickBtnGo(ActionEvent actionEvent) {
        AuthRequest authRequest = new AuthRequest(AuthLogin.getText().trim(), AuthPassword.getText().trim());
        connect.getChannel().writeAndFlush(authRequest);
    }

    @FXML
    public void clearMsg(){
        EmptyFieldReg.setManaged(false);
        EmptyFieldReg.setVisible(false);
        RegNo.setManaged(false);
        RegNo.setVisible(false);
        RegOk.setManaged(false);
        RegOk.setVisible(false);
        PassSame.setManaged(false);
        PassSame.setVisible(false);
    }

    @FXML
    public void viewMsg(Label msg){
        msg.setVisible(true);
        msg.setManaged(true);
    }

    @FXML
    public void ClickBtnReg(ActionEvent actionEvent) {
        clearMsg();
        if(!RegPassword.getText().trim().equals(RegPasswordCopy.getText().trim())){
            viewMsg(PassSame);
        } else if(RegPassword.getText().isEmpty() || RegPasswordCopy.getText().isEmpty() || RegLogin.getText().isEmpty()){
            viewMsg(EmptyFieldReg);
        }
        else {
            RegRequest regRequest = new RegRequest(RegLogin.getText().trim(), RegPassword.getText().trim());
            connect.getChannel().writeAndFlush(regRequest);
        }
    }
}
