module com.example.bibliotecadecodigopmi {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;
    requires java.sql;
    requires mpxj;
    requires org.apache.commons.io;
    requires java.desktop;

    opens com.example.bibliotecadecodigopmi to javafx.fxml;
    exports com.example.bibliotecadecodigopmi.gui;
    opens com.example.bibliotecadecodigopmi.gui to javafx.fxml;
    exports com.example.bibliotecadecodigopmi.scrumlibrary;
    opens com.example.bibliotecadecodigopmi.scrumlibrary to javafx.fxml;
}