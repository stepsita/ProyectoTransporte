import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

public class GestorCobro {

    public static class SaldoInsuficienteException extends Exception {
        public SaldoInsuficienteException(String mensaje) {
            super(mensaje);
        }
    }

    public static class DatosInvalidosException extends Exception {
        public DatosInvalidosException(String mensaje) {
            super(mensaje);
        }
    }

    private static double porcentajeEstudiante = 15.0;
    private static double porcentajeEmpleado = 40.0;
    private static double tarifaBaseGlobal = 80.0;

    static {
        cargarConfiguracionDesdeBD();
    }

    public static void cargarConfiguracionDesdeBD() {
        try (Connection conn = ConexionBD.conectar();
             Statement stmt = conn.createStatement()) {

            
            ResultSet rsTipos = stmt.executeQuery("SELECT id_tipo, porcentaje_tarifa FROM tipos_pasajero");
            while (rsTipos.next()) {
                int idTipo = rsTipos.getInt("id_tipo");
                double pct = rsTipos.getDouble("porcentaje_tarifa");
                if (idTipo == 1) porcentajeEstudiante = pct;
                else if (idTipo == 2) porcentajeEmpleado = pct;
            }

            
            ResultSet rsRuta = stmt.executeQuery("SELECT tarifa_base FROM rutas WHERE id_tipo_ruta = 2 LIMIT 1");
            if (rsRuta.next()) {
                double tarifaBD = rsRuta.getDouble("tarifa_base");
                
                if (tarifaBD > 0) {
                    tarifaBaseGlobal = tarifaBD;
                } else {
                    stmt.executeUpdate("UPDATE rutas SET tarifa_base = 80.0 WHERE id_tipo_ruta = 2");
                    tarifaBaseGlobal = 80.0;
                }
            }
        } catch (SQLException e) {
            System.out.println("Aviso al cargar tarifas iniciales: " + e.getMessage());
        }
    }

    public static double getTarifaBaseGlobal() { return tarifaBaseGlobal; }
    public static double getPorcentajeEstudiante() { return porcentajeEstudiante; }
    public static double getPorcentajeEmpleado() { return porcentajeEmpleado; }

    

