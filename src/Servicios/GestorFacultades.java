package Servicios;

import Modelo.Facultad;

public class GestorFacultades {

    // Arreglo nativo obligatorio — tamaño fijo de 5
    private Facultad[] facultades;
    private static final int TOTAL_FACULTADES = 5;

    public GestorFacultades() {
        facultades = new Facultad[TOTAL_FACULTADES];

        // Inicializar las 5 facultades fijas
        facultades[0] = new Facultad("FING", "Ingeniería de Sistemas",  "Dr. Carlos Mora");
        facultades[1] = new Facultad("FCIEN", "Ciencias Básicas",       "Dra. Laura Rios");
        facultades[2] = new Facultad("FADM", "Administración",          "Dr. Pedro Suarez");
        facultades[3] = new Facultad("FDER", "Derecho",                 "Dra. Maria Castro");
        facultades[4] = new Facultad("FMED", "Medicina",                "Dr. Jorge Peña");
    }

    // Listar todas las facultades
    public void listarFacultades() {
        System.out.println("=== FACULTADES DE LA UNIVERSIDAD ===");
        for (int i = 0; i < TOTAL_FACULTADES; i++) {
            System.out.println("\n[" + i + "]");
            facultades[i].mostrarInformacion();
        }
    }

    // Buscar facultad por índice
    public Facultad getFacultad(int indice) {
        if (indice < 0 || indice >= TOTAL_FACULTADES) {
            System.out.println("Índice inválido. Debe ser entre 0 y 4.");
            return null;
        }
        return facultades[indice];
    }

    public Facultad[] getFacultades() { return facultades; }
}