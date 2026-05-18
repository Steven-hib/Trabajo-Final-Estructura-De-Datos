package Excepciones;

public class CupoLlenoException extends Exception {
    public CupoLlenoException(String mensaje) {
        super(mensaje);
    }
}