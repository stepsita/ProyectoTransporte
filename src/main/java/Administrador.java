import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

// Importamos nuestra librería de componentes
import componentes.*;

public class Administrador extends JFrame {

    private JTabbedPane contenedorPestañas;
    
    // Cambiamos JTable por nuestro componente TablaEstilizada
    private TablaEstilizada tablaItinerarioSemanal;
    private DefaultTableModel modeloTablaItinerario;
    
    private TablaEstilizada tablaUnidadesFlota;
    private DefaultTableModel modeloTablaUnidades;

    private JComboBox<String> cmbRuta;
    private JComboBox<String> cmbConductor;
    private JComboBox<String> cmbUnidad;
    private JTextField txtHoraSalida;

    public Administrador() {
        setTitle("Sistema de Transporte UCV - Panel de Administrador");
        setSize(950, 640); 
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); 

        JPanel panelBase = new JPanel(new BorderLayout(15, 15));
        panelBase.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panelBase.setBackground(Paleta.ARENA_FONDO);

        JPanel panelNav = new JPanel(new BorderLayout());
        panelNav.setOpaque(false); // Fondo transparente para heredar el Arena
        
        JLabel lblBienvenida = new JLabel("Bienvenido, Administrador UCV");
        lblBienvenida.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblBienvenida.setForeground(Paleta.AZUL_OSCURO);
        panelNav.add(lblBienvenida, BorderLayout.WEST);

        // Usamos el Botón de Peligro de la librería
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

        // Utilizamos FormularioLabel para unificar la fuente de los textos
        gbc.gridy = 0;
        panelFormulario.add(new FormularioLabel("Seleccionar Ruta:"), gbc);
        gbc.gridy = 1;
        String[] opcionesRutas = {"Urbana - Plaza Venezuela", "Urbana - La Rinconada", "Extraurbana - Guarenas", "Extraurbana - Maracay"};
        cmbRuta = new JComboBox<>(opcionesRutas);
        panelFormulario.add(cmbRuta, gbc);

        gbc.gridy = 2;
        panelFormulario.add(new FormularioLabel("Asignar Conductor:"), gbc);
        gbc.gridy = 3;
        String[] opcionesConductores = {"Carlos Mendoza", "Jose Rodriguez", "Luis Gomez", "Ana Martinez"};
        cmbConductor = new JComboBox<>(opcionesConductores);
        panelFormulario.add(cmbConductor, gbc);

        gbc.gridy = 4;
        panelFormulario.add(new FormularioLabel("Asignar Unidad:"), gbc);
        gbc.gridy = 5;
        String[] opcionesUnidades = {"Yutong Urbano (Cap. 25)", "Encava Autobus (Cap. 40)"};
        cmbUnidad = new JComboBox<>(opcionesUnidades);
        panelFormulario.add(cmbUnidad, gbc);

        gbc.gridy = 6;
        panelFormulario.add(new FormularioLabel("Hora de Salida:"), gbc);
        gbc.gridy = 7;
        txtHoraSalida = new JTextField(10);
        txtHoraSalida.putClientProperty("JTextField.placeholderText", "Ej. 08:15 AM");
        panelFormulario.add(txtHoraSalida, gbc);

        gbc.gridy = 8;
        gbc.insets = new Insets(15, 12, 8, 12);
        
        // Uso de BotonPrimario
        BotonPrimario btnPublicar = new BotonPrimario("Publicar Itinerario");
        panelFormulario.add(btnPublicar, gbc);
        
        panelFormulario.setPreferredSize(new Dimension(280, 0));
        panelPlanificacion.add(panelFormulario, BorderLayout.WEST);

        String[] columnas = {"Ruta/Destino", "Hora Salida", "Conductor", "Unidad", "Estado"};
        modeloTablaItinerario = new DefaultTableModel(columnas, 0) {
            @Override 
            public boolean isCellEditable(int row, int column) { return false; }
        };

        modeloTablaItinerario.addRow(new Object[]{"Urbana - Plaza Venezuela", "07:30 AM", "Carlos Mendoza", "Yutong Urbano", "Programado"});
        modeloTablaItinerario.addRow(new Object[]{"Extraurbana - Guarenas", "04:30 PM", "Jose Rodriguez", "Encava Autobus", "Programado"});

        // Instanciamos la TablaEstilizada (ya no hace falta configurarEstiloTabla)
        tablaItinerarioSemanal = new TablaEstilizada(modeloTablaItinerario);
        
