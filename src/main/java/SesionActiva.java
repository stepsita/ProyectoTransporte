public class SesionActiva {
    
    private static Usuario usuarioLogueado = null;

    public static void setUsuario(Usuario usuario) {
        usuarioLogueado = usuario;
    }

    public static Usuario getUsuario() {
        return usuarioLogueado;
    }

    public static void cerrarSesion() {
        usuarioLogueado = null;
    }
}