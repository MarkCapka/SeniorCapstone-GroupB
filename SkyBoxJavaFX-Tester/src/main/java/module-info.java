module com.example.skyboxjavafxtester {
    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;

    opens com.example.skyboxjavafxtester to javafx.fxml;
    exports com.example.skyboxjavafxtester;
}