package com.example.bibliotecadecodigopmi.scrumlibrary;
public abstract class Sprint {
    protected int numero;
    protected String objetivo;
    protected int duracionEnSemanas;
    protected String nombre;

    public Sprint(int numero, String objetivo, int duracionEnSemanas, String nombre) {
        this.numero = numero;
        this.objetivo = objetivo;
        this.duracionEnSemanas = duracionEnSemanas;
        this.nombre = nombre;
    }
    public abstract void ejecutar();
}

class SprintPlanificacion extends Sprint {

    //Aqui van los productos a entregar al final o bien las funcionalidades o requerimientos
    //a crear
    private String[] entregables;
    public SprintPlanificacion(int numero, String objetivo, int duracionEnSemanas, String[] entregables, String nombre) {
        super(numero, objetivo, duracionEnSemanas,nombre);
        this.entregables = entregables;
    }
    @Override
    public void ejecutar() {
        System.out.println("Ejecutando Sprint de Planificación...");
        // Implementación específica para Sprint de Planificación
    }
}

class SprintDesarrollo extends Sprint {
    private int puntosDeHistoria;
    public SprintDesarrollo(int numero, String objetivo, int duracionEnSemanas, int puntosDeHistoria,String nombre) {
        super(numero, objetivo, duracionEnSemanas, nombre);
        this.puntosDeHistoria = puntosDeHistoria;
    }
    @Override
    public void ejecutar() {
        System.out.println("Ejecutando Sprint de Desarrollo...");
        // Implementación específica para Sprint de Desarrollo
    }
}

class SprintTesting extends Sprint {
    private int casosDePrueba;
    public SprintTesting(int numero, String objetivo, int duracionEnSemanas, int casosDePrueba,String nombre) {
        super(numero, objetivo, duracionEnSemanas,nombre);
        this.casosDePrueba = casosDePrueba;
    }
    @Override
    public void ejecutar() {
        System.out.println("Ejecutando Sprint de Testing...");
        // Implementación específica para Sprint de Testing
    }
}
