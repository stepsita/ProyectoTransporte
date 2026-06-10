import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Administrador extends JFrame {

    private JTabbedPane contenedorPestañas;
    private JTable tablaItinerarioSemanal;
    private DefaultTableModel modeloTablaItinerario;
    
    // Nuevos atributos para la gestión de unidades
    private JTable tablaUnidadesFlota;
    private DefaultTableModel modeloTablaUnidades;

    private JComboBox<String> cmbRuta;
    private JComboBox<String> cmbConductor;
    private JComboBox<String> cmbUnidad;
    private JTextField txtHoraSalida;

    private final Color colorAzulOscuro = new Color(0x11, 0x18, 0x44);   // #111844
    private final Color colorAzulMedio = new Color(0x4B, 0x56, 0x94);    // #4B5694
    private final Color colorAzulGris = new Color(0x72, 0x88, 0xAE);     // #7288AE
    private final Color colorArenaFondo = new Color(0xEA, 0xE0, 0xCF);    // #EAE0CF

    public Administrador() {
        setTitle("Sistema de Transporte UCV - Panel de Administrador");
        setSize(950, 640); // Ajustamos ligeramente el alto para dar espacio a los nuevos botones
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centra la ventana

       JPanel panelBase = new JPanel(new BorderLayout(15, 15));
        panelBase.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panelBase.setBackground(colorArenaFondo);

        JPanel panelNav = new JPanel(new BorderLayout());
        panelNav.setBackground(colorArenaFondo);
        
        JLabel lblBienvenida = new JLabel("Bienvenido, Administrador UCV");
        lblBienvenida.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblBienvenida.setForeground(colorAzulOscuro);
        panelNav.add(lblBienvenida, BorderLayout.WEST);

        JButton btnCerrarSesion = new JButton("Cerrar Sesion");
        btnCerrarSesion.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnCerrarSesion.setBackground(new Color(220, 53, 69)); 
        btnCerrarSesion.setForeground(Color.WHITE);
        btnCerrarSesion.setFocusPainted(false);
        panelNav.add(btnCerrarSesion, BorderLayout.EAST);
        panelBase.add(panelNav, BorderLayout.NORTH); 

        btnCerrarSesion.addActionListener(e -> {
            dispose(); 
            new InicioSesion().setVisible(true); 
        });

        contenedorPestañas = new JTabbedPane();
        contenedorPestañas.setFont(new Font("Segoe UI", Font.BOLD, 13));
        contenedorPestañas.setBackground(Color.WHITE);
        contenedorPestañas.setForeground(colorAzulOscuro);

        //pestañas de gestión y reportes (Agregamos la pestaña intermedia de flota)
        contenedorPestañas.addTab("Planificacion de Itinerario", crearPanelPlanificacion());
        contenedorPestañas.addTab("Gestion de Unidades", crearPanelFlota());
        contenedorPestañas.addTab("Reportes y Estadisticas", crearPanelReportes());

        panelBase.add(contenedorPestañas, BorderLayout.CENTER);
        add(panelBase);
    }

    //planificación de itinerarios 
    private JPanel crearPanelPlanificacion() {
        JPanel panelPlanificacion = new JPanel(new BorderLayout(20, 20));
        panelPlanificacion.setBackground(Color.WHITE);
        panelPlanificacion.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        //parte iaquierda: formulario de creación de servicios
        JPanel panelFormulario = new JPanel(new GridBagLayout());
        panelFormulario.setBackground(Color.WHITE);
        panelFormulario.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(colorAzulGris, 1), 
            "Crear Nuevo Servicio", 0, 0, 
            new Font("Segoe UI", Font.BOLD, 13), colorAzulOscuro
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 12, 8, 12);
        gbc.gridx = 0;

        //seleccionar Ruta
        gbc.gridy = 0;
        panelFormulario.add(new JLabel("Seleccionar Ruta:"), gbc);
        gbc.gridy = 1;
        String[] opcionesRutas = {"Urbana - Plaza Venezuela", "Urbana - La Rinconada", "Extraurbana - Guarenas", "Extraurbana - Maracay"};
        cmbRuta = new JComboBox<>(opcionesRutas);
        panelFormulario.add(cmbRuta, gbc);

        //asignar Conductor
        gbc.gridy = 2;
        panelFormulario.add(new JLabel("Asignar Conductor:"), gbc);
        gbc.gridy = 3;
        String[] opcionesConductores = {"Carlos Mendoza", "Jose Rodriguez", "Luis Gomez", "Ana Martinez"};
        cmbConductor = new JComboBox<>(opcionesConductores);
        panelFormulario.add(cmbConductor, gbc);

        //asignar Unidad
        gbc.gridy = 4;
        panelFormulario.add(new JLabel("Asignar Unidad:"), gbc);
        gbc.gridy = 5;
        String[] opcionesUnidades = {"Yutong Urbano (Cap. 25)", "Encava Autobus (Cap. 40)"};
        cmbUnidad = new JComboBox<>(opcionesUnidades);
        panelFormulario.add(cmbUnidad, gbc);

        //hora de Salida
        gbc.gridy = 6;
        panelFormulario.add(new JLabel("Hora de Salida:"), gbc);
        gbc.gridy = 7;
        txtHoraSalida = new JTextField(10);
        txtHoraSalida.putClientProperty("JTextField.placeholderText", "Ej. 08:15 AM");
        panelFormulario.add(txtHoraSalida, gbc);

        //botón de Publicar
        gbc.gridy = 8;
        gbc.insets = new Insets(15, 12, 8, 12);
        JButton btnPublicar = new JButton("Publicar Itinerario");
        btnPublicar.setBackground(colorAzulOscuro);
        btnPublicar.setForeground(Color.WHITE);
        btnPublicar.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnPublicar.setFocusPainted(false);
        panelFormulario.add(btnPublicar, gbc);
        panelFormulario.setPreferredSize(new Dimension(280, 0));
        panelPlanificacion.add(panelFormulario, BorderLayout.WEST);

        //parte derecha tabla de itinerarios programados
        String[] columnas = {"Ruta/Destino", "Hora Salida", "Conductor", "Unidad", "Estado"};
        modeloTablaItinerario = new DefaultTableModel(columnas, 0) {
            @Override //hacemos que las celdas no sean editables directamente
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        //datos de ejemplo pq no tenemos back 
        modeloTablaItinerario.addRow(new Object[]{"Urbana - Plaza Venezuela", "07:30 AM", "Carlos Mendoza", "Yutong Urbano", "Programado"});
        modeloTablaItinerario.addRow(new Object[]{"Extraurbana - Guarenas", "04:30 PM", "Jose Rodriguez", "Encava Autobus", "Programado"});

        tablaItinerarioSemanal = new JTable(modeloTablaItinerario);
        configurarEstiloTabla(tablaItinerarioSemanal);
        JScrollPane scrollTabla = new JScrollPane(tablaItinerarioSemanal);
        scrollTabla.setBorder(BorderFactory.createLineBorder(colorArenaFondo, 1));
        panelPlanificacion.add(scrollTabla, BorderLayout.CENTER);

        //evento para agregar un nuevo itinerario a la tabla 
        btnPublicar.addActionListener(e -> {
            String ruta = (String) cmbRuta.getSelectedItem();
            String conductor = (String) cmbConductor.getSelectedItem();
            String unidad = (String) cmbUnidad.getSelectedItem();
            String hora = txtHoraSalida.getText().trim();

            if (!hora.isEmpty()) {
                //agregamos el nuevo itinerario a la tabla con estado "Programado"
                modeloTablaItinerario.addRow(new Object[]{ruta, hora, conductor, unidad.split(" ")[0], "Programado"});
                txtHoraSalida.setText(""); // Limpia el campo
                JOptionPane.showMessageDialog(this, "Itinerario publicado con exito para la semana.", "Exito", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Por favor, defina una hora de salida.", "Campos Vacios", JOptionPane.WARNING_MESSAGE);
            }
        });

        return panelPlanificacion;
    }

    //vista de la gestión de las unidades (Nueva Pestaña Solicitada)
    private JPanel crearPanelFlota() {
        JPanel panelFlota = new JPanel(new BorderLayout(20, 20));
        panelFlota.setBackground(Color.WHITE);
        panelFlota.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        //tabla con la lista sencilla de las unidades registradas
        String[] columnasUnidades = {"Placa", "Modelo del Vehiculo", "Capacidad Maxima", "Estado Tecnico"};
        modeloTablaUnidades = new DefaultTableModel(columnasUnidades, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        //datos de ejemplo iniciales para la flota
        modeloTablaUnidades.addRow(new Object[]{"UCV-78A", "Encava Autobus", "40 Pasajeros", "Activo"});
        modeloTablaUnidades.addRow(new Object[]{"UCV-12B", "Yutong Urbano", "25 Pasajeros", "En Mantenimiento"});
        modeloTablaUnidades.addRow(new Object[]{"UCV-90X", "Encava Autobus", "40 Pasajeros", "Activo"});

        tablaUnidadesFlota = new JTable(modeloTablaUnidades);
        configurarEstiloTabla(tablaUnidadesFlota);
        JScrollPane scrollUnidades = new JScrollPane(tablaUnidadesFlota);
        scrollUnidades.setBorder(BorderFactory.createLineBorder(colorArenaFondo, 1));
        panelFlota.add(scrollUnidades, BorderLayout.CENTER);

        //bloque de abajo para mostrar detalles y alojar botones de agregar y editar
        JPanel panelInferiorFlota = new JPanel(new BorderLayout(15, 15));
        panelInferiorFlota.setBackground(Color.WHITE);

        //caja de texto que muestra el estado en la parte de abajo al seleccionar una unidad
        JTextArea txtEstadoUnidad = new JTextArea("Selecciona una unidad de la lista para consultar su estado detallado.");
        txtEstadoUnidad.setFont(new Font("Segoe UI", Font.ITALIC, 13));
        txtEstadoUnidad.setEditable(false);
        txtEstadoUnidad.setBackground(colorArenaFondo);
        txtEstadoUnidad.setForeground(colorAzulOscuro);
        txtEstadoUnidad.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        panelInferiorFlota.add(txtEstadoUnidad, BorderLayout.CENTER);

        //panel derecho para agrupar los botones de inserción y modificación
        JPanel panelAccionesFlota = new JPanel(new GridLayout(2, 1, 0, 10));
        panelAccionesFlota.setBackground(Color.WHITE);
        
        JButton btnAgregarUnidad = new JButton("Agregar Nueva Unidad");
        btnAgregarUnidad.setBackground(colorAzulOscuro);
        btnAgregarUnidad.setForeground(Color.WHITE);
        btnAgregarUnidad.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnAgregarUnidad.setFocusPainted(false);
        
        JButton btnEditarUnidad = new JButton("Editar Unidad Seleccionada");
        btnEditarUnidad.setBackground(colorAzulMedio);
        btnEditarUnidad.setForeground(Color.WHITE);
        btnEditarUnidad.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnEditarUnidad.setFocusPainted(false);

        panelAccionesFlota.add(btnAgregarUnidad);
        panelAccionesFlota.add(btnEditarUnidad);
        panelInferiorFlota.add(panelAccionesFlota, BorderLayout.EAST);
        panelFlota.add(panelInferiorFlota, BorderLayout.SOUTH);

        //evento para actualizar el texto descriptivo del estado al seleccionar la fila
        tablaUnidadesFlota.getSelectionModel().addListSelectionListener(e -> {
            int fila = tablaUnidadesFlota.getSelectedRow();
            if (fila != -1) {
                String placa = modeloTablaUnidades.getValueAt(fila, 0).toString();
                String estado = modeloTablaUnidades.getValueAt(fila, 3).toString();
                txtEstadoUnidad.setText("Unidad: " + placa + " | Diagnostico tecnico: Operando bajo la condicion de [" + estado + "].");
            }
        });

        // --- LOGIC REAL PARA AGREGAR UNIDAD (Interactiva en memoria RAM) ---
        btnAgregarUnidad.addActionListener(e -> {
            // Pedimos los datos paso a paso usando cajitas de entrada de texto sencillas
            String placa = JOptionPane.showInputDialog(this, "Ingrese la placa de la nueva unidad:", "Agregar Unidad", JOptionPane.QUESTION_MESSAGE);
            if (placa == null || placa.trim().isEmpty()) return; // Si cancela o deja vacío, no hace nada

            String modelo = JOptionPane.showInputDialog(this, "Ingrese el modelo del vehiculo:", "Agregar Unidad", JOptionPane.QUESTION_MESSAGE);
            if (modelo == null || modelo.trim().isEmpty()) return;

            String capacidad = JOptionPane.showInputDialog(this, "Ingrese la capacidad maxima de pasajeros:", "Agregar Unidad", JOptionPane.QUESTION_MESSAGE);
            if (capacidad == null || capacidad.trim().isEmpty()) return;

            // Agregamos la nueva fila directamente al modelo de la tabla con estado inicial "Activo"
            modeloTablaUnidades.addRow(new Object[]{placa.toUpperCase().trim(), modelo.trim(), capacidad.trim(), "Activo"});
            
            JOptionPane.showMessageDialog(this, "Unidad incorporada con exito a la flota semanal.", "Registro Completo", JOptionPane.INFORMATION_MESSAGE);
        });

        // --- LOGICA REAL PARA EDITAR UNIDAD SELECCIONADA ---
        btnEditarUnidad.addActionListener(e -> {
            // Verificamos primero si el administrador seleccionó una fila de la tabla
            int filaSeleccionada = tablaUnidadesFlota.getSelectedRow();
            
            if (filaSeleccionada != -1) {
                // Obtenemos los valores actuales de esa fila por si el usuario los quiere usar de referencia
                String placaActual = modeloTablaUnidades.getValueAt(filaSeleccionada, 0).toString();
                String modeloActual = modeloTablaUnidades.getValueAt(filaSeleccionada, 1).toString();
                String capacidadActual = modeloTablaUnidades.getValueAt(filaSeleccionada, 2).toString();
                
                // Opciones disponibles para el estado técnico (Evitamos errores de escritura con un combo desplegable)
                String[] opcionesEstado = {"Activo", "En Mantenimiento", "Fuera de Servicio"};
                String nuevoEstado = (String) JOptionPane.showInputDialog(this, 
                        "Seleccione el nuevo estado tecnico para la unidad " + placaActual + ":", 
                        "Editar Estado Operativo", 
                        JOptionPane.QUESTION_MESSAGE, 
                        null, opcionesEstado, opcionesEstado[0]);
                
                if (nuevoEstado != null) {
                    // Pedimos actualizar también la capacidad si es necesario
                    String nuevaCapacidad = JOptionPane.showInputDialog(this, "Modificar Capacidad Maxima:", capacidadActual);
                    if (nuevaCapacidad == null || nuevaCapacidad.trim().isEmpty()) nuevaCapacidad = capacidadActual;

                    // Actualizamos el modelo interno en las columnas correspondientes (Columna 2 y Columna 3)
                    modeloTablaUnidades.setValueAt(nuevaCapacidad.trim(), filaSeleccionada, 2);
                    modeloTablaUnidades.setValueAt(nuevoEstado, filaSeleccionada, 3);
                    
                    // Forzamos la actualización manual del cuadro de texto descriptivo inferior de inmediato
                    txtEstadoUnidad.setText("Unidad: " + placaActual + " | Diagnostico tecnico: Operando bajo la condicion de [" + nuevoEstado + "].");
                    
                    JOptionPane.showMessageDialog(this, "Especificaciones de la unidad actualizadas con exito.", "Modificacion Guardada", JOptionPane.INFORMATION_MESSAGE);
                }
            } else {
                // Alerta en caso de que le den al botón sin tocar la tabla
                JOptionPane.showMessageDialog(this, "Por favor, seleccione una unidad de la lista para poder editarla.", "Fila no seleccionada", JOptionPane.WARNING_MESSAGE);
            }
        });

        return panelFlota;
    }

    //reportes
    private JPanel crearPanelReportes() {
        JPanel panelReportes = new JPanel(new BorderLayout(20, 20));
        panelReportes.setBackground(Color.WHITE);
        panelReportes.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        //resumen de metrikcas
        JPanel panelTarjetas = new JPanel(new GridLayout(1, 3, 20, 0));
        panelTarjetas.setBackground(Color.WHITE);
        panelTarjetas.add(crearTarjetaMétrica("Total Pasajeros", "1,248", "Usuarios esta semana"));
        panelTarjetas.add(crearTarjetaMétrica("Ruta mas Solicitada", "Guarenas", "Alta demanda extraurbana"));
        panelTarjetas.add(crearTarjetaMétrica("Unidades Activas", "8 / 10", "2 en Mantenimiento"));
        panelReportes.add(panelTarjetas, BorderLayout.NORTH);

        //historial de ocupación diaria
        JPanel panelHistorial = new JPanel(new BorderLayout(10, 10));
        panelHistorial.setBackground(Color.WHITE);
        panelHistorial.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(colorArenaFondo, 1), "Historico de Ocupacion Diario"
        ));

        String[] columnasHistorial = {"Fecha", "Servicios Ofrecidos", "Pasajeros Totales", "Promedio por Unidad"};
        DefaultTableModel modeloHistorial = new DefaultTableModel(columnasHistorial, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        modeloHistorial.addRow(new Object[]{"Lunes 01/06", "12 Viajes", "340 Alumnos", "28 Pasajeros"});
        modeloHistorial.addRow(new Object[]{"Martes 02/06", "14 Viajes", "412 Alumnos", "29 Pasajeros"});
        modeloHistorial.addRow(new Object[]{"Miercoles 03/06", "11 Viajes", "296 Alumnos", "26 Pasajeros"});
        
        JTable tablaHistorial = new JTable(modeloHistorial);
        configurarEstiloTabla(tablaHistorial);
        panelHistorial.add(new JScrollPane(tablaHistorial), BorderLayout.CENTER);
        
        //botón inferior para generar el reporte / exportar los datos (Añadido)
        JButton btnExportarReporte = new JButton("Exportar Datos de Estadisticas Semanales");
        btnExportarReporte.setBackground(colorAzulOscuro);
        btnExportarReporte.setForeground(Color.WHITE);
        btnExportarReporte.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnExportarReporte.setFocusPainted(false);
        btnExportarReporte.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        
        panelHistorial.add(btnExportarReporte, BorderLayout.SOUTH);
        panelReportes.add(panelHistorial, BorderLayout.CENTER);

        //evento simulado del boton de exportacion
        btnExportarReporte.addActionListener(e -> JOptionPane.showMessageDialog(this, "Generando y descargando el archivo excel/PDF con todas las metricas analizadas.", "Exportar Reporte", JOptionPane.INFORMATION_MESSAGE));

        return panelReportes;
    }

    // métodos auxiliares para crear tarjetas de métricas y configurar tablas
    private JPanel crearTarjetaMétrica(String titulo, String valor, String subtitulo) {
        JPanel tarjeta = new JPanel(new GridLayout(3, 1, 2, 2));
        tarjeta.setBackground(colorArenaFondo);
        tarjeta.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(colorAzulGris, 1),
            BorderFactory.createEmptyBorder(12, 15, 12, 15)
        ));

        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblTitulo.setForeground(colorAzulMedio);

        JLabel lblValor = new JLabel(valor);
        lblValor.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblValor.setForeground(colorAzulOscuro);

        JLabel lblSub = new JLabel(subtitulo);
        lblSub.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        lblSub.setForeground(colorAzulOscuro);

        tarjeta.add(lblTitulo);
        tarjeta.add(lblValor);
        tarjeta.add(lblSub);

        return tarjeta;
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
        encabezado.setReorderingAllowed(false);
    }
}