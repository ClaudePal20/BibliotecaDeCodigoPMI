package com.example.bibliotecadecodigopmi.scrumlibrary;

public class SprintTesting extends Sprint {
    private int casosDePrueba;

    public SprintTesting(int numero, String objetivo, int duracionEnSemanas, int casosDePrueba, String nombre) {
        super(numero, objetivo, duracionEnSemanas, nombre);
        this.casosDePrueba = casosDePrueba;
    }
    public void setCasosDePrueba(int casosDePrueba){
        this.casosDePrueba=casosDePrueba;
    }
    public int getCasosDePrueba(){
        return casosDePrueba;
    }
    public void ejecutar() {
        System.out.println("Ejecutando Sprint de Testing...");
        // Implementación específica para Sprint de Testing
    }
}
