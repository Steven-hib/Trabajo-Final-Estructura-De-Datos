package Util;

import Modelo.Estudiante;
import Servicios.GestorEstudiantes;
import Servicios.GestorMaterias;
import Excepciones.*;
import java.io.*;
import java.util.ArrayDeque;
import java.util.Queue;

public class ProcesadorBatch {

    private GestorEstudiantes gestorEstudiantes;
    private GestorMaterias    gestorMaterias;

    // Cola de procesamiento batch
    private Queue<String[]> colaSolicitudes;

    public ProcesadorBatch(GestorEstudiantes gestorEstudiantes,
                           GestorMaterias gestorMaterias) {
        this.gestorEstudiantes = gestorEstudiantes;
        this.gestorMaterias    = gestorMaterias;
        this.colaSolicitudes   = new ArrayDeque<>();
    }

    // ─── CARGAR CSV ───────────────────────────────────────────
    public void cargarCSV(String rutaArchivo) throws ArchivoInvalidoException {
        colaSolicitudes.clear();

        try {
            BufferedReader reader = new BufferedReader(new FileReader(rutaArchivo));
            String linea;

            while ((linea = reader.readLine()) != null) {
                linea = linea.trim();
                if (linea.isEmpty()) continue;

                String[] partes = linea.split(",");
                if (partes.length != 2) {
                    throw new ArchivoInvalidoException(
                        "Formato inválido en línea: " + linea +
                        ". Esperado: idEstudiante,codigoMateria"
                    );
                }
                colaSolicitudes.add(new String[]{
                    partes[0].trim(),
                    partes[1].trim()
                });
            }
            reader.close();
            System.out.println("Se encolaron " + colaSolicitudes.size() +
                " solicitudes desde: " + rutaArchivo);

        } catch (FileNotFoundException e) {
            throw new ArchivoInvalidoException("Archivo no encontrado: " + rutaArchivo);
        } catch (IOException e) {
            throw new ArchivoInvalidoException("Error leyendo archivo: " + e.getMessage());
        }
    }

    // ─── PROCESAR COLA ────────────────────────────────────────
    public void procesarCola() {
        if (colaSolicitudes.isEmpty()) {
            System.out.println("No hay solicitudes en la cola.");
            return;
        }

        int total    = colaSolicitudes.size();
        int exitosas = 0;
        int fallidas = 0;
        int numero   = 1;

        System.out.println("\n=== PROCESANDO COLA BATCH ===");

        while (!colaSolicitudes.isEmpty()) {
            String[] solicitud  = colaSolicitudes.poll(); // saca el primero
            String idEstudiante = solicitud[0];
            String codMateria   = solicitud[1];

            System.out.print("[" + numero + "/" + total + "] " +
                idEstudiante + " → " + codMateria + " → ");

            try {
                Estudiante e = gestorEstudiantes.buscarEstudiante(idEstudiante);
                gestorMaterias.inscribirEstudiante(e, codMateria);
                exitosas++;

            } catch (EstudianteNoEncontradoException e) {
                System.out.println("Fallida - Estudiante no encontrado");
                fallidas++;
            } catch (PreRequisitoNoAprobadoException e) {
                System.out.println("Fallida - Pre-requisito no cumplido");
                fallidas++;
            } catch (CupoLlenoException e) {
                System.out.println("En cola de espera");
                exitosas++; // cuenta como procesada
            } catch (Exception e) {
                System.out.println("Fallida - " + e.getMessage());
                fallidas++;
            }
            numero++;
        }

        System.out.println("\n=== RESUMEN BATCH ===");
        System.out.println("Total procesadas: " + total);
        System.out.println("Exitosas:         " + exitosas);
        System.out.println("Fallidas:         " + fallidas);
    }
}