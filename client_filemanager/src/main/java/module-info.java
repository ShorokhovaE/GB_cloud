module client_filemanager {
    requires javafx.controls;
    requires javafx.fxml;
    requires io.netty.transport;
    requires io.netty.codec;
    requires java.sql;
    requires javax.inject;

    opens ru.gb.client to javafx.fxml;
    exports ru.gb.client;
}
