import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;


public class ItinerarioDAO {
    
    public Itinerario registrarItinerario(int idRuta, int idUnidad, int idChofer, String fecha, String horaSalida) {

        if (!validarDisponibilidadUnidad(idUnidad, fecha, horaSalida)) {
            System.out.println("Error: La unidad seleccionada no cumple con la brecha de 4 horas requerida.");
            return null; 
        }

        if (!validarDisponibilidadConductor(idChofer, fecha, horaSalida)) {
            System.out.println("Error: El conductor seleccionada no cumple con la brecha de 4 horas requerida.");
            return null; 
        }

        int cupos = obtenerCapacidadUnidad(idUnidad);
        if (cupos == 0) {
            System.out.println("Error: No se encontró la unidad o su capacidad es 0.");
            return null; 
        }

        String sql = "INSERT INTO itinerarios(id_ruta, id_unidad, id_chofer, fecha, hora_salida, cupos_disponibles) VALUES(?,?,?,?,?,?)";

        try (Connection conn = ConexionBD.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idRuta);
            pstmt.setInt(2, idUnidad);
            pstmt.setInt(3, idChofer);
            pstmt.setString(4, fecha);
            pstmt.setString(5, horaSalida);
            pstmt.setInt(6, cupos);

            int filasAfectadas = pstmt.executeUpdate();

            if (filasAfectadas > 0) {
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        int idGenerado = rs.getInt(1);
                        
                        return obtenerItinerarioPorId(idGenerado);
                    }
                }
            }

        } catch (SQLException e) {
            System.out.println("Error al registrar itinerario: " + e.getMessage());
        }
        return null;
    }
    //Metodos auxiliares para registro
    private int obtenerCapacidadUnidad(int idUnidad) {
        String sql = "SELECT capacidad FROM unidades WHERE id_unidad = ?";
        try (Connection conn = ConexionBD.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
             
            pstmt.setInt(1, idUnidad);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("capacidad");
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener capacidad: " + e.getMessage());
        }
        return 0;
    }

    private Itinerario obtenerItinerarioPorId(int idItinerario) {
        
        String sql = "SELECT it.id_itinerario, it.fecha, it.hora_salida AS hora, it.estado_recorrido AS estado, "
                   + "it.cupos_disponibles AS cupos, un.modelo AS modelo, ru.nombre_ruta AS ruta, tipRut.nombre AS tipo_ruta, us.nombre as nombre_chofer, us.apellido as apellido_chofer "
                   + "FROM itinerarios it "
                   + "INNER JOIN usuarios us ON it.id_chofer = us.id_usuario "
                   + "INNER JOIN unidades un ON it.id_unidad = un.id_unidad "
                   + "INNER JOIN rutas ru ON it.id_ruta = ru.id_ruta "
                   + "INNER JOIN tipos_ruta tipRut ON ru.id_tipo_ruta = tipRut.id_tipo "
                   + "WHERE it.id_itinerario = ?";

        try (Connection conn = ConexionBD.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
             
            pstmt.setInt(1, idItinerario);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return new Itinerario(
                    rs.getInt("id_itinerario"),
                    rs.getString("fecha"),
                    rs.getString("hora"),
                    rs.getString("estado"),
                    rs.getInt("cupos"),
                    rs.getString("modelo"),
                    rs.getString("ruta"),
                    rs.getString("tipo_ruta"),
                    rs.getString("nombre_chofer")+" "+rs.getString("apellido_chofer")

                );
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener el itinerario completo: " + e.getMessage());
        }
        return null;
    }

    private boolean validarDisponibilidadUnidad(int idUnidad, String fechaSolicitada, String horaSolicitadaStr) {
    
        String sql = "SELECT hora_salida FROM itinerarios "
                + "WHERE id_unidad = ? AND fecha = ? AND estado_recorrido != 'Finalizado'";

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

        try {
            LocalTime horaSolicitada = LocalTime.parse(horaSolicitadaStr.trim(), formatter);

            try (Connection conn = ConexionBD.conectar();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

                pstmt.setInt(1, idUnidad);
                pstmt.setString(2, fechaSolicitada); 
                ResultSet rs = pstmt.executeQuery();

                while (rs.next()) {
                    String horaRegistradaStr = rs.getString("hora_salida");
                    LocalTime horaRegistrada = LocalTime.parse(horaRegistradaStr.trim(), formatter);

                    long diferenciaMinutos = Math.abs(ChronoUnit.MINUTES.between(horaSolicitada, horaRegistrada));

                    if (diferenciaMinutos < 240) {
                        System.out.println("Conflicto: La unidad ya tiene un viaje a las " + horaRegistradaStr 
                                        + " (Diferencia: " + diferenciaMinutos + " min).");
                        return false; 
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Error al validar disponibilidad o formato de hora inválido: " + e.getMessage());
            return false; 
        }

        return true; 
    }

    private boolean validarDisponibilidadConductor(int idChofer, String fechaSolicitada, String horaSolicitadaStr) {
    
        String sql = "SELECT hora_salida FROM itinerarios "
                + "WHERE id_chofer = ? AND fecha = ? AND estado_recorrido != 'Finalizado'";

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

        try {
            LocalTime horaSolicitada = LocalTime.parse(horaSolicitadaStr.trim(), formatter);

            try (Connection conn = ConexionBD.conectar();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

                pstmt.setInt(1, idChofer);
                pstmt.setString(2, fechaSolicitada); 
                ResultSet rs = pstmt.executeQuery();

                while (rs.next()) {
                    String horaRegistradaStr = rs.getString("hora_salida");
                    LocalTime horaRegistrada = LocalTime.parse(horaRegistradaStr.trim(), formatter);

                    long diferenciaMinutos = Math.abs(ChronoUnit.MINUTES.between(horaSolicitada, horaRegistrada));

                    if (diferenciaMinutos < 240) {
                        System.out.println("Conflicto: El conductor ya tiene un viaje a las " + horaRegistradaStr 
                                        + " (Diferencia: " + diferenciaMinutos + " min).");
                        return false; 
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Error al validar disponibilidad o formato de hora inválido: " + e.getMessage());
            return false; 
        }

        return true; 
    }
}