        JScrollPane scrollTabla = new JScrollPane(tablaItinerarioSemanal);
        scrollTabla.setBorder(BorderFactory.createLineBorder(Paleta.ARENA_FONDO, 1));
        panelPlanificacion.add(scrollTabla, BorderLayout.CENTER);

        btnPublicar.addActionListener(e -> {
            String ruta = (String) cmbRuta.getSelectedItem();
            String conductor = (String) cmbConductor.getSelectedItem();
            String unidad = (String) cmbUnidad.getSelectedItem();
            String hora = txtHoraSalida.getText().trim();

            if (!hora.isEmpty()) {
                modeloTablaItinerario.addRow(new Object[]{ruta, hora, conductor, unidad.split(" ")[0], "Programado"});
                txtHoraSalida.setText(""); 
                JOptionPane.showMessageDialog(this, "Itinerario publicado con exito para la semana.", "Exito", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Por favor, defina una hora de salida.", "Campos Vacios", JOptionPane.WARNING_MESSAGE);
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

        modeloTablaUnidades.addRow(new Object[]{"UCV-78A", "Encava Autobus", "40 Pasajeros", "Activo"});
        modeloTablaUnidades.addRow(new Object[]{"UCV-12B", "Yutong Urbano", "25 Pasajeros", "En Mantenimiento"});
        modeloTablaUnidades.addRow(new Object[]{"UCV-90X", "Encava Autobus", "40 Pasajeros", "Activo"});

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
                txtEstadoUnidad.setText("Unidad: " + placa + " | Diagnostico tecnico: Operando bajo la condicion de [" + estado + "].");
            }
        });

        btnAgregarUnidad.addActionListener(e -> {
            String placa = JOptionPane.showInputDialog(this, "Ingrese la placa de la nueva unidad:", "Agregar Unidad", JOptionPane.QUESTION_MESSAGE);
            if (placa == null || placa.trim().isEmpty()) return; 

            String modelo = JOptionPane.showInputDialog(this, "Ingrese el modelo del vehiculo:", "Agregar Unidad", JOptionPane.QUESTION_MESSAGE);
            if (modelo == null || modelo.trim().isEmpty()) return;

            String capacidad = JOptionPane.showInputDialog(this, "Ingrese la capacidad maxima de pasajeros:", "Agregar Unidad", JOptionPane.QUESTION_MESSAGE);
            if (capacidad == null || capacidad.trim().isEmpty()) return;

            modeloTablaUnidades.addRow(new Object[]{placa.toUpperCase().trim(), modelo.trim(), capacidad.trim(), "Activo"});
            JOptionPane.showMessageDialog(this, "Unidad incorporada con exito a la flota semanal.", "Registro Completo", JOptionPane.INFORMATION_MESSAGE);
        });

        btnEditarUnidad.addActionListener(e -> {
            int filaSeleccionada = tablaUnidadesFlota.getSelectedRow();
            
            if (filaSeleccionada != -1) {
                String placaActual = modeloTablaUnidades.getValueAt(filaSeleccionada, 0).toString();
                String capacidadActual = modeloTablaUnidades.getValueAt(filaSeleccionada, 2).toString();
                
                String[] opcionesEstado = {"Activo", "En Mantenimiento", "Fuera de Servicio"};
                String nuevoEstado = (String) JOptionPane.showInputDialog(this, 
                        "Seleccione el nuevo estado tecnico para la unidad " + placaActual + ":", 
                        "Editar Estado Operativo", 
                        JOptionPane.QUESTION_MESSAGE, 
                        null, opcionesEstado, opcionesEstado[0]);
                
                if (nuevoEstado != null) {
                    String nuevaCapacidad = JOptionPane.showInputDialog(this, "Modificar Capacidad Maxima:", capacidadActual);
                    if (nuevaCapacidad == null || nuevaCapacidad.trim().isEmpty()) nuevaCapacidad = capacidadActual;

                    modeloTablaUnidades.setValueAt(nuevaCapacidad.trim(), filaSeleccionada, 2);
                    modeloTablaUnidades.setValueAt(nuevoEstado, filaSeleccionada, 3);
                    
                    txtEstadoUnidad.setText("Unidad: " + placaActual + " | Diagnostico tecnico: Operando bajo la condicion de [" + nuevoEstado + "].");
                    JOptionPane.showMessageDialog(this, "Especificaciones de la unidad actualizadas con exito.", "Modificacion Guardada", JOptionPane.INFORMATION_MESSAGE);
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