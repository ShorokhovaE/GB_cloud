package ru.gb.client;

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
//    @FXML
//    public Label msgMaxDirDepth;
//    public Button BtnViewNewDiePanel;

    private Connect connect;
    private PrimaryController pr;

    private static final int MAXDIRDEPTH = 3;
    private static int currentDirDepth = 1;

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

    public void updatePath(Path path){
        try {
            pathField.setText(path.normalize().toAbsolutePath().toString());
            fileTable.getItems().clear();
            fileTable.getItems().addAll(Files.list(path).map(FileInfo::new).collect(Collectors.toList()));
            fileTable.sort();
            if(!checkMaxDirDepth()){
                BtnViewNewDiePanel.setVisible(false);
                BtnViewNewDiePanel.setManaged(false);
            } else {
                BtnViewNewDiePanel.setVisible(true);
                BtnViewNewDiePanel.setManaged(true);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void clickBtnDownload(ActionEvent actionEvent) {

        if(fileTable.getSelectionModel().getSelectedItem().getFileName() == null){
            System.out.println("файл не выбран");
        } else {
            System.out.println(fileTable.getSelectionModel().getSelectedItem().getFileName());
            String s = String.valueOf(fileTable.getSelectionModel().getSelectedItem().getPath());
            System.out.println(s);
        }

        DownloadFileRequest dfr =
                new DownloadFileRequest(String.valueOf(fileTable.getSelectionModel().getSelectedItem().getPath()),
                        String.valueOf(fileTable.getSelectionModel().getSelectedItem().getFileName()));

        connect = pr.getConnect();
        connect.getChannel().writeAndFlush(dfr);
    }

    @FXML
    public void btnUpdateFileList(ActionEvent actionEvent) {
        updatePath(Path.of(pathField.getText()));
    }

    @FXML
    public void clickBtnViewNewDirPanel(ActionEvent actionEvent) {

        if(checkMaxDirDepth()){
            System.out.println("Все в порядке, можно создавать");
        } else {
            System.out.println("Слишком много папок");
        }
        if(currentDirDepth <= MAXDIRDEPTH){
            if(newDirPanel.isVisible()){
                newDirPanel.setVisible(false);
                newDirPanel.setManaged(false);
            } else {
                newDirPanel.setVisible(true);
                newDirPanel.setManaged(true);
            }
        } else {
//            BtnViewNewDiePanel.setVisible(false);
//            BtnViewNewDiePanel.setManaged(false);
        }
    }

    public boolean checkMaxDirDepth(){

        String s = pathField.getText();
        StringBuilder[] sb = Arrays.stream(s.split("/"))
                .map(StringBuilder::new)
                .filter(stringBuilder -> !stringBuilder.isEmpty())
                .skip(5)
                .toArray(StringBuilder[]::new);

        System.out.println("Текущий уровень: " + sb.length);

        if(sb.length<MAXDIRDEPTH){
            return true;
        } else {
            return false;
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
}
