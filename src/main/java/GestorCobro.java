public class GestorCobro{

    public static class SaldoInsuficienteException extends Exception{
        public SaldoInsuficienteException(String mensaje){
            super(mensaje);
        }
    }

    public static class DatosInvalidosException extends Exception{
        public DatosInvalidosException(String mensaje) {
            super(mensaje);
        }
    }

    private static double porcentajeEstudiante = 20.0;
    private static double porcentajeEmpleado = 50.0;
    private static double tarifaBaseGlobal = 80.0;
    public static void actualizarConfiguracionTarifas(double tarifaBase, double pctEstudiante, double pctEmpleado) {
        tarifaBaseGlobal = tarifaBase;
        porcentajeEstudiante = pctEstudiante;
        porcentajeEmpleado = pctEmpleado;
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
        double tarifaFinal = calcularTarifaSolidaria(tarifaBase, usuario.getIdTipo());
        if (usuario.getSaldo() < tarifaFinal){
            throw new SaldoInsuficienteException(String.format("Saldo insuficiente (Saldo: %.2f Bs., Requerido: %.2f Bs.).", usuario.getSaldo(), tarifaFinal));
        }
        usuario.setSaldo(usuario.getSaldo() - tarifaFinal);
        return true;
    }

    public static boolean recargarSaldo(Usuario usuario, String banco, String telefono, String cedula, String referencia, double monto) throws DatosInvalidosException {
        if (usuario == null || monto <= 0) {
            throw new DatosInvalidosException("El monto a recargar debe ser mayor a 0.");
        }

        if (banco == null || banco.trim().isEmpty() || telefono == null || telefono.trim().isEmpty() || cedula == null || cedula.trim().isEmpty() || referencia == null || referencia.trim().isEmpty()) {
            throw new DatosInvalidosException("Por favor, complete todos los campos obligatorios del Pago Móvil.");
        }

        if (!telefono.trim().matches("^(0412|0414|0416|0422|0424|0426)\\d{7}$")){
            throw new DatosInvalidosException("El formato del teléfono es incorrecto. Debe empezar con un prefijo válido (ej. 0412, 0424) seguido de 7 dígitos, sin guiones.");
        }

        if (!cedula.trim().matches("^\\d{7,8}$") || Double.parseDouble(cedula) <= 0){
            throw new DatosInvalidosException("La cédula es inválida. Debe contener entre 7 y 8 dígitos numéricos.");
        }

        if (!referencia.trim().matches("^\\d{4}$")){
            throw new DatosInvalidosException("El número de referencia es incorrecto. Debe ingresar exactamente los últimos 4 dígitos numéricos.");
        }
        usuario.setSaldo(usuario.getSaldo() + monto);
        return true;
    }
}
