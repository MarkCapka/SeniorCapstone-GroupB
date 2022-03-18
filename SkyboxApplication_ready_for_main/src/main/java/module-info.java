module com.example.skyboxjavafxtester {
    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires eu.hansolo.tilesfx;
    requires javafx.graphics;
    requires javafx.controls;
    requires jim3dsModelImporterJFX;
    requires sunset1;
    requires javafx.fxml;
    requires java.desktop;

    opens com.example.skyboxjavafxtester to javafx.fxml;
    exports com.example.skyboxjavafxtester;
}