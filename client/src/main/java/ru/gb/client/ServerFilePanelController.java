package ru.gb.client;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
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
    public TextField pathField;
    @FXML
    public TableView <FileInfo> fileTable;
    @FXML
    public HBox newDirPanel;
    @FXML
    public TextField NameOfNewDir;
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

        pr= (PrimaryController) ControllerRegistry.getControllerObject(PrimaryController.class);
        updatePath(Path.of("client-dir/", pr.AuthLogin.getText().trim()));

    }

    @FXML
    public void btnUpAction(ActionEvent actionEvent) {

        String minPath = String.format("/Users/elenasorohova/Desktop/GB_cloud/client-dir/%s", pr.AuthLogin.getText().trim());

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

        this.currentFilesSize = Files.walk(Path.of("/Users/elenasorohova/Desktop/GB_cloud/client-dir/", pr.AuthLogin.getText().trim()))
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
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Файл не выбран", ButtonType.OK);
            alert.showAndWait();
        } else if(fileTable.getSelectionModel().getSelectedItem().getType().equals("D")){
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Папку нельзя скачать. Выберите файл", ButtonType.OK);
            alert.showAndWait();
        } else {
            DownloadFileRequest dfr =
                    new DownloadFileRequest(String.valueOf(fileTable.getSelectionModel().getSelectedItem().getPath()),
                            String.valueOf(fileTable.getSelectionModel().getSelectedItem().getFileName()));

            connect = pr.getConnect();
            connect.getChannel().writeAndFlush(dfr);
        }

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
        if(fileTable.getSelectionModel().getSelectedItem() == null){
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Файл не выбран", ButtonType.OK);
            alert.showAndWait();
        }  else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Удаление");
            alert.setHeaderText("Вы действительно хотите удалить " + fileTable.getSelectionModel().getSelectedItem().getFileName() + " ?");
            Optional<ButtonType> option = alert.showAndWait();

            if (option.get() == ButtonType.OK) {
                recursiveDelete(new File(String.valueOf(fileTable.getSelectionModel().getSelectedItem().getPath())));
                updatePath((Path.of(pathField.getText())));
                Alert alertDel = new Alert(Alert.AlertType.INFORMATION);
                alertDel.setHeaderText("Файл удален!");
                alertDel.showAndWait();
            } else if (option.get() == ButtonType.CANCEL) {
                return;
            } else {
                return;
            }
        }
    }


    public static void recursiveDelete(File file) {
        if (!file.exists())
            return;
        if (file.isDirectory()) {
            for (File f : file.listFiles()) {
                recursiveDelete(f);
            }
        }
        file.delete();
    }
}
