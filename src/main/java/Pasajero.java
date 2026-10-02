import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

import componentes.BotonPeligro;
import componentes.BotonPrimario;
import componentes.Paleta;
import componentes.TablaEstilizada;

public class Pasajero extends JFrame {

    private JTabbedPane contenedorPestañas;
    private TablaEstilizada tablaRutasDisponibles;
    private DefaultTableModel modeloTablaRutas;
    private TablaEstilizada tablaViajesReservados;
    private DefaultTableModel modeloTablaReservas;
    
    // Usuario en sesión
    Usuario usuario = SesionActiva.getUsuario();

    public Pasajero() {
        setTitle("Sistema de Transporte UCV - Panel de Pasajero");
        setSize(900, 580);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); 

        JPanel panelBase = new JPanel(new BorderLayout(15, 15));
        panelBase.setBackground(Paleta.ARENA_FONDO);
        panelBase.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel panelNav = new JPanel(new BorderLayout());
        panelNav.setOpaque(false);
        
        // Bienvenida usando la Paleta
        JLabel lblBienvenida = new JLabel("Bienvenido, " + usuario.getNombre() + " " + usuario.getApellido());
        lblBienvenida.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblBienvenida.setForeground(Paleta.AZUL_OSCURO);
        panelNav.add(lblBienvenida, BorderLayout.WEST);

        BotonPeligro btnCerrarSesion = new BotonPeligro("Cerrar Sesion");
        panelNav.add(btnCerrarSesion, BorderLayout.EAST);
        panelBase.add(panelNav, BorderLayout.NORTH);

        btnCerrarSesion.addActionListener(e -> {
            SesionActiva.cerrarSesion();
            dispose(); 
            new InicioSesion().setVisible(true);
        });

        contenedorPestañas = new JTabbedPane();
        contenedorPestañas.setFont(new Font("Segoe UI", Font.BOLD, 13));
        contenedorPestañas.setBackground(Color.WHITE);
        contenedorPestañas.setForeground(Paleta.AZUL_OSCURO);

