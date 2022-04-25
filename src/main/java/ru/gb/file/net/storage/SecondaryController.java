package ru.gb.file.net.storage;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class SecondaryController {

    @FXML
    public void clickBtnExit(ActionEvent actionEvent) throws IOException {
        App.setRoot("primary");
    }
}