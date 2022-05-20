package ru.gb.client;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.VBox;
import ru.gb.dto.DisconnectRequest;
//import ru.gb.file.gb_cloud.dto.DisconnectRequest;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class SecondaryController implements Initializable {

    @FXML
    public LocalFilePanelController leftPC;
    @FXML
    public ServerFilePanelController rightPC;
    @FXML
    public VBox leftPanel, rightPanel;
    private Connect connect;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ControllerRegistry.register(this);

        leftPC = (LocalFilePanelController) leftPanel.getUserData();
        rightPC = (ServerFilePanelController) rightPanel.getUserData();
    }

    @FXML
    public void clickBtnExit(ActionEvent actionEvent) throws IOException {

        PrimaryController pr =
                (PrimaryController) ControllerRegistry.getControllerObject(PrimaryController.class);
        connect = pr.getConnect();

        DisconnectRequest disconnectRequest = new DisconnectRequest();
        connect.getChannel().writeAndFlush(disconnectRequest);

        App.setRoot("primary");
    }
}