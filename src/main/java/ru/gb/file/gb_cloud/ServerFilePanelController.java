package ru.gb.file.gb_cloud;

import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class ServerFilePanelController implements Initializable {

    @FXML
    public TextField pathField;
    @FXML
    public TableView <FileInfo> fileTable;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ControllerRegistry.register(this);

        TableColumn<FileInfo, String> fileNameColumn //
                = new TableColumn<FileInfo, String>("Имя файла");

        fileNameColumn.setCellValueFactory(param ->
                new SimpleStringProperty(param.getValue().getFileName()));

        fileTable.getColumns().add(fileNameColumn);

        fileTable.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if(mouseEvent.getClickCount() == 2) {
                    Path path = Paths.get(pathField.getText()).resolve(fileTable.getSelectionModel().getSelectedItem().getFileName());
                    if (Files.isDirectory(path)) {
                        updatePath(path);
                    }
                }
            }
        });

        PrimaryController pr=
                (PrimaryController) ControllerRegistry.getControllerObject(PrimaryController.class);
        updatePath(Path.of("src/main/clients.directory/", pr.AuthLogin.getText().trim()));

    }
    @FXML
    public void btnUpAction(ActionEvent actionEvent) {
        Path upperPath = Paths.get(pathField.getText()).getParent();
        if (upperPath != null) {
            updatePath(upperPath);
        }
    }

    public void updatePath(Path path){
        try {
            pathField.setText(path.normalize().toAbsolutePath().toString());
            fileTable.getItems().clear();
            fileTable.getItems().addAll(Files.list(path).map(FileInfo::new).collect(Collectors.toList()));
            fileTable.sort();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void clickBtnDownload(ActionEvent actionEvent) {
        System.out.println(fileTable.getSelectionModel().getSelectedItem().getFileName());
    }
}
