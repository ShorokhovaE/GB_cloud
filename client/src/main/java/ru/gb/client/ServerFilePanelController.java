package ru.gb.client;

import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import ru.gb.dto.DeleteFileRequest;
import ru.gb.dto.DownloadFileRequest;
//import ru.gb.file.gb_cloud.dto.DownloadFileRequest;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Array;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class ServerFilePanelController implements Initializable {

    @FXML
    public TextField pathField, NameOfNewDir;
    @FXML
    public TableView <FileInfo> fileTable;
    @FXML
    public HBox newDirPanel;
    @FXML
    public Button BtnViewNewDiePanel;
    private Connect connect;
    private PrimaryController pr;
    private static final int MAXFILESSIZE = 2 * 1_000_000;
    private static long currentFilesSize;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ControllerRegistry.register(this);

        TableColumn<FileInfo, String> fileNameColumn
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

        pr = (PrimaryController) ControllerRegistry.getControllerObject(PrimaryController.class);
        updatePath(Path.of("./client-dir/", pr.AuthLogin.getText().trim()));

        connect = pr.getConnect();

    }

    @FXML
    public void btnUpAction(ActionEvent actionEvent) {

        String  minPath = Path.of(String.format("./client-dir/%s", pr.AuthLogin.getText().trim()))
                .normalize().toAbsolutePath().toString();

        if (pathField.getText().equals(minPath)){
            updatePath(Path.of(pathField.getText()));
        } else {
            Path upperPath = Paths.get(pathField.getText()).getParent();
            if (upperPath != null) {
                updatePath(upperPath);
            }
        }
    }

    public boolean checkLimitForLoad(long size) throws IOException {

        this.currentFilesSize = Files.walk(Path.of("./client-dir/", pr.AuthLogin.getText().trim()))
                .map(Path::toFile)
                .filter(File::isFile)
                .mapToLong(File::length)
                .sum();
        if((currentFilesSize + size) > MAXFILESSIZE){
            return false;
        } else {
            return true;
        }
    }

    @FXML
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

        if(fileTable.getSelectionModel().getSelectedItem() == null){
            alertInfo("Файл не выбран");
        } else if(fileTable.getSelectionModel().getSelectedItem().getPath().toFile().isDirectory()) {
            alertInfo("Папку нельзя скачать. Выберите файл");
        } else if(fileTable.getSelectionModel().getSelectedItem().getSize() > Connect.MB_20){
            alertInfo("Слишком большой файл, давай не будем рисковать :)");
        } else {

            DownloadFileRequest dfr =
                    new DownloadFileRequest(String.valueOf(fileTable.getSelectionModel().getSelectedItem().getPath()),
                            String.valueOf(fileTable.getSelectionModel().getSelectedItem().getFileName()));

            connect.getChannel().writeAndFlush(dfr);
        }

    }

    @FXML
    public void alertInfo(String msg){
        Alert alert = new Alert(Alert.AlertType.INFORMATION, msg, ButtonType.OK);
        alert.showAndWait();
    }

    @FXML
    public void btnUpdateFileList(ActionEvent actionEvent) {
        updatePath(Path.of(pathField.getText()));
    }

    @FXML
    public void clickBtnViewNewDirPanel(ActionEvent actionEvent) {

            if(newDirPanel.isVisible()){
                newDirPanel.setVisible(false);
                newDirPanel.setManaged(false);
            } else {
                newDirPanel.setVisible(true);
                newDirPanel.setManaged(true);
            }
        }


    @FXML
    public void clickBtnCreateNewDir(ActionEvent actionEvent) {
            String pathOfNewDir = String.format(pathField.getText() + "/" + NameOfNewDir.getText());
            new File(pathOfNewDir).mkdirs();
            NameOfNewDir.clear();
            newDirPanel.setVisible(false);
            newDirPanel.setManaged(false);
            updatePath(Path.of(pathField.getText()));
    }

    @FXML
    public void clickBtnDelete(ActionEvent actionEvent) {

        if (fileTable.getSelectionModel().getSelectedItem() == null) {
                alertInfo("Файл не выбран");
        } else {
            Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Удаление");
                alert.setHeaderText("Вы действительно хотите удалить " + fileTable.getSelectionModel().getSelectedItem().getFileName() + " ?");
                Optional<ButtonType> option = alert.showAndWait();
                if (option.get() == ButtonType.OK) {
                    DeleteFileRequest dfr = new DeleteFileRequest(fileTable.getSelectionModel().getSelectedItem().getPath().toString());
                    connect.getChannel().writeAndFlush(dfr);
                } else if (option.get() == ButtonType.CANCEL) {
                    return;
                } else {
                    return;
                }
            });
        }
    }
}