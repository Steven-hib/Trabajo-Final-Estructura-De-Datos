package Servicios;

import Modelo.Estudiante;
import Excepciones.EstudianteNoEncontradoException;
import java.util.HashMap;
import java.util.Stack;

public class GestorEstudiantes {

    // Estructura principal: búsqueda rápida por ID
    private HashMap<String, Estudiante> estudiantes;

    // Pilas para deshacer/rehacer
    private Stack<Object[]> pilaDeshacer;
    private Stack<Object[]> pilaRehacer;

    public GestorEstudiantes(Stack<Object[]> pilaDeshacer, Stack<Object[]> pilaRehacer) {
        this.estudiantes = new HashMap<>();
        this.pilaDeshacer = pilaDeshacer;
        this.pilaRehacer = pilaRehacer;
    }

    // ─── REGISTRAR ───────────────────────────────────────────
    public void registrarEstudiante(String nombre, String id, String email, int semestre) {
        Estudiante e = new Estudiante(nombre, id, email, semestre);
        estudiantes.put(id, e);

        // Guardamos en pila para poder deshacer
        pilaDeshacer.push(new Object[]{"REGISTRAR_ESTUDIANTE", id});
        pilaRehacer.clear();

        System.out.println("Estudiante registrado exitosamente.");
    }

    // ─── BUSCAR ───────────────────────────────────────────────
    public Estudiante buscarEstudiante(String id) throws EstudianteNoEncontradoException {
        Estudiante e = estudiantes.get(id);
        if (e == null) {
            throw new EstudianteNoEncontradoException(
                "No existe estudiante con ID: " + id
            );
        }
        return e;
    }

    // ─── ELIMINAR ─────────────────────────────────────────────
    public void eliminarEstudiante(String id) throws EstudianteNoEncontradoException {
        Estudiante e = buscarEstudiante(id); // lanza excepción si no existe
        estudiantes.remove(id);

        // Guardamos el estudiante completo para poder restaurarlo
        pilaDeshacer.push(new Object[]{"ELIMINAR_ESTUDIANTE", e});
        pilaRehacer.clear();

        System.out.println("Estudiante eliminado: " + e.getNombre());
    }

    // ─── LISTAR ───────────────────────────────────────────────
    public void listarEstudiantes() {
        if (estudiantes.isEmpty()) {
            System.out.println("No hay estudiantes registrados.");
            return;
        }
        System.out.println("============ LISTA DE ESTUDIANTES ============");
        for (Estudiante e : estudiantes.values()) {
            e.mostrarInformacion();
        }
    }

    // ─── GETTER (lo necesitan otros módulos) ──────────────────
    public HashMap<String, Estudiante> getEstudiantes() {
        return estudiantes;
    }
}