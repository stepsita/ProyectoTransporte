package componentes;
import javax.swing.JTable;
import javax.swing.table.TableModel;
import javax.swing.table.JTableHeader;
import java.awt.Font;
import java.awt.Color;

public class TablaEstilizada extends JTable {
    public TablaEstilizada(TableModel modelo) {
        super(modelo);
        setFont(new Font("Segoe UI", Font.PLAIN, 13));
        setRowHeight(30);
        setSelectionBackground(Paleta.ARENA_FONDO);
        setSelectionForeground(Paleta.AZUL_OSCURO);
        setShowVerticalLines(false);
        setGridColor(new Color(230, 230, 230));

        JTableHeader encabezado = getTableHeader();
        encabezado.setFont(new Font("Segoe UI", Font.BOLD, 13));
        encabezado.setBackground(Paleta.AZUL_OSCURO);
        encabezado.setForeground(Color.WHITE);
        encabezado.setReorderingAllowed(false);
    }
}