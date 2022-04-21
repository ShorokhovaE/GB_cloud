module org.example {
    requires javafx.controls;
    requires javafx.fxml;

    opens ru.gb.file.net.storage to javafx.fxml;
    exports ru.gb.file.net.storage;
}
