module com.example.skyboxjavafxtester {
    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires javafx.graphics;
    requires javafx.controls;
    requires jim3dsModelImporterJFX;

    opens com.example.skyboxjavafxtester to javafx.fxml;
    exports com.example.skyboxjavafxtester;
}