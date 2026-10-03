import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.Vector;
import java.awt.*;

import componentes.*;

public class Administrador extends JFrame {

    private JTabbedPane contenedorPestañas;
    
    private TablaEstilizada tablaItinerarioSemanal;
    private DefaultTableModel modeloTablaItinerario;
    
    private TablaEstilizada tablaUnidadesFlota;
    private DefaultTableModel modeloTablaUnidades;

    private JComboBox<ItemCombo> cmbRuta;
    private JComboBox<ItemCombo>  cmbConductor;
    private JComboBox<ItemCombo>  cmbUnidad;
    private JTextField txtHoraSalida;
    private JTextField txtFecha;

    public Administrador() {
        setTitle("Sistema de Transporte UCV - Panel de Administrador");
        setSize(950, 640); 
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); 
        Usuario usuario = SesionActiva.getUsuario();

        JPanel panelBase = new JPanel(new BorderLayout(15, 15));
        panelBase.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panelBase.setBackground(Paleta.ARENA_FONDO);

        JPanel panelNav = new JPanel(new BorderLayout());
        panelNav.setOpaque(false);
        
        JLabel lblBienvenida = new JLabel("Bienvenido al panel de Administrador, "+usuario.getNombreCompleto());
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

        contenedorPestañas.addTab("Planificacion de Itinerario", crearPanelPlanificacion());
        contenedorPestañas.addTab("Gestion de Unidades", crearPanelFlota());
        contenedorPestañas.addTab("Reportes y Estadisticas", crearPanelReportes());

