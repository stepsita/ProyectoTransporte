import javax.swing.*;
import com.formdev.flatlaf.FlatLightLaf; // Importamos el tema claro moderno

public class App {
    public static void main(String[] args) {
        ConexionBD.inicializarBaseDeDatos();
        try {
            // Activamos el Look and Feel moderno de FlatLaf
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception e) {
            System.err.println("No se pudo inicializar el tema moderno.");
        }

        // Ejecuta la interfaz gráfica dentro del hilo seguro de Swing
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                InicioSesion login = new InicioSesion();
                login.setVisible(true); // Hace visible el formulario
            }
        });
    }
}