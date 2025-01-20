module com.example.watertall {
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;
    requires java.desktop;
    requires java.sql;
    requires io.github.cdimascio.dotenv.java;
    requires mysql.connector.j;
    requires org.json;
    requires jbcrypt;
    requires com.fazecast.jSerialComm;
    requires com.jfoenix;

    opens com.example.watertall to javafx.fxml;
    exports com.example.watertall;
}