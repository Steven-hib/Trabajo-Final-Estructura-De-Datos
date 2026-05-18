package Util;

import java.util.ArrayList;

public class GestorEdificios {

    private static final int INFINITO = 999999;

    private String[] edificios;
    private int[][] matrizDistancias;
    private int numEdificios;

    public GestorEdificios(String[] nombresEdificios) {
        this.numEdificios    = nombresEdificios.length;
        this.edificios       = nombresEdificios;
        this.matrizDistancias = new int[numEdificios][numEdificios];

        // Inicializar matriz: 0 en diagonal, INFINITO en el resto
        for (int i = 0; i < numEdificios; i++) {
            for (int j = 0; j < numEdificios; j++) {
                if (i == j) matrizDistancias[i][j] = 0;
                else        matrizDistancias[i][j] = INFINITO;
            }
        }
    }

    // ─── AGREGAR CONEXIÓN ─────────────────────────────────────
    public void agregarConexion(int origen, int destino, int distancia) {
        matrizDistancias[origen][destino] = distancia;
        matrizDistancias[destino][origen] = distancia; // grafo no dirigido
        System.out.println("Conexión agregada: " + edificios[origen] +
            " ↔ " + edificios[destino] + " (" + distancia + "m)");
    }

    // ─── DIJKSTRA ─────────────────────────────────────────────
    public void calcularRutaMasCorta(int origen, int destino) {

        int[] distancia  = new int[numEdificios];
        int[] anterior   = new int[numEdificios];
        boolean[] visitado = new boolean[numEdificios];

        // Inicializar todo en INFINITO
        for (int i = 0; i < numEdificios; i++) {
            distancia[i] = INFINITO;
            anterior[i]  = -1;
            visitado[i]  = false;
        }
        distancia[origen] = 0;

        // Algoritmo principal
        for (int count = 0; count < numEdificios - 1; count++) {

            // Encontrar el nodo no visitado con menor distancia
            int u = -1;
            for (int v = 0; v < numEdificios; v++) {
                if (!visitado[v] && (u == -1 || distancia[v] < distancia[u])) {
                    u = v;
                }
            }

            if (distancia[u] == INFINITO) break;
            visitado[u] = true;

            // Actualizar distancias de los vecinos
            for (int v = 0; v < numEdificios; v++) {
                if (!visitado[v] &&
                    matrizDistancias[u][v] != INFINITO &&
                    distancia[u] + matrizDistancias[u][v] < distancia[v]) {

                    distancia[v] = distancia[u] + matrizDistancias[u][v];
                    anterior[v]  = u;
                }
            }
        }

        // Mostrar resultado
        mostrarRuta(origen, destino, distancia, anterior);
    }

    // ─── MOSTRAR RUTA ─────────────────────────────────────────
    private void mostrarRuta(int origen, int destino,
                             int[] distancia, int[] anterior) {

        System.out.println("\n=== RUTA MÁS CORTA ===");
        System.out.println("Origen:  " + edificios[origen]);
        System.out.println("Destino: " + edificios[destino]);

        if (distancia[destino] == INFINITO) {
            System.out.println("No existe ruta entre estos edificios.");
            return;
        }

        // Reconstruir el camino usando el arreglo anterior[]
        ArrayList<Integer> camino = new ArrayList<>();
        for (int at = destino; at != -1; at = anterior[at]) {
            camino.add(0, at); // insertar al inicio
        }

        System.out.print("Ruta: ");
        for (int i = 0; i < camino.size(); i++) {
            System.out.print(edificios[camino.get(i)]);
            if (i < camino.size() - 1) System.out.print(" → ");
        }
        System.out.println("\nDistancia total: " + distancia[destino] + " metros");
    }

    // ─── MOSTRAR EDIFICIOS ────────────────────────────────────
    public void mostrarEdificios() {
        System.out.println("=== EDIFICIOS REGISTRADOS ===");
        for (int i = 0; i < numEdificios; i++) {
            System.out.println(i + ": " + edificios[i]);
        }
    }

    // ─── MOSTRAR MATRIZ ───────────────────────────────────────
    public void mostrarMatriz() {
        System.out.println("\n=== MATRIZ DE DISTANCIAS ===");
        System.out.printf("%15s", "");
        for (String e : edificios) System.out.printf("%12s", e);
        System.out.println();

        for (int i = 0; i < numEdificios; i++) {
            System.out.printf("%15s", edificios[i]);
            for (int j = 0; j < numEdificios; j++) {
                if (matrizDistancias[i][j] == INFINITO)
                    System.out.printf("%12s", "∞");
                else
                    System.out.printf("%12d", matrizDistancias[i][j]);
            }
            System.out.println();
        }
    }
}