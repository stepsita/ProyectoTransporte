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

    
    public boolean actualizarUnidad(String placaOriginal, String nuevaPlaca, String nuevoModelo, int nuevaCapacidad, String nuevoEstado) {
        
        String sqlValidacion = "SELECT COUNT(*) FROM itinerarios it "
                             + "INNER JOIN unidades un ON it.id_unidad = un.id_unidad "
                             + "WHERE un.placa = ? AND it.estado_recorrido != 'Finalizado'";

        
        String sqlUpdate = "UPDATE unidades SET placa = ?, modelo = ?, capacidad = ?, estado_operativo = ? WHERE placa = ?";

        try (Connection conn = ConexionBD.conectar()) {
            
            
            try (PreparedStatement pstmtVal = conn.prepareStatement(sqlValidacion)) {
                pstmtVal.setString(1, placaOriginal);
                ResultSet rs = pstmtVal.executeQuery();
                if (rs.next() && rs.getInt(1) > 0) {
                    System.out.println("Bloqueo: La unidad " + placaOriginal + " tiene un servicio activo.");
                    return false;
                }
            }

            
            try (PreparedStatement pstmtUpd = conn.prepareStatement(sqlUpdate)) {
                pstmtUpd.setString(1, nuevaPlaca);
                pstmtUpd.setString(2, nuevoModelo);
                pstmtUpd.setInt(3, nuevaCapacidad);
                pstmtUpd.setString(4, nuevoEstado);
                pstmtUpd.setString(5, placaOriginal);

                int filasAfectadas = pstmtUpd.executeUpdate();
                return filasAfectadas > 0;
            }

        } catch (SQLException e) {
            System.out.println("Error al actualizar la unidad (¿Placa duplicada?): " + e.getMessage());
            return false;
        }
    }
}
