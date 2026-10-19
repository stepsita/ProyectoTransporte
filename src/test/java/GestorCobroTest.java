import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class GestorCobroTest {
    @Test
    void testCalcularTarifaSolidiariaEstudiante(){
        GestorCobro.actualizarConfiguracionTarifas(80.0, 20.0, 50.0);
        double tarifaBaseServicio = 100.0;
        int idTipoEstudiante = 1;
        double tarifaEsperada = 20.0; 
        double tarifaCalculada = GestorCobro.calcularTarifaSolidaria(tarifaBaseServicio, idTipoEstudiante);
        assertEquals(tarifaEsperada, tarifaCalculada, "El estudiante debe pagar exactamente el 20% del costo base");
    }

    @Test
    void testCalcularTarifaSolidariaEmpleado(){
        GestorCobro.actualizarConfiguracionTarifas(80.0, 20.0, 50.0);
        double tarifaBaseServicio = 100.0;
        int idTipoEmpleado = 2;
        double tarifaEsperada = 50.0;
        double tarifaCalculada = GestorCobro.calcularTarifaSolidaria(tarifaBaseServicio, idTipoEmpleado);
        assertEquals(tarifaEsperada, tarifaCalculada, "El empleado debe pagar exactamente el 50% del costo base");
    }

    @Test
    void testCalcularTarifaSolidiariaPublicoGeneral(){
        GestorCobro.actualizarConfiguracionTarifas(80.0, 20.0, 50.0);
        double tarifaBaseServicio = 100.0;
        int idTipoGeneral = 3;
        double tarifaEsperada = 100.0; 
        double tarifaCalculada = GestorCobro.calcularTarifaSolidaria(tarifaBaseServicio, idTipoGeneral);
        assertEquals(tarifaEsperada, tarifaCalculada, "El público general debe pagar la tarifa completa");
    }

    @Test
    void testProcesarPagoExitoso() throws GestorCobro.SaldoInsuficienteException {
        GestorCobro.actualizarConfiguracionTarifas(80.0, 20.0, 50.0);
        Usuario usuarioPrueba = new Usuario(1, "prueba", "test", "31893338", 1, 1);
        usuarioPrueba.setSaldo(50.0);
        double tarifaBase = 100.0;
        double saldoEsperado = 30.0;
        boolean resultado = GestorCobro.procesarPago(usuarioPrueba, tarifaBase);
        assertTrue(resultado, "El pago debe retornar true si hay saldo suficiente");
        assertEquals(saldoEsperado, usuarioPrueba.getSaldo(), "El saldo restante debe ser el saldo inicial menos la tarifa descontada");
    }
    
    @Test
    void testProcesarPagoSaldoInsuficiente(){
        GestorCobro.actualizarConfiguracionTarifas(80.0, 20.0, 50.0);
        Usuario usuarioPrueba = new Usuario(1, "prueba", "test", "31893338", 1, 3);
        usuarioPrueba.setSaldo(10.0);
        double tarifaACobrar = 50.0;
        assertThrows(GestorCobro.SaldoInsuficienteException.class, () -> {
            GestorCobro.procesarPago(usuarioPrueba, tarifaACobrar);
        }, "Debe abortar la transacción y lanzar excepción si el saldo es menor a la tarifa calculada");
    }

    @Test
    void testRecargaExitosaDatosValidos() throws GestorCobro.DatosInvalidosException{
        Usuario usuarioPrueba = new Usuario(1, "prueba", "test", "31893338", 1, 3);
        usuarioPrueba.setSaldo(20.0); 
        String banco = "Banesco";
        String telefono = "04121234567";
        String cedula = "31893338";
        String referencia = "1234";
        double montoRecarga = 50.0;
        double saldoEsperado = 70.0;

        boolean resultado = GestorCobro.recargarSaldo(usuarioPrueba, banco, telefono, cedula, referencia, montoRecarga);
        assertTrue(resultado, "La función debe retornar true cuando los datos son válidos");
        assertEquals(saldoEsperado, usuarioPrueba.getSaldo(), "El monedero debe sumar el monto recargado al saldo previo");
    }

    @Test
    void testRecargaFallaTelefonoInvalido(){
        Usuario usuarioPrueba = new Usuario(1, "prueba", "test", "31893338", 1, 3);
        String banco = "Mercantil";
        String telefonoMalo = "04991234567";
        String cedula = "31893338";
        String referencia = "1234";
        double montoRecarga = 50.0;

        assertThrows(GestorCobro.DatosInvalidosException.class, () -> {
            GestorCobro.recargarSaldo(usuarioPrueba, banco, telefonoMalo, cedula, referencia, montoRecarga);
        }, "Debe lanzar DatosInvalidosException al detectar un prefijo telefónico erróneo");
    }

    @Test
    void testRecargaFallaTelefonoVacio(){
        Usuario usuarioPrueba = new Usuario(1, "prueba", "test", "31893338", 1, 3);
        String bancoVacio = "";
        String telefonoBlanco = "";
        String cedulaNula = null;
        String referencia = "";
        double montoRecarga = 50.0;

        assertThrows(GestorCobro.DatosInvalidosException.class, () -> {
            GestorCobro.recargarSaldo(usuarioPrueba, bancoVacio, telefonoBlanco, cedulaNula, referencia, montoRecarga);
        }, "Debe lanzar DatosInvalidosException si algún campo de texto es nulo o está vacío");
    }

    @Test
    void testRecargaFallaRangoExcedido(){
        Usuario usuarioPrueba = new Usuario(1, "prueba", "test", "31893338", 1, 3);
        String banco = "Mercantil";
        String telefonoMalo = "049912345672332";
        String cedula = "31893338";
        String referencia = "1234";
        double montoRecarga = 50.0;

        assertThrows(GestorCobro.DatosInvalidosException.class, () -> {
            GestorCobro.recargarSaldo(usuarioPrueba, banco, telefonoMalo, cedula, referencia, montoRecarga);
        }, "Debe lanzar DatosInvalidosException al detectar cantidad inválida de dígitos en número telefónico");
    }

    @Test
    void testRecargaFallaTelefonoInexistente(){
        Usuario usuarioPrueba = new Usuario(1, "prueba", "test", "31893338", 1, 3);
        String banco = "Mercantil";
        String telefonoMalo = "0499";
        String cedula = "31893338";
        String referencia = "1234";
        double montoRecarga = 50.0;

        assertThrows(GestorCobro.DatosInvalidosException.class, () -> {
            GestorCobro.recargarSaldo(usuarioPrueba, banco, telefonoMalo, cedula, referencia, montoRecarga);
        }, "Debe lanzar DatosInvalidosException al detectar un formato telefónico no existente");
    }

    @Test
    void testRecargaFallaReferenciaIncorrecta(){
        Usuario usuarioPrueba = new Usuario(1, "prueba", "test", "31893338", 1, 3);
        String banco = "Mercantil";
        String telefonoMalo = "04126056730";
        String cedula = "31893338";
        String referencia = "12";
        double montoRecarga = 50.0;

        assertThrows(GestorCobro.DatosInvalidosException.class, () -> {
            GestorCobro.recargarSaldo(usuarioPrueba, banco, telefonoMalo, cedula, referencia, montoRecarga);
        }, "Debe lanzar DatosInvalidosException al detectar una referencia con una cantidad de dígitos distinta a 4");
    }

    @Test
    void testRecargaFallaReferenciaExcede(){
        Usuario usuarioPrueba = new Usuario(1, "prueba", "test", "31893338", 1, 3);
        String banco = "Mercantil";
        String telefonoMalo = "04126056730";
        String cedula = "31893338";
        String referencia = "12111";
        double montoRecarga = 50.0;

        assertThrows(GestorCobro.DatosInvalidosException.class, () -> {
            GestorCobro.recargarSaldo(usuarioPrueba, banco, telefonoMalo, cedula, referencia, montoRecarga);
        }, "Debe lanzar DatosInvalidosException al detectar una referencia con una cantidad de dígitos distinta a 4");
    }

    @Test
    void testRecargaFallaMontoNulo(){
        Usuario usuarioPrueba = new Usuario(1, "prueba", "test", "31893338", 1, 3);
        String banco = "Mercantil";
        String telefonoMalo = "04126056730";
        String cedula = "31893338";
        String referencia = "1211";
        double montoRecarga = 0.00;

        assertThrows(GestorCobro.DatosInvalidosException.class, () -> {
            GestorCobro.recargarSaldo(usuarioPrueba, banco, telefonoMalo, cedula, referencia, montoRecarga);
        }, "Debe lanzar DatosInvalidosException al detectar un monto igual a 0");
    }

    @Test
    void testRecargaFallaMontoNegativo(){
        Usuario usuarioPrueba = new Usuario(1, "prueba", "test", "31893338", 1, 3);
        String banco = "Mercantil";
        String telefonoMalo = "04126056730";
        String cedula = "31893338";
        String referencia = "1211";
        double montoRecarga = -0.50;

        assertThrows(GestorCobro.DatosInvalidosException.class, () -> {
            GestorCobro.recargarSaldo(usuarioPrueba, banco, telefonoMalo, cedula, referencia, montoRecarga);
        }, "Debe lanzar DatosInvalidosException al detectar un monto menor a 0");
    }

    @Test
    void testRecargaFallaCedulaInvalida(){
        Usuario usuarioPrueba = new Usuario(1, "prueba", "test", "31893338", 1, 3);
        String banco = "Mercantil";
        String telefonoMalo = "04126056730";
        String cedula = "-31893338";
        String referencia = "1211";
        double montoRecarga = 50.00;

        assertThrows(GestorCobro.DatosInvalidosException.class, () -> {
            GestorCobro.recargarSaldo(usuarioPrueba, banco, telefonoMalo, cedula, referencia, montoRecarga);
        }, "Debe lanzar DatosInvalidosException al detectar una cedula negativa");
    }

    @Test
    void testRecargaFallaCedulaPeque(){
        Usuario usuarioPrueba = new Usuario(1, "prueba", "test", "31893338", 1, 3);
        String banco = "Mercantil";
        String telefonoMalo = "04126056730";
        String cedula = "3189";
        String referencia = "1211";
        double montoRecarga = 50.00;

        assertThrows(GestorCobro.DatosInvalidosException.class, () -> {
            GestorCobro.recargarSaldo(usuarioPrueba, banco, telefonoMalo, cedula, referencia, montoRecarga);
        }, "Debe lanzar DatosInvalidosException al detectar una cedula menor a 7 digitos");
    }

    @Test
    void testRecargaFallaCedulaGrande(){
        Usuario usuarioPrueba = new Usuario(1, "prueba", "test", "31893338", 1, 3);
        String banco = "Mercantil";
        String telefonoMalo = "04126056730";
        String cedula = "318933381";
        String referencia = "1211";
        double montoRecarga = 50.00;

        assertThrows(GestorCobro.DatosInvalidosException.class, () -> {
            GestorCobro.recargarSaldo(usuarioPrueba, banco, telefonoMalo, cedula, referencia, montoRecarga);
        }, "Debe lanzar DatosInvalidosException al detectar una cedula mayor a 8 digitos");
    }
}