package componentes;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.Font;


public class SubtituloLabel extends JLabel {
    public SubtituloLabel(String texto) {
        super(texto, SwingConstants.CENTER);
        setFont(new Font("Segoe UI", Font.PLAIN, 12));
        setForeground(Paleta.AZUL_MEDIO);
    }
}