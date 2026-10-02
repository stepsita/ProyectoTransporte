import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Pasajero extends JFrame {

    //creamos los componentes que se van a usar en la interfaz
    private JTabbedPane contenedorPestañas;
    private JTable tablaRutasDisponibles;
    private DefaultTableModel modeloTablaRutas;
    private JTable tablaViajesReservados;
    private DefaultTableModel modeloTablaReservas;
    
    //paleta de colores q puedo cambiar si no les gusta
    private final Color colorAzulOscuro = new Color(0x11, 0x18, 0x44);   // #111844
    private final Color colorAzulMedio = new Color(0x4B, 0x56, 0x94);    // #4B5694
    private final Color colorAzulGris = new Color(0x72, 0x88, 0xAE);     // #7288AE
    private final Color colorArenaFondo = new Color(0xEA, 0xE0, 0xCF);    // #EAE0CF

    //Usuario en sesión
    Usuario usuario = SesionActiva.getUsuario();

    public Pasajero() {
        setTitle("Sistema de Transporte UCV - Panel de Pasajero");
        setSize(900, 580);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centra la ventana

        JPanel panelBase = new JPanel(new BorderLayout(15, 15));
        panelBase.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panelBase.setBackground(colorArenaFondo);

        //esto es para la bienvenida y el boton de cerrar sesion
        JPanel panelNav = new JPanel(new BorderLayout());
        panelNav.setBackground(colorArenaFondo);
        
        JLabel lblBienvenida = new JLabel("Bienvenido, "+usuario.getNombreCompleto());
        lblBienvenida.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblBienvenida.setForeground(colorAzulOscuro);
        panelNav.add(lblBienvenida, BorderLayout.WEST);

        JButton btnCerrarSesion = new JButton("Cerrar Sesion");
        btnCerrarSesion.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnCerrarSesion.setBackground(new Color(220, 53, 69));
        btnCerrarSesion.setForeground(Color.WHITE);
        btnCerrarSesion.setFocusPainted(false);
        panelNav.add(btnCerrarSesion, BorderLayout.EAST);
        
        panelBase.add(panelNav, BorderLayout.NORTH); //con esto se ancla arriba

        //evento para cerrar sesion y regresar al login
        btnCerrarSesion.addActionListener(e -> {
            SesionActiva.cerrarSesion();
            dispose(); 
            new InicioSesion().setVisible(true);
        });

        //aqui se crean las pestañas y se añaden al contenedor principal
        contenedorPestañas = new JTabbedPane();
        contenedorPestañas.setFont(new Font("Segoe UI", Font.BOLD, 13));
        contenedorPestañas.setBackground(Color.WHITE);
        contenedorPestañas.setForeground(colorAzulOscuro);

        //construimos y añadimos cada pestaña
        contenedorPestañas.addTab("Rutas Disponibles", crearPanelRutas());
        contenedorPestañas.addTab("Mis Viajes Reservados", crearPanelReservas());
        panelBase.add(contenedorPestañas, BorderLayout.CENTER);
        add(panelBase);
    }




    private JPanel crearPanelRutas() {
        JPanel panelRutas = new JPanel(new BorderLayout(20, 20));
        panelRutas.setBackground(Color.WHITE);
        panelRutas.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        //aca creamos un subpanel para los filtros de rutas
        JPanel panelFiltros = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        panelFiltros.setBackground(Color.WHITE);
        
        JLabel lblFiltrar = new JLabel("Filtrar por:");
        lblFiltrar.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblFiltrar.setForeground(colorAzulOscuro);
        panelFiltros.add(lblFiltrar);

        JButton btnFiltroTodas = new JButton("Todas");
        JButton btnFiltroUrbanas = new JButton("Urbanas");
        JButton btnFiltroExtraurbanas = new JButton("Extraurbanas");
        
        estilarBotonFiltro(btnFiltroTodas);
        estilarBotonFiltro(btnFiltroUrbanas);
        estilarBotonFiltro(btnFiltroExtraurbanas);
        
        btnFiltroTodas.setBackground(colorAzulMedio);
        btnFiltroTodas.setForeground(Color.WHITE);

        panelFiltros.add(btnFiltroTodas);
        panelFiltros.add(btnFiltroUrbanas);
        panelFiltros.add(btnFiltroExtraurbanas);
        panelRutas.add(panelFiltros, BorderLayout.NORTH);

        //aca se crea la tabla de rutas disponibles con su modelo y se añaden filas de ejemplo
        String[] columnas = {"Destino", "Hora Salida", "Conductor", "Modelo Unidad", "Cupos"};
        modeloTablaRutas = new DefaultTableModel(columnas, 0) { //el modelo es para manejar los datos de la tabla 
            @Override //con esto se hace que las celdas no sean editables por el usuario
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        modeloTablaRutas.addRow(new Object[]{"Ruta Interna - Plaza Venezuela", "07:30 AM", "Carlos Mendoza", "Yutong Urbano", 25});
        modeloTablaRutas.addRow(new Object[]{"Extraurbana - Guarenas/Guatire", "04:30 PM", "Jose Rodriguez", "Encava Autobus", 40});
        modeloTablaRutas.addRow(new Object[]{"Extraurbana - Los Teques", "05:15 PM", "Luis Gomez", "Encava Autobus", 0});
        modeloTablaRutas.addRow(new Object[]{"Ruta Interna - La Rinconada", "12:00 PM", "Ana Martinez", "Yutong Urbano", 12});

        tablaRutasDisponibles = new JTable(modeloTablaRutas); //la tabla es el componente visual que muestra los datos del modelo
        configurarEstiloTabla(tablaRutasDisponibles); //con este metodo se le da un estilo a la tabla (colores, fuentes, etc)
        
        JScrollPane scrollTabla = new JScrollPane(tablaRutasDisponibles); 
        scrollTabla.setBorder(BorderFactory.createLineBorder(colorArenaFondo, 1));
        panelRutas.add(scrollTabla, BorderLayout.CENTER);

        //detalle de paradas e informacion de ruta
        JPanel panelInferior = new JPanel(new BorderLayout(15, 15));
        panelInferior.setBackground(Color.WHITE);

        //estados en tiempo real del conductor 
        JPanel panelEstadoVehiculo = new JPanel(new GridLayout(1, 4, 10, 0));
        panelEstadoVehiculo.setBackground(Color.WHITE);
        panelEstadoVehiculo.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(colorArenaFondo, 1), 
            "Estatus en Tiempo Real del Conductor", 0, 0, 
            new Font("Segoe UI", Font.BOLD, 11), colorAzulMedio
        ));

        //creamos etiquetas para cada estado del viaje y les damos un estilo inicial de "apagado" 
        JLabel lblEstatus1 = new JLabel("1. Programado", SwingConstants.CENTER);
        JLabel lblEstatus2 = new JLabel("2. En Parada", SwingConstants.CENTER);
        JLabel lblEstatus3 = new JLabel("3. En Trayecto", SwingConstants.CENTER);
        JLabel lblEstatus4 = new JLabel("4. Finalizado", SwingConstants.CENTER);

        // Estilo inicial para las tarjetas (Apagadas en gris claro)
        estilarEtiquetaEstado(lblEstatus1);
        estilarEtiquetaEstado(lblEstatus2);
        estilarEtiquetaEstado(lblEstatus3);
        estilarEtiquetaEstado(lblEstatus4);

        panelEstadoVehiculo.add(lblEstatus1);
        panelEstadoVehiculo.add(lblEstatus2);
        panelEstadoVehiculo.add(lblEstatus3);
        panelEstadoVehiculo.add(lblEstatus4);
        
        panelInferior.add(panelEstadoVehiculo, BorderLayout.NORTH);

        //detalle de zonas de parada e informacion adicional de la ruta seleccionada
        JTextArea txtDetalleZonas = new JTextArea("Selecciona una ruta de la tabla para consultar detalles e itinerario.");
        txtDetalleZonas.setFont(new Font("Segoe UI", Font.ITALIC, 13));
        txtDetalleZonas.setEditable(false);
        txtDetalleZonas.setBackground(colorArenaFondo);
        txtDetalleZonas.setForeground(colorAzulOscuro);
        txtDetalleZonas.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        panelInferior.add(txtDetalleZonas, BorderLayout.CENTER);

        JButton btnReservar = new JButton("Asegurar Puesto");
        btnReservar.setBackground(colorAzulOscuro);
        btnReservar.setForeground(Color.WHITE);
        btnReservar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnReservar.setFocusPainted(false);
        btnReservar.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));
        panelInferior.add(btnReservar, BorderLayout.EAST);
        
        panelRutas.add(panelInferior, BorderLayout.SOUTH);

        //evento para actualizar el detalle de la ruta y el estado del conductor segun la ruta seleccionada en la tabla
        tablaRutasDisponibles.getSelectionModel().addListSelectionListener(e -> {
            int filaSeleccionada = tablaRutasDisponibles.getSelectedRow();
            if (filaSeleccionada != -1) {
                String destino = modeloTablaRutas.getValueAt(filaSeleccionada, 0).toString();
                int cupos = (int) modeloTablaRutas.getValueAt(filaSeleccionada, 4);
                
                //aca todo esta apagado por default 
                lblEstatus1.setBackground(new Color(240, 240, 240)); lblEstatus1.setForeground(Color.GRAY);
                lblEstatus2.setBackground(new Color(240, 240, 240)); lblEstatus2.setForeground(Color.GRAY);
                lblEstatus3.setBackground(new Color(240, 240, 240)); lblEstatus3.setForeground(Color.GRAY);
                lblEstatus4.setBackground(new Color(240, 240, 240)); lblEstatus4.setForeground(Color.GRAY);

                // Simulación dinámica de estados según la ruta seleccionada
                if (destino.contains("Guarenas")) {
                    txtDetalleZonas.setText("Zonas de Parada: Terminal Oriente -> Entrada de Guarenas -> Plaza El Maestro.");
                    //en trayecto (Estado 3 activo)
                    lblEstatus3.setBackground(colorAzulMedio);
                    lblEstatus3.setForeground(Color.WHITE);
                } else if (destino.contains("Plaza Venezuela")) {
                    txtDetalleZonas.setText("Zonas de Parada: Via Directa Interna UCV -> Salida Puerta Tamanaquito.");
                    //en el anden (Estado 2 activo)
                    lblEstatus2.setBackground(new Color(40, 167, 69)); // Verde éxito
                    lblEstatus2.setForeground(Color.WHITE);
                } else if (destino.contains("Los Teques")) {
                    txtDetalleZonas.setText("Zonas de Parada: Paradas oficiales establecidas en el trayecto directo.");
                    //ya culminado (Estado 4 activo)
                    lblEstatus4.setBackground(colorAzulOscuro);
                    lblEstatus4.setForeground(Color.WHITE);
                } else {
                    txtDetalleZonas.setText("Zonas de Parada: Paradas oficiales establecidas en el trayecto directo.");
                    // aún no inicia (Estado 1 activo)
                    lblEstatus1.setBackground(colorAzulGris);
                    lblEstatus1.setForeground(Color.WHITE);
                }
                
                btnReservar.setEnabled(cupos > 0);
            }
        });

        //evento para reservar 
        btnReservar.addActionListener(e -> {
            int filaSeleccionada = tablaRutasDisponibles.getSelectedRow();
            if (filaSeleccionada != -1) { //si hay una fila seleccionada
                String destino = modelObtenerTexto(filaSeleccionada, 0); 
                String hora = modelObtenerTexto(filaSeleccionada, 1);
                int cuposActuales = (int) modeloTablaRutas.getValueAt(filaSeleccionada, 4);
                modeloTablaRutas.setValueAt(cuposActuales - 1, filaSeleccionada, 4); // Actualiza el número de cupos en la tabla de rutas
                String codigoReserva = "UCV-" + (int)(Math.random() * 9000 + 1000); 
                modeloTablaReservas.addRow(new Object[]{codigoReserva, destino, hora, "Activa"});

                JOptionPane.showMessageDialog(this, 
                    "--- BOLETO DIGITAL UCV ---\n" +
                    "Codigo: " + codigoReserva + "\n" +
                    "Ruta: " + destino + "\n" +
                    "Hora: " + hora + "\n" +
                    "Estado: Activa\n" +
                    "---------------------------------\n" +
                    "Muestre este boleto al abordar.", 
                    "Reserva Confirmada", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Por favor, seleccione una ruta de la tabla.", "Aviso", JOptionPane.WARNING_MESSAGE);
            }
        });

        return panelRutas;
    }




    //viajes reservados
    private JPanel crearPanelReservas() {
        JPanel panelReservas = new JPanel(new BorderLayout(20, 20));
        panelReservas.setBackground(Color.WHITE);
        panelReservas.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        String[] columnasReservas = {"Codigo Reserva", "Destino", "Hora Salida", "Estado"};
        modeloTablaReservas = new DefaultTableModel(columnasReservas, 0);
        tablaViajesReservados = new JTable(modeloTablaReservas);
        configurarEstiloTabla(tablaViajesReservados);

        JScrollPane scrollTablaReservas = new JScrollPane(tablaViajesReservados);
        scrollTablaReservas.setBorder(BorderFactory.createLineBorder(colorArenaFondo, 1));
        panelReservas.add(scrollTablaReservas, BorderLayout.CENTER);

        JButton btnCancelarReserva = new JButton("Cancelar Reserva Seleccionada");
        btnCancelarReserva.setBackground(new Color(220, 53, 69)); // Rojo de alerta
        btnCancelarReserva.setForeground(Color.WHITE);
        btnCancelarReserva.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnCancelarReserva.setFocusPainted(false);
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

    

    //metodos para estilar componentes 
    private void estilarBotonFiltro(JButton boton) {
        boton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        boton.setBackground(Color.WHITE);
        boton.setForeground(colorAzulMedio);
        boton.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(colorAzulGris, 1),
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

    private void configurarEstiloTabla(JTable tabla) {
        tabla.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tabla.setRowHeight(30); 
        tabla.setSelectionBackground(colorArenaFondo);
        tabla.setSelectionForeground(colorAzulOscuro);
        tabla.setShowVerticalLines(false); 
        tabla.setGridColor(new Color(230, 230, 230));

        JTableHeader encabezado = tabla.getTableHeader();
        encabezado.setFont(new Font("Segoe UI", Font.BOLD, 13));
        encabezado.setBackground(colorAzulOscuro);
        encabezado.setForeground(Color.WHITE);
        encabezado.setReorderingAllowed(false); // Evita que el usuario mueva las columnas de sitio
    }

    private String modelObtenerTexto(int fila, int col) {
        return modeloTablaRutas.getValueAt(fila, col).toString();
    }
}