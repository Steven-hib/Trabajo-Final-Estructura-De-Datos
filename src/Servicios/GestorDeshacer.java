package Servicios;

import Modelo.Estudiante;
import Excepciones.PilaDeshacerVaciaException;
import java.util.Stack;
import java.util.HashMap;

public class GestorDeshacer {

    private Stack<Object[]> pilaDeshacer;
    private Stack<Object[]> pilaRehacer;

    // Referencias a los otros gestores para restaurar estados
    private GestorEstudiantes gestorEstudiantes;
    private GestorMaterias gestorMaterias;
    private GestorHorarios gestorHorarios;

    public GestorDeshacer(Stack<Object[]> pilaDeshacer,
            Stack<Object[]> pilaRehacer,
            GestorEstudiantes gestorEstudiantes,
            GestorMaterias gestorMaterias,
            GestorHorarios gestorHorarios) {
        this.pilaDeshacer = pilaDeshacer;
        this.pilaRehacer = pilaRehacer;
        this.gestorEstudiantes = gestorEstudiantes;
        this.gestorMaterias = gestorMaterias;
        this.gestorHorarios = gestorHorarios;
    }

    // ─── DESHACER ─────────────────────────────────────────────
    public void deshacer() throws PilaDeshacerVaciaException {
        if (pilaDeshacer.isEmpty()) {
            throw new PilaDeshacerVaciaException(
                    "No hay operaciones para deshacer.");
        }

        Object[] operacion = pilaDeshacer.pop();
        String tipo = (String) operacion[0];

        switch (tipo) {

            case "REGISTRAR_ESTUDIANTE": {
                String id = (String) operacion[1];
                Estudiante e = gestorEstudiantes.getEstudiantes().get(id);
                gestorEstudiantes.getEstudiantes().remove(id);
                pilaRehacer.push(new Object[] { "RESTAURAR_ESTUDIANTE", e });
                System.out.println("Deshecho: registro de estudiante " + id);
                break;
            }

            case "ELIMINAR_ESTUDIANTE": {
                Estudiante e = (Estudiante) operacion[1];
                gestorEstudiantes.getEstudiantes().put(e.getId(), e);
                pilaRehacer.push(new Object[] { "ELIMINAR_ESTUDIANTE_REDO", e.getId() });
                System.out.println("Deshecho: eliminación de " + e.getNombre());
                break;
            }

            case "INSCRIBIR": {
                String idEst = (String) operacion[1];
                String codMat = (String) operacion[2];
                Estudiante e = gestorEstudiantes.getEstudiantes().get(idEst);
                gestorMaterias.getMaterias().get(codMat)
                        .getEstudiantesInscritos().remove(idEst);
                gestorMaterias.getMaterias().get(codMat).incrementarCupo();
                e.getHistorial().remove(codMat);
                pilaRehacer.push(operacion);
                System.out.println("Deshecho: inscripción de " + idEst + " en " + codMat);
                break;
            }

            case "RESERVAR": {
                String aula = (String) operacion[1];
                int dia = (int) operacion[2];
                int hora = (int) operacion[3];
                int duracion = (int) operacion[4];
                gestorHorarios.getAulas().get(aula).liberar(dia, hora, duracion);
                pilaRehacer.push(operacion);
                System.out.println("Deshecho: reserva en aula " + aula);
                break;
            }

            default:
                System.out.println("Operación no reconocida: " + tipo);
        }
    }

    // ─── REHACER ──────────────────────────────────────────────
    public void rehacer() throws PilaDeshacerVaciaException {
        if (pilaRehacer.isEmpty()) {
            throw new PilaDeshacerVaciaException(
                    "No hay operaciones para rehacer.");
        }

        Object[] operacion = pilaRehacer.pop();
        String tipo = (String) operacion[0];

        switch (tipo) {

            case "RESTAURAR_ESTUDIANTE": {
                Estudiante e = (Estudiante) operacion[1];
                gestorEstudiantes.getEstudiantes().put(e.getId(), e);
                pilaDeshacer.push(new Object[] { "REGISTRAR_ESTUDIANTE", e.getId() });
                System.out.println("Rehecho: registro de " + e.getNombre());
                break;
            }

            case "INSCRIBIR": {
                String idEst = (String) operacion[1];
                String codMat = (String) operacion[2];
                Estudiante e = gestorEstudiantes.getEstudiantes().get(idEst);
                gestorMaterias.getMaterias().get(codMat).inscribirEstudiante(idEst);
                e.agregarAlHistorial(codMat);
                pilaDeshacer.push(operacion);
                System.out.println("Rehecho: inscripción de " + idEst + " en " + codMat);
                break;
            }

            case "RESERVAR": {
                String aula = (String) operacion[1];
                int dia = (int) operacion[2];
                int hora = (int) operacion[3];
                int duracion = (int) operacion[4];
                try {
                    gestorHorarios.getAulas().get(aula).reservar(dia, hora, duracion);
                } catch (Exception e) {
                    System.out.println("No se pudo rehacer la reserva: " + e.getMessage());
                }
                pilaDeshacer.push(operacion);
                System.out.println("Rehecho: reserva en aula " + aula);
                break;
            }

            default:
                System.out.println("Operación no reconocida: " + tipo);
        }
    }
}