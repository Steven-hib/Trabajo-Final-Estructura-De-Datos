package Modelo;

public class Facultad {

    private String nombre;
    private String codigo;
    private String decano;

    public Facultad(String codigo, String nombre, String decano) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.decano = decano;
    }

    public void mostrarInformacion() {
        System.out.println("Código:  " + codigo);
        System.out.println("Nombre:  " + nombre);
        System.out.println("Decano:  " + decano);
        System.out.println("----------");
    }

    public String getNombre() { return nombre; }
    public String getCodigo() { return codigo; }
    public String getDecano() { return decano; }
}