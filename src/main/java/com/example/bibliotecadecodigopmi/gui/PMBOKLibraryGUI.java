package com.example.bibliotecadecodigopmi.gui;
import com.example.bibliotecadecodigopmi.scrumlibrary.Project;
import com.example.bibliotecadecodigopmi.scrumlibrary.Tarea;
import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
public class PMBOKLibraryGUI extends Application {
    private VBox projectDetailsLayout;
    private ObservableList<Project> observableProjects;
    private ProjectManager projectManager;
    public static void main(String[] args) {
        launch(args);
    }
    @Override
    public void start(Stage primaryStage){
        projectManager = new ProjectManager();
        primaryStage.setTitle("Libreria para gestion de desarrollo de software");
        setupUI(primaryStage);
        primaryStage.show();
    }
    private void setupButtons(ListView<Project> listaDeProyectos,BorderPane bordePrincipal,Stage primaryStage,ProjectManager projectManager){
        // Create the Agregar proyecto button
        Button agregarProyecto = new Button("Agregar proyecto");
        agregarProyecto.setOnAction(event -> agregarProyectoCuadro());

        // Create the Eliminar proyecto button
        Button eliminarProyecto = new Button("Eliminar proyecto");
        eliminarProyecto.setOnAction(event -> projectManager.eliminarProjecto(listaDeProyectos.getSelectionModel().getSelectedItem()));
        eliminarProyecto.setOnAction(event -> this.observableProjects.remove(listaDeProyectos.getSelectionModel().getSelectedItem()));
        // Create an HBox to hold the buttons
        HBox buttonsBox = new HBox(10, agregarProyecto, eliminarProyecto);
        buttonsBox.setAlignment(Pos.CENTER_LEFT);
        bordePrincipal.setBottom(buttonsBox);
        // Create the export button
        Button exportButton = new Button("Exportar proyectos a XML");
        exportButton.setOnAction(event -> {
            try {
                projectManager.exportToXML(projectManager.getProjects());
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Los proyectos se han exportado a XML correctamente");
                alert.showAndWait();
            } catch (Exception ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Error al exportar proyectos a XML: " + ex.getMessage());
                alert.showAndWait();
            }
        });
        // Create the scene
        Scene scene = new Scene(bordePrincipal);
        primaryStage.setScene(scene);
        primaryStage.show();
        // Create the export button for MPXJ
        Button exportButtonMPXJ = new Button("Exportar proyectos a MPXJ");
        exportButtonMPXJ.setOnAction(event -> {
            try {
                projectManager.exportToMPXJ(projectManager.getProjects(), "proyectos.mpxj");
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Los proyectos se han exportado a MPXJ correctamente");
                alert.showAndWait();
            } catch (Exception ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Error al exportar proyectos a MPXJ: " + ex.getMessage());
                alert.showAndWait();
            }
        });

        // Create the import button for MPXJ
        Button importButtonMPXJ = new Button("Importar proyectos desde MPXJ");
        importButtonMPXJ.setOnAction(event -> {
            try {
                projectManager.importFromMPXJ("proyectos.mpxj");
                this.observableProjects = FXCollections.observableList(projectManager.getProjects());
                listaDeProyectos.setItems(observableProjects);

                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Los proyectos se han importado desde MPXJ correctamente");
                alert.showAndWait();
            } catch (Exception ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Error al importar proyectos desde MPXJ: " + ex.getMessage());
                alert.showAndWait();
            }
        });
        HBox buttonsBox2 = new HBox(10, exportButtonMPXJ, exportButton, importButtonMPXJ);
        buttonsBox2.setAlignment(Pos.CENTER_LEFT);
        bordePrincipal.setTop(buttonsBox2);
    }

    private void setupUI(Stage primaryStage) {
        // Create the main layout
        BorderPane bordePrincipal = new BorderPane();
        bordePrincipal.setPadding(new Insets(10));
        bordePrincipal.setPrefSize(800, 600);
        // Create the project list view
        ListView<Project> listaDeProyectos = new ListView<>();
        listaDeProyectos.setPrefWidth(200);
        observableProjects = FXCollections.observableList(projectManager.getProjects());
        listaDeProyectos.setItems(observableProjects);
        listaDeProyectos.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                mostrarDetallesProjectos(primaryStage,newValue);
            }
        });
        bordePrincipal.setLeft(listaDeProyectos);
        // Create the project details layout
        projectDetailsLayout = new VBox(10);
        projectDetailsLayout.setAlignment(Pos.TOP_CENTER);
        projectDetailsLayout.setPadding(new Insets(10));
        bordePrincipal.setCenter(projectDetailsLayout);
        setupButtons(listaDeProyectos,bordePrincipal,primaryStage,projectManager);
    }

    private void mostrarDetallesProjectos(Stage primaryStage, Project project) {
        //Boton para revisar sprints
        Button Sprints = new Button("Sprints");
        Sprints.setOnAction(event -> {
            try {
                mostrarSprintsFases(primaryStage, project);
            } catch (Exception ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error al mostrar sprints");
                alert.setHeaderText(null);

                TextArea textArea = new TextArea(ex.getMessage());
                textArea.setEditable(false);
                textArea.setWrapText(true);
                textArea.setMaxWidth(Double.MAX_VALUE);
                textArea.setMaxHeight(Double.MAX_VALUE);

                GridPane.setVgrow(textArea, Priority.ALWAYS);
                GridPane.setHgrow(textArea, Priority.ALWAYS);

                GridPane contentPane = new GridPane();
                contentPane.setMaxWidth(Double.MAX_VALUE);
                contentPane.add(textArea, 0, 0);

                alert.getDialogPane().setExpandableContent(contentPane);
                alert.showAndWait();
            }
        });

        BorderPane borderPane = new BorderPane();
        // Create the project details section
        VBox cuadroDetallesDeProyecto = new VBox();
        Label nombreLabel = new Label(project.getNombre());
        nombreLabel.setStyle("-fx-font-size: 20pt; -fx-font-weight: bold;");
        Label fechaDeInicioLabel = new Label("Fecha de inicio: " + project.getFechaDeInicio());
        fechaDeInicioLabel.setStyle("-fx-font-size: 14pt;");
        Label fechaDeTerminadoLabel = new Label("Fecha de terminado: " + project.getFechaDeTerminado());
        fechaDeTerminadoLabel.setStyle("-fx-font-size: 14pt;");
        Label presupuestoLabel = new Label("Presupuesto: $" + project.getPresupuesto());
        presupuestoLabel.setStyle("-fx-font-size: 14pt;");
        Label projectManagerLabel = new Label("Manager de proyecto: " + project.getManagerDeProyecto());
        projectManagerLabel.setStyle("-fx-font-size: 14pt;");
        cuadroDetallesDeProyecto.getChildren().addAll(nombreLabel, fechaDeInicioLabel, fechaDeTerminadoLabel, presupuestoLabel, projectManagerLabel);
        cuadroDetallesDeProyecto.setSpacing(10);
        cuadroDetallesDeProyecto.setPadding(new Insets(10));
        borderPane.setTop(cuadroDetallesDeProyecto);

        // Crear la seccion de tareas de la tabla
        TableView<Tarea> tablaDeTareas = new TableView<>();
        TableColumn<Tarea, String> columnaDeNombres = new TableColumn<>("Nombre");
        columnaDeNombres.setCellValueFactory(new PropertyValueFactory<>("Nombre"));
        TableColumn<Tarea, LocalDate> fechaDeInicio = new TableColumn<>("FechaDeInicio");
        fechaDeInicio.setCellValueFactory(new PropertyValueFactory<>("FechaDeInicio"));

        TableColumn<Tarea, LocalDate> fechaParaTerminar = new TableColumn<>("FechaDeTerminado");
        fechaParaTerminar.setCellValueFactory(new PropertyValueFactory<>("FechaDeTerminado"));
        fechaParaTerminar.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(LocalDate fechaDeTerminado, boolean empty) {
                super.updateItem(fechaDeTerminado, empty);
                if (empty || fechaDeTerminado == null) {
                    setText(null);
                } else {
                    setText(fechaDeTerminado.toString());
                }
            }
        });
        TableColumn<Tarea, String> Descripcion = new TableColumn<>("Descripcion");
        Descripcion.setCellValueFactory(new PropertyValueFactory<>("Descripcion"));
        tablaDeTareas.getColumns().addAll(columnaDeNombres, fechaDeInicio, fechaParaTerminar, Descripcion);
        // Agregar las tareas a la vista de la tabla

        if (project.getTareas() == null) {
            project.setTareas(new ArrayList<>());
        }
        ObservableList<Tarea> tareasObservables = FXCollections.observableList(project.getTareas());
        tablaDeTareas.setItems(tareasObservables);
        // Crear el boton de agregar tareas
        Button botonAgregarTarea = new Button("Agregar tarea");
        botonAgregarTarea.setOnAction(event -> {
            Tarea nuevaTarea = mostrarCuadroAgregarTarea();
            tablaDeTareas.getItems().add(nuevaTarea);
        });
        Button botonEditarTarea = new Button("Editar tarea");
        botonEditarTarea.setOnAction(event -> {
            Tarea tareaSeleccionada = tablaDeTareas.getSelectionModel().getSelectedItem();
            if (tareaSeleccionada != null) {
                mostrarPanelEditarTarea(tareaSeleccionada,primaryStage);
            }
            tablaDeTareas.refresh();
        });

        // Crear el panel
        VBox vbox = new VBox(cuadroDetallesDeProyecto,Sprints, tablaDeTareas, botonAgregarTarea,botonEditarTarea);
        vbox.setAlignment(Pos.TOP_CENTER);
        vbox.setPadding(new Insets(10));
        vbox.setSpacing(10);
        borderPane.setCenter(vbox);
        projectDetailsLayout.getChildren().setAll(borderPane);
    }
    private void agregarProyectoCuadro() {
        Dialog<Project> AgregarProyectoCuadro = new Dialog<>();
        AgregarProyectoCuadro.setTitle("Agregar proyecto");
        // Set the dialog buttons
        ButtonType agregarBoton = new ButtonType("Agregar ", ButtonBar.ButtonData.OK_DONE);
        AgregarProyectoCuadro.getDialogPane().getButtonTypes().addAll(agregarBoton, ButtonType.CANCEL);
        // Create the dialog content
        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(10));
        // Create the name field
        TextField campoNombre = new TextField();
        campoNombre.setPromptText("Escriba aqui");
        // Create the start date picker
        DatePicker fechaDeInicioPicker = new DatePicker();
        fechaDeInicioPicker.setValue(LocalDate.now());
        // Create the end date picker
        DatePicker fechaDeTerminadoPicker = new DatePicker();
        fechaDeTerminadoPicker.setPromptText("Escriba aqui");
        fechaDeTerminadoPicker.setValue(LocalDate.now());
        TextField campoPresupuesto = new TextField();
        campoPresupuesto.setPromptText("Escriba aqui");
        TextField campoManagerDeProyecto = new TextField();
        campoManagerDeProyecto.setPromptText("Escriba aqui");
        gridPane.add(new Label("Nombre:"), 0, 0);
        gridPane.add(campoNombre, 1, 0);
        gridPane.add(new Label("Fecha de inicio:"), 0, 1);
        gridPane.add(fechaDeInicioPicker, 1, 1);
        gridPane.add(new Label("Fecha de terminado:"), 0, 2);
        gridPane.add(fechaDeTerminadoPicker, 1, 2);
        gridPane.add(new Label("Presupuesto:"), 0, 3);
        gridPane.add(campoPresupuesto, 1, 3);
        gridPane.add(new Label("Manager de proyecto:"), 0, 4);
        gridPane.add(campoManagerDeProyecto, 1, 4);
        AgregarProyectoCuadro.getDialogPane().setContent(gridPane);
        AgregarProyectoCuadro.setResultConverter(buttonType -> {
            if (buttonType == agregarBoton) {
                //Convertir las fechas a LocalDate
                LocalDate fechaDeInicio = fechaDeInicioPicker.getValue();
                LocalDate fechaDeTerminado = fechaDeTerminadoPicker.getValue();
                Project project = new Project(campoNombre.getText(),fechaDeInicio,fechaDeTerminado,null,campoPresupuesto.getText(), campoManagerDeProyecto.getText());
                observableProjects.add(project);
                return project;
            }
            return null;
        });
        AgregarProyectoCuadro.showAndWait();
    }
    private Tarea mostrarCuadroAgregarTarea(){
        Dialog<Tarea> cuadroAgregarTarea = new Dialog<>();
        cuadroAgregarTarea.setTitle("Agregar tarea");
        // Set the dialog buttons
        ButtonType agregarBoton = new ButtonType("Agregar ", ButtonBar.ButtonData.OK_DONE);
        cuadroAgregarTarea.getDialogPane().getButtonTypes().addAll(agregarBoton, ButtonType.CANCEL);
        // Create the name field
        TextField campoNombre = new TextField();
        campoNombre.setPromptText("Tarea");
        //Crear campo de descripcion
        TextField campoDescripcion = new TextField();
        campoDescripcion.setPromptText("Descripcion");
        // Create the start date field
        DatePicker fechaDeInicioPicker = new DatePicker();
        fechaDeInicioPicker.setValue(LocalDate.now());
        fechaDeInicioPicker.setPromptText("Fecha de inicio");
        // Create the end date field
        DatePicker fechaDeTerminadoPicker = new DatePicker();
        fechaDeTerminadoPicker.setValue(LocalDate.now());
        fechaDeTerminadoPicker.setPromptText("Fecha de terminado");
        // Create the dialog layout
        GridPane dialogLayout = new GridPane();
        dialogLayout.setHgap(10);
        dialogLayout.setVgap(10);
        dialogLayout.setAlignment(Pos.CENTER);
        dialogLayout.add(new Label("Tarea:"), 0, 0);
        dialogLayout.add(campoNombre, 1, 0);
        dialogLayout.add(new Label("Fecha de inicio:"), 0, 1);
        dialogLayout.add(fechaDeInicioPicker, 1, 1);
        dialogLayout.add(new Label("Fecha de terminado:"), 0, 2);
        dialogLayout.add(fechaDeTerminadoPicker, 1, 2);
        dialogLayout.add(new Label("Descripcion:"), 0, 3);
        dialogLayout.add(campoDescripcion, 1, 3);
        cuadroAgregarTarea.getDialogPane().setContent(dialogLayout);
        // Convert the result to a Tarea object
        cuadroAgregarTarea.setResultConverter(dialogButton -> {
            if (dialogButton == agregarBoton) {
                return new Tarea(campoNombre.getText(),fechaDeInicioPicker.getValue(),fechaDeTerminadoPicker.getValue(),campoDescripcion.getText());
            }
            return null;
        });
        Optional<Tarea> result = cuadroAgregarTarea.showAndWait();
        return result.orElse(null);
    }
    private void mostrarPanelEditarTarea(Tarea tareaSeleccionada, Stage primaryStage) {
        // Crear el panel emergente
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(primaryStage);
        VBox vbox = new VBox();
        Scene dialogScene = new Scene(vbox, 300, 200);
        dialog.setScene(dialogScene);

        // Crear los campos de edición
        TextField nombreTextField = new TextField(tareaSeleccionada.getNombre());
        DatePicker fechaInicioDatePicker = new DatePicker(tareaSeleccionada.getFechaDeInicio());
        DatePicker fechaTerminadoDatePicker = new DatePicker(tareaSeleccionada.getFechaDeTerminado());
        TextArea descripcionTextArea = new TextArea(tareaSeleccionada.getDescripcion());
        // Crear el botón guardar
        Button guardarButton = new Button("Guardar");
        guardarButton.setOnAction(event -> {
            tareaSeleccionada.setNombre(nombreTextField.getText());
            tareaSeleccionada.setFechaDeInicio(fechaInicioDatePicker.getValue());
            tareaSeleccionada.setFechaDeTerminado(fechaTerminadoDatePicker.getValue());
            tareaSeleccionada.setDescripcion(descripcionTextArea.getText());
            dialog.close();
        });
        // Agregar los campos al panel emergente
        vbox.getChildren().addAll(
                new Label("Nombre:"),
                nombreTextField,
                new Label("Fecha de inicio:"),
                fechaInicioDatePicker,
                new Label("Fecha de terminado:"),
                fechaTerminadoDatePicker,
                new Label("Descripción:"),
                descripcionTextArea,
                guardarButton);

        // Mostrar el panel emergente
        dialog.showAndWait();
    }

    private void mostrarSprintsFases(Stage primaryStage, Project project) {
        // Crear el panel emergente
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(primaryStage);
        VBox vbox = new VBox();
        Scene dialogScene = new Scene(vbox, 300, 200);
        dialog.setScene(dialogScene);

        // Crear la tabla de Sprints/Fases
        TableView<String> tablaSprintsFases = new TableView<>();
        TableColumn<String, String> columnaSprintFase = new TableColumn<>("Sprint/Fase");
        columnaSprintFase.setCellValueFactory(data -> new SimpleStringProperty(data.getValue()));
        tablaSprintsFases.getColumns().add(columnaSprintFase);

        // Obtener la lista de Sprints/Fases del proyecto
        List<String> sprintsFases = project.getSprintsFases();

        // Agregar los Sprints/Fases a la tabla
        ObservableList<String> sprintsFasesObservable = FXCollections.observableArrayList(sprintsFases);
        tablaSprintsFases.setItems(sprintsFasesObservable);

        // Crear el botón de agregar Sprint/Fase
        Button agregarSprintFaseButton = new Button("Agregar Sprint/Fase");
        agregarSprintFaseButton.setOnAction(event -> {
            TextInputDialog dialogAgregarSprintFase = new TextInputDialog();
            dialogAgregarSprintFase.setTitle("Agregar Sprint/Fase");
            dialogAgregarSprintFase.setHeaderText(null);
            dialogAgregarSprintFase.setContentText("Ingrese el nombre del Sprint/Fase:");
            Optional<String> resultado = dialogAgregarSprintFase.showAndWait();
            resultado.ifPresent(nombreSprintFase -> {
                sprintsFases.add(nombreSprintFase);
                sprintsFasesObservable.add(nombreSprintFase);
            });
        });

        // Crear el botón de eliminar Sprint/Fase
        Button eliminarSprintFaseButton = new Button("Eliminar Sprint/Fase");
        eliminarSprintFaseButton.setOnAction(event -> {
            String seleccionado = tablaSprintsFases.getSelectionModel().getSelectedItem();
            if (seleccionado != null) {
                sprintsFases.remove(seleccionado);
                sprintsFasesObservable.remove(seleccionado);
            }
        });

        // Agregar la tabla y los botones al panel
        vbox.getChildren().addAll(tablaSprintsFases, agregarSprintFaseButton, eliminarSprintFaseButton);

        dialog.showAndWait();
    }
}

