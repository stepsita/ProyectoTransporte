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


    public static Vector<Unidad> obtenerUnidades(){
        Vector<Unidad> lista = new Vector<>();
        String sql = "SELECT id_unidad, placa, modelo, capacidad, estado_operativo FROM unidades;";

        try (Connection conn = ConexionBD.conectar(); 
            Statement estate = conn.createStatement(); 
            ResultSet rs = estate.executeQuery(sql)){

            while (rs.next()) {
                lista.add(new Unidad(rs.getInt("id_unidad"), rs.getString("placa"), rs.getString("modelo"), rs.getInt("capacidad"), rs.getString("estado_operativo")));
            }
            
        } catch (SQLException e) {
            System.out.println("Error cargando tipos: " + e.getMessage());
        }
        return lista;
    }

    public static Vector<ItemCombo> obtenerRutas(){
        Vector<ItemCombo> lista = new Vector<>();
        String sql = "SELECT r.id_ruta, r.nombre_ruta, t.nombre as nombre_tipo_ruta "
                   + "FROM rutas r "
                   + "INNER JOIN tipos_ruta t ON r.id_tipo_ruta = t.id_tipo";
        
        try (Connection conn = ConexionBD.conectar(); 
            Statement estate = conn.createStatement(); 
            ResultSet rs = estate.executeQuery(sql)){

            while (rs.next()) {
                lista.add(new ItemCombo(rs.getInt("id_ruta"), rs.getString("nombre_tipo_ruta") + " - " + rs.getString("nombre_ruta")));
            }
            
        } catch (SQLException e) {
            System.out.println("Error cargando rutas: " + e.getMessage());
        }
        return  lista;
    }

    public static Vector<ItemCombo> obtenerConductores(){
        Vector<ItemCombo> lista = new Vector<>();
        String sql = "SELECT id_usuario, nombre, apellido "
                   + "FROM usuarios "
                   + "WHERE id_rol = 2 ORDER BY nombre ASC";

        try (Connection conn = ConexionBD.conectar(); 
            Statement estate = conn.createStatement(); 
            ResultSet rs = estate.executeQuery(sql)){
            
            while (rs.next()) {
                lista.add(new ItemCombo(rs.getInt("id_usuario"), rs.getString("nombre") + " " + rs.getString("apellido")));
            }
            
        } catch (SQLException e) {
            System.out.println("Error cargando rutas: " + e.getMessage());
        }
        return  lista;
    }

    public static Vector<ItemCombo> obtenerUnidadesCombo(){
        Vector<ItemCombo> lista = new Vector<>();
        String sql = "SELECT id_unidad, modelo, capacidad FROM unidades WHERE estado_operativo = 'activo';";

        try (Connection conn = ConexionBD.conectar();
            Statement estate = conn.createStatement(); 
            ResultSet rs = estate.executeQuery(sql)){

            while (rs.next()) {
                lista.add(new ItemCombo(rs.getInt("id_unidad"), rs.getString("modelo") + " (Cap. " + rs.getString("capacidad")+".)"));
            }
            
        } catch (SQLException e) {
            System.out.println("Error cargando rutas: " + e.getMessage());
        }
        return  lista;
    }


    public static Vector<Itinerario> obtenerItinerarios(){
        Vector<Itinerario> lista = new Vector<>();
        String sql = "SELECT it.id_itinerario, it.fecha as fecha, it.hora_salida AS hora, it.estado_recorrido AS estado, "
                    + "it.cupos_disponibles AS cupos, un.modelo AS modelo, ru.nombre_ruta AS ruta, tipRut.nombre AS tipo_ruta, us.nombre as nombre_chofer, us.apellido as apellido_chofer "
                    + "FROM itinerarios it "
                    + "INNER JOIN usuarios us ON it.id_chofer = us.id_usuario "
                    + "INNER JOIN unidades un ON it.id_unidad = un.id_unidad "
                    + "INNER JOIN rutas ru ON it.id_ruta = ru.id_ruta "
                    + "INNER JOIN tipos_ruta tipRut ON ru.id_tipo_ruta = tipRut.id_tipo";

        try (Connection conn = ConexionBD.conectar(); 
            Statement estate = conn.createStatement(); 
            ResultSet rs = estate.executeQuery(sql)){

            while (rs.next()) {
                lista.add(new Itinerario(rs.getInt("id_itinerario"), rs.getString("fecha"), rs.getString("hora"), rs.getString("estado"), rs.getInt("cupos"), rs.getString("modelo"), rs.getString("ruta"), rs.getString("tipo_ruta"), rs.getString("nombre_chofer")+" "+rs.getString("apellido_chofer")));
            }
            
        } catch (SQLException e) {
            System.out.println("Error cargando tipos: " + e.getMessage());
        }
        return lista;

    }

}
