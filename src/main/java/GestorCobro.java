public class GestorCobro{

    public static class SaldoInsuficienteException extends Exception{
        public SaldoInsuficienteException(String mensaje){
            super(mensaje);
        }
    }

    public static double calcularTarifaSolidaria(double tarifaBase, int idTipo){
        if (tarifaBase <= 0){
            return 0.0;
        }

        switch (idTipo){
            case 1:
                return tarifaBase * 0.20;
            case 2:
                return tarifaBase * 0.50;
            case 3:
            default:
                return tarifaBase;
        }
    }

    public static boolean procesarPago(Usuario usuario, double tarifaBase) throws SaldoInsuficienteException{
        if (usuario == null){
            return false;
        }

        double tarifaFinal = calcularTarifaSolidaria(tarifaBase, usuario.getIdTipo());
        if (usuario.getSaldo() < tarifaFinal){
            throw new SaldoInsuficienteException(
                String.format("Saldo insuficiente (Saldo: %.2f Bs., Requerido: %.2f Bs.). Recargue su monedero digital.", 
                              usuario.getSaldo(), tarifaFinal)
            );
        }
        usuario.setSaldo(usuario.getSaldo() - tarifaFinal);
        return true;
    }

    public static boolean recargarSaldo(Usuario usuario, String banco, String telefono, String cedula, String referencia, double monto){
        if (usuario == null || monto <= 0){
            return false;
        }

        if (banco == null || banco.trim().isEmpty() || telefono == null || telefono.trim().isEmpty() || cedula == null || cedula.trim().isEmpty() || referencia == null || referencia.trim().isEmpty()){
            return false;
        }

        if (!telefono.trim().matches("^(0412|0414|0416|0422|0424|0426)\\d{7}$")){
            return false;
        }

        if (!cedula.trim().matches("^\\d{7,8}$")){
            return false;
        }

        if (!referencia.trim().matches("^\\d{4}$")){
            return false;
        }

        usuario.setSaldo(usuario.getSaldo() + monto);
        return true;
    }
}
