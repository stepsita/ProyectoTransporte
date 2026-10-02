import javax.swing.*;
import java.awt.*;

public class Conductor extends JFrame {

    private JLabel lblRutaAsignada;
    private JLabel lblUnidadInfo;
    private JLabel lblEstadoActual;
    
    private final Color colorAzulOscuro = new Color(0x11, 0x18, 0x44);
    private final Color colorAzulMedio = new Color(0x4B, 0x56, 0x94);
    private final Color colorArenaFondo = new Color(0xEA, 0xE0, 0xCF);

    public Conductor() {
        setTitle("Sistema de Transporte UCV - Panel de Conductor");
        setSize(650, 480); 
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panelBase = new JPanel(new BorderLayout(15, 15));
        panelBase.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panelBase.setBackground(colorArenaFondo);

        JPanel panelSuperior = new JPanel();
        panelSuperior.setLayout(new BoxLayout(panelSuperior, BoxLayout.Y_AXIS));
        panelSuperior.setBackground(colorArenaFondo);

        JPanel panelNav = new JPanel(new BorderLayout());
        panelNav.setBackground(colorArenaFondo);
        
        JLabel lblBienvenida = new JLabel("Bienvenido, Conductor UCV");
        lblBienvenida.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblBienvenida.setForeground(colorAzulOscuro);
        panelNav.add(lblBienvenida, BorderLayout.WEST);

        JButton btnCerrarSesion = new JButton("Cerrar Sesion");
        btnCerrarSesion.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnCerrarSesion.setBackground(new Color(220, 53, 69)); 
        btnCerrarSesion.setForeground(Color.WHITE);
        btnCerrarSesion.setFocusPainted(false);
        panelNav.add(btnCerrarSesion, BorderLayout.EAST);
        
        panelNav.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        panelSuperior.add(panelNav);

        JPanel panelInformacion = new JPanel(new GridLayout(3, 1, 8, 8));
        panelInformacion.setBackground(Color.WHITE);
        panelInformacion.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(colorAzulMedio, 1),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        lblRutaAsignada = new JLabel("Ruta Asignada: Extraurbana - Guarenas/Guatire (Turno Tarde)");
        lblRutaAsignada.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblRutaAsignada.setForeground(colorAzulOscuro);

        lblUnidadInfo = new JLabel("Unidad Operativa: Encava Autobus [Placa: UCV-78A]");
        lblUnidadInfo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblUnidadInfo.setForeground(colorAzulMedio);

        lblEstadoActual = new JLabel("ESTADO ACTUAL: PROGRAMADO", SwingConstants.LEFT);
        lblEstadoActual.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblEstadoActual.setForeground(new Color(13, 110, 253)); 

        panelInformacion.add(lblRutaAsignada);
        panelInformacion.add(lblUnidadInfo);
        panelInformacion.add(lblEstadoActual);
        
        panelSuperior.add(panelInformacion);
        panelBase.add(panelSuperior, BorderLayout.NORTH); 

        JPanel panelControles = new JPanel(new GridLayout(2, 2, 15, 15));
        panelControles.setBackground(colorArenaFondo);

        JButton btnParada = new JButton("Situado en la Parada (En Anden)");
        JButton btnTrayecto = new JButton("Comenzar Trayecto");
        JButton btnLlegando = new JButton("Aproximando al Destino");
        JButton btnFinalizar = new JButton("Finalizar viaje");

        estilarBotonControl(btnParada);
        estilarBotonControl(btnTrayecto);
        estilarBotonControl(btnLlegando);
        estilarBotonControl(btnFinalizar);
        btnFinalizar.setBackground(new Color(220, 53, 69)); 

        panelControles.add(btnParada);
        panelControles.add(btnTrayecto);
        panelControles.add(btnLlegando);
        panelControles.add(btnFinalizar);
        panelBase.add(panelControles, BorderLayout.CENTER);
        add(panelBase);

        btnCerrarSesion.addActionListener(e -> {
            dispose(); 
            new InicioSesion().setVisible(true); 
        });

        btnParada.addActionListener(e -> lblEstadoActual.setText("ESTADO ACTUAL: EN PARADA (ABORDAN PASAJEROS)"));
        btnTrayecto.addActionListener(e -> lblEstadoActual.setText("ESTADO ACTUAL: EN TRAYECTO (EN MARCHA)"));
        btnLlegando.addActionListener(e -> lblEstadoActual.setText("ESTADO ACTUAL: LLEGANDO AL DESTINO"));
        btnFinalizar.addActionListener(e -> {
            lblEstadoActual.setText("ESTADO ACTUAL: FINALIZADO (RUTA COMPLETADA)");
            JOptionPane.showMessageDialog(this, "Servicio concluido. Registro guardado en el reporte del Administrador.", "Fin de Jornada", JOptionPane.INFORMATION_MESSAGE);
        });
    }

    private void estilarBotonControl(JButton boton) {
        boton.setFont(new Font("Segoe UI", Font.BOLD, 13));
        boton.setBackground(colorAzulOscuro);
        boton.setForeground(Color.WHITE);
        boton.setFocusPainted(false);
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }
}