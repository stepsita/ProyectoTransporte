package componentes;
import javax.swing.JButton;
import java.awt.Font;
import java.awt.Color;
import java.awt.Cursor;

public class BotonPrimario extends JButton {
    public BotonPrimario(String texto) {
        super(texto);
        setFont(new Font("Segoe UI", Font.BOLD, 13));
        setBackground(Paleta.AZUL_OSCURO);
        setForeground(Color.WHITE);
        setFocusPainted(false);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
    }
}