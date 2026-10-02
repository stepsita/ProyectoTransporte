import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import java.sql.Statement;


public class ConsultasBD {

    //Consulta de tipo_pasajero
    public static Vector<ItemCombo> obtenerTiposPasajero() {
        Vector<ItemCombo> lista = new Vector<>();
        String sql = "SELECT id_tipo, nombre FROM tipos_pasajero";
        
        try (Connection conn = ConexionBD.conectar(); 
            Statement estate = conn.createStatement(); 
            ResultSet rs = estate.executeQuery(sql)) {
            
            while (rs.next()) {
                lista.add(new ItemCombo(rs.getInt("id_tipo"), rs.getString("nombre")));
            }
        } catch (SQLException e) {
            System.out.println("Error cargando tipos: " + e.getMessage());
        }
        return lista;
    }

}
