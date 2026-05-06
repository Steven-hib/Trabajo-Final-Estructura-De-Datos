import Modelo.Estudiante;
import Servicios.*;
import Util.*;
import Excepciones.*;
import java.util.Scanner;
import java.util.Stack;

public class Main {

    // ─── GESTORES GLOBALES ────────────────────────────────────
    static Stack<Object[]> pilaDeshacer = new Stack<>();
    static Stack<Object[]> pilaRehacer = new Stack<>();

    static GestorEstudiantes gestorEst = new GestorEstudiantes(pilaDeshacer, pilaRehacer);
    static GestorMaterias gestorMat = new GestorMaterias(pilaDeshacer, pilaRehacer);
    static GestorHorarios gestorHorarios = new GestorHorarios(pilaDeshacer, pilaRehacer);
    static GestorReportes gestorReportes = new GestorReportes(gestorEst);
    static GestorDeshacer gestorDeshacer = new GestorDeshacer(
            pilaDeshacer, pilaRehacer, gestorEst, gestorMat, gestorHorarios);
    static GestorEdificios gestorEdificios;
    static ProcesadorBatch batch = new ProcesadorBatch(gestorEst, gestorMat);
    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        inicializarEdificios();
        int opcion = 0;

        do {
            mostrarMenu();
            try {
                opcion = Integer.parseInt(scanner.nextLine().trim());
                procesarOpcion(opcion);
            } catch (NumberFormatException e) {
                System.out.println("Ingrese un número válido.");
            }
        } while (opcion != 22);
    }

    // ─── MENÚ ─────────────────────────────────────────────────
    static void mostrarMenu() {
        System.out.println("============================================================");
        System.out.println("     PLANIFICACIÓN ACADÉMICA - SISTEMA UNIVERSITARIO");
        System.out.println("============================================================");
        System.out.println("=== GESTIÓN DE ESTUDIANTES ===");
        System.out.println("1.  Registrar estudiante");
        System.out.println("2.  Buscar estudiante por ID");
        System.out.println("3.  Listar todos los estudiantes");
        System.out.println("4.  Eliminar estudiante");
        System.out.println("=== GESTIÓN DE MATERIAS ===");
        System.out.println("5.  Crear materia");
        System.out.println("6.  Agregar pre-requisito");
        System.out.println("7.  Mostrar pre-requisitos");
        System.out.println("8.  Inscribir estudiante en materia");
        System.out.println("9.  Cancelar inscripción");
        System.out.println("10. Mostrar cola de espera");
        System.out.println("=== GESTIÓN DE HORARIOS ===");
        System.out.println("11. Registrar aula");
        System.out.println("12. Reservar horario");
        System.out.println("13. Liberar horario");
        System.out.println("14. Consultar disponibilidad");
        System.out.println("=== RUTAS ENTRE EDIFICIOS ===");
        System.out.println("15. Mostrar edificios");
        System.out.println("16. Calcular ruta más corta");
        System.out.println("=== REPORTES ACADÉMICOS ===");
        System.out.println("17. Registrar nota");
        System.out.println("18. Ver reporte académico");
        System.out.println("19. Volver al reporte anterior");
        System.out.println("=== DESHACER / REHACER ===");
        System.out.println("20. Deshacer última operación");
        System.out.println("21. Rehacer última operación");
        System.out.println("=== PROCESAMIENTO POR LOTES ===");
        System.out.println("22. Procesar archivo CSV");
        System.out.println("=== SALIR ===");
        System.out.println("0.  Salir");
        System.out.print("Seleccione una opción: ");
    }

    // ─── PROCESAR OPCIÓN ──────────────────────────────────────
    static void procesarOpcion(int opcion) {
        try {
            switch (opcion) {

                // ── ESTUDIANTES ──
                case 1:
                    registrarEstudiante();
                    break;
                case 2:
                    buscarEstudiante();
                    break;
                case 3:
                    gestorEst.listarEstudiantes();
                    break;
                case 4:
                    eliminarEstudiante();
                    break;

                // ── MATERIAS ──
                case 5:
                    crearMateria();
                    break;
                case 6:
                    agregarPreRequisito();
                    break;
                case 7:
                    mostrarPreRequisitos();
                    break;
                case 8:
                    inscribirEstudiante();
                    break;
                case 9:
                    cancelarInscripcion();
                    break;
                case 10:
                    mostrarColaEspera();
                    break;

                // ── HORARIOS ──
                case 11:
                    registrarAula();
                    break;
                case 12:
                    reservarHorario();
                    break;
                case 13:
                    liberarHorario();
                    break;
                case 14:
                    consultarDisponibilidad();
                    break;

                // ── EDIFICIOS ──
                case 15:
                    gestorEdificios.mostrarEdificios();
                    break;
                case 16:
                    calcularRuta();
                    break;

                // ── REPORTES ──
                case 17:
                    registrarNota();
                    break;
                case 18:
                    verReporte();
                    break;
                case 19:
                    gestorReportes.atras();
                    break;

                // ── DESHACER/REHACER ──
                case 20:
                    gestorDeshacer.deshacer();
                    break;
                case 21:
                    gestorDeshacer.rehacer();
                    break;

                // ── BATCH ──
                case 22:
                    procesarBatch();
                    break;

                // ── SALIR ──
                case 0:
                    System.out.println("Saliendo del sistema. ¡Hasta luego!");
                    System.exit(0);
                    break;

                default:
                    System.out.println("Opción no válida.");
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // ─── MÉTODOS DE CADA OPCIÓN ───────────────────────────────

    static void registrarEstudiante() {
        System.out.println("--- REGISTRAR ESTUDIANTE ---");
        System.out.print("ID: ");
        String id = scanner.nextLine().trim();
        System.out.print("Nombre: ");
        String nombre = scanner.nextLine().trim();
        System.out.print("Email: ");
        String email = scanner.nextLine().trim();
        System.out.print("Semestre actual: ");
        int semestre = Integer.parseInt(scanner.nextLine().trim());
        gestorEst.registrarEstudiante(nombre, id, email, semestre);
    }

    static void buscarEstudiante() throws EstudianteNoEncontradoException {
        System.out.print("ID del estudiante: ");
        String id = scanner.nextLine().trim();
        Estudiante e = gestorEst.buscarEstudiante(id);
        e.mostrarInformacion();
    }

    static void eliminarEstudiante() throws EstudianteNoEncontradoException {
        System.out.print("ID del estudiante a eliminar: ");
        String id = scanner.nextLine().trim();
        gestorEst.eliminarEstudiante(id);
    }

    static void crearMateria() {
        System.out.println("--- CREAR MATERIA ---");
        System.out.print("Código: ");
        String codigo = scanner.nextLine().trim();
        System.out.print("Nombre: ");
        String nombre = scanner.nextLine().trim();
        System.out.print("Cupos máximos: ");
        int cupos = Integer.parseInt(scanner.nextLine().trim());
        System.out.print("Créditos: ");
        int creditos = Integer.parseInt(scanner.nextLine().trim());
        gestorMat.crearMateria(codigo, nombre, cupos, creditos);
    }

    static void agregarPreRequisito() throws Exception {
        System.out.print("Código de materia: ");
        String cod = scanner.nextLine().trim();
        System.out.print("Código del pre-requisito: ");
        String pre = scanner.nextLine().trim();
        gestorMat.agregarPreRequisito(cod, pre);
    }

    static void mostrarPreRequisitos() throws Exception {
        System.out.print("Código de materia: ");
        String cod = scanner.nextLine().trim();
        gestorMat.mostrarPreRequisitos(cod);
    }

    static void inscribirEstudiante() throws Exception {
        System.out.print("ID del estudiante: ");
        String id = scanner.nextLine().trim();
        System.out.print("Código de materia: ");
        String cod = scanner.nextLine().trim();
        Estudiante e = gestorEst.buscarEstudiante(id);
        gestorMat.inscribirEstudiante(e, cod);
    }

    static void cancelarInscripcion() throws Exception {
        System.out.print("ID del estudiante: ");
        String id = scanner.nextLine().trim();
        System.out.print("Código de materia: ");
        String cod = scanner.nextLine().trim();
        Estudiante e = gestorEst.buscarEstudiante(id);
        gestorMat.cancelarInscripcion(e, cod);
    }

    static void mostrarColaEspera() throws Exception {
        System.out.print("Código de materia: ");
        String cod = scanner.nextLine().trim();
        gestorMat.getMaterias().get(cod).mostrarColaEspera();
    }

    static void registrarAula() {
        System.out.print("Nombre del aula: ");
        String nombre = scanner.nextLine().trim();
        System.out.print("Capacidad: ");
        int cap = Integer.parseInt(scanner.nextLine().trim());
        gestorHorarios.agregarAula(nombre, cap);
    }

    static void reservarHorario() throws HorarioConflictivoException {
        System.out.print("Nombre del aula: ");
        String aula = scanner.nextLine().trim();
        System.out.print("Día (0=Dom, 1=Lun, 2=Mar, 3=Mié, 4=Jue, 5=Vie, 6=Sáb): ");
        int dia = Integer.parseInt(scanner.nextLine().trim());
        System.out.print("Hora inicio (0-23): ");
        int hora = Integer.parseInt(scanner.nextLine().trim());
        System.out.print("Duración en horas: ");
        int dur = Integer.parseInt(scanner.nextLine().trim());
        gestorHorarios.reservar(aula, dia, hora, dur);
    }

    static void liberarHorario() {
        System.out.print("Nombre del aula: ");
        String aula = scanner.nextLine().trim();
        System.out.print("Día: ");
        int dia = Integer.parseInt(scanner.nextLine().trim());
        System.out.print("Hora inicio: ");
        int hora = Integer.parseInt(scanner.nextLine().trim());
        System.out.print("Duración: ");
        int dur = Integer.parseInt(scanner.nextLine().trim());
        gestorHorarios.liberar(aula, dia, hora, dur);
    }

    static void consultarDisponibilidad() {
        System.out.print("Nombre del aula: ");
        String aula = scanner.nextLine().trim();
        System.out.print("Día: ");
        int dia = Integer.parseInt(scanner.nextLine().trim());
        System.out.print("Hora: ");
        int hora = Integer.parseInt(scanner.nextLine().trim());
        gestorHorarios.consultarDisponibilidad(aula, dia, hora);
    }

    static void calcularRuta() {
        gestorEdificios.mostrarEdificios();
        System.out.print("Índice edificio origen: ");
        int origen = Integer.parseInt(scanner.nextLine().trim());
        System.out.print("Índice edificio destino: ");
        int destino = Integer.parseInt(scanner.nextLine().trim());
        gestorEdificios.calcularRutaMasCorta(origen, destino);
    }

    static void registrarNota() throws EstudianteNoEncontradoException {
        System.out.print("ID del estudiante: ");
        String id = scanner.nextLine().trim();
        System.out.print("Semestre (1-10): ");
        int sem = Integer.parseInt(scanner.nextLine().trim());
        System.out.print("Índice materia (1-20): ");
        int mat = Integer.parseInt(scanner.nextLine().trim());
        System.out.print("Nota (0.0 - 5.0): ");
        double nota = Double.parseDouble(scanner.nextLine().trim());
        gestorReportes.registrarNota(id, sem, mat, nota);
    }

    static void verReporte() throws EstudianteNoEncontradoException {
        System.out.print("ID del estudiante: ");
        String id = scanner.nextLine().trim();
        gestorReportes.verReporte(id);
    }

    static void procesarBatch() throws ArchivoInvalidoException {
        System.out.print("Ruta del archivo CSV: ");
        String ruta = scanner.nextLine().trim();
        batch.cargarCSV(ruta);
        batch.procesarCola();
    }

    // ─── INICIALIZAR EDIFICIOS ────────────────────────────────
    static void inicializarEdificios() {
        String[] nombres = {
                "Ingenieria", "Biblioteca", "Cafeteria", "Rectoria", "Laboratorios"
        };
        gestorEdificios = new GestorEdificios(nombres);
        gestorEdificios.agregarConexion(0, 1, 100);
        gestorEdificios.agregarConexion(0, 2, 150);
        gestorEdificios.agregarConexion(0, 4, 200);
        gestorEdificios.agregarConexion(1, 2, 80);
        gestorEdificios.agregarConexion(1, 3, 120);
        gestorEdificios.agregarConexion(2, 3, 180);
        gestorEdificios.agregarConexion(2, 4, 90);
        gestorEdificios.agregarConexion(3, 4, 160);
    }
}