package Modelo;

import java.util.LinkedList;
import java.util.ArrayDeque;
import java.util.Queue;

public class Materia {

    private String codigo;
    private String nombre;
    private int cuposMaximos;
    private int cuposDisponibles;
    private int creditos;

    // Lista enlazada de pre-requisitos (códigos de materias)
    private LinkedList<String> preRequisitos;

    // Cola de espera cuando no hay cupos
    private Queue<String> colaEspera;

    // Lista de estudiantes inscritos (IDs)
    private LinkedList<String> estudiantesInscritos;

    public Materia(String codigo, String nombre, int cuposMaximos, int creditos) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.cuposMaximos = cuposMaximos;
        this.cuposDisponibles = cuposMaximos;
        this.creditos = creditos;
        this.preRequisitos = new LinkedList<>();
        this.colaEspera = new ArrayDeque<>();
        this.estudiantesInscritos = new LinkedList<>();
    }

    // ─── PRE-REQUISITOS ───────────────────────────────────────
    public void agregarPreRequisito(String codigoMateria) {
        preRequisitos.add(codigoMateria);
    }

    public LinkedList<String> getPreRequisitos() {
        return preRequisitos;
    }

    // ─── INSCRIPCIÓN ──────────────────────────────────────────
    public boolean hayCupos() {
        return cuposDisponibles > 0;
    }

    public void inscribirEstudiante(String idEstudiante) {
        estudiantesInscritos.add(idEstudiante);
        cuposDisponibles--;
    }

    public void agregarAColaEspera(String idEstudiante) {
        colaEspera.add(idEstudiante);
    }

    // Cuando se cancela una inscripción, el primero en cola toma el cupo
    public String liberarCupo(String idEstudiante) {
        estudiantesInscritos.remove(idEstudiante);
        cuposDisponibles++;

        // Si hay alguien en espera, le asignamos el cupo
        if (!colaEspera.isEmpty()) {
            String siguiente = colaEspera.poll(); // saca el primero
            inscribirEstudiante(siguiente);
            return siguiente;
        }
        return null;
    }

    public boolean estaInscrito(String idEstudiante) {
        return estudiantesInscritos.contains(idEstudiante);
    }

    // ─── MOSTRAR COLA ─────────────────────────────────────────
    public void mostrarColaEspera() {
        if (colaEspera.isEmpty()) {
            System.out.println("No hay estudiantes en espera para " + nombre);
            return;
        }
        System.out.println("=== COLA DE ESPERA: " + nombre + " ===");
        int pos = 1;
        for (String id : colaEspera) {
            System.out.println("Posicion " + pos + ": " + id);
            pos++;
        }
        System.out.println("Total en espera: " + colaEspera.size());
    }

    // ─── GETTERS ──────────────────────────────────────────────
    public String getCodigo() {
        return codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public int getCuposMaximos() {
        return cuposMaximos;
    }

    public int getCuposDisponibles() {
        return cuposDisponibles;
    }

    public int getCreditos() {
        return creditos;
    }

    public Queue<String> getColaEspera() {
        return colaEspera;
    }

    public LinkedList<String> getEstudiantesInscritos() {
        return estudiantesInscritos;
    }

    public void incrementarCupo() {
        cuposDisponibles++;
    }
}