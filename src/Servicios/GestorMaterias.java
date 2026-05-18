package Servicios;

import Modelo.Materia;
import Modelo.Estudiante;
import Excepciones.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Stack;

public class GestorMaterias {

    private HashMap<String, Materia> materias;
    private Stack<Object[]> pilaDeshacer;
    private Stack<Object[]> pilaRehacer;

    public GestorMaterias(Stack<Object[]> pilaDeshacer, Stack<Object[]> pilaRehacer) {
        this.materias = new HashMap<>();
        this.pilaDeshacer = pilaDeshacer;
        this.pilaRehacer = pilaRehacer;
    }

    // ─── CREAR MATERIA ────────────────────────────────────────
    public void crearMateria(String codigo, String nombre, int cupos, int creditos) {
        Materia m = new Materia(codigo, nombre, cupos, creditos);
        materias.put(codigo, m);
        System.out.println("Materia creada: " + nombre);
    }

    // ─── AGREGAR PRE-REQUISITO ────────────────────────────────
    public void agregarPreRequisito(String codigoMateria, String codigoPreReq)
            throws Exception {
        Materia m = getMateriaOException(codigoMateria);
        m.agregarPreRequisito(codigoPreReq);
        System.out.println("Pre-requisito " + codigoPreReq + " agregado a " + codigoMateria);
    }

    // ─── INSCRIBIR ESTUDIANTE ─────────────────────────────────
    public void inscribirEstudiante(Estudiante estudiante, String codigoMateria)
            throws PreRequisitoNoAprobadoException, CupoLlenoException, Exception {

        Materia m = getMateriaOException(codigoMateria);

        // Verificar pre-requisitos
        LinkedList<String> preReqs = m.getPreRequisitos();
        for (String preReq : preReqs) {
            if (!estudiante.getHistorial().contains(preReq)) {
                throw new PreRequisitoNoAprobadoException(
                    "El estudiante no cumple el pre-requisito: " + preReq
                );
            }
        }

        // Verificar cupos
        if (!m.hayCupos()) {
            m.agregarAColaEspera(estudiante.getId());
            throw new CupoLlenoException(
                "Materia llena. " + estudiante.getNombre() + " agregado a cola de espera."
            );
        }

        // Inscribir
        m.inscribirEstudiante(estudiante.getId());
        estudiante.agregarAlHistorial(codigoMateria);

        pilaDeshacer.push(new Object[]{"INSCRIBIR", estudiante.getId(), codigoMateria});
        pilaRehacer.clear();

        System.out.println(estudiante.getNombre() + " inscrito en " + m.getNombre() +
            " (cupos restantes: " + m.getCuposDisponibles() + ")");
    }

    // ─── CANCELAR INSCRIPCIÓN ─────────────────────────────────
    public void cancelarInscripcion(Estudiante estudiante, String codigoMateria)
            throws Exception {

        Materia m = getMateriaOException(codigoMateria);

        if (!m.estaInscrito(estudiante.getId())) {
            throw new Exception("El estudiante no está inscrito en " + codigoMateria);
        }

        String siguiente = m.liberarCupo(estudiante.getId());
        estudiante.getHistorial().remove(codigoMateria);

        pilaDeshacer.push(new Object[]{"CANCELAR", estudiante.getId(), codigoMateria});
        pilaRehacer.clear();

        System.out.println("Cancelación exitosa. Cupo liberado.");
        if (siguiente != null) {
            System.out.println("Cupo asignado a: " + siguiente);
        }
    }

    // ─── MOSTRAR PRE-REQUISITOS ───────────────────────────────
    public void mostrarPreRequisitos(String codigoMateria) throws Exception {
        Materia m = getMateriaOException(codigoMateria);
        LinkedList<String> preReqs = m.getPreRequisitos();

        if (preReqs.isEmpty()) {
            System.out.println(codigoMateria + " no tiene pre-requisitos.");
            return;
        }
        System.out.println("Pre-requisitos de " + codigoMateria + ":");
        for (String p : preReqs) {
            System.out.println("  - " + p);
        }
    }

    // ─── UTILIDAD ─────────────────────────────────────────────
    private Materia getMateriaOException(String codigo) throws Exception {
        Materia m = materias.get(codigo);
        if (m == null) throw new Exception("Materia no encontrada: " + codigo);
        return m;
    }

    public HashMap<String, Materia> getMaterias() { return materias; }
}