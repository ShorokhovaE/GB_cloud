package ru.gb.file.gb_cloud;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import io.netty.channel.ChannelHandlerContext;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import ru.gb.file.gb_cloud.dto.AuthRequest;
import ru.gb.file.gb_cloud.dto.BasicResponse;
import ru.gb.file.gb_cloud.dto.DisconnectRequest;

public class SecondaryController implements Initializable {

    @FXML
    public LocalFilePanelController leftPC;
    @FXML
    public ServerFilePanelController rightPC;
    @FXML
    public VBox leftPanel;
    @FXML
    public VBox rightPanel;

    private Connect connect;

    @FXML
    public void clickBtnExit(ActionEvent actionEvent) throws IOException {

        PrimaryController pr =
                (PrimaryController) ControllerRegistry.getControllerObject(PrimaryController.class);
        connect = pr.getConnect();

        DisconnectRequest disconnectRequest = new DisconnectRequest();
        connect.getChannel().writeAndFlush(disconnectRequest);

        App.setRoot("primary");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ControllerRegistry.register(this);

        leftPC = (LocalFilePanelController) leftPanel.getUserData();
        rightPC = (ServerFilePanelController) rightPanel.getUserData();
    }
}