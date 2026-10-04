package componentes;
import javax.swing.JLabel;
import java.awt.Font;

public class FormularioLabel extends JLabel {
    public FormularioLabel(String texto) {
        super(texto);
        setFont(new Font("Segoe UI", Font.BOLD, 12));
        setForeground(Paleta.AZUL_OSCURO);
    }
}