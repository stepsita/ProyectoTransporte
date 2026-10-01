import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GestionDAO {

    // Método para registrar un usuario nuevo
    public boolean registrarUsuario(String cedula, String nombre, String apellido, String correo, String contrasena, int rol, int tipo, String facultad, String escuela) {
        String sql = "INSERT INTO usuarios(cedula, nombre, apellido, correo, contrasena, id_rol, id_tipo_pasajero, facultad, escuela) VALUES(?,?,?,?,?,?,?,?,?)";

        // El try-with-resources cierra la conexión automáticamente
        try (Connection conn = ConexionBD.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, cedula);
            pstmt.setString(2, nombre);
            pstmt.setString(3, apellido);
            pstmt.setString(4, correo);
            pstmt.setString(5, contrasena);
            pstmt.setInt(6, rol);
            pstmt.setInt(7, tipo);
            pstmt.setString(8, facultad);
            pstmt.setString(9, escuela);


            pstmt.executeUpdate();
            return true; 
        } catch (SQLException e) {
            System.out.println("Error al registrar: " + e.getMessage());
            return false; 
        }
    }

    // Método para el Login (La validación)
    public boolean validarLogin(String correo, String contrasena) {
        String sql = "SELECT id FROM usuarios WHERE correo = ? AND contrasena = ?";

        try (Connection conn = ConexionBD.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, correo);
            pstmt.setString(2, contrasena);

            ResultSet rs = pstmt.executeQuery();
            
            // Si el ResultSet tiene al menos un resultado (next() es true), las credenciales son válidas
            return rs.next(); 

        } catch (SQLException e) {
            System.out.println("Error en login: " + e.getMessage());
            return false;
        }
    }
}