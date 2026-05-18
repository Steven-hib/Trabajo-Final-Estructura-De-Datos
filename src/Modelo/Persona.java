package Modelo;

public abstract class Persona {

    private String nombre;
    private String id;
    private String email;

    public Persona(String nombre, String id, String email) {
        this.nombre = nombre;
        this.id = id;
        this.email = email;
    }

    // Cada subclase DEBE implementar este método
    public abstract void mostrarInformacion();

    public String getNombre() { return nombre; }
    public String getId()     { return id; }
    public String getEmail()  { return email; }

    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setEmail(String email)   { this.email = email; }
}