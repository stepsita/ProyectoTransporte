import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class ConexionBD {
    // Ruta del archivo
    private static final String URL = "jdbc:sqlite:transporte.db";

    // método de conexión universal
    public static Connection conectar() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(URL);
        } catch (SQLException e) {
            System.out.println("Error de conexión a la BD: " + e.getMessage());
        }
        return conn;
    }

    // Crear tablas
    public static void inicializarBaseDeDatos() {
        String tablaUsuarios = "CREATE TABLE IF NOT EXISTS usuarios ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "nombre TEXT NOT NULL,"
                + "correo TEXT UNIQUE NOT NULL,"
                + "contrasena TEXT NOT NULL,"
                + "rol TEXT NOT NULL" // ej: 'Admin', 'Pasajero'
                + ");";

        try (Connection conn = conectar(); Statement stmt = conn.createStatement()) {
            if (conn != null) {
                stmt.execute(tablaUsuarios);
                System.out.println("Base de datos inicializada correctamente.");
            }
        } catch (SQLException e) {
            System.out.println("Error al crear tablas: " + e.getMessage());
        }
    }
}