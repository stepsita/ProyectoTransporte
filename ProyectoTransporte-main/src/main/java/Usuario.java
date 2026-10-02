public class Usuario {
    private int idUsuario;
    private String nombre;
    private String apellido;
    private int idRol;
    private  int idTipo;

    public Usuario(int idUsuario, String nombre, String apellido, int idRol, int idTipo) {
        this.idUsuario = idUsuario;
        this.nombre = nombre;
        this.apellido = apellido;
        this.idRol = idRol;
        this.idTipo = idTipo;
    }

    // Getters
    public int getIdUsuario() { return idUsuario; }
    public String getNombre() { return nombre; }
    public String getApellido() { return apellido; }
    public int getIdRol() { return idRol; }
    public  int getIdTipo() { return idTipo; }
    public String getNombreCompleto() { return nombre+" "+apellido;}
}