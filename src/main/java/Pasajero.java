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

    private double saldoMonedero = 50.0; 
    private JLabel lblSaldoNav;
    private JLabel lblMontoMonedero;
    private JTextField txtTelefono;
    private JTextField txtCedula;
    private JComboBox<String> cmbBanco;
    private JTextField txtReferencia;
    private JTextField txtMontoRecarga;
    
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

        // Panel derecho de barra de navegación: Saldo en vivo + Cerrar Sesión
        JPanel panelNavDerecho = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        panelNavDerecho.setOpaque(false);

        lblSaldoNav = new JLabel(String.format("Saldo: %.2f Bs", saldoMonedero));
        lblSaldoNav.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblSaldoNav.setForeground(Paleta.AZUL_MEDIO);
        panelNavDerecho.add(lblSaldoNav);

        BotonPeligro btnCerrarSesion = new BotonPeligro("Cerrar Sesion");
        panelNavDerecho.add(btnCerrarSesion);

        panelNav.add(panelNavDerecho, BorderLayout.EAST); // <-- Así se ven el saldo Y el botón juntos a la derecha
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
        contenedorPestañas.addTab("Mi Monedero Virtual", crearPanelMonedero());
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

        String[] columnas = {"Destino", "Hora Salida", "Conductor", "Modelo Unidad", "Cupos", "Precio Base"};
        modeloTablaRutas = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        modeloTablaRutas.addRow(new Object[]{"Ruta Interna - Plaza Venezuela", "07:30 AM", "Carlos Mendoza", "Yutong Urbano", 25, 0.0});
        modeloTablaRutas.addRow(new Object[]{"Extraurbana - Guarenas/Guatire", "04:30 PM", "Jose Rodriguez", "Encava Autobus", 40, 80.0});
        modeloTablaRutas.addRow(new Object[]{"Extraurbana - Los Teques", "05:15 PM", "Luis Gomez", "Encava Autobus", 0, 90.0});
        modeloTablaRutas.addRow(new Object[]{"Ruta Interna - La Rinconada", "12:00 PM", "Ana Martinez", "Yutong Urbano", 12, 0.0});

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
            if (filaSeleccionada == -1) { 
                JOptionPane.showMessageDialog(this, "Por favor, seleccione una ruta de la tabla.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String destino = modelObtenerTexto(filaSeleccionada, 0); 

            // Validar que no tenga ya una reserva activa para esta ruta
            for (int i = 0; i < modeloTablaReservas.getRowCount(); i++) {
                String rutaReservada = modeloTablaReservas.getValueAt(i, 1).toString();
                if (rutaReservada.equals(destino)) {
                    JOptionPane.showMessageDialog(this, "Ya posees una reserva activa para la ruta: " + destino + ".\nSi deseas cambiarla, primero debes cancelar la reserva actual en la pestaña 'Mis Viajes Reservados'.", "Reserva Duplicada", JOptionPane.WARNING_MESSAGE);
                    return; // Detiene el proceso, no permite reservar de nuevo
                }
            }

            int cuposActuales = (int) modeloTablaRutas.getValueAt(filaSeleccionada, 4);
            if (cuposActuales <= 0) {
                JOptionPane.showMessageDialog(this, "No quedan cupos disponibles para esta unidad.", "Sin Cupos", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String hora = modelObtenerTexto(filaSeleccionada, 1);
            double precioBase = Double.parseDouble(modeloTablaRutas.getValueAt(filaSeleccionada, 5).toString());

            // Cobro Solidario según idTipo (1=Estudiante, 2=Empleado, 3=General)
            int idTipo = (usuario != null) ? usuario.getIdTipo() : 1;
            String tipoTexto = "Estudiante";
            double montoACobrar = 0.0;

            if (destino.toLowerCase().contains("extraurbana")) {
                if (idTipo == 1) { // 10% - 20%
                    montoACobrar = precioBase * 0.15; 
                    tipoTexto = "Estudiante";
                } else if (idTipo == 2) { // 30% - 60%
                    montoACobrar = precioBase * 0.40; 
                    tipoTexto = "Empleado";
                } else { // 100%
                    montoACobrar = precioBase; 
                    tipoTexto = "Público General";
                }

                // Validar si el saldo alcanza
                if (saldoMonedero < montoACobrar) {
                    JOptionPane.showMessageDialog(this, 
                        String.format("Saldo insuficiente para ruta extraurbana.\nTarifa (%s): %.2f Bs\nSaldo disponible: %.2f Bs\nRecarga en 'Mi Monedero Virtual'.", tipoTexto, montoACobrar, saldoMonedero),
                        "Saldo Insuficiente", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                saldoMonedero -= montoACobrar;
                actualizarVistasSaldo();
            }

            // Descontar cupo y registrar reserva con el monto pagado
            modeloTablaRutas.setValueAt(cuposActuales - 1, filaSeleccionada, 4); 
            String codigoReserva = "UCV-" + (int)(Math.random() * 9000 + 1000); 
            modeloTablaReservas.addRow(new Object[]{codigoReserva, destino, hora, "Activa", montoACobrar});

            JOptionPane.showMessageDialog(this, 
                "--- BOLETO DIGITAL UCV ---\n" +
                "Codigo: " + codigoReserva + "\n" +
                "Ruta: " + destino + "\n" +
                "Hora: " + hora + "\n" +
                "Tipo Pasajero: " + tipoTexto + "\n" +
                String.format("Monto Cobrado: %.2f Bs\n", montoACobrar) +
                String.format("Saldo Restante: %.2f Bs\n", saldoMonedero) +
                "---------------------------------\nMuestre este boleto al abordar.", 
                "Reserva Confirmada", JOptionPane.INFORMATION_MESSAGE);
        });

        return panelRutas;
    }

    private JPanel crearPanelReservas() {
        JPanel panelReservas = new JPanel(new BorderLayout(20, 20));
        panelReservas.setBackground(Color.WHITE);
        panelReservas.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        String[] columnasReservas = {"Codigo Reserva", "Destino", "Hora Salida", "Estado", "Monto Pagado"};
        modeloTablaReservas = new DefaultTableModel(columnasReservas, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        // INSTANCIAR ANTES DE METER AL SCROLL:
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
                String destinoCancelado = modeloTablaReservas.getValueAt(filaSeleccionada, 1).toString();
                double montoPagado = Double.parseDouble(modeloTablaReservas.getValueAt(filaSeleccionada, 4).toString());

                // Devolver cupo a la tabla de rutas
                for (int i = 0; i < modeloTablaRutas.getRowCount(); i++) {
                    if (modeloTablaRutas.getValueAt(i, 0).toString().equals(destinoCancelado)) {
                        int cupos = (int) modeloTablaRutas.getValueAt(i, 4);
                        modeloTablaRutas.setValueAt(cupos + 1, i, 4);
                        break;
                    }
                }

                // Si pagó pasaje extraurbano, se reembolsa
                if (montoPagado > 0) {
                    saldoMonedero += montoPagado;
                    actualizarVistasSaldo();
                    JOptionPane.showMessageDialog(this, 
                        String.format("Reserva cancelada con exito.\nSe han reembolsado %.2f Bs a tu monedero.", montoPagado), 
                        "Reembolso Procesado", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Reserva cancelada con exito. El cupo ha sido liberado.", "Cancelacion", JOptionPane.INFORMATION_MESSAGE);
                }

                modeloTablaReservas.removeRow(filaSeleccionada);
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

    // ==========================================
    // METODOS DEL MONEDERO VIRTUAL
    // ==========================================
    private JPanel crearPanelMonedero() {
        JPanel panelMonedero = new JPanel(new BorderLayout(20, 20));
        panelMonedero.setBackground(Color.WHITE);
        panelMonedero.setBorder(BorderFactory.createEmptyBorder(25, 40, 25, 40));

        // Tarjeta de saldo
        JPanel cardSaldo = new JPanel(new GridLayout(2, 1, 5, 5));
        cardSaldo.setBackground(Paleta.AZUL_OSCURO);
        cardSaldo.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JLabel lblTituloSaldo = new JLabel("Saldo Disponible en tu Monedero", SwingConstants.CENTER);
        lblTituloSaldo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblTituloSaldo.setForeground(Color.WHITE);

        lblMontoMonedero = new JLabel(String.format("%.2f Bs", saldoMonedero), SwingConstants.CENTER);
        lblMontoMonedero.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblMontoMonedero.setForeground(Paleta.ARENA_FONDO);

        cardSaldo.add(lblTituloSaldo);
        cardSaldo.add(lblMontoMonedero);
        panelMonedero.add(cardSaldo, BorderLayout.NORTH);

        // Formulario de Pago Móvil
        JPanel formPagoMovil = new JPanel(new GridBagLayout());
        formPagoMovil.setBackground(Color.WHITE);
        formPagoMovil.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Paleta.ARENA_FONDO, 1), 
            "Recarga de Saldo vía Pago Móvil", 0, 0, 
            new Font("Segoe UI", Font.BOLD, 12), Paleta.AZUL_MEDIO
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 12, 8, 12);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        formPagoMovil.add(new JLabel("Banco Emisor:"), gbc);
        gbc.gridx = 1;
        cmbBanco = new JComboBox<>(new String[]{"0102 - Banco de Venezuela", "0108 - Banco Provincial", "0134 - Banesco", "0105 - Banco Mercantil"});
        formPagoMovil.add(cmbBanco, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        formPagoMovil.add(new JLabel("Teléfono Origen (04XX...):"), gbc);
        gbc.gridx = 1;
        txtTelefono = new JTextField(15);
        formPagoMovil.add(txtTelefono, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        formPagoMovil.add(new JLabel("Cédula de Identidad:"), gbc);
        gbc.gridx = 1;
        txtCedula = new JTextField(15);
        formPagoMovil.add(txtCedula, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        formPagoMovil.add(new JLabel("Número de Referencia:"), gbc);
        gbc.gridx = 1;
        txtReferencia = new JTextField(15);
        formPagoMovil.add(txtReferencia, gbc);

        gbc.gridx = 0; gbc.gridy = 4;
        formPagoMovil.add(new JLabel("Monto a Recargar (Bs):"), gbc);
        gbc.gridx = 1;
        txtMontoRecarga = new JTextField(15);
        formPagoMovil.add(txtMontoRecarga, gbc);

        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 2;
        BotonPrimario btnRecargar = new BotonPrimario("Confirmar Recarga");
        btnRecargar.addActionListener(e -> procesarRecarga());
        formPagoMovil.add(btnRecargar, gbc);

        panelMonedero.add(formPagoMovil, BorderLayout.CENTER);
        return panelMonedero;
    }

    private void procesarRecarga() {
        String tel = txtTelefono.getText().trim();
        String ci = txtCedula.getText().trim();
        String ref = txtReferencia.getText().trim();
        String montoStr = txtMontoRecarga.getText().trim();

        if (tel.isEmpty() || ci.isEmpty() || ref.isEmpty() || montoStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor complete todos los datos del formulario.", "Campos Requeridos", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!tel.matches("\\d+") || !ci.matches("\\d+") || !ref.matches("\\d+")) {
            JOptionPane.showMessageDialog(this, "Teléfono, Cédula y Referencia solo deben contener números.", "Formato Inválido", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            double monto = Double.parseDouble(montoStr.replace(",", "."));
            if (monto <= 0) {
                JOptionPane.showMessageDialog(this, "El monto a recargar debe ser mayor a 0 Bs.", "Monto Inválido", JOptionPane.ERROR_MESSAGE);
                return;
            }

            saldoMonedero += monto;
            actualizarVistasSaldo();

            txtTelefono.setText("");
            txtCedula.setText("");
            txtReferencia.setText("");
            txtMontoRecarga.setText("");

            JOptionPane.showMessageDialog(this, 
                String.format("¡Recarga procesada exitosamente!\nSe han sumado %.2f Bs a tu monedero.", monto), 
                "Operación Exitosa", JOptionPane.INFORMATION_MESSAGE);

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Ingrese un monto numérico válido.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void actualizarVistasSaldo() {
        lblSaldoNav.setText(String.format("Saldo: %.2f Bs", saldoMonedero));
        if (lblMontoMonedero != null) {
            lblMontoMonedero.setText(String.format("%.2f Bs", saldoMonedero));
        }
    }
}