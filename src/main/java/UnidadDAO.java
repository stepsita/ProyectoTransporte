import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class UnidadDAO {
    

    public Unidad registrarUnidad(String placa, String modelo, int capacidad){
        String sql = "INSERT INTO unidades(placa, modelo, capacidad, estado_operativo) VALUES(?,?,?,?)";

        try (Connection conn = ConexionBD.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, placa);
            pstmt.setString(2, modelo);
            pstmt.setInt(3, capacidad);
            pstmt.setString(4, "activo");
            

            int filasAfectadas = pstmt.executeUpdate();

            if (filasAfectadas > 0) {
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        int idGenerado = rs.getInt(1);
                        
                        return new Unidad(idGenerado, placa, modelo, capacidad, "activo");
                    }
                }
            }

        } catch (SQLException e) {
            System.out.println("Error al registrar: " + e.getMessage());
            
        }
        return null;

    }

    public boolean actualizarUnidad(String placa, int nuevaCapacidad, String nuevoEstado) {
        
        String sqlValidacion = "SELECT COUNT(*) FROM itinerarios it "
                            + "INNER JOIN unidades un ON it.id_unidad = un.id_unidad "
                            + "WHERE un.placa = ? AND it.estado_recorrido != 'Finalizado'";

        
        String sqlUpdate = "UPDATE unidades SET capacidad = ?, estado_operativo = ? WHERE placa = ?";

        try (Connection conn = ConexionBD.conectar()) {
            
            try (PreparedStatement pstmtVal = conn.prepareStatement(sqlValidacion)) {
                pstmtVal.setString(1, placa);
                ResultSet rs = pstmtVal.executeQuery();
                
                if (rs.next()) {
                    int viajesActivos = rs.getInt(1);
                    if (viajesActivos > 0) {
                        System.out.println("Regla de negocio: La unidad " + placa + " está en un servicio activo.");
                        return false;
                    }
                }
            }

            
            try (PreparedStatement pstmtUpd = conn.prepareStatement(sqlUpdate)) {
                pstmtUpd.setInt(1, nuevaCapacidad);
                pstmtUpd.setString(2, nuevoEstado);
                pstmtUpd.setString(3, placa);

                int filasAfectadas = pstmtUpd.executeUpdate();
                return filasAfectadas > 0;
            }

        } catch (SQLException e) {
            System.out.println("Error al actualizar la unidad: " + e.getMessage());
            return false;
        }
    }
}
