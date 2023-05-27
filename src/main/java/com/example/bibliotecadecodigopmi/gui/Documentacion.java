package com.example.bibliotecadecodigopmi.gui;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class Documentacion extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Documentación");

        TabPane tabPane = new TabPane();
        Tab proyectosTab = new Tab();
        proyectosTab.setText("Proyectos");
        proyectosTab.setContent(setupProyectos());

        Tab documentacionTab = new Tab();
        documentacionTab.setText("Documentación");
        documentacionTab.setContent(setupDocumentacion());

        tabPane.getTabs().add(proyectosTab);
        tabPane.getTabs().add(documentacionTab);

        Scene scene = new Scene(tabPane, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private BorderPane setupProyectos() {
        BorderPane proyectosLayout = new BorderPane();
        proyectosLayout.setPadding(new Insets(10));
        proyectosLayout.setPrefSize(800, 600);

        // Aquí configura la lista de proyectos y los detalles del proyecto según tus necesidades

        return proyectosLayout;
    }

    protected BorderPane setupDocumentacion() {
        BorderPane documentacionLayout = new BorderPane();
        documentacionLayout.setPadding(new Insets(10));

        VBox contenidoLayout = new VBox(10);
        contenidoLayout.setAlignment(Pos.TOP_CENTER);

        Label titulo = new Label("Metodología Scrum");
        titulo.setFont(Font.font("Arial", FontWeight.BOLD, 16));

        TextFlow texto = new TextFlow();
        texto.setLineSpacing(5);
        texto.setTextAlignment(javafx.scene.text.TextAlignment.JUSTIFY);

        texto.getChildren().addAll(
                new Text("""
                        Scrum es una metodología ágil para la gestión y desarrollo de proyectos de software. Se basa en la idea de realizar el trabajo en iteraciones cortas y enfocarse en la entrega de incrementos de valor de forma regular.

                        """),
                new Text("Los principales roles en Scrum son:\n"),
                new Text("- Scrum Master: Es responsable de facilitar el proceso y asegurarse de que el equipo siga las prácticas de Scrum.\n"),
                new Text("- Product Owner: Representa al cliente y define los requisitos del producto.\n"),
                new Text("- Equipo de Desarrollo: Es el equipo encargado de desarrollar el producto.\n\n"),
                new Text("Scrum se basa en eventos, artefactos y reglas. Los eventos principales son:\n"),
                new Text("- Sprint Planning: Reunión en la que se planifica el trabajo para el siguiente sprint.\n"),
                new Text("- Daily Scrum: Reunión diaria en la que el equipo comparte el progreso y planifica el trabajo del día.\n"),
                new Text("- Sprint Review: Reunión al final del sprint para revisar el trabajo realizado.\n"),
                new Text("- Sprint Retrospective: Reunión para reflexionar sobre el sprint y mejorar el proceso.\n\n"),
                new Text("Existen diversos artefactos en Scrum, como el Product Backlog, el Sprint Backlog y el Incremento del producto.\n\n"),
                new Text("Para obtener más información sobre Scrum, puedes consultar la documentación oficial en el siguiente enlace: ")
        );

        Hyperlink enlace = new Hyperlink("https://www.scrum.org/resources/what-is-scrum");
        enlace.setOnAction(e -> {
            // Abrir el enlace en un navegador externo
            try {
                Desktop.getDesktop().browse(new URI(enlace.getText()));
            } catch (IOException | URISyntaxException ex) {
                ex.printStackTrace();
            }
        });

        texto.getChildren().add(enlace);
        contenidoLayout.getChildren().addAll(titulo, texto);

        documentacionLayout.setCenter(contenidoLayout);

        return documentacionLayout;
    }
}
