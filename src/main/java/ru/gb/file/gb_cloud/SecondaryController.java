package ru.gb.file.gb_cloud;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

public class SecondaryController {

    public TextArea Local;
    public TextArea Server;

    @FXML
    public void clickBtnExit(ActionEvent actionEvent) throws IOException {
        App.setRoot("primary");
    }
    
    






}