        contenedorPestañas.addTab("Rutas Disponibles", crearPanelRutas());
        contenedorPestañas.addTab("Mis Viajes Reservados", crearPanelReservas());
        panelBase.add(contenedorPestañas, BorderLayout.CENTER);
        add(panelBase);
    }

    private JPanel crearPanelRutas() {
        JPanel panelRutas = new JPanel(new BorderLayout(20, 20));
        panelRutas.setBackground(Color.WHITE);
        panelRutas.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel panelFiltros = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        panelFiltros.setBackground(Color.WHITE);
        
        JLabel lblFiltrar = new JLabel("Filtrar por:");
        lblFiltrar.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblFiltrar.setForeground(Paleta.AZUL_OSCURO);
        panelFiltros.add(lblFiltrar);

        JButton btnFiltroTodas = new JButton("Todas");
        JButton btnFiltroUrbanas = new JButton("Urbanas");
        JButton btnFiltroExtraurbanas = new JButton("Extraurbanas");
        
        estilarBotonFiltro(btnFiltroTodas);
        estilarBotonFiltro(btnFiltroUrbanas);
        estilarBotonFiltro(btnFiltroExtraurbanas);
        // BotonSecundario btnFiltroTodas = new BotonSecundario("Todas");
        // BotonSecundario btnFiltroUrbanas = new BotonSecundario("Urbanas");
        // BotonSecundario btnFiltroExtraurbanas = new BotonSecundario("Extraurbanas");
        
        // Sobrescribimos visualmente solo el botón activo
        btnFiltroTodas.setBackground(Paleta.AZUL_MEDIO);
        btnFiltroTodas.setForeground(Color.WHITE);

        panelFiltros.add(btnFiltroTodas);
        panelFiltros.add(btnFiltroUrbanas);
        panelFiltros.add(btnFiltroExtraurbanas);
        panelRutas.add(panelFiltros, BorderLayout.NORTH);

        String[] columnas = {"Destino", "Hora Salida", "Conductor", "Modelo Unidad", "Cupos"};
        modeloTablaRutas = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        modeloTablaRutas.addRow(new Object[]{"Ruta Interna - Plaza Venezuela", "07:30 AM", "Carlos Mendoza", "Yutong Urbano", 25});
        modeloTablaRutas.addRow(new Object[]{"Extraurbana - Guarenas/Guatire", "04:30 PM", "Jose Rodriguez", "Encava Autobus", 40});
        modeloTablaRutas.addRow(new Object[]{"Extraurbana - Los Teques", "05:15 PM", "Luis Gomez", "Encava Autobus", 0});
        modeloTablaRutas.addRow(new Object[]{"Ruta Interna - La Rinconada", "12:00 PM", "Ana Martinez", "Yutong Urbano", 12});

        // La tabla ya viene con su estilo gracias a la herencia
        tablaRutasDisponibles = new TablaEstilizada(modeloTablaRutas);
        
        JScrollPane scrollTabla = new JScrollPane(tablaRutasDisponibles); 
        scrollTabla.setBorder(BorderFactory.createLineBorder(Paleta.ARENA_FONDO, 1));
        panelRutas.add(scrollTabla, BorderLayout.CENTER);

        JPanel panelInferior = new JPanel(new BorderLayout(15, 15));
        panelInferior.setBackground(Color.WHITE);

        JPanel panelEstadoVehiculo = new JPanel(new GridLayout(1, 4, 10, 0));
        panelEstadoVehiculo.setBackground(Color.WHITE);
        panelEstadoVehiculo.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Paleta.ARENA_FONDO, 1), 
            "Estatus en Tiempo Real del Conductor", 0, 0, 
            new Font("Segoe UI", Font.BOLD, 11), Paleta.AZUL_MEDIO
        ));

        JLabel lblEstatus1 = new JLabel("1. Programado", SwingConstants.CENTER);
        JLabel lblEstatus2 = new JLabel("2. En Parada", SwingConstants.CENTER);
        JLabel lblEstatus3 = new JLabel("3. En Trayecto", SwingConstants.CENTER);
        JLabel lblEstatus4 = new JLabel("4. Finalizado", SwingConstants.CENTER);

        estilarEtiquetaEstado(lblEstatus1);
        estilarEtiquetaEstado(lblEstatus2);
        estilarEtiquetaEstado(lblEstatus3);
        estilarEtiquetaEstado(lblEstatus4);

        panelEstadoVehiculo.add(lblEstatus1);
        panelEstadoVehiculo.add(lblEstatus2);
        panelEstadoVehiculo.add(lblEstatus3);
        panelEstadoVehiculo.add(lblEstatus4);
        
        panelInferior.add(panelEstadoVehiculo, BorderLayout.NORTH);

        JTextArea txtDetalleZonas = new JTextArea("Selecciona una ruta de la tabla para consultar detalles e itinerario.");
        txtDetalleZonas.setFont(new Font("Segoe UI", Font.ITALIC, 13));
        txtDetalleZonas.setEditable(false);
        txtDetalleZonas.setBackground(Paleta.ARENA_FONDO);
        txtDetalleZonas.setForeground(Paleta.AZUL_OSCURO);
        txtDetalleZonas.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        panelInferior.add(txtDetalleZonas, BorderLayout.CENTER);

        BotonPrimario btnReservar = new BotonPrimario("Asegurar Puesto");
        btnReservar.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25)); // Ajuste específico
        panelInferior.add(btnReservar, BorderLayout.EAST);
        
        panelRutas.add(panelInferior, BorderLayout.SOUTH);

        tablaRutasDisponibles.getSelectionModel().addListSelectionListener(e -> {
            int filaSeleccionada = tablaRutasDisponibles.getSelectedRow();
            if (filaSeleccionada != -1) {
                String destino = modeloTablaRutas.getValueAt(filaSeleccionada, 0).toString();
                int cupos = (int) modeloTablaRutas.getValueAt(filaSeleccionada, 4);
                
                lblEstatus1.setBackground(new Color(240, 240, 240)); lblEstatus1.setForeground(Color.GRAY);
                lblEstatus2.setBackground(new Color(240, 240, 240)); lblEstatus2.setForeground(Color.GRAY);
                lblEstatus3.setBackground(new Color(240, 240, 240)); lblEstatus3.setForeground(Color.GRAY);
                lblEstatus4.setBackground(new Color(240, 240, 240)); lblEstatus4.setForeground(Color.GRAY);

                if (destino.contains("Guarenas")) {
                    txtDetalleZonas.setText("Zonas de Parada: Terminal Oriente -> Entrada de Guarenas -> Plaza El Maestro.");
                    lblEstatus3.setBackground(Paleta.AZUL_MEDIO);
                    lblEstatus3.setForeground(Color.WHITE);
                } else if (destino.contains("Plaza Venezuela")) {
                    txtDetalleZonas.setText("Zonas de Parada: Via Directa Interna UCV -> Salida Puerta Tamanaquito.");
                    lblEstatus2.setBackground(new Color(40, 167, 69));
                    lblEstatus2.setForeground(Color.WHITE);
                } else if (destino.contains("Los Teques")) {
                    txtDetalleZonas.setText("Zonas de Parada: Paradas oficiales establecidas en el trayecto directo.");
                    lblEstatus4.setBackground(Paleta.AZUL_OSCURO);
                    lblEstatus4.setForeground(Color.WHITE);
                } else {
                    txtDetalleZonas.setText("Zonas de Parada: Paradas oficiales establecidas en el trayecto directo.");
                    lblEstatus1.setBackground(Paleta.AZUL_GRIS);
                    lblEstatus1.setForeground(Color.WHITE);
                }
                
                btnReservar.setEnabled(cupos > 0);
            }
        });

        btnReservar.addActionListener(e -> {
            int filaSeleccionada = tablaRutasDisponibles.getSelectedRow();
            if (filaSeleccionada != -1) { 
                String destino = modelObtenerTexto(filaSeleccionada, 0); 
                String hora = modelObtenerTexto(filaSeleccionada, 1);
                int cuposActuales = (int) modeloTablaRutas.getValueAt(filaSeleccionada, 4);
                modeloTablaRutas.setValueAt(cuposActuales - 1, filaSeleccionada, 4); 
                String codigoReserva = "UCV-" + (int)(Math.random() * 9000 + 1000); 
                modeloTablaReservas.addRow(new Object[]{codigoReserva, destino, hora, "Activa"});

                JOptionPane.showMessageDialog(this, 
                    "--- BOLETO DIGITAL UCV ---\nCodigo: " + codigoReserva + "\nRuta: " + destino + "\nHora: " + hora + "\nEstado: Activa\n---------------------------------\nMuestre este boleto al abordar.", 
                    "Reserva Confirmada", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Por favor, seleccione una ruta de la tabla.", "Aviso", JOptionPane.WARNING_MESSAGE);
            }
        });

        return panelRutas;
    }

    private JPanel crearPanelReservas() {
        JPanel panelReservas = new JPanel(new BorderLayout(20, 20));
        panelReservas.setBackground(Color.WHITE);
        panelReservas.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        String[] columnasReservas = {"Codigo Reserva", "Destino", "Hora Salida", "Estado"};
        modeloTablaReservas = new DefaultTableModel(columnasReservas, 0);
        tablaViajesReservados = new TablaEstilizada(modeloTablaReservas);

        JScrollPane scrollTablaReservas = new JScrollPane(tablaViajesReservados);
        scrollTablaReservas.setBorder(BorderFactory.createLineBorder(Paleta.ARENA_FONDO, 1));
        panelReservas.add(scrollTablaReservas, BorderLayout.CENTER);

        // Sustituimos el botón por el componente reutilizable
        BotonPeligro btnCancelarReserva = new BotonPeligro("Cancelar Reserva Seleccionada");
        btnCancelarReserva.setBorder(BorderFactory.createEmptyBorder(12, 0, 12, 0));
        panelReservas.add(btnCancelarReserva, BorderLayout.SOUTH);

        btnCancelarReserva.addActionListener(e -> {
            int filaSeleccionada = tablaViajesReservados.getSelectedRow();
            if (filaSeleccionada != -1) {
                modeloTablaReservas.removeRow(filaSeleccionada);
                JOptionPane.showMessageDialog(this, "Reserva cancelada con exito. El cupo ha sido liberado.", "Cancelacion", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Seleccione la reserva que desea cancelar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            }
        });

        return panelReservas;
    }

    private void estilarBotonFiltro(JButton boton) {
        boton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        boton.setBackground(Color.WHITE);
        boton.setForeground(Paleta.AZUL_MEDIO);
        boton.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Paleta.AZUL_MEDIO, 1),
            BorderFactory.createEmptyBorder(6, 14, 6, 14)
        ));
        boton.setFocusPainted(false);
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }


    private void estilarEtiquetaEstado(JLabel etiqueta) {
        etiqueta.setFont(new Font("Segoe UI", Font.BOLD, 12));
        etiqueta.setOpaque(true); 
        etiqueta.setBackground(new Color(240, 240, 240));
        etiqueta.setForeground(Color.GRAY);
        etiqueta.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
            BorderFactory.createEmptyBorder(8, 5, 8, 5)
        ));
    }

    private String modelObtenerTexto(int fila, int col) {
        return modeloTablaRutas.getValueAt(fila, col).toString();
    }
}