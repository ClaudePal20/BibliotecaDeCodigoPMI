package com.example.bibliotecadecodigopmi.scrumlibrary;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
public class Project {
    private String nombre;
    private LocalDate FechaDeInicio;
    private LocalDate FechaDeTerminado;
    private String presupuesto;
    private String ManagerDeProyecto;
    private List<Tarea> tareas;
    private List<String> sprints;
    public Project(String nombre, LocalDate FechaDeInicio, LocalDate FechaDeTerminado, List<Tarea> tareas, String presupuesto, String ManagerDeProyecto) {
        this.nombre = nombre;
        this.FechaDeInicio = FechaDeInicio;
        this.FechaDeTerminado = FechaDeTerminado;
        this.tareas = tareas != null ? tareas : new ArrayList<>();
        this.presupuesto = presupuesto;
        this.ManagerDeProyecto = ManagerDeProyecto;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public String getNombre() {
        return nombre;
    }
    public void setFechaDeInicio(LocalDate FechaDeInicio) {
        this.FechaDeInicio = FechaDeInicio;
    }
    public void setFechaDeTerminado(LocalDate FechaDeTerminado) {
        this.FechaDeTerminado = FechaDeTerminado;
    }
    public Date getFechaDeInicio() {
        //default time zone
        ZoneId defaultZoneId = ZoneId.systemDefault();
        //creating the instance of LocalDate using the day, month, year info
        LocalDate localDate = this.FechaDeInicio;
        //local date + atStartOfDay() + default time zone + toInstant() = Date
        Date date = Date.from(localDate.atStartOfDay(defaultZoneId).toInstant());
        return date;
    }
    public Date getFechaDeTerminado() {
        //default time zone
        ZoneId defaultZoneId = ZoneId.systemDefault();
        //creating the instance of LocalDate using the day, month, year info
        LocalDate localDate = this.FechaDeTerminado;
        //local date + atStartOfDay() + default time zone + toInstant() = Date
        Date date = Date.from(localDate.atStartOfDay(defaultZoneId).toInstant());
        return date;
    }
    public void createTarea(String nombre, LocalDate FechaDeInicio, LocalDate FechaDeTerminado, String Descripcion) {
        Tarea tarea = new Tarea(nombre, FechaDeInicio, FechaDeTerminado, Descripcion);
        this.tareas.add(tarea);
    }
    public List<Tarea> getTareas() {
        return tareas;
    }
    public void setTareas(List<Tarea> tareas) {
        this.tareas = tareas;
    }
    public void addTarea(Tarea tarea) {
        this.tareas.add(tarea);
    }
    public void removeTarea(Tarea tarea) {
        this.tareas.remove(tarea);
    }

    public String getPresupuesto() {
        return presupuesto;
    }
    public String getManagerDeProyecto() {
        return ManagerDeProyecto;
    }
    @Override
    public String toString() {
        return nombre;
    }

    public void agregarTarea(Tarea task) {
        this.tareas.add(task);
    }
    public List<String> getSprintsFases(){
        return this.sprints;
    }

}
