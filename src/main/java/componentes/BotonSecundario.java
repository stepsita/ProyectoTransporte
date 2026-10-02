package componentes;
import javax.swing.JButton;
import javax.swing.BorderFactory;
import java.awt.Font;
import java.awt.Color;
import java.awt.Cursor;

public class BotonSecundario extends JButton {
    public BotonSecundario(String texto) {
        super(texto);
        setFont(new Font("Segoe UI", Font.BOLD, 13));
        setBackground(Color.WHITE);
        setForeground(Paleta.AZUL_OSCURO);
        setBorder(BorderFactory.createLineBorder(Paleta.AZUL_GRIS, 1));
        setFocusPainted(false);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
    }
}