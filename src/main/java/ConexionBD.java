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
        // 1. Tablas de Catálogo (Roles y Tipos)
        String tablaRoles = "CREATE TABLE IF NOT EXISTS roles ("
                + "id_rol INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "nombre TEXT UNIQUE NOT NULL"
                + ");";

        String tablaTiposPasajero = "CREATE TABLE IF NOT EXISTS tipos_pasajero ("
                + "id_tipo INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "nombre TEXT UNIQUE NOT NULL"
                + ");";

        String tablaTiposRuta = "CREATE TABLE IF NOT EXISTS tipos_ruta ("
                + "id_tipo INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "nombre TEXT UNIQUE NOT NULL CHECK(nombre IN ('Urbana', 'Extraurbana'))"
                + ");";

        // 2. Usuarios del Sistema
        String tablaUsuarios = "CREATE TABLE IF NOT EXISTS usuarios ("
                + "id_usuario INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "cedula TEXT UNIQUE NOT NULL, "
                + "nombre TEXT NOT NULL, "
                + "apellido TEXT NOT NULL, "
                + "correo TEXT UNIQUE NOT NULL, "
                + "contrasena TEXT NOT NULL, "
                + "id_rol INTEGER NOT NULL, "
                + "id_tipo_pasajero INTEGER, "
                + "facultad TEXT, "
                + "escuela TEXT, "
                + "FOREIGN KEY(id_rol) REFERENCES roles(id_rol), "
                + "FOREIGN KEY(id_tipo_pasajero) REFERENCES tipos_pasajero(id_tipo)"
                + ");";

        // 3. Gestión de la Flota
        String tablaUnidades = "CREATE TABLE IF NOT EXISTS unidades ("
                + "id_unidad INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "placa TEXT UNIQUE NOT NULL, "
                + "modelo TEXT NOT NULL, "
                + "capacidad INTEGER NOT NULL, "
                + "estado_operativo TEXT NOT NULL CHECK(estado_operativo IN ('activo', 'en mantenimiento', 'fuera de servicio'))"
                + ");";

        // 4. Rutas
        String tablaRutas = "CREATE TABLE IF NOT EXISTS rutas ("
                + "id_ruta INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "nombre_ruta TEXT NOT NULL, "
                + "id_tipo_ruta INTEGER NOT NULL, "
                + "FOREIGN KEY(id_tipo_ruta) REFERENCES tipos_ruta(id_tipo)"
                + ");";

        // 5. Control de Itinerarios
        String tablaItinerarios = "CREATE TABLE IF NOT EXISTS itinerarios ("
                + "id_itinerario INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "id_ruta INTEGER NOT NULL, "
                + "id_unidad INTEGER NOT NULL, "
                + "id_chofer INTEGER NOT NULL, "
                + "fecha TEXT NOT NULL, "
                + "hora_salida TEXT NOT NULL, "
                + "estado_recorrido TEXT DEFAULT 'En Parada' CHECK(estado_recorrido IN ('En Parada', 'En Trayecto', 'Llegando al Destino')), "
                + "cupos_disponibles INTEGER NOT NULL, "
                + "FOREIGN KEY(id_ruta) REFERENCES rutas(id_ruta), "
                + "FOREIGN KEY(id_unidad) REFERENCES unidades(id_unidad), "
                + "FOREIGN KEY(id_chofer) REFERENCES usuarios(id_usuario)"
                + ");";

        // 6. Sistema de Reservas
        String tablaReservas = "CREATE TABLE IF NOT EXISTS reservas ("
                + "id_reserva INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "id_itinerario INTEGER NOT NULL, "
                + "id_usuario INTEGER NOT NULL, "
                + "fecha_reserva TIMESTAMP DEFAULT CURRENT_TIMESTAMP, "
                + "estado TEXT DEFAULT 'Activa' CHECK(estado IN ('Activa', 'Cancelada', 'Abordado')), "
                + "FOREIGN KEY(id_itinerario) REFERENCES itinerarios(id_itinerario), "
                + "FOREIGN KEY(id_usuario) REFERENCES usuarios(id_usuario)"
                + ");";

        // Ejecución de los scripts en la base de datos
        try (Connection conn = conectar(); Statement stmt = conn.createStatement()) {
            if (conn != null) {
                // Habilitar el chequeo de llaves foráneas en SQLite
                stmt.execute("PRAGMA foreign_keys = ON;");

                stmt.execute(tablaRoles);
                stmt.execute(tablaTiposPasajero);
                stmt.execute(tablaTiposRuta);
                stmt.execute(tablaUsuarios);
                stmt.execute(tablaUnidades);
                stmt.execute(tablaRutas);
                stmt.execute(tablaItinerarios);
                stmt.execute(tablaReservas);

                System.out.println("Esquema de base de datos generado correctamente.");
            }
        } catch (SQLException e) {
            System.out.println("Error al crear las tablas: " + e.getMessage());
        }
    }
}