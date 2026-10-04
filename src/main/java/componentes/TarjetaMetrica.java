package componentes;

import javax.swing.*;
import java.awt.*;

public class TarjetaMetrica extends JPanel {
    
    public TarjetaMetrica(String titulo, String valor, String subtitulo) {
        super(new GridLayout(3, 1, 2, 2));
        setBackground(Paleta.ARENA_FONDO);
        setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Paleta.AZUL_GRIS, 1),
            BorderFactory.createEmptyBorder(12, 15, 12, 15)
        ));

        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblTitulo.setForeground(Paleta.AZUL_MEDIO);

        JLabel lblValor = new JLabel(valor);
        lblValor.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblValor.setForeground(Paleta.AZUL_OSCURO);

        JLabel lblSub = new JLabel(subtitulo);
        lblSub.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        lblSub.setForeground(Paleta.AZUL_OSCURO);

        add(lblTitulo);
        add(lblValor);
        add(lblSub);
    }
}