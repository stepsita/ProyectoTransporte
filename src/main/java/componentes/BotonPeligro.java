package componentes;
import javax.swing.JButton;
import java.awt.Font;
import java.awt.Color;
import java.awt.Cursor;

public class BotonPeligro extends JButton {
    public BotonPeligro(String texto) {
        super(texto);
        setFont(new Font("Segoe UI", Font.BOLD, 12));
        setBackground(Paleta.ROJO_PELIGRO);
        setForeground(Color.WHITE);
        setFocusPainted(false);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
    }
}