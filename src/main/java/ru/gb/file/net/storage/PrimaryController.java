package ru.gb.file.net.storage;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;

public class PrimaryController {

    public VBox RegPanel;
    public VBox AuthPanel;

    @FXML
    private void switchToSecondary() throws IOException {
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

    @FXML
    public void clickBtnGo(ActionEvent actionEvent) {

    }
}
