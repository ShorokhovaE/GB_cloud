package ru.gb.file.gb_cloud;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.socket.nio.NioSocketChannel;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
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
    public TextField RegLogin;
    @FXML
    public PasswordField RegPassword;
    @FXML
    public PasswordField RegPasswordCopy;
    private Connect connect;
    private SecondaryController secondaryController;




    @FXML
    public void switchToSecondary() throws IOException {

       App.setRoot("secondary");
    }


    @FXML
    public void clickBtnAuth(ActionEvent actionEvent) {
        RegPanel.setVisible(false);
        RegPanel.setManaged(false);
        AuthPanel.setVisible(true);
        AuthPanel.setManaged(true);

    }

    @FXML
    public void clickBtnReg(ActionEvent actionEvent) {
        AuthPanel.setVisible(false);
        AuthPanel.setManaged(false);
        RegPanel.setVisible(true);
        RegPanel.setManaged(true);
    }
// авторизация
    public void clickBtnGo(ActionEvent actionEvent) {
        AuthRequest authRequest = new AuthRequest(AuthLogin.getText().trim(), AuthPassword.getText().trim());
        connect.getChannel().writeAndFlush(authRequest);

    }

    public void labelWrongLogPass(){
            this.LoginNo.setVisible(true);
            this.LoginNo.setManaged(true);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        connect = new Connect();
    }

    public void ClickBtnReg(ActionEvent actionEvent) {
        if(!RegPassword.getText().trim().equals(RegPasswordCopy.getText().trim())){
            System.out.println("Пароли не совпадают");
        } else {
            RegRequest regRequest = new RegRequest(RegLogin.getText().trim(), RegPassword.getText().trim());
            connect.getChannel().writeAndFlush(regRequest);
        }
    }

    public void GoSecondary(ActionEvent actionEvent) throws IOException {
        App.setRoot("secondary");
    }
}
