package Servicios;

import Modelo.Estudiante;
import Excepciones.EstudianteNoEncontradoException;
import Excepciones.PilaDeshacerVaciaException;
import java.util.Stack;

public class GestorReportes {

    private GestorEstudiantes gestorEstudiantes;

    // Pila de navegación entre reportes (funcionalidad "atrás")
    private Stack<String> pilaReportes;

    public GestorReportes(GestorEstudiantes gestorEstudiantes) {
        this.gestorEstudiantes = gestorEstudiantes;
        this.pilaReportes = new Stack<>();
    }

    // ─── REGISTRAR NOTA ───────────────────────────────────────
    public void registrarNota(String idEstudiante, int semestre,
                              int indiceMat, double nota)
            throws EstudianteNoEncontradoException {

        Estudiante e = gestorEstudiantes.buscarEstudiante(idEstudiante);

        // semestre va de 1-10, el índice del arreglo va de 0-9
        e.registrarNota(semestre - 1, indiceMat - 1, nota);
        System.out.println("Nota registrada: Semestre " + semestre +
            ", Materia " + indiceMat + " → " + nota);
    }

    // ─── VER REPORTE ──────────────────────────────────────────
    public void verReporte(String idEstudiante)
            throws EstudianteNoEncontradoException {

        Estudiante e = gestorEstudiantes.buscarEstudiante(idEstudiante);

        // Guardamos en la pila de navegación
        pilaReportes.push(idEstudiante);

        System.out.println("\n=== REPORTE ACADÉMICO ===");
        System.out.println("Estudiante: " + e.getNombre() + " (ID: " + e.getId() + ")");

        Double[][] notas = e.getNotas();
        int totalAprobadas  = 0;
        int totalReprobadas = 0;

        for (int sem = 0; sem < 10; sem++) {
            boolean tieneMaterias = false;

            // Verificar si el semestre tiene notas
            for (int mat = 0; mat < 20; mat++) {
                if (notas[sem][mat] != null) {
                    tieneMaterias = true;
                    break;
                }
            }

            if (!tieneMaterias) continue;

            System.out.println("\nSemestre " + (sem + 1) + ":");
            for (int mat = 0; mat < 20; mat++) {
                if (notas[sem][mat] != null) {
                    String estado = notas[sem][mat] >= 3.0 ? "✓" : "✗ REPROBADA";
                    System.out.println("  Materia " + (mat + 1) +
                        ": " + notas[sem][mat] + " " + estado);
                    if (notas[sem][mat] >= 3.0) totalAprobadas++;
                    else totalReprobadas++;
                }
            }
            System.out.printf("  Promedio semestre: %.2f%n",
                e.calcularPromedioPorSemestre(sem));
        }

        System.out.println("\n=== RESUMEN ===");
        System.out.printf("Promedio acumulado: %.2f%n", e.calcularPromedioAcumulado());
        System.out.println("Materias aprobadas:  " + totalAprobadas);
        System.out.println("Materias reprobadas: " + totalReprobadas);
    }

    // ─── NAVEGACIÓN ATRÁS ─────────────────────────────────────
    public void atras() throws PilaDeshacerVaciaException,
                               EstudianteNoEncontradoException {
        if (pilaReportes.isEmpty()) {
            throw new PilaDeshacerVaciaException(
                "No hay reportes anteriores."
            );
        }

        // Saca el reporte actual
        pilaReportes.pop();

        if (pilaReportes.isEmpty()) {
            System.out.println("No hay reporte anterior.");
            return;
        }

        // Muestra el anterior
        String idAnterior = pilaReportes.peek();
        System.out.println("\n--- Volviendo al reporte anterior ---");
        verReporte(idAnterior);
    }
}