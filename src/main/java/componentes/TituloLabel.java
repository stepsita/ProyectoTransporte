package componentes;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.Font;

public class TituloLabel extends JLabel {
    public TituloLabel(String texto) {
        super(texto, SwingConstants.CENTER);
        setFont(new Font("Segoe UI", Font.BOLD, 24));
        setForeground(Paleta.AZUL_OSCURO);
    }
}