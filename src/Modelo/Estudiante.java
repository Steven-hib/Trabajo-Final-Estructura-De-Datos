package Modelo;

import java.util.LinkedList;

public class Estudiante extends Persona {

    private int semestre;
    private Double[][] notas;
    private LinkedList<String> historialMaterias;

    public Estudiante(String nombre, String id, String email, int semestre) {
        super(nombre, id, email);
        this.semestre = semestre;
        this.notas = new Double[10][20];
        this.historialMaterias = new LinkedList<>();
    }

    @Override
    public void mostrarInformacion() {
        System.out.println("==========================================");
        System.out.println("ID:       " + getId());
        System.out.println("Nombre:   " + getNombre());
        System.out.println("Email:    " + getEmail());
        System.out.println("Semestre: " + semestre);
        System.out.println("Promedio: " + calcularPromedioAcumulado());
        System.out.println("==========================================");
    }

    public void registrarNota(int semIdx, int matIdx, double nota) {
        notas[semIdx][matIdx] = nota;
    }

    public double calcularPromedioPorSemestre(int semIdx) {
        double suma = 0;
        int count = 0;
        for (int i = 0; i < 20; i++) {
            if (notas[semIdx][i] != null) {
                suma += notas[semIdx][i];
                count++;
            }
        }
        return count == 0 ? 0.0 : suma / count;
    }

    public double calcularPromedioAcumulado() {
        double suma = 0;
        int count = 0;
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 20; j++) {
                if (notas[i][j] != null) {
                    suma += notas[i][j];
                    count++;
                }
            }
        }
        return count == 0 ? 0.0 : Math.round((suma / count) * 100.0) / 100.0;
    }

    public int getSemestre()                 { return semestre; }
    public Double[][] getNotas()             { return notas; }
    public LinkedList<String> getHistorial() { return historialMaterias; }
    public void setSemestre(int semestre)    { this.semestre = semestre; }

    public void agregarAlHistorial(String codigoMateria) {
        historialMaterias.add(codigoMateria);
    }
}