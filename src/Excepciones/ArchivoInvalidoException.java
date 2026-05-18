package Excepciones;

public class ArchivoInvalidoException extends Exception {
    public ArchivoInvalidoException(String mensaje) {
        super(mensaje);
    }
}