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
                + "estado_recorrido TEXT DEFAULT 'En Parada' CHECK(estado_recorrido IN ('En Parada', 'En Trayecto', 'Llegando al Destino', 'Finalizado')), "
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
        try (Connection conn = conectar(); Statement state = conn.createStatement()) {
            if (conn != null) {
                
                state.execute("PRAGMA foreign_keys = ON;");

                state.execute(tablaRoles);
                state.execute(tablaTiposPasajero);
                state.execute(tablaTiposRuta);
                state.execute(tablaUsuarios);
                state.execute(tablaUnidades);
                state.execute(tablaRutas);
                state.execute(tablaItinerarios);
                state.execute(tablaReservas);

                System.out.println("Esquema de base de datos generado correctamente.");
            }
        } catch (SQLException e) {
            System.out.println("Error al crear las tablas: " + e.getMessage());
        }
    }


    public static void poblarCatalogos() {
        String insertRoles = "INSERT OR IGNORE INTO roles (id_rol, nombre) VALUES "
                + "(1, 'Admin'), "
                + "(2, 'Chofer'), "
                + "(3, 'Pasajero');";

        String insertTiposPasajero = "INSERT OR IGNORE INTO tipos_pasajero (id_tipo, nombre) VALUES "
                + "(1, 'Estudiante'), "
                + "(2, 'Empleado');";

        String insertTiposRuta = "INSERT OR IGNORE INTO tipos_ruta (id_tipo, nombre) VALUES "
                + "(1, 'Urbana'), "
                + "(2, 'Extraurbana');";

        String insertUsuario = "INSERT OR IGNORE INTO usuarios (id_usuario, nombre, apellido, cedula, id_rol, correo, contrasena) VALUES"
                +"(1, 'Alejandro', 'Petit', '31046647', 1, 'luis@gmail.com', '123456'), "
                +"(2, 'Luis', 'Petit', '31046648', 3, 'luias@gmail.com', '123456');";

        String insertRutas = "INSERT OR IGNORE INTO rutas (id_ruta, nombre_ruta,id_tipo_ruta) VALUES "
                            +"(1, 'Catia', 1), "
                            +"(2, 'La Rinconada', 1), "
                            +"(3, 'Caricuao', 1), "
                            +"(4, 'La Guaria', 2), "
                            +"(5, 'Maracay', 2), "
                            +"(6, 'La Vega', 1); ";

        String insertConductores = "INSERT OR IGNORE INTO usuarios (cedula, nombre, apellido, correo, contrasena, id_rol) VALUES "
                            + "('30123456', 'Sergio', 'Silva', 'sergio@gmail.com', '123456', 2), "
                            + "('12345678', 'Stephanie', 'Salazar', 'steph@gmail.com', '123456', 2), "
                            + "('1234567', 'Livia', 'Bernal', 'livia@gmail.com', '123456', 2);";

        String insertUnidades = "INSERT OR IGNORE INTO unidades (id_unidad, placa, modelo, capacidad, estado_operativo) VALUES "
                            + "(1, 'UCV-01A', 'Yutong Urbano', 25, 'activo'), "
                            + "(2, 'UCV-02B', 'Encava Autobus', 40, 'activo'), "
                            + "(3, 'UCV-03C', 'Yutong Urbano', 25, 'en mantenimiento'), "
                            + "(4, 'UCV-04D', 'Marcopolo Paradiso', 50, 'activo'), "
                            + "(5, 'UCV-05E', 'Encava Autobus', 40, 'fuera de servicio');";

        String insertItinerarios = "INSERT OR IGNORE INTO itinerarios (id_itinerario, id_ruta, id_unidad, id_chofer, fecha, hora_salida, estado_recorrido, cupos_disponibles) VALUES "
                            + "(1, 1, 1, 3, '2026-10-05', '07:00', 'En Parada', 25), "  
                            + "(2, 4, 4, 4, '2026-10-05', '08:30', 'En Parada', 50), "  
                            + "(3, 2, 2, 5, '2026-10-05', '12:00', 'En Trayecto', 15), " 
                            + "(4, 5, 4, 3, '2026-10-06', '13:00', 'En Parada', 50), "  
                            + "(5, 3, 1, 4, '2026-10-06', '16:00', 'Finalizado', 0);";

                            



        try (Connection conn = conectar(); Statement state = conn.createStatement()) {
            if (conn != null) {
                state.execute(insertRoles);
                state.execute(insertTiposPasajero);
                state.execute(insertTiposRuta);
                state.execute(insertUsuario);
                state.execute(insertRutas);
                state.execute(insertConductores);
                state.execute(insertUnidades);
                state.execute(insertItinerarios);
                System.out.println("Catálogos poblados correctamente.");
            }
        } catch (SQLException e) {
            System.out.println("Error al poblar catálogos: " + e.getMessage());
        }
    }


}