package com.example.bibliotecadecodigopmi.scrumlibrary;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
public class Tarea {
    private String Nombre;
    private LocalDate FechaDeInicio;
    private LocalDate FechaDeTerminado;
    private String Descripcion;
    private List<Recurso> recursos;
    public Tarea(String Nombre, LocalDate FechaDeInicio, LocalDate FechaDeTerminado, String Descripcion){
        this.Nombre = Nombre;
        this.FechaDeInicio = FechaDeInicio;
        this.FechaDeTerminado = FechaDeTerminado;
        this.Descripcion= Descripcion;
        this.recursos = new ArrayList<>();
    }
    public String getNombre() {
        return Nombre;
    }
    public LocalDate getFechaDeInicio() {
        return FechaDeInicio;
    }
    public LocalDate getFechaDeTerminado() {
        return FechaDeTerminado;
    }
    public void setResources(Recurso recurso) {
        this.recursos.add(recurso);
    }
    public List<Recurso> getResources() {
        return recursos;
    }

    @Override
    public String toString() {
        return Nombre;
    }
    public String getDescripcion() {
        return Descripcion;
    }
    public void setNombre(String text) {
        this.Nombre = text;
    }
    public void setFechaDeInicio(LocalDate value) {
        this.FechaDeInicio = value;
    }
    public void setFechaDeTerminado(LocalDate value) {
        this.FechaDeTerminado = value;
    }
    public void setDescripcion(String text) {
        this.Descripcion = text;
    }
}

