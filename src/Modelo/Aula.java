package Modelo;

import Excepciones.HorarioConflictivoException;

public class Aula {

    private String nombre;
    private int capacidad;

    // Matriz obligatoria: 7 días x 24 horas
    private boolean[][] horario;

    private static final String[] DIAS = {
        "Domingo", "Lunes", "Martes", "Miércoles",
        "Jueves", "Viernes", "Sábado"
    };

    public Aula(String nombre, int capacidad) {
        this.nombre = nombre;
        this.capacidad = capacidad;
        this.horario = new boolean[7][24];
        // Java inicializa boolean[] en false automáticamente = todo libre
    }

    // ─── RESERVAR ─────────────────────────────────────────────
    public void reservar(int dia, int hora, int duracion)
            throws HorarioConflictivoException {

        // Primero verificar que todo el bloque está libre
        for (int h = hora; h < hora + duracion; h++) {
            if (horario[dia][h]) {
                throw new HorarioConflictivoException(
                    DIAS[dia] + " " + h + ":00 ya está reservado en aula " + nombre
                );
            }
        }

        // Si está libre, reservar todo el bloque
        for (int h = hora; h < hora + duracion; h++) {
            horario[dia][h] = true;
            System.out.println(DIAS[dia] + " " + h + ":00 → RESERVADO");
        }
        System.out.println("Reserva exitosa en aula " + nombre);
    }

    // ─── LIBERAR ──────────────────────────────────────────────
    public void liberar(int dia, int hora, int duracion) {
        for (int h = hora; h < hora + duracion; h++) {
            horario[dia][h] = false;
        }
        System.out.println("Horario liberado en aula " + nombre +
            " - " + DIAS[dia] + " " + hora + ":00");
    }

    // ─── CONSULTAR ────────────────────────────────────────────
    public boolean consultarDisponibilidad(int dia, int hora) {
        return !horario[dia][hora];
    }

    // ─── MOSTRAR HORARIO COMPLETO ─────────────────────────────
    public void mostrarHorario() {
        System.out.println("=== HORARIO AULA: " + nombre + " ===");
        for (int dia = 0; dia < 7; dia++) {
            for (int hora = 0; hora < 24; hora++) {
                if (horario[dia][hora]) {
                    System.out.println(DIAS[dia] + " " + hora + ":00 → OCUPADO");
                }
            }
        }
    }

    // ─── GETTERS ──────────────────────────────────────────────
    public String getNombre()      { return nombre; }
    public int getCapacidad()      { return capacidad; }
    public boolean[][] getHorario(){ return horario; }
}