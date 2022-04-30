module org.example {
    requires javafx.controls;
    requires javafx.fxml;
    requires io.netty.transport;
    requires io.netty.codec;
    requires java.sql;
    requires javax.inject;

    opens ru.gb.file.gb_cloud to javafx.fxml;
    exports ru.gb.file.gb_cloud;
}
