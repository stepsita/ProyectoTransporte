import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Vector;

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

    private JLabel lblSaldoNav;
    private JLabel lblMontoMonedero;
    private JTextField txtTelefono;
    private JTextField txtCedula;
    private JComboBox<String> cmbBanco;
    private JTextField txtReferencia;
    private JTextField txtMontoRecarga;
    
    // Lista en memoria de los itinerarios cargados desde BD para conservar sus IDs
    private Vector<Itinerario> listaItinerariosBD = new Vector<>();

    Usuario usuario = SesionActiva.getUsuario();

    public Pasajero() {
        // Sincronizamos el saldo del usuario y tarifas desde SQLite al abrir la ventana
        GestorCobro.cargarConfiguracionDesdeBD();
        GestorCobro.sincronizarSaldoUsuario(usuario);

        setTitle("Sistema de Transporte UCV - Panel de Pasajero");
        setSize(900, 580);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); 

        JPanel panelBase = new JPanel(new BorderLayout(15, 15));
        panelBase.setBackground(Paleta.ARENA_FONDO);
        panelBase.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel panelNav = new JPanel(new BorderLayout());
        panelNav.setOpaque(false);
        
        JLabel lblBienvenida = new JLabel("Bienvenido, " + usuario.getNombre() + " " + usuario.getApellido());
        lblBienvenida.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblBienvenida.setForeground(Paleta.AZUL_OSCURO);
        panelNav.add(lblBienvenida, BorderLayout.WEST);

        // Panel derecho de barra de navegación: Saldo en vivo + Cerrar Sesión (Bug de sobreescritura corregido)
        JPanel panelNavDerecho = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        panelNavDerecho.setOpaque(false);

        lblSaldoNav = new JLabel(String.format("Saldo: %.2f Bs", usuario.getSaldo()));
        lblSaldoNav.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblSaldoNav.setForeground(Paleta.AZUL_MEDIO);
        panelNavDerecho.add(lblSaldoNav);

        BotonPeligro btnCerrarSesion = new BotonPeligro("Cerrar Sesion");
        panelNavDerecho.add(btnCerrarSesion);

        panelNav.add(panelNavDerecho, BorderLayout.EAST);
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

    private void cargarRutasDesdeBD(String filtroTipo) {
        modeloTablaRutas.setRowCount(0);
        listaItinerariosBD.clear();

        Vector<Itinerario> todos = ConsultasBD.obtenerItinerarios();
        for (Itinerario it : todos) {
            String tipoRuta = it.getTipoRuta();
            if (filtroTipo.equals("TODAS") 
                    || (filtroTipo.equals("URBANAS") && !tipoRuta.equalsIgnoreCase("Extraurbana"))
                    || (filtroTipo.equals("EXTRAURBANAS") && tipoRuta.equalsIgnoreCase("Extraurbana"))) {
                
                listaItinerariosBD.add(it);
                double precioBase = tipoRuta.equalsIgnoreCase("Extraurbana") ? GestorCobro.getTarifaBaseGlobal() : 0.0;
                modeloTablaRutas.addRow(new Object[]{
                    it.getTipoRuta() + " - " + it.getNombreRuta(),
                    it.getFecha() + " " + it.getHora(),
                    it.getNombreChofer(),
                    it.getModeloUnidad(),
                    it.getCupos(),
                    precioBase
                });
            }
        }
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

        cargarRutasDesdeBD("TODAS");

        // Eventos de los botones de filtro conectados a SQLite
        btnFiltroTodas.addActionListener(e -> {
            estilarBotonFiltro(btnFiltroTodas); estilarBotonFiltro(btnFiltroUrbanas); estilarBotonFiltro(btnFiltroExtraurbanas);
            btnFiltroTodas.setBackground(Paleta.AZUL_MEDIO); btnFiltroTodas.setForeground(Color.WHITE);
            cargarRutasDesdeBD("TODAS");
        });
        btnFiltroUrbanas.addActionListener(e -> {
            estilarBotonFiltro(btnFiltroTodas); estilarBotonFiltro(btnFiltroUrbanas); estilarBotonFiltro(btnFiltroExtraurbanas);
            btnFiltroUrbanas.setBackground(Paleta.AZUL_MEDIO); btnFiltroUrbanas.setForeground(Color.WHITE);
            cargarRutasDesdeBD("URBANAS");
        });
        btnFiltroExtraurbanas.addActionListener(e -> {
            estilarBotonFiltro(btnFiltroTodas); estilarBotonFiltro(btnFiltroUrbanas); estilarBotonFiltro(btnFiltroExtraurbanas);
            btnFiltroExtraurbanas.setBackground(Paleta.AZUL_MEDIO); btnFiltroExtraurbanas.setForeground(Color.WHITE);
            cargarRutasDesdeBD("EXTRAURBANAS");
        });

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

        JLabel lblEstatus1 = new JLabel("1. En Parada", SwingConstants.CENTER);
        JLabel lblEstatus2 = new JLabel("2. En Trayecto", SwingConstants.CENTER);
        JLabel lblEstatus3 = new JLabel("3. Llegando al Destino", SwingConstants.CENTER);
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
        btnReservar.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));
        panelInferior.add(btnReservar, BorderLayout.EAST);
        
        panelRutas.add(panelInferior, BorderLayout.SOUTH);

        tablaRutasDisponibles.getSelectionModel().addListSelectionListener(e -> {
            int filaSeleccionada = tablaRutasDisponibles.getSelectedRow();
            if (filaSeleccionada != -1) {
                Itinerario itSeleccionado = listaItinerariosBD.get(filaSeleccionada);
                String destino = modeloTablaRutas.getValueAt(filaSeleccionada, 0).toString();
                int cupos = (int) modeloTablaRutas.getValueAt(filaSeleccionada, 4);
                String estadoReal = itSeleccionado.getEstado();
                
                estilarEtiquetaEstado(lblEstatus1);
                estilarEtiquetaEstado(lblEstatus2);
                estilarEtiquetaEstado(lblEstatus3);
                estilarEtiquetaEstado(lblEstatus4);

                if (estadoReal.equalsIgnoreCase("En Parada")) {
                    lblEstatus1.setBackground(new Color(40, 167, 69));
                    lblEstatus1.setForeground(Color.WHITE);
                } else if (estadoReal.equalsIgnoreCase("En Trayecto")) {
                    lblEstatus2.setBackground(Paleta.AZUL_MEDIO);
                    lblEstatus2.setForeground(Color.WHITE);
                } else if (estadoReal.equalsIgnoreCase("Llegando al Destino")) {
                    lblEstatus3.setBackground(Paleta.AZUL_GRIS);
                    lblEstatus3.setForeground(Color.WHITE);
                } else if (estadoReal.equalsIgnoreCase("Finalizado")) {
                    lblEstatus4.setBackground(Paleta.AZUL_OSCURO);
                    lblEstatus4.setForeground(Color.WHITE);
                }

                txtDetalleZonas.setText("Ruta seleccionada: " + destino + " | Unidad: " + itSeleccionado.getModeloUnidad() + " | Estado del viaje: " + estadoReal);
                btnReservar.setEnabled(cupos > 0 && !estadoReal.equalsIgnoreCase("Finalizado"));
            }
        });

        btnReservar.addActionListener(e -> {
            int filaSeleccionada = tablaRutasDisponibles.getSelectedRow();
            if (filaSeleccionada == -1) { 
                JOptionPane.showMessageDialog(this, "Por favor, seleccione una ruta de la tabla.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }
            String destino = modelObtenerTexto(filaSeleccionada, 0); 
            for (int i = 0; i < modeloTablaReservas.getRowCount(); i++) {
                String rutaReservada = modeloTablaReservas.getValueAt(i, 1).toString();
                if (rutaReservada.equals(destino)) {
                    JOptionPane.showMessageDialog(this, "Ya posees una reserva activa para la ruta: " + destino + ".\nSi deseas cambiarla, primero debes cancelar la reserva actual en la pestaña 'Mis Viajes Reservados'.", "Reserva Duplicada", JOptionPane.WARNING_MESSAGE);
                    return;
                }
            }

            int cuposActuales = (int) modeloTablaRutas.getValueAt(filaSeleccionada, 4);
            if (cuposActuales <= 0) {
                JOptionPane.showMessageDialog(this, "No quedan cupos disponibles para esta unidad.", "Sin Cupos", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Itinerario itSeleccionado = listaItinerariosBD.get(filaSeleccionada);
            String hora = modelObtenerTexto(filaSeleccionada, 1);
            double precioBase = Double.parseDouble(modeloTablaRutas.getValueAt(filaSeleccionada, 5).toString());
            int idTipo = (usuario != null) ? usuario.getIdTipo() : 1;
            String tipoTexto = (idTipo == 1) ? "Estudiante" : (idTipo == 2) ? "Empleado" : "Público General";
            
            double montoACobrar = 0.0;

            if (destino.toLowerCase().contains("extraurbana")) {
                try {
                    GestorCobro.procesarPago(usuario, precioBase);
                    montoACobrar = GestorCobro.calcularTarifaSolidaria(precioBase, idTipo);
                    actualizarVistasSaldo();
                } catch (GestorCobro.SaldoInsuficienteException ex) {
                    JOptionPane.showMessageDialog(this, ex.getMessage(), "Saldo Insuficiente", JOptionPane.ERROR_MESSAGE);
                    return; 
                }
            }

            // Persistimos la reserva y descontamos el cupo en SQLite
            String codigoReserva = GestorCobro.registrarReservaBD(
                itSeleccionado.getIdItinerario(),
                usuario.getCedula(),
                destino,
                hora,
                montoACobrar
            );

            if (codigoReserva != null) {
                modeloTablaRutas.setValueAt(cuposActuales - 1, filaSeleccionada, 4); 
                modeloTablaReservas.addRow(new Object[]{codigoReserva, destino, hora, "Activa", montoACobrar});

                JOptionPane.showMessageDialog(this, 
                    "--- BOLETO DIGITAL UCV ---\n" +
                    "Codigo: " + codigoReserva + "\n" +
                    "Ruta: " + destino + "\n" +
                    "Hora: " + hora + "\n" +
                    "Tipo Pasajero: " + tipoTexto + "\n" +
                    String.format("Monto Cobrado: %.2f Bs\n", montoACobrar) +
                    String.format("Saldo Restante: %.2f Bs\n", usuario.getSaldo()) +
                    "---------------------------------\nMuestre este boleto al abordar.", 
                    "Reserva Confirmada", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Error al registrar la reserva en la base de datos.", "Error SQL", JOptionPane.ERROR_MESSAGE);
            }
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

        // Cargamos las reservas activas guardadas en SQLite para este pasajero
        Vector<Object[]> reservasGuardadas = GestorCobro.obtenerReservasActivasPorPasajero(usuario.getCedula());
        for (Object[] fila : reservasGuardadas) {
            modeloTablaReservas.addRow(fila);
        }

        tablaViajesReservados = new TablaEstilizada(modeloTablaReservas);

        JScrollPane scrollTablaReservas = new JScrollPane(tablaViajesReservados);
        scrollTablaReservas.setBorder(BorderFactory.createLineBorder(Paleta.ARENA_FONDO, 1));
        panelReservas.add(scrollTablaReservas, BorderLayout.CENTER);

        BotonPeligro btnCancelarReserva = new BotonPeligro("Cancelar Reserva Seleccionada");
        btnCancelarReserva.setBorder(BorderFactory.createEmptyBorder(12, 0, 12, 0));
        panelReservas.add(btnCancelarReserva, BorderLayout.SOUTH);

        btnCancelarReserva.addActionListener(e -> {
            int filaSeleccionada = tablaViajesReservados.getSelectedRow();
            if (filaSeleccionada != -1) {
                String codigoTicket = modeloTablaReservas.getValueAt(filaSeleccionada, 0).toString();
                String destinoCancelado = modeloTablaReservas.getValueAt(filaSeleccionada, 1).toString();
                double montoPagado = Double.parseDouble(modeloTablaReservas.getValueAt(filaSeleccionada, 4).toString());

                if (GestorCobro.cancelarReservaBD(codigoTicket)) {
                    // Devolver cupo a la tabla visual de rutas
                    for (int i = 0; i < modeloTablaRutas.getRowCount(); i++) {
                        if (modeloTablaRutas.getValueAt(i, 0).toString().equals(destinoCancelado)) {
                            int cupos = (int) modeloTablaRutas.getValueAt(i, 4);
                            modeloTablaRutas.setValueAt(cupos + 1, i, 4);
                            break;
                        }
                    }

                    // Si pagó pasaje extraurbano, se reembolsa en SQLite y en memoria
                    if (montoPagado > 0) {
                        GestorCobro.procesarReembolso(usuario, montoPagado, destinoCancelado);
                        actualizarVistasSaldo();
                        JOptionPane.showMessageDialog(this, 
                            String.format("Reserva cancelada con exito.\nSe han reembolsado %.2f Bs a tu monedero.", montoPagado), 
                            "Reembolso Procesado", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(this, "Reserva cancelada con exito. El cupo ha sido liberado.", "Cancelacion", JOptionPane.INFORMATION_MESSAGE);
                    }

                    modeloTablaReservas.removeRow(filaSeleccionada);
                } else {
                    JOptionPane.showMessageDialog(this, "No se pudo cancelar la reserva en la base de datos.", "Error SQL", JOptionPane.ERROR_MESSAGE);
                }
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

    private JPanel crearPanelMonedero() {
        JPanel panelMonedero = new JPanel(new BorderLayout(20, 20));
        panelMonedero.setBackground(Color.WHITE);
        panelMonedero.setBorder(BorderFactory.createEmptyBorder(25, 40, 25, 40));

        JPanel cardSaldo = new JPanel(new GridLayout(2, 1, 5, 5));
        cardSaldo.setBackground(Paleta.AZUL_OSCURO);
        cardSaldo.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JLabel lblTituloSaldo = new JLabel("Saldo Disponible en tu Monedero", SwingConstants.CENTER);
        lblTituloSaldo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblTituloSaldo.setForeground(Color.WHITE);

        lblMontoMonedero = new JLabel(String.format("%.2f Bs", usuario.getSaldo()), SwingConstants.CENTER);
        lblMontoMonedero.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblMontoMonedero.setForeground(Paleta.ARENA_FONDO);

        cardSaldo.add(lblTituloSaldo);
        cardSaldo.add(lblMontoMonedero);
        panelMonedero.add(cardSaldo, BorderLayout.NORTH);

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
        formPagoMovil.add(new JLabel("Número de Referencia (4 dígitos):"), gbc);
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
        String banco = (String) cmbBanco.getSelectedItem();

        if (tel.isEmpty() || ci.isEmpty() || ref.isEmpty() || montoStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Por favor, complete todos los campos obligatorios para procesar la recarga.", 
                "Campos Vacíos", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            double monto = Double.parseDouble(montoStr.replace(",", "."));
            
            GestorCobro.recargarSaldo(usuario, banco, tel, ci, ref, monto);
            actualizarVistasSaldo();

            txtTelefono.setText("");
            txtCedula.setText("");
            txtReferencia.setText("");
            txtMontoRecarga.setText("");

            JOptionPane.showMessageDialog(this, 
                String.format("¡Recarga procesada y registrada en BD exitosamente!\nSe han sumado %.2f Bs a tu monedero.", monto), 
                "Operación Exitosa", JOptionPane.INFORMATION_MESSAGE);

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Ingrese un monto numérico válido.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
        } catch (GestorCobro.DatosInvalidosException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error de Validación", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void actualizarVistasSaldo() {
        lblSaldoNav.setText(String.format("Saldo: %.2f Bs", usuario.getSaldo()));
        if (lblMontoMonedero != null) {
            lblMontoMonedero.setText(String.format("%.2f Bs", usuario.getSaldo()));
        }
    }
}