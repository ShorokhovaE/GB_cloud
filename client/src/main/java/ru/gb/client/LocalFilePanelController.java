package ru.gb.client;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import ru.gb.dto.LoadFileRequest;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class LocalFilePanelController implements Initializable {

    @FXML
    public TextField pathField;
    @FXML
    public TableView <FileInfo> fileTable;
    private Connect connect;
    private ServerFilePanelController serverPanel;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        ControllerRegistry.register(this);


        TableColumn<FileInfo, String> fileNameColumn //
                = new TableColumn<FileInfo, String>("Имя файла");

        fileNameColumn.setCellValueFactory(param ->
                new SimpleStringProperty(param.getValue().getFileName()));
        fileNameColumn.setPrefWidth(100);

        TableColumn<FileInfo, Long> fileSizeColumn = new TableColumn<>("Размер");
        fileSizeColumn.setCellValueFactory(param ->
                new SimpleObjectProperty<>(param.getValue().getSize()));
        fileSizeColumn.setPrefWidth(100);
        fileSizeColumn.setCellFactory(param -> {
            return new TableCell<FileInfo, Long>() {
                @Override
                protected void updateItem(Long item, boolean empty) {
                    super.updateItem(item,empty);
                    if (item == null || empty) {
                        setText(null);
                        setStyle("");
                    } else {
                        String text = String.format("%,d bytes",item);
                        if (item == -1L) {
                            text = "[DIR]";
                        }
                        setText(text);
                    }
                }
            };
        });

        fileTable.getColumns().addAll(fileNameColumn, fileSizeColumn);


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

        updatePath(Paths.get("."));
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
    public void clickBtnLoad(ActionEvent actionEvent) throws IOException {

        serverPanel = (ServerFilePanelController) ControllerRegistry.getControllerObject(ServerFilePanelController.class);

        if(fileTable.getSelectionModel().getSelectedItem() == null){
            alertInfo("Файл не выбран");
        } else if(fileTable.getSelectionModel().getSelectedItem().getPath().toFile().isDirectory()){
            alertInfo("Папку нельзя загрузить. Выберите файл");
        } else if(fileTable.getSelectionModel().getSelectedItem().getSize() > Connect.MB_20){
            alertInfo("Слишком большой файл, давай не будем рисковать :)");
        } else if(!serverPanel.checkLimitForLoad(fileTable.getSelectionModel().getSelectedItem().getSize())){
            alertInfo("Для этого файла не хватает места :( \nВыберите другой файл или удалите что-нибудь");
        } else {

            LoadFileRequest loadFileRequest =
                    new LoadFileRequest
                            (new File(String.valueOf(fileTable.getSelectionModel().getSelectedItem().getPath())),
                                    fileTable.getSelectionModel().getSelectedItem().getFileName(),
                                    serverPanel.pathField.getText());

            PrimaryController pr =
                    (PrimaryController) ControllerRegistry.getControllerObject(PrimaryController.class);

            connect = pr.getConnect();
            connect.getChannel().writeAndFlush(loadFileRequest);
        }

    }

    public void alertInfo(String msg){
        Alert alert = new Alert(Alert.AlertType.INFORMATION, msg, ButtonType.OK);
        alert.showAndWait();
    }

    @FXML
    public void btnUpdateFileList(ActionEvent actionEvent) {
        updatePath(Path.of(pathField.getText()));
    }

}