        panelBase.add(contenedorPestañas, BorderLayout.CENTER);
        add(panelBase);
    }

    private JPanel crearPanelPlanificacion() {
        JPanel panelPlanificacion = new JPanel(new BorderLayout(20, 20));
        panelPlanificacion.setBackground(Color.WHITE);
        panelPlanificacion.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // --- PANEL IZQUIERDO: FORMULARIO ---
        JPanel panelFormulario = new JPanel(new GridBagLayout());
        panelFormulario.setBackground(Color.WHITE);
        panelFormulario.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Paleta.AZUL_GRIS, 1), 
            "Crear Nuevo Servicio", 0, 0, 
            new Font("Segoe UI", Font.BOLD, 13), Paleta.AZUL_OSCURO
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 12, 8, 12);
        gbc.gridx = 0;

        gbc.gridy = 0;
        panelFormulario.add(new FormularioLabel("Seleccionar Ruta:"), gbc);
        gbc.gridy = 1;
        cmbRuta = new JComboBox<ItemCombo>(ConsultasBD.obtenerRutas());
        panelFormulario.add(cmbRuta, gbc);

        gbc.gridy = 2;
        panelFormulario.add(new FormularioLabel("Asignar Conductor:"), gbc);
        gbc.gridy = 3;
        cmbConductor = new JComboBox<ItemCombo>(ConsultasBD.obtenerConductores());
        panelFormulario.add(cmbConductor, gbc);

        gbc.gridy = 4;
        panelFormulario.add(new FormularioLabel("Asignar Unidad:"), gbc);
        gbc.gridy = 5;
        cmbUnidad = new JComboBox<ItemCombo>(ConsultasBD.obtenerUnidadesCombo());
        panelFormulario.add(cmbUnidad, gbc);

        gbc.gridy = 6; 
        JPanel panelFechaHora = new JPanel(new GridLayout(1, 2, 10, 0));
        panelFechaHora.setOpaque(false);

        JPanel colFecha = new JPanel(new BorderLayout(0, 2));
        colFecha.setOpaque(false);
        colFecha.add(new FormularioLabel("Fecha:"), BorderLayout.NORTH);
        txtFecha = new JTextField();
        txtFecha.putClientProperty("JTextField.placeholderText", "AAAA-MM-DD");
        colFecha.add(txtFecha, BorderLayout.CENTER);

        JPanel colHora = new JPanel(new BorderLayout(0, 2));
        colHora.setOpaque(false);
        colHora.add(new FormularioLabel("Hora Salida:"), BorderLayout.NORTH);
        txtHoraSalida = new JTextField();
        txtHoraSalida.putClientProperty("JTextField.placeholderText", "Ej. 08:15");
        colHora.add(txtHoraSalida, BorderLayout.CENTER);

        panelFechaHora.add(colFecha);
        panelFechaHora.add(colHora);
        panelFormulario.add(panelFechaHora, gbc);

        gbc.gridy = 7;
        gbc.insets = new Insets(20, 12, 8, 12); 

        BotonPrimario btnPublicar = new BotonPrimario("Publicar Itinerario");
        panelFormulario.add(btnPublicar, gbc);

        panelFormulario.setPreferredSize(new Dimension(320, 0));
        panelPlanificacion.add(panelFormulario, BorderLayout.WEST);


        // --- PANEL DERECHO: TABLA + DETALLES ---
        JPanel panelDerecho = new JPanel(new BorderLayout(0, 15)); // Separación vertical de 15px entre tabla y cuadro
        panelDerecho.setBackground(Color.WHITE);

        String[] columnas = {"Ruta", "Hora Salida", "Conductor", "Unidad", "Estado"};
        modeloTablaItinerario = new DefaultTableModel(columnas, 0) {
            @Override 
            public boolean isCellEditable(int row, int column) { return false; }
        };

        Vector<Itinerario> itinerarios = new Vector<>(ConsultasBD.obtenerItinerarios());
        for (Itinerario itinerario : itinerarios) {
            modeloTablaItinerario.addRow(new Object[]{
                itinerario.getTipoRuta()+" - "+itinerario.getNombreRuta(), 
                itinerario.getFecha()+" - "+itinerario.getHora(), 
                itinerario.getNombreChofer(), // Asumiendo que agregaste este atributo a la clase Itinerario
                itinerario.getModeloUnidad(), 
                itinerario.getEstado()
            });    
        }

        tablaItinerarioSemanal = new TablaEstilizada(modeloTablaItinerario);

        JScrollPane scrollTabla = new JScrollPane(tablaItinerarioSemanal);
        scrollTabla.setBorder(BorderFactory.createLineBorder(Paleta.ARENA_FONDO, 1));
        panelDerecho.add(scrollTabla, BorderLayout.CENTER);

        // Cuadro de texto inferior para los detalles
        JTextArea txtDetalleItinerario = new JTextArea("Selecciona un itinerario de la lista para consultar su informacion detallada.");
        txtDetalleItinerario.setFont(new Font("Segoe UI", Font.ITALIC, 13));
        txtDetalleItinerario.setEditable(false);
        txtDetalleItinerario.setBackground(Paleta.ARENA_FONDO);
        txtDetalleItinerario.setForeground(Paleta.AZUL_OSCURO);
        txtDetalleItinerario.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        panelDerecho.add(txtDetalleItinerario, BorderLayout.SOUTH);

        panelPlanificacion.add(panelDerecho, BorderLayout.CENTER);

        // --- EVENTO DE SELECCIÓN PARA ACTUALIZAR EL TEXTO ---
        tablaItinerarioSemanal.getSelectionModel().addListSelectionListener(e -> {
            int fila = tablaItinerarioSemanal.getSelectedRow();
            if (fila != -1) {
                // Extraemos los valores de la fila seleccionada
                String ruta = modeloTablaItinerario.getValueAt(fila, 0).toString();
                String salida = modeloTablaItinerario.getValueAt(fila, 1).toString();
                String conductor = modeloTablaItinerario.getValueAt(fila, 2).toString();
                String unidad = modeloTablaItinerario.getValueAt(fila, 3).toString();
                String estado = modeloTablaItinerario.getValueAt(fila, 4).toString();
                
                txtDetalleItinerario.setText("Servicio hacia: " + ruta + " | Programado para: " + salida + 
                                            "\nOperado por: " + conductor + " en unidad " + unidad + 
                                            " | Estatus actual: [" + estado + "]");
            }
        });

        btnPublicar.addActionListener(e -> {
            ItemCombo rutaSeleccionada = (ItemCombo) cmbRuta.getSelectedItem();
            ItemCombo conductorSeleccionado = (ItemCombo) cmbConductor.getSelectedItem();
            ItemCombo unidadSeleccionada = (ItemCombo) cmbUnidad.getSelectedItem();
            
            if (rutaSeleccionada == null || conductorSeleccionado == null || unidadSeleccionada == null) {
                JOptionPane.showMessageDialog(this, "Debe seleccionar una ruta, un conductor y una unidad.", "Selección Inválida", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String hora = txtHoraSalida.getText().trim();
            String fecha = txtFecha.getText().trim();
            
            if (hora.isEmpty() || fecha.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Complete la fecha y la hora de salida.", "Campos Vacíos", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (!hora.matches("[0-1][0-9]:[0-5][0-9]") || !fecha.matches("[0-9]{4}-[0-1][0-9]-[0-3][0-9]")) {
                JOptionPane.showMessageDialog(this, "La fecha o la hora están en formatos incorrectos. \n Formatos: AAA-MM-DD  00:00", "Formatos inválidos", JOptionPane.WARNING_MESSAGE);
                return;
            }

            ItinerarioDAO gestion = new ItinerarioDAO();

            Itinerario nuevoItinerario = gestion.registrarItinerario(
                rutaSeleccionada.getId(), 
                unidadSeleccionada.getId(), 
                conductorSeleccionado.getId(), 
                fecha, 
                hora
            );

            if (nuevoItinerario != null) {
                String textoRutaVisual = nuevoItinerario.getTipoRuta() + " - " + nuevoItinerario.getNombreRuta();
                String textoConductor = conductorSeleccionado.toString();
                
                modeloTablaItinerario.addRow(new Object[]{
                    textoRutaVisual, 
                    nuevoItinerario.getFecha()+" - "+nuevoItinerario.getHora(), 
                    textoConductor, 
                    nuevoItinerario.getModeloUnidad(), 
                    nuevoItinerario.getEstado()
                });
                
                txtHoraSalida.setText(""); 
                txtFecha.setText("");
                
                JOptionPane.showMessageDialog(this, "Itinerario publicado con éxito para la semana.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Ocurrió un error interno al registrar el itinerario.", "Error de Base de Datos", JOptionPane.ERROR_MESSAGE);
            }
        });

        return panelPlanificacion;
    }

    private JPanel crearPanelFlota() {
        JPanel panelFlota = new JPanel(new BorderLayout(20, 20));
        panelFlota.setBackground(Color.WHITE);
        panelFlota.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        String[] columnasUnidades = {"Placa", "Modelo del Vehiculo", "Capacidad Maxima", "Estado Tecnico"};
        modeloTablaUnidades = new DefaultTableModel(columnasUnidades, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        Vector<Unidad> unidades = new Vector<>(ConsultasBD.obtenerUnidades());
        for (Unidad unidad : unidades) {
            modeloTablaUnidades.addRow(new Object[]{unidad.getPlaca(), unidad.getModelo(), unidad.getCapacidad()+" pasajeros", unidad.getEstado()});
        }
        
        // Instanciamos la TablaEstilizada
        tablaUnidadesFlota = new TablaEstilizada(modeloTablaUnidades);
        
        JScrollPane scrollUnidades = new JScrollPane(tablaUnidadesFlota);
        scrollUnidades.setBorder(BorderFactory.createLineBorder(Paleta.ARENA_FONDO, 1));
        panelFlota.add(scrollUnidades, BorderLayout.CENTER);

        JPanel panelInferiorFlota = new JPanel(new BorderLayout(15, 15));
        panelInferiorFlota.setBackground(Color.WHITE);

        JTextArea txtEstadoUnidad = new JTextArea("Selecciona una unidad de la lista para consultar su estado detallado.");
        txtEstadoUnidad.setFont(new Font("Segoe UI", Font.ITALIC, 13));
        txtEstadoUnidad.setEditable(false);
        txtEstadoUnidad.setBackground(Paleta.ARENA_FONDO);
        txtEstadoUnidad.setForeground(Paleta.AZUL_OSCURO);
        txtEstadoUnidad.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        panelInferiorFlota.add(txtEstadoUnidad, BorderLayout.CENTER);

        JPanel panelAccionesFlota = new JPanel(new GridLayout(2, 1, 0, 10));
        panelAccionesFlota.setBackground(Color.WHITE);
        
        // Uso de Componentes para Botones
        BotonPrimario btnAgregarUnidad = new BotonPrimario("Agregar Nueva Unidad");
        BotonSecundario btnEditarUnidad = new BotonSecundario("Editar Unidad Seleccionada");

        panelAccionesFlota.add(btnAgregarUnidad);
        panelAccionesFlota.add(btnEditarUnidad);
        panelInferiorFlota.add(panelAccionesFlota, BorderLayout.EAST);
        panelFlota.add(panelInferiorFlota, BorderLayout.SOUTH);

        tablaUnidadesFlota.getSelectionModel().addListSelectionListener(e -> {
            int fila = tablaUnidadesFlota.getSelectedRow();
            if (fila != -1) {
                String placa = modeloTablaUnidades.getValueAt(fila, 0).toString();
                String estado = modeloTablaUnidades.getValueAt(fila, 3).toString();
                String modelo = modeloTablaUnidades.getValueAt(fila, 1).toString();
                txtEstadoUnidad.setText("Unidad: " + placa + " | Estado del vehículo "+modelo+" bajo la condicion de [" + estado + "].");
            }
        });

        btnAgregarUnidad.addActionListener(e -> {
            String placa;
            while (true) {
                placa = JOptionPane.showInputDialog(this, "Ingrese la placa de la nueva unidad:", "Agregar Unidad", JOptionPane.QUESTION_MESSAGE);
                
                if (placa == null) return; 
                
                placa = placa.trim().toUpperCase(); // Limpiamos espacios y forzamos mayúsculas
                if (placa.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "La placa no puede estar vacía.", "Validación Fallida", JOptionPane.ERROR_MESSAGE);
                } else {
                    break;
                }
            }

            String modelo;
            while (true) {
                modelo = JOptionPane.showInputDialog(this, "Ingrese el modelo del vehiculo:", "Agregar Unidad", JOptionPane.QUESTION_MESSAGE);
                if (modelo == null) return;
                
                modelo = modelo.trim();
                if (modelo.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "El modelo no puede estar vacío.", "Validación Fallida", JOptionPane.ERROR_MESSAGE);
                } else {
                    break; 
                }
            }

            int capacidad;
            while (true) {
                String capInput = JOptionPane.showInputDialog(this, "Ingrese la capacidad maxima de pasajeros:", "Agregar Unidad", JOptionPane.QUESTION_MESSAGE);
                if (capInput == null) return;
                
                capInput = capInput.trim();
                
                if (capInput.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "La capacidad no puede estar vacía.", "Validación Fallida", JOptionPane.ERROR_MESSAGE);
                } else if (!capInput.matches("\\d+")) { 
                    JOptionPane.showMessageDialog(this, "La capacidad debe ser un número entero, sin letras ni espacios.", "Formato Inválido", JOptionPane.ERROR_MESSAGE);
                } else {
                    capacidad = Integer.parseInt(capInput);
                    
                    if (capacidad <= 0) {
                        JOptionPane.showMessageDialog(this, "La capacidad debe ser mayor a 0.", "Dato Ilógico", JOptionPane.ERROR_MESSAGE);
                    } else {
                        break;
                    }
                }
            }

            UnidadDAO gestion = new UnidadDAO();
            Unidad unidad = gestion.registrarUnidad(placa, modelo, capacidad);

            if (unidad != null) {
                System.out.println(unidad);
                
                modeloTablaUnidades.addRow(new Object[]{unidad.getPlaca(), unidad.getModelo(), unidad.getCapacidad() + " Pasajeros", unidad.getEstado()});
                
                JOptionPane.showMessageDialog(this, "Unidad incorporada con éxito a la flota semanal.", "Registro Completo", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Error al registrar la unidad. Es posible que la placa ingresada ya exista en el sistema.", "Error de Base de Datos", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnEditarUnidad.addActionListener(e -> {
            int filaSeleccionada = tablaUnidadesFlota.getSelectedRow();
            
            if (filaSeleccionada != -1) {
                String placaActual = modeloTablaUnidades.getValueAt(filaSeleccionada, 0).toString();
                
                String capacidadActualString = modeloTablaUnidades.getValueAt(filaSeleccionada, 2).toString().replace(" Pasajeros", "").trim();
                
                
                String[] opcionesEstado = {"activo", "en mantenimiento", "fuera de servicio"};
                String nuevoEstado = (String) JOptionPane.showInputDialog(this, 
                        "Seleccione el nuevo estado tecnico para la unidad " + placaActual + ":", 
                        "Editar Estado Operativo", 
                        JOptionPane.QUESTION_MESSAGE, 
                        null, opcionesEstado, opcionesEstado[0]);
                
                if (nuevoEstado != null) {
                    
                    int capacidadFinal = 0;
                    while (true) {
                        String nuevaCapacidadStr = JOptionPane.showInputDialog(this, "Modificar Capacidad Maxima:", capacidadActualString);
                        
                        if (nuevaCapacidadStr == null) return;
                        
                        nuevaCapacidadStr = nuevaCapacidadStr.trim();
                        
                        if (nuevaCapacidadStr.isEmpty()) {
                            JOptionPane.showMessageDialog(this, "La capacidad no puede estar vacía.", "Validación Fallida", JOptionPane.ERROR_MESSAGE);
                        } else if (!nuevaCapacidadStr.matches("\\d+")) {
                            JOptionPane.showMessageDialog(this, "La capacidad debe ser un número entero, sin letras ni espacios.", "Formato Inválido", JOptionPane.ERROR_MESSAGE);
                        } else {
                            capacidadFinal = Integer.parseInt(nuevaCapacidadStr);
                            if (capacidadFinal <= 0) {
                                JOptionPane.showMessageDialog(this, "La capacidad debe ser mayor a 0.", "Dato Ilógico", JOptionPane.ERROR_MESSAGE);
                            } else {
                                break;
                            }
                        }
                    }

                    // --- EJECUCIÓN EN LA BASE DE DATOS ---
                    UnidadDAO gestion = new UnidadDAO();
                    boolean exito = gestion.actualizarUnidad(placaActual, capacidadFinal, nuevoEstado);
                    
                    if (exito) {
                        modeloTablaUnidades.setValueAt(capacidadFinal + " Pasajeros", filaSeleccionada, 2);
                        modeloTablaUnidades.setValueAt(nuevoEstado, filaSeleccionada, 3);
                        
                        txtEstadoUnidad.setText("Unidad: " + placaActual + " | Diagnostico tecnico: Operando bajo la condicion de [" + nuevoEstado + "].");
                        JOptionPane.showMessageDialog(this, "Especificaciones de la unidad actualizadas con exito.", "Modificacion Guardada", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(this, 
                            "No se pudo guardar la modificación.\nVerifique que la unidad no se encuentre actualmente prestando un servicio activo.", 
                            "Edición Denegada", 
                            JOptionPane.WARNING_MESSAGE);
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "Por favor, seleccione una unidad de la lista para poder editarla.", "Fila no seleccionada", JOptionPane.WARNING_MESSAGE);
            }
        });

        return panelFlota;
    }

    private JPanel crearPanelReportes() {
        JPanel panelReportes = new JPanel(new BorderLayout(20, 20));
        panelReportes.setBackground(Color.WHITE);
        panelReportes.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel panelTarjetas = new JPanel(new GridLayout(1, 3, 20, 0));
        panelTarjetas.setBackground(Color.WHITE);
        
        // ¡Usamos el nuevo componente TarjetaMetrica!
        panelTarjetas.add(new TarjetaMetrica("Total Pasajeros", "1,248", "Usuarios esta semana"));
        panelTarjetas.add(new TarjetaMetrica("Ruta mas Solicitada", "Guarenas", "Alta demanda extraurbana"));
        panelTarjetas.add(new TarjetaMetrica("Unidades Activas", "8 / 10", "2 en Mantenimiento"));
        panelReportes.add(panelTarjetas, BorderLayout.NORTH);

        JPanel panelHistorial = new JPanel(new BorderLayout(10, 10));
        panelHistorial.setBackground(Color.WHITE);
        panelHistorial.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Paleta.ARENA_FONDO, 1), "Historico de Ocupacion Diario"
        ));

        String[] columnasHistorial = {"Fecha", "Servicios Ofrecidos", "Pasajeros Totales", "Promedio por Unidad"};
        DefaultTableModel modeloHistorial = new DefaultTableModel(columnasHistorial, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        modeloHistorial.addRow(new Object[]{"Lunes 01/06", "12 Viajes", "340 Alumnos", "28 Pasajeros"});
        modeloHistorial.addRow(new Object[]{"Martes 02/06", "14 Viajes", "412 Alumnos", "29 Pasajeros"});
        modeloHistorial.addRow(new Object[]{"Miercoles 03/06", "11 Viajes", "296 Alumnos", "26 Pasajeros"});
        
        // Instanciamos la TablaEstilizada
        TablaEstilizada tablaHistorial = new TablaEstilizada(modeloHistorial);
        panelHistorial.add(new JScrollPane(tablaHistorial), BorderLayout.CENTER);
        
        // Uso de BotonPrimario
        BotonPrimario btnExportarReporte = new BotonPrimario("Exportar Datos de Estadisticas Semanales");
        btnExportarReporte.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        
        panelHistorial.add(btnExportarReporte, BorderLayout.SOUTH);
        panelReportes.add(panelHistorial, BorderLayout.CENTER);

        btnExportarReporte.addActionListener(e -> JOptionPane.showMessageDialog(this, "Generando y descargando el archivo excel/PDF con todas las metricas analizadas.", "Exportar Reporte", JOptionPane.INFORMATION_MESSAGE));

        return panelReportes;
    }
}