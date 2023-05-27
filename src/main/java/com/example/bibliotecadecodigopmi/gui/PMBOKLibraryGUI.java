    package com.example.bibliotecadecodigopmi.gui;
    import com.example.bibliotecadecodigopmi.scrumlibrary.*;
    import javafx.application.Application;
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
    import java.util.Arrays;
    import java.util.Optional;

    public class PMBOKLibraryGUI extends Application{
        private VBox projectDetailsLayout;
        private ObservableList<Project> observableProjects;
        private ProjectManager projectManager;
        public static void main(String[] args) {
            System.setProperty("prism.order", "sw"); // Set the desired graphics pipeline
            launch(args);
        }
        @Override
        public void start(Stage primaryStage){
            projectManager = new ProjectManager();
            primaryStage.setTitle("Libreria para gestion de desarrollo de software");
            setupUI(primaryStage);
            primaryStage.show();
        }
        private void setupButtons(ListView<Project> listaDeProyectos, BorderPane bordePrincipal, Stage primaryStage, ProjectManager projectManager){
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
            bordePrincipal.setPrefSize(1000, 600);
            // Create the project list view
            ListView<Project> listaDeProyectos = new ListView<>();
            listaDeProyectos.setPrefWidth(150);
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
    
            // Crear el segundo main layout
                // Llamar a la clase Documentacion y su método setupDocumentacion
            com.example.bibliotecadecodigopmi.gui.Documentacion documentacion = new Documentacion();
            BorderPane bordePrincipal2 = documentacion.setupDocumentacion();
    
            //Pestaña de proyectos
            TabPane tabPane = new TabPane();
            Tab tab1 = new Tab();
            tab1.setText("Proyectos");
            tab1.setContent(bordePrincipal);
    
            //Pestaña de documentacion
            Tab tab2 = new Tab();
            tab2.setText("Documentacion");
            tab2.setContent(bordePrincipal2);
    
            tabPane.getTabs().add(tab1);
            tabPane.getTabs().add(tab2);
            Scene scene2 = new Scene(tabPane, 800, 600);
            primaryStage.setScene(scene2);
            primaryStage.show();
        }
    
        private void mostrarDetallesProjectos(Stage primaryStage, Project project) {
            //Boton para revisar sprints
            Button Sprints = new Button("Sprints");
            Sprints.setOnAction(event -> {
                try {
                    SetupSprintsPanel(project);
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
            if(project.getTareas()!=null){
                ObservableList<Tarea> tareasObservables = FXCollections.observableList(project.getTareas());
                tablaDeTareas.setItems(tareasObservables);
            }
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
        private void SetupSprintsPanel(Project project) {
            //Crear el panel
            Stage SprintsPanel = new Stage();
            SprintsPanel.setTitle("Sprints");
            //Crear tabla para cada tipo de sprint (desarrollo, planificacion, testing)
            TableView<SprintDesarrollo> tablaDesarrollo = new TableView<>();
    
            //Crear columnas para cada tipo de sprint
            TableColumn<SprintDesarrollo,String> numeroColumnaDesarrollo = new TableColumn<>("Numero");
            numeroColumnaDesarrollo.setCellValueFactory(new PropertyValueFactory<>("numero"));
            TableColumn<SprintDesarrollo, String> nombreColumnaDesarrollo = new TableColumn<>("Nombre");
            nombreColumnaDesarrollo.setCellValueFactory(new PropertyValueFactory<>("nombre"));
            TableColumn<SprintDesarrollo, String> objetivoColumnaDesarrollo = new TableColumn<>("Objetivo");
            objetivoColumnaDesarrollo.setCellValueFactory(new PropertyValueFactory<>("objetivo"));
            TableColumn<SprintDesarrollo, String> duracionEnSemanasColumnaDesarrollo = new TableColumn<>("Duracion en semanas");
            duracionEnSemanasColumnaDesarrollo.setCellValueFactory(new PropertyValueFactory<>("duracionEnSemanas"));
            TableColumn<SprintDesarrollo,String> puntosHistoriaColumnaDesarrollo = new TableColumn<>("Puntos de historia");
            puntosHistoriaColumnaDesarrollo.setCellValueFactory(new PropertyValueFactory<>("puntosDeHistoria"));
    
            //Agregar columnas a la tabla
            tablaDesarrollo.getColumns().addAll(numeroColumnaDesarrollo,nombreColumnaDesarrollo, objetivoColumnaDesarrollo,duracionEnSemanasColumnaDesarrollo,puntosHistoriaColumnaDesarrollo);
    
            //Crear tabla para cada tipo de sprint (desarrollo, planificacion, testing)
            TableView<SprintPlanificacion> tablaPlanificacion = new TableView<>();
    
            //Agregar columnas a la tabla
            TableColumn<SprintPlanificacion,String> numeroColumnaPlanificacion = new TableColumn<>("Numero");
            numeroColumnaPlanificacion.setCellValueFactory(new PropertyValueFactory<>("numero"));
            TableColumn<SprintPlanificacion, String> nombreColumnaPlanificacion = new TableColumn<>("Nombre");
            nombreColumnaPlanificacion.setCellValueFactory(new PropertyValueFactory<>("nombre"));
            TableColumn<SprintPlanificacion,String> ObjetivoColumnaPlanificacion= new TableColumn<>("Objetivo");
            ObjetivoColumnaPlanificacion.setCellValueFactory(new PropertyValueFactory<>("objetivo"));
            TableColumn<SprintPlanificacion,String> duracionEnSemanas = new TableColumn<>("Duracion en semanas");
            duracionEnSemanas.setCellValueFactory(new PropertyValueFactory<>("duracionEnSemanas"));
            TableColumn<SprintPlanificacion,String> entregables = new TableColumn<>("Entregables");
            entregables.setCellValueFactory(new PropertyValueFactory<>("entregables"));
    
            //Agregar columnas a la tabla
            tablaPlanificacion.getColumns().addAll(numeroColumnaPlanificacion,nombreColumnaPlanificacion, ObjetivoColumnaPlanificacion, duracionEnSemanas, entregables);
    
            //Crear tabla para cada tipo de sprint (desarrollo, planificacion, testing)
            TableView<SprintTesting> tablaTesting = new TableView<>();
            //Crear columnas para cada tipo de sprint
            TableColumn<SprintTesting, String> numero = new TableColumn<>("Numero");
            numero.setCellValueFactory(new PropertyValueFactory<>("numero"));
            TableColumn<SprintTesting, String> nombreColumnaTesting = new TableColumn<>("Nombre");
            nombreColumnaTesting.setCellValueFactory(new PropertyValueFactory<>("nombre"));
            TableColumn<SprintTesting, String> ObjetivoColumnaTesting= new TableColumn<>("Objetivo");
            ObjetivoColumnaTesting.setCellValueFactory(new PropertyValueFactory<>("objetivo"));
            TableColumn<SprintTesting, String> duracionEnSemanasTesting = new TableColumn<>("Duracion en semanas");
            duracionEnSemanasTesting.setCellValueFactory(new PropertyValueFactory<>("duracionEnSemanas"));
            TableColumn<SprintTesting, String> casosDePrueba = new TableColumn<>("Casos de prueba");
            casosDePrueba.setCellValueFactory(new PropertyValueFactory<>("casosDePrueba"));
            //Agregar columnas a la tabla
            tablaTesting.getColumns().addAll(numero,nombreColumnaTesting, ObjetivoColumnaTesting, duracionEnSemanasTesting, casosDePrueba);
    
            //Crear botones para cada tipo de sprint
            Button botonAgregarDesarrollo = new Button("Agregar");
            Button botonEditarDesarrollo = new Button("Editar");
            Button botonEliminarDesarrollo = new Button("Eliminar");
            Button botonAgregarPlanificacion = new Button("Agregar");
            Button botonEditarPlanificacion = new Button("Editar");
            Button botonEliminarPlanificacion = new Button("Eliminar");
            Button botonAgregarTesting = new Button("Agregar");
            Button botonEditarTesting = new Button("Editar");
            Button botonEliminarTesting = new Button("Eliminar");
            Button botonGuardar = new Button("Cerrar");
            //Agregar funcionalidad a los botones
            botonAgregarDesarrollo.setOnAction(event -> {
                SprintDesarrollo sprintDesarrollo = mostrarPanelAgregarSprintDesarrollo();
                tablaDesarrollo.getItems().add(sprintDesarrollo);
                project.numSprints++;
            });
            botonEditarDesarrollo.setOnAction(event -> {
                SprintDesarrollo sprintDesarrolloSeleccionado = tablaDesarrollo.getSelectionModel().getSelectedItem();
                mostrarPanelEditarSprintDesarrollo(sprintDesarrolloSeleccionado);
                tablaDesarrollo.refresh();
            });
            botonEliminarDesarrollo.setOnAction(event -> {
                SprintDesarrollo sprintDesarrolloSeleccionado = tablaDesarrollo.getSelectionModel().getSelectedItem();
                tablaDesarrollo.getItems().remove(sprintDesarrolloSeleccionado);
                if(project.numSprints>0){
                    project.numSprints--;
                }
            });
            botonAgregarPlanificacion.setOnAction(event -> {
                SprintPlanificacion sprintPlanificacion = mostrarPanelAgregarSprintPlanificacion();
                tablaPlanificacion.getItems().add(sprintPlanificacion);
                project.numSprints++;
            });
            botonEditarPlanificacion.setOnAction(event -> {
                SprintPlanificacion sprintPlanificacionSeleccionado = tablaPlanificacion.getSelectionModel().getSelectedItem();
                if (sprintPlanificacionSeleccionado != null) {
                    mostrarPanelEditarSprintPlanificacion(sprintPlanificacionSeleccionado);
                    tablaPlanificacion.refresh();
                }
            });
            botonEliminarPlanificacion.setOnAction(event -> {
                SprintPlanificacion sprintPlanificacionSeleccionado = tablaPlanificacion.getSelectionModel().getSelectedItem();
                if (sprintPlanificacionSeleccionado != null) {
                    tablaPlanificacion.getItems().remove(sprintPlanificacionSeleccionado);
                }
                if(project.numSprints>0){
                    project.numSprints--;
                }
            });
            botonAgregarTesting.setOnAction(event -> {
                SprintTesting sprintTesting = mostrarPanelAgregarSprintTesting();
                tablaTesting.getItems().add(sprintTesting);
                project.numSprints++;
            });
            botonEditarTesting.setOnAction(event -> {
                SprintTesting sprintTestingSeleccionado = tablaTesting.getSelectionModel().getSelectedItem();
                if (sprintTestingSeleccionado != null) {
                    mostrarPanelEditarSprintTesting(sprintTestingSeleccionado);
                    tablaTesting.refresh();
                }
            });
            botonEliminarTesting.setOnAction(event -> {
                SprintTesting sprintTestingSeleccionado = tablaTesting.getSelectionModel().getSelectedItem();
                if (sprintTestingSeleccionado != null) {
                    tablaTesting.getItems().remove(sprintTestingSeleccionado);
                }
                if(project.numSprints>0){
                    project.numSprints--;
                }
            });
            botonGuardar.setOnAction(event -> {
                //Agregar todos los datos al proyecto
                project.setSprintsDesarrollo((ObservableList)tablaDesarrollo.getItems());
                project.setSprintsTesting((ObservableList)tablaDesarrollo.getItems());
                project.setSprintsPlanificacion((ObservableList)tablaDesarrollo.getItems());
                SprintsPanel.close();
            });
            //Crear contenedor para cada tipo de sprint
            VBox contenedorDesarrollo = new VBox();
            contenedorDesarrollo.getChildren().addAll(tablaDesarrollo, botonAgregarDesarrollo, botonEditarDesarrollo, botonEliminarDesarrollo,botonGuardar);
            VBox contenedorPlanificacion = new VBox();
            contenedorPlanificacion.getChildren().addAll(tablaPlanificacion, botonAgregarPlanificacion, botonEditarPlanificacion, botonEliminarPlanificacion,botonGuardar);
            VBox contenedorTesting = new VBox();
            contenedorTesting.getChildren().addAll(tablaTesting, botonAgregarTesting, botonEditarTesting, botonEliminarTesting, botonGuardar);
            //Crear pestañas para cada tipo de sprint
            Tab tabDesarrollo = new Tab("Desarrollo", contenedorDesarrollo);
            Tab tabPlanificacion = new Tab("Planificacion", contenedorPlanificacion);
            Tab tabTesting = new Tab("Testing", contenedorTesting);
            //Agregar pestañas a la ventana
            TabPane tabPane = new TabPane();
            tabPane.getTabs().addAll(tabDesarrollo, tabPlanificacion, tabTesting);
            //Crear escena
            Scene scene = new Scene(tabPane, 800, 600);
    
    
            //Mostrar ventana
            SprintsPanel.setScene(scene);
            SprintsPanel.show();
        }
    
        private void mostrarPanelEditarSprintTesting(SprintTesting sprintTestingSeleccionado) {
            Stage primaryStage = new Stage();
            primaryStage.setTitle("Agregar Sprint Desarrollo");
    
            //Crear contenedor
            GridPane contenedor = new GridPane();
            contenedor.setPadding(new Insets(10));
            contenedor.setHgap(10);
            contenedor.setVgap(10);
    
            //Crear campos de texto
            TextField campoNombre = new TextField();
            campoNombre.setPromptText("Nombre");
            TextField campoObjetivo = new TextField();
            campoObjetivo.setPromptText("Objetivo");
            Spinner<Integer> spinnerDuracion = new Spinner<>(1, 100, 1);
            spinnerDuracion.setEditable(true);
            Spinner<Integer> spinnerCasosDePrueba = new Spinner<>(1, 100, 1);
            spinnerCasosDePrueba.setEditable(true);
    
    
            //Agregar campos de texto al contenedor
            contenedor.add(campoNombre, 0, 0);
            contenedor.add(campoObjetivo, 0, 1);
            contenedor.add(spinnerDuracion, 0, 2);
            contenedor.add(spinnerCasosDePrueba, 0, 3);
    
            //Crear botones
            Button botonGuardar = new Button("Guardar");
            Button botonCancelar = new Button("Cancelar");
            Button agregarEntregableBoton = new Button("Agregar Entregable");
            //Agregar botones al contenedor
            contenedor.add(botonGuardar, 0, 5);
            contenedor.add(botonCancelar, 1, 5);
            contenedor.add(agregarEntregableBoton, 1, 3);
            //Crear escena
            Scene scene = new Scene(contenedor, 400, 300);
    
            botonGuardar.setOnAction(event -> {
                sprintTestingSeleccionado.setNumero(0);
                sprintTestingSeleccionado.setNombre(campoNombre.getText());
                sprintTestingSeleccionado.setObjetivo(campoObjetivo.getText());
                sprintTestingSeleccionado.setSemanas(spinnerDuracion.getValue());
                sprintTestingSeleccionado.setCasosDePrueba(spinnerCasosDePrueba.getValue());
                primaryStage.close();
            });
            botonCancelar.setOnAction(event -> primaryStage.close());
            //Mostrar ventana
            primaryStage.setScene(scene);
            primaryStage.show();
        }
    
        private SprintTesting mostrarPanelAgregarSprintTesting() {
            Stage primaryStage = new Stage();
            SprintTesting sprintTesting = new SprintTesting(0, null, 0, 0, null);
            primaryStage.setTitle("Agregar Sprint Desarrollo");
    
            //Crear contenedor
            GridPane contenedor = new GridPane();
            contenedor.setPadding(new Insets(10));
            contenedor.setHgap(10);
            contenedor.setVgap(10);
    
            //Crear campos de texto
            TextField campoNombre = new TextField();
            campoNombre.setPromptText("Nombre");
            TextField campoObjetivo = new TextField();
            campoObjetivo.setPromptText("Objetivo");
            Spinner<Integer> spinnerDuracion = new Spinner<>(1, 100, 1);
            spinnerDuracion.setEditable(true);
            Spinner<Integer> spinnerCasosDePrueba = new Spinner<>(1, 100, 1);
            spinnerCasosDePrueba.setEditable(true);
    
    
            //Agregar campos de texto al contenedor
            contenedor.add(campoNombre, 0, 0);
            contenedor.add(campoObjetivo, 0, 1);
            contenedor.add(spinnerDuracion, 0, 2);
            contenedor.add(spinnerCasosDePrueba, 0, 3);
    
            //Crear botones
            Button botonGuardar = new Button("Guardar");
            Button botonCancelar = new Button("Cancelar");
            Button agregarEntregableBoton = new Button("Agregar Entregable");
            //Agregar botones al contenedor
            contenedor.add(botonGuardar, 0, 5);
            contenedor.add(botonCancelar, 1, 5);
            contenedor.add(agregarEntregableBoton, 1, 3);
            //Crear escena
            Scene scene = new Scene(contenedor, 400, 300);
    
            botonGuardar.setOnAction(event -> {
                sprintTesting.setNumero(0);
                sprintTesting.setNombre(campoNombre.getText());
                sprintTesting.setObjetivo(campoObjetivo.getText());
                sprintTesting.setSemanas(spinnerDuracion.getValue());
                sprintTesting.setCasosDePrueba(spinnerCasosDePrueba.getValue());
                primaryStage.close();
            });
            botonCancelar.setOnAction(event -> primaryStage.close());
            //Mostrar ventana
            primaryStage.setScene(scene);
            primaryStage.show();
            return sprintTesting;
        }
    
        private void mostrarPanelEditarSprintPlanificacion(SprintPlanificacion sprintPlanificacionSeleccionado) {
            Stage primaryStage = new Stage();
            primaryStage.setTitle("Agregar Sprint Desarrollo");
    
            //Crear contenedor
            GridPane contenedor = new GridPane();
            contenedor.setPadding(new Insets(10));
            contenedor.setHgap(10);
            contenedor.setVgap(10);
    
            //Crear campos de texto
            TextField campoNombre = new TextField();
            campoNombre.setPromptText("Nombre");
            TextField campoObjetivo = new TextField();
            campoObjetivo.setPromptText("Objetivo");
            Spinner<Integer> spinnerDuracion = new Spinner<>(1, 100, 1);
            spinnerDuracion.setEditable(true);
            //TextField para un arreglo de strings
            TextField campoEntregables = new TextField();
            campoEntregables.setPromptText("Entregables");
    
    
            //Agregar campos de texto al contenedor
            contenedor.add(campoNombre, 0, 0);
            contenedor.add(campoObjetivo, 0, 1);
            contenedor.add(spinnerDuracion, 0, 2);
            contenedor.add(campoEntregables, 0, 3);
    
            //Crear botones
            Button botonGuardar = new Button("Guardar");
            Button botonCancelar = new Button("Cancelar");
            Button agregarEntregableBoton = new Button("Agregar Entregable");
            //Agregar botones al contenedor
            contenedor.add(botonGuardar, 0, 5);
            contenedor.add(botonCancelar, 1, 5);
            contenedor.add(agregarEntregableBoton, 1, 3);
            //Crear escena
            Scene scene = new Scene(contenedor, 400, 300);
            String[] entregables = campoEntregables.getText().split(",");
            ArrayList<String> Entregables = new ArrayList<>(Arrays.asList(entregables));
            agregarEntregableBoton.setOnAction(event -> Entregables.add(campoEntregables.getText()));
            botonGuardar.setOnAction(event -> {
                sprintPlanificacionSeleccionado.setNombre(campoNombre.getText());
                sprintPlanificacionSeleccionado.setObjetivo(campoObjetivo.getText());
                sprintPlanificacionSeleccionado.setDuracionEnSemanas(spinnerDuracion.getValue());
                //Convert campoEntregables to array
                sprintPlanificacionSeleccionado.setEntregables(Entregables);
                primaryStage.close();
            });
            botonCancelar.setOnAction(event -> primaryStage.close());
            //Mostrar ventana
            primaryStage.setScene(scene);
            primaryStage.show();
        }
    
        private SprintPlanificacion mostrarPanelAgregarSprintPlanificacion() {
            SprintPlanificacion sprintPlanificacion = new SprintPlanificacion(0,null,0,null,null);
            Stage primaryStage3 = new Stage();
            primaryStage3.setTitle("Agregar Sprint Desarrollo");
    
            //Crear contenedor
            GridPane contenedor = new GridPane();
            contenedor.setPadding(new Insets(10));
            contenedor.setHgap(10);
            contenedor.setVgap(10);
    
            //Crear campos de texto
            TextField campoNombre = new TextField();
            campoNombre.setPromptText("Nombre");
            TextField campoObjetivo = new TextField();
            campoObjetivo.setPromptText("Objetivo");
            Spinner<Integer> spinnerDuracion = new Spinner<>(1, 100, 1);
            spinnerDuracion.setEditable(true);
            //TextField para un arreglo de strings
            TextField campoEntregables = new TextField();
            campoEntregables.setPromptText("Entregables");
    
    
            //Agregar campos de texto al contenedor
            contenedor.add(campoNombre, 0, 0);
            contenedor.add(campoObjetivo, 0, 1);
            contenedor.add(spinnerDuracion, 0, 2);
            contenedor.add(campoEntregables, 0, 3);
    
            //Crear botones
            Button botonGuardar = new Button("Guardar");
            Button botonCancelar = new Button("Cancelar");
            Button agregarEntregableBoton = new Button("Agregar Entregable");
    
            //Agregar botones al contenedor
            contenedor.add(botonGuardar, 0, 5);
            contenedor.add(botonCancelar, 1, 5);
            contenedor.add(agregarEntregableBoton, 1, 3);
    
            //Crear escena
            Scene scene = new Scene(contenedor, 400, 300);
    
            //Crear arreglo para los entregables
            String[] entregables = campoEntregables.getText().split(",");
            ArrayList<String> Entregables = new ArrayList<>(Arrays.asList(entregables));
    
            agregarEntregableBoton.setOnAction(event -> Entregables.add(campoEntregables.getText()));
            botonGuardar.setOnAction(event -> {
                sprintPlanificacion.setNombre(campoNombre.getText());
                sprintPlanificacion.setObjetivo(campoObjetivo.getText());
                sprintPlanificacion.setDuracionEnSemanas(spinnerDuracion.getValue());
                //Convert campoEntregables to array
                sprintPlanificacion.setEntregables(Entregables);
                primaryStage3.close();
            });
            botonCancelar.setOnAction(event -> primaryStage3.close());
            primaryStage3.setScene(scene);
            primaryStage3.show();
            return sprintPlanificacion;
        }
    
        private void mostrarPanelEditarSprintDesarrollo(SprintDesarrollo sprintDesarrolloSeleccionado) {
            Stage primaryStage = new Stage();
            primaryStage.setTitle("Agregar Sprint Desarrollo");
    
            //Crear contenedor
            GridPane contenedor = new GridPane();
            contenedor.setPadding(new Insets(10));
            contenedor.setHgap(10);
            contenedor.setVgap(10);
    
            //Crear campos de texto
            TextField campoObjetivo = new TextField();
            campoObjetivo.setPromptText("Objetivo");
            Spinner<Integer> spinnerDuracion = new Spinner<>(1, 100, 1);
            spinnerDuracion.setEditable(true);
            Spinner<Integer> spinnerPuntosDeHistoria = new Spinner<>(1, 100, 1);
            spinnerPuntosDeHistoria.setEditable(true);
            //TextField para un arreglo de strings
            TextField campoEntregables = new TextField();
            campoEntregables.setPromptText("Entregables");
            TextField campoNombre = new TextField();
            campoNombre.setPromptText("Nombre");
    
            //Agregar campos de texto al contenedor
            contenedor.add(campoNombre, 0, 0);
            contenedor.add(campoObjetivo, 0, 1);
            contenedor.add(spinnerDuracion, 0, 2);
            contenedor.add(spinnerPuntosDeHistoria, 0, 3);
    
            //Crear botones
            Button botonGuardar = new Button("Guardar");
            Button botonCancelar = new Button("Cancelar");
    
            //Agregar botones al contenedor
            contenedor.add(botonGuardar, 0, 5);
            contenedor.add(botonCancelar, 1, 5);
    
            //Crear escena
            Scene scene = new Scene(contenedor, 400, 300);
            botonGuardar.setOnAction(event -> {
                sprintDesarrolloSeleccionado.setNombre(campoNombre.getText());
                sprintDesarrolloSeleccionado.setObjetivo(campoObjetivo.getText());
                sprintDesarrolloSeleccionado.setDuracionEnSemanas(spinnerDuracion.getValue());
                sprintDesarrolloSeleccionado.setPuntosDeHistoria(spinnerPuntosDeHistoria.getValue());
                primaryStage.close();
            });
            botonCancelar.setOnAction(event -> primaryStage.close());
            primaryStage.setScene(scene);
            primaryStage.show();
        }

        private SprintDesarrollo mostrarPanelAgregarSprintDesarrollo() {
            SprintDesarrollo sprintDesarrollo = new SprintDesarrollo(0,null,0,0,null);
            Stage primaryStage = new Stage();
            primaryStage.setTitle("Agregar Sprint Desarrollo");
    
            //Crear contenedor
            GridPane contenedor = new GridPane();
            contenedor.setPadding(new Insets(10));
            contenedor.setHgap(10);
            contenedor.setVgap(10);
    
            //Crear campos de texto
            TextField campoObjetivo = new TextField();
            campoObjetivo.setPromptText("Objetivo");
            Spinner<Integer> spinnerDuracion = new Spinner<>(1, 100, 1);
            spinnerDuracion.setEditable(true);
            Spinner<Integer> spinnerPuntosDeHistoria = new Spinner<>(1, 100, 1);
            spinnerPuntosDeHistoria.setEditable(true);
            TextField campoNombre = new TextField();
            campoNombre.setPromptText("Nombre");
    
            //Agregar campos de texto al contenedor
            contenedor.add(campoNombre, 0, 0);
            contenedor.add(campoObjetivo, 0, 1);
            contenedor.add(spinnerDuracion, 0, 2);
            contenedor.add(spinnerPuntosDeHistoria, 0, 3);
    
            //Crear botones
            Button botonAceptar = new Button("Guardar");
            Button botonCancelar = new Button("Cancelar");
    
            //Agregar botones al contenedor
            contenedor.add(botonAceptar, 0, 5);
            contenedor.add(botonCancelar, 1, 5);
    
            //Crear escena
            Scene scene = new Scene(contenedor, 400, 300);
            //Agregar eventos
            botonAceptar.setOnAction(event -> {
                sprintDesarrollo.setNumero(0);
                sprintDesarrollo.setNombre(campoNombre.getText());
                sprintDesarrollo.setObjetivo(campoObjetivo.getText());
                sprintDesarrollo.setDuracionEnSemanas(spinnerDuracion.getValue());
                sprintDesarrollo.setPuntosDeHistoria(spinnerPuntosDeHistoria.getValue());
                primaryStage.close();
            });
            botonCancelar.setOnAction(event -> primaryStage.close());
            //Mostrar ventana
            primaryStage.setScene(scene);
            primaryStage.showAndWait();
    
            return sprintDesarrollo;
        }
    
    }