    private static int obtenerIdUsuarioPorCedula(Connection conn, String cedula) throws SQLException {
        String sql = "SELECT id_usuario FROM usuarios WHERE cedula = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, cedula);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id_usuario");
                }
            }
        }
        return -1;
    }

    private static int obtenerOCrearIdMonedero(Connection conn, int idUsuario) throws SQLException {
        
        String sqlInsert = "INSERT OR IGNORE INTO monederos (id_usuario, saldo_actual) VALUES (?, 0.0)";
        try (PreparedStatement pstmtIns = conn.prepareStatement(sqlInsert)) {
            pstmtIns.setInt(1, idUsuario);
            pstmtIns.executeUpdate();
        }

        
        String sqlSelect = "SELECT id_monedero FROM monederos WHERE id_usuario = ?";
        try (PreparedStatement pstmtSel = conn.prepareStatement(sqlSelect)) {
            pstmtSel.setInt(1, idUsuario);
            try (ResultSet rs = pstmtSel.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id_monedero");
                }
            }
        }
        return -1;
    }

    

    public static boolean actualizarConfiguracionTarifas(double tarifaBase, double pctEstudiante, double pctEmpleado) {
        String sqlUpdateTipo = "UPDATE tipos_pasajero SET porcentaje_tarifa = ? WHERE id_tipo = ?";
        String sqlUpdateRutasExtra = "UPDATE rutas SET tarifa_base = ? WHERE id_tipo_ruta = 2";

        try (Connection conn = ConexionBD.conectar()) {
            conn.setAutoCommit(false);
            try (PreparedStatement pstmtTipo = conn.prepareStatement(sqlUpdateTipo);
                 PreparedStatement pstmtRuta = conn.prepareStatement(sqlUpdateRutasExtra)) {

                pstmtTipo.setDouble(1, pctEstudiante);
                pstmtTipo.setInt(2, 1);
                pstmtTipo.executeUpdate();

                pstmtTipo.setDouble(1, pctEmpleado);
                pstmtTipo.setInt(2, 2);
                pstmtTipo.executeUpdate();

                pstmtRuta.setDouble(1, tarifaBase);
                pstmtRuta.executeUpdate();

                conn.commit();

                tarifaBaseGlobal = tarifaBase;
                porcentajeEstudiante = pctEstudiante;
                porcentajeEmpleado = pctEmpleado;
                return true;
            } catch (SQLException ex) {
                conn.rollback();
                System.out.println("Rollback en actualizarConfiguracionTarifas: " + ex.getMessage());
                return false;
            }
        } catch (SQLException e) {
            System.out.println("Error de BD al actualizar tarifas: " + e.getMessage());
            return false;
        }
    }

    

    public static void sincronizarSaldoUsuario(Usuario usuario) {
        if (usuario == null) return;

        try (Connection conn = ConexionBD.conectar()) {
            int idUsuario = obtenerIdUsuarioPorCedula(conn, usuario.getCedula());
            if (idUsuario == -1) return;

            
            obtenerOCrearIdMonedero(conn, idUsuario);

            String sqlSelect = "SELECT saldo_actual FROM monederos WHERE id_usuario = ?";
            try (PreparedStatement pstmtSel = conn.prepareStatement(sqlSelect)) {
                pstmtSel.setInt(1, idUsuario);
                try (ResultSet rs = pstmtSel.executeQuery()) {
                    if (rs.next()) {
                        usuario.setSaldo(rs.getDouble("saldo_actual"));
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Error sincronizando monedero: " + e.getMessage());
        }
    }

    public static double calcularTarifaSolidaria(double tarifaBase, int idTipo) {
        if (tarifaBase <= 0) return 0.0;
        switch (idTipo) {
            case 1:
                return tarifaBase * (porcentajeEstudiante / 100.0);
            case 2:
                return tarifaBase * (porcentajeEmpleado / 100.0);
            case 3:
            default:
                return tarifaBase;
        }
    }

    public static boolean procesarPago(Usuario usuario, double tarifaBase) throws SaldoInsuficienteException {
        if (usuario == null) return false;
        sincronizarSaldoUsuario(usuario);

        double tarifaFinal = calcularTarifaSolidaria(tarifaBase, usuario.getIdTipo());
        double saldoAnterior = usuario.getSaldo();

        if (saldoAnterior < tarifaFinal) {
            throw new SaldoInsuficienteException(
                String.format("Saldo insuficiente (Saldo: %.2f Bs., Requerido: %.2f Bs.).", saldoAnterior, tarifaFinal)
            );
        }

        double nuevoSaldo = saldoAnterior - tarifaFinal;

        if (registrarMovimientoMonedero(usuario.getCedula(), "PAGO_PASAJE", tarifaFinal, saldoAnterior, nuevoSaldo,
                "Pago de boleto ruta extraurbana", null, null, null, null)) {
            usuario.setSaldo(nuevoSaldo);
            return true;
        }
        return false;
    }

    public static boolean procesarReembolso(Usuario usuario, double montoReembolso, String destino) {
        if (usuario == null || montoReembolso <= 0) return false;
        sincronizarSaldoUsuario(usuario);

        double saldoAnterior = usuario.getSaldo();
        double nuevoSaldo = saldoAnterior + montoReembolso;

        if (registrarMovimientoMonedero(usuario.getCedula(), "REEMBOLSO", montoReembolso, saldoAnterior, nuevoSaldo,
                "Reembolso por cancelación: " + destino, null, null, null, null)) {
            usuario.setSaldo(nuevoSaldo);
            return true;
        }
        return false;
    }

    public static boolean recargarSaldo(Usuario usuario, String banco, String telefono, String cedula, String referencia, double monto) throws DatosInvalidosException {
        if (usuario == null || monto <= 0) {
            throw new DatosInvalidosException("El monto a recargar debe ser mayor a 0.");
        }

        if (banco == null || banco.trim().isEmpty() || telefono == null || telefono.trim().isEmpty()
                || cedula == null || cedula.trim().isEmpty() || referencia == null || referencia.trim().isEmpty()) {
            throw new DatosInvalidosException("Por favor, complete todos los campos obligatorios del Pago Móvil.");
        }

        if (!telefono.trim().matches("^(0412|0414|0416|0422|0424|0426)\\d{7}$")) {
            throw new DatosInvalidosException("El formato del teléfono es incorrecto. Debe empezar con un prefijo válido (ej. 0412, 0424) seguido de 7 dígitos, sin guiones.");
        }

        if (!cedula.trim().matches("^\\d{7,8}$") || Double.parseDouble(cedula) <= 0) {
            throw new DatosInvalidosException("La cédula es inválida. Debe contener entre 7 y 8 dígitos numéricos.");
        }

        if (!referencia.trim().matches("^\\d{4}$")) {
            throw new DatosInvalidosException("El número de referencia es incorrecto. Debe ingresar exactamente los últimos 4 dígitos numéricos.");
        }

        sincronizarSaldoUsuario(usuario);
        double saldoAnterior = usuario.getSaldo();
        double nuevoSaldo = saldoAnterior + monto;

        boolean exitoBD = registrarMovimientoMonedero(
            usuario.getCedula(), "RECARGA", monto, saldoAnterior, nuevoSaldo,
            "Recarga Pago Móvil (" + banco + " - Ref: " + referencia.trim() + ")",
            banco, telefono.trim(), cedula.trim(), referencia.trim()
        );

        if (exitoBD) {
            usuario.setSaldo(nuevoSaldo);
            return true;
        } else {
            throw new DatosInvalidosException("Error interno al guardar la recarga en la base de datos.");
        }
    }

    
    private static boolean registrarMovimientoMonedero(String cedulaUsuario, String tipo, double monto,
            double saldoAnt, double saldoPost, String concepto,
            String banco, String telefono, String cedulaOrigen, String referencia) {

        String sqlUpdateMonedero = "UPDATE monederos SET saldo_actual = ?, ultima_actualizacion = CURRENT_TIMESTAMP WHERE id_monedero = ?";
        String sqlInsertTrans = "INSERT INTO transacciones_monedero "
                + "(id_monedero, tipo_movimiento, monto, saldo_anterior, saldo_posterior, fecha_hora, concepto, referencia_bancaria, banco_origen, telefono_origen, cedula_origen) "
                + "VALUES (?, ?, ?, ?, ?, datetime('now', 'localtime'), ?, ?, ?, ?, ?)";

        try (Connection conn = ConexionBD.conectar()) {
            conn.setAutoCommit(false);
            try {
                int idUsuario = obtenerIdUsuarioPorCedula(conn, cedulaUsuario);
                if (idUsuario == -1) {
                    conn.rollback();
                    return false;
                }

                int idMonedero = obtenerOCrearIdMonedero(conn, idUsuario);
                if (idMonedero == -1) {
                    conn.rollback();
                    return false;
                }

                try (PreparedStatement pstmtMon = conn.prepareStatement(sqlUpdateMonedero);
                     PreparedStatement pstmtTr = conn.prepareStatement(sqlInsertTrans)) {

                    
                    pstmtMon.setDouble(1, saldoPost);
                    pstmtMon.setInt(2, idMonedero);
                    pstmtMon.executeUpdate();

                    
                    pstmtTr.setInt(1, idMonedero);
                    pstmtTr.setString(2, tipo);
                    pstmtTr.setDouble(3, monto);
                    pstmtTr.setDouble(4, saldoAnt);
                    pstmtTr.setDouble(5, saldoPost);
                    pstmtTr.setString(6, concepto);
                    pstmtTr.setString(7, referencia);
                    pstmtTr.setString(8, banco);
                    pstmtTr.setString(9, telefono);
                    pstmtTr.setString(10, cedulaOrigen);
                    pstmtTr.executeUpdate();

                    conn.commit();
                    return true;
                }
            } catch (SQLException ex) {
                conn.rollback();
                System.out.println("Rollback en transacción de monedero: " + ex.getMessage());
                return false;
            }
        } catch (SQLException e) {
            System.out.println("Error de conexión en monedero: " + e.getMessage());
            return false;
        }
    }

    

    public static String registrarReservaBD(int idItinerario, String cedulaPasajero, String destino, String horaSalida, double montoPagado) {
        String codigoReserva = "UCV-" + (int) (Math.random() * 9000 + 1000);
        String sqlRestarCupo = "UPDATE itinerarios SET cupos_disponibles = cupos_disponibles - 1 WHERE id_itinerario = ? AND cupos_disponibles > 0";
        String sqlInsertReserva = "INSERT INTO reservas (id_itinerario, id_usuario, codigo_ticket, monto_pagado, estado) VALUES (?, ?, ?, ?, 'Activa')";

        try (Connection conn = ConexionBD.conectar()) {
            conn.setAutoCommit(false);
            try {
                int idUsuario = obtenerIdUsuarioPorCedula(conn, cedulaPasajero);
                if (idUsuario == -1) {
                    conn.rollback();
                    return null;
                }

                try (PreparedStatement pstmtCupo = conn.prepareStatement(sqlRestarCupo);
                     PreparedStatement pstmtRes = conn.prepareStatement(sqlInsertReserva)) {

                    pstmtCupo.setInt(1, idItinerario);
                    if (pstmtCupo.executeUpdate() == 0) {
                        conn.rollback();
                        return null;
                    }

                    pstmtRes.setInt(1, idItinerario);
                    pstmtRes.setInt(2, idUsuario);
                    pstmtRes.setString(3, codigoReserva);
                    pstmtRes.setDouble(4, montoPagado);
                    pstmtRes.executeUpdate();

                    conn.commit();
                    return codigoReserva;
                }
            } catch (SQLException ex) {
                conn.rollback();
                System.out.println("Error guardando reserva: " + ex.getMessage());
                return null;
            }
        } catch (SQLException e) {
            System.out.println("Error BD en reserva: " + e.getMessage());
            return null;
        }
    }

    public static boolean cancelarReservaBD(String codigoTicket) {
        String sqlSelect = "SELECT id_itinerario FROM reservas WHERE codigo_ticket = ? AND estado = 'Activa'";
        String sqlUpdateReserva = "UPDATE reservas SET estado = 'Cancelada' WHERE codigo_ticket = ?";
        String sqlSumarCupo = "UPDATE itinerarios SET cupos_disponibles = cupos_disponibles + 1 WHERE id_itinerario = ?";

        try (Connection conn = ConexionBD.conectar()) {
            conn.setAutoCommit(false);
            int idItinerario = -1;

            try (PreparedStatement pstmtSel = conn.prepareStatement(sqlSelect)) {
                pstmtSel.setString(1, codigoTicket);
                try (ResultSet rs = pstmtSel.executeQuery()) {
                    if (rs.next()) {
                        idItinerario = rs.getInt("id_itinerario");
                    } else {
                        return false;
                    }
                }
            }

            try (PreparedStatement pstmtUpdRes = conn.prepareStatement(sqlUpdateReserva);
                 PreparedStatement pstmtCupo = conn.prepareStatement(sqlSumarCupo)) {

                pstmtUpdRes.setString(1, codigoTicket);
                pstmtUpdRes.executeUpdate();

                pstmtCupo.setInt(1, idItinerario);
                pstmtCupo.executeUpdate();

                conn.commit();
                return true;
            } catch (SQLException ex) {
                conn.rollback();
                return false;
            }
        } catch (SQLException e) {
            System.out.println("Error al cancelar reserva en BD: " + e.getMessage());
            return false;
        }
    }

    
    public static Vector<Object[]> obtenerReservasActivasPorPasajero(String cedulaPasajero) {
        Vector<Object[]> lista = new Vector<>();
        String sql = "SELECT r.codigo_ticket, "
                   + "(tr.nombre || ' - ' || ru.nombre_ruta) AS destino, "
                   + "(it.fecha || ' ' || it.hora_salida) AS hora_salida, "
                   + "r.estado, "
                   + "r.monto_pagado "
                   + "FROM reservas r "
                   + "INNER JOIN usuarios u ON r.id_usuario = u.id_usuario "
                   + "INNER JOIN itinerarios it ON r.id_itinerario = it.id_itinerario "
                   + "INNER JOIN rutas ru ON it.id_ruta = ru.id_ruta "
                   + "INNER JOIN tipos_ruta tr ON ru.id_tipo_ruta = tr.id_tipo "
                   + "WHERE u.cedula = ? AND r.estado = 'Activa'";

        try (Connection conn = ConexionBD.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, cedulaPasajero);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    lista.add(new Object[]{
                        rs.getString("codigo_ticket"),
                        rs.getString("destino"),
                        rs.getString("hora_salida"),
                        rs.getString("estado"),
                        rs.getDouble("monto_pagado")
                    });
                }
            }
        } catch (SQLException e) {
            System.out.println("Error cargando reservas del pasajero: " + e.getMessage());
        }
        return lista;
    }
}