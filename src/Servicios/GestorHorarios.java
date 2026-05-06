package Servicios;

import Modelo.Aula;
import Excepciones.HorarioConflictivoException;
import java.util.TreeMap;
import java.util.Stack;

public class GestorHorarios {

    // TreeMap mantiene aulas ordenadas por nombre automáticamente
    private TreeMap<String, Aula> aulas;
    private Stack<Object[]> pilaDeshacer;
    private Stack<Object[]> pilaRehacer;

    public GestorHorarios(Stack<Object[]> pilaDeshacer, Stack<Object[]> pilaRehacer) {
        this.aulas = new TreeMap<>();
        this.pilaDeshacer = pilaDeshacer;
        this.pilaRehacer = pilaRehacer;
    }

    // ─── AGREGAR AULA ─────────────────────────────────────────
    public void agregarAula(String nombre, int capacidad) {
        aulas.put(nombre, new Aula(nombre, capacidad));
        System.out.println("Aula registrada: " + nombre);
    }

    // ─── RESERVAR ─────────────────────────────────────────────
    public void reservar(String nombreAula, int dia, int hora, int duracion)
            throws HorarioConflictivoException {

        Aula aula = getAulaOException(nombreAula);
        aula.reservar(dia, hora, duracion);

        pilaDeshacer.push(new Object[]{"RESERVAR", nombreAula, dia, hora, duracion});
        pilaRehacer.clear();
    }

    // ─── LIBERAR ──────────────────────────────────────────────
    public void liberar(String nombreAula, int dia, int hora, int duracion) {
        Aula aula = getAulaOException(nombreAula);
        aula.liberar(dia, hora, duracion);

        pilaDeshacer.push(new Object[]{"LIBERAR", nombreAula, dia, hora, duracion});
        pilaRehacer.clear();
    }

    // ─── CONSULTAR ────────────────────────────────────────────
    public void consultarDisponibilidad(String nombreAula, int dia, int hora) {
        Aula aula = getAulaOException(nombreAula);
        boolean libre = aula.consultarDisponibilidad(dia, hora);
        String[] dias = {"Domingo","Lunes","Martes","Miércoles","Jueves","Viernes","Sábado"};
        System.out.println(dias[dia] + " " + hora + ":00 en aula " + nombreAula +
            " → " + (libre ? "LIBRE" : "OCUPADO"));
    }

    // ─── LISTAR AULAS ─────────────────────────────────────────
    public void listarAulas() {
        System.out.println("=== AULAS REGISTRADAS (orden alfabético) ===");
        for (Aula a : aulas.values()) {
            System.out.println("- " + a.getNombre() + " (capacidad: " + a.getCapacidad() + ")");
        }
    }

    // ─── UTILIDAD ─────────────────────────────────────────────
    private Aula getAulaOException(String nombre) {
        Aula a = aulas.get(nombre);
        if (a == null) throw new RuntimeException("Aula no encontrada: " + nombre);
        return a;
    }

    public TreeMap<String, Aula> getAulas() { return aulas; }
}