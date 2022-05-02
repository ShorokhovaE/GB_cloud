package ru.gb.file.gb_cloud;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

public class SecondaryController implements Initializable {

    @FXML
    public TableView <FileInfo> local;
    @FXML
    public TableView <FileInfo> server;
    @FXML
    public TextField localPathField;
    @FXML
    public TextField serverPathField;

    @FXML
    public void clickBtnExit(ActionEvent actionEvent) throws IOException {
        App.setRoot("primary");
    }

    public void updatePathLocal(Path path) {
        try {
        localPathField.setText(path.normalize().toAbsolutePath().toString());
        local.getItems().clear();
        local.getItems().addAll(Files.list(path).map(FileInfo::new).collect(Collectors.toList()));
        local.sort();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void updatePathServer(Path path){
        try {
            serverPathField.setText(path.normalize().toAbsolutePath().toString());
            server.getItems().clear();
            server.getItems().addAll(Files.list(path).map(FileInfo::new).collect(Collectors.toList()));
            server.sort();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ControllerRegistry.register(this);
//        try {
//            updateServerFileList(pr.AuthLogin.getText().trim());
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }

        TableColumn<FileInfo, String> serverFileNameColumn //
                = new TableColumn<FileInfo, String>("Имя файла");

        serverFileNameColumn.setCellValueFactory(param ->
                new SimpleStringProperty(param.getValue().getFileName()));

        TableColumn<FileInfo, String> localFileNameColumn //
                = new TableColumn<FileInfo, String>("Имя файла");

        localFileNameColumn.setCellValueFactory(param ->
                new SimpleStringProperty(param.getValue().getFileName()));

        server.getColumns().add(serverFileNameColumn);
        local.getColumns().add(localFileNameColumn);

        updatePathLocal(Paths.get("."));

        PrimaryController pr=
                (PrimaryController) ControllerRegistry.getControllerObject(PrimaryController.class);
        updatePathServer(Path.of("src/main/clients.directory/", pr.AuthLogin.getText().trim()));
    }

    public void btnLocalPathUpAction(ActionEvent actionEvent) {
        Path upperPath = Paths.get(localPathField.getText()).getParent();
        if (upperPath != null) {
            updatePathLocal(upperPath);
        }
    }

    public void btnServerPathUpAction(ActionEvent actionEvent) {
        Path upperPath = Paths.get(serverPathField.getText()).getParent();
        if (upperPath != null) {
            updatePathLocal(upperPath);
        }
    }

//    public void pathDir(String login) throws IOException {
//        Path p = Path.of("src/main/clients.directory/", login);
//        Files.walk(p, 3)
//                .map(Path::toFile)
//                .filter(file -> file.isFile())
//                .map(file -> file.getName())
//                .forEach(System.out::println);
//    }

//    public void updateServerFileList(String login) throws IOException{
//        Path p = Path.of(serverPathField.getText());
//        Files.walk(p, 3)
//                .map(Path::toFile)
//                .filter(file -> file.isFile())
//                .map(file -> file.getName())
//                .forEach(System.out::println);
//
////        for (String s: filesList) {
////            server.appendText(s + " \n");
////        }
//    }
}