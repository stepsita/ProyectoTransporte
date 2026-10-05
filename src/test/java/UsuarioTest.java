import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
public class UsuarioTest {
    public boolean validacionCorreo(String correo){
        if (correo == null) {
        return false;
    }
    
    return correo.matches("^[\\w-\\.]+@[\\w-\\.]+\\.[a-zA-Z]{2,63}$");
    }

    @Test
    void testCorreoValido(){
        String prueba = "sergio.silva@ucv.ve";
        boolean resultado = validacionCorreo(prueba);
        assertTrue(resultado, "El correo sigue el formayto y es aceptado");
    }

    @Test
    void testCorreoInvalido(){
        String prueba = "sergio.silva@gmail";
        boolean resultado = validacionCorreo(prueba);
        assertFalse(resultado, "El correo no tiene un dominio correcto y es rechazado");
    }

    @Test
    void testCorreoSinArroba(){
        String prueba = "sergio.silvaucv.ve";
        boolean resultado = validacionCorreo(prueba);
        assertFalse(resultado, "El correo no tiene @, por lo que es rechazado");
    }

    @Test
    void testCorreoNulo(){
        String correoNulo = null;
        boolean resultado = validacionCorreo(correoNulo);
        assertFalse(resultado, "Un correo nulo debe ser rechazado");
    }

    public boolean validacionClave(String clave){
        if (clave == null || clave.length() < 6 || clave.length() > 16){
            return false;
        }
        return true;
    }

    @Test
    void testPasswordValida(){
        String prueba = "password123_";
        boolean resultado = validacionClave(prueba);
        assertTrue(resultado, "La password cumple con el rango, es aceptada");
    }

    @Test
    void testPasswordMinRange(){
        String prueba = "holis";
        boolean resultado = validacionClave(prueba);
        assertFalse(resultado, "La password debe tener al menos 6 caracteres, es rechazada");
    }

    @Test
    void testPasswordMaxRange(){
        String prueba = "holiscomo_estaist0d0schavales";
        boolean resultado = validacionClave(prueba);
        assertFalse(resultado, "La password debe tener como maximo 16 caracteres, es rechazada");
    }

    @Test
    void testClaveNula(){
        String claveNula = null;
        boolean resultado = validacionClave(claveNula);
        assertFalse(resultado, "Una clave nula debe ser rechazada");
    }
    
    public boolean validacionCedula(String cedula){
        if (cedula == null || cedula.trim().isEmpty()){
            return false;
        }
        String c = cedula.trim();
        if (!c.contains(".")){
            return c.matches("\\d{6,9}");
        }
        return false;
    }

    @Test
    void testCedulaValida(){
        String cadena = "31893338";
        boolean resultado = validacionCedula(cadena);
        assertTrue(resultado, "Cedula valida, es aceptada");
    }

    @Test
    void testCedulaInvalida(){
        String cadena = "3189.";
        boolean resultado = validacionCedula(cadena);
        assertFalse(resultado, "Cedula invalida, es rechazada");
    }

    @Test
    void testCedulaMaxRange(){
        String cadena = "31893338888";
        boolean resultado = validacionCedula(cadena);
        assertFalse(resultado, "Cedula excede los limites, es rechazada");
    }

    public boolean validacionTextoNombre(String texto){
        if (texto == null || texto.trim().isEmpty()){
            return false;
        }
        return texto.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+");
    }

    @Test
    void testNombre(){
        String cadena = "Sergio Andrés";
        boolean resultado = validacionTextoNombre(cadena);
        assertTrue(resultado, "Nombre valido, es aceptado");
    }

    @Test
    void testNombreInvalido(){
        String cadena = "Sergio Andrés111222";
        boolean resultado = validacionTextoNombre(cadena);
        assertFalse(resultado, "Nombre invalido, es rechazado");
    }

    @Test
    void testNombreEspacios(){
        String cadena = "   ";
        boolean resultado = validacionTextoNombre(cadena);
        assertFalse(resultado, "Nombre invalido, es rechazado");
    }

    public boolean validacionCapacidadUnidad(int capacidad){
        return capacidad >= 10 && capacidad <= 80;
    }

    public boolean validacionPlacaUnidad(String placa){
        if (placa == null || placa.trim().isEmpty()) return false;
        return placa.length() == 7;
    }

    @Test
    void testCapacidadFlotaValida(){
        int num = 32;
        boolean resultado = validacionCapacidadUnidad(num);
        assertTrue(resultado, "Capacidad dentro del rango (10-80)");
    }

    @Test
    void testCapacidadFlotaInvalida(){
        int num = 100;
        boolean resultado = validacionCapacidadUnidad(num);
        assertFalse(resultado, "Capacidad mayor a 80 rechazada");
    }

    @Test
    void testPlacaValida(){
        String cadena = "UCV-102";
        boolean resultado = validacionPlacaUnidad(cadena);
        assertTrue(resultado, "Placa válida aceptada");
    }

    @Test
    void testPlacaNula(){
        String cadena = null;
        boolean resultado = validacionPlacaUnidad(cadena);
        assertFalse(resultado, "Placa nula rechazada");
    }

    @Test
    void testPlacaInvalida(){
        String cadena = "UCV-";
        boolean resultado = validacionPlacaUnidad(cadena);
        assertFalse(resultado, "Placa invalida rechazada");
    }

    public boolean validacionHoraSalida(String hora){
        if (hora == null || hora.trim().isEmpty()) return false;
        return hora.trim().matches("^(0?[0-9]|1[0-9]|2[0-3]):[0-5][0-9]$");
    }

    @Test
    void testHoraSalidaValida(){
        String cadena = "16:15";
        boolean resultado = validacionHoraSalida(cadena);
        assertTrue(resultado, "Hora en formato correcto aceptada");
    }

    @Test
    void testHoraSalidaInvalida(){
        String cadena = "25:99 AM";
        boolean resultado = validacionHoraSalida(cadena);
        assertFalse(resultado, "Hora fuera de formato rechazada");
    }
}