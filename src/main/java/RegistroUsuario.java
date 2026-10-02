import  componentes.*;

import javax.swing.*;
import java.awt.*;

public class RegistroUsuario extends JFrame {

    public RegistroUsuario() {
        setTitle("Sistema de Transporte UCV - Crear Cuenta");
        setSize(500, 600); 
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Uso directo de la Paleta centralizada
        JPanel panelPrincipal = new JPanel(new BorderLayout(20, 20));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(25, 35, 25, 35));
        panelPrincipal.setBackground(Paleta.ARENA_FONDO); 

        // Encabezado usando componentes reutilizables
        JPanel panelHeader = new JPanel(new GridLayout(2, 1, 2, 2));
        panelHeader.setOpaque(false);
        panelHeader.add(new TituloLabel("Registro de Usuario"));
        panelHeader.add(new SubtituloLabel("Crea tu cuenta para el sistema de transporte"));
        panelPrincipal.add(panelHeader, BorderLayout.NORTH);

        // Formulario
        JPanel panelFormulario = new JPanel(new GridBagLayout());
        panelFormulario.setBackground(Color.WHITE);
        panelFormulario.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Paleta.AZUL_GRIS, 1),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.weightx = 1.0;

        // --- CÉDULA ---
        gbc.gridy = 0; gbc.insets = new Insets(5, 0, 2, 0);
        panelFormulario.add(new FormularioLabel("Cedula:"), gbc);
        
        gbc.gridy = 1; gbc.insets = new Insets(0, 0, 10, 0);
        JTextField txtCedula = new JTextField();
        panelFormulario.add(txtCedula, gbc);

        // --- NOMBRE Y APELLIDO ---
        gbc.gridy = 2; gbc.insets = new Insets(0, 0, 10, 0);
        
        JPanel panelNombres = new JPanel(new GridLayout(1, 2, 10, 0));
        panelNombres.setOpaque(false);
        
        // (Nombre)
        JPanel colNombre = new JPanel(new BorderLayout(0, 2));
        colNombre.setOpaque(false);
        colNombre.add(new FormularioLabel("Nombre:"), BorderLayout.NORTH);
        JTextField txtNombre = new JTextField();
        colNombre.add(txtNombre, BorderLayout.CENTER);
        
        // (Apellido)
        JPanel colApellido = new JPanel(new BorderLayout(0, 2));
        colApellido.setOpaque(false);
        colApellido.add(new FormularioLabel("Apellido:"), BorderLayout.NORTH);
        JTextField txtApellido = new JTextField();
        colApellido.add(txtApellido, BorderLayout.CENTER);
        
        panelNombres.add(colNombre);
        panelNombres.add(colApellido);
        panelFormulario.add(panelNombres, gbc);

        // --- CORREO ---
        gbc.gridy = 3; gbc.insets = new Insets(5, 0, 2, 0);
        panelFormulario.add(new FormularioLabel("Correo UCV:"), gbc);
        
        gbc.gridy = 4; gbc.insets = new Insets(0, 0, 10, 0);
        JTextField txtCorreo = new JTextField();
        panelFormulario.add(txtCorreo, gbc);

        // --- FACULTAD Y ESCUELA (Paneles Anidados lado a lado) ---
        gbc.gridy = 5; gbc.insets = new Insets(0, 0, 10, 0);
        JPanel panelAcademicos = new JPanel(new GridLayout(1, 2, 10, 0));
        panelAcademicos.setOpaque(false);
        
        JPanel colFacultad = new JPanel(new BorderLayout(0, 2));
        colFacultad.setOpaque(false);
        colFacultad.add(new FormularioLabel("Facultad:"), BorderLayout.NORTH);
        JTextField txtFacultad = new JTextField();
        colFacultad.add(txtFacultad, BorderLayout.CENTER);
        
        JPanel colEscuela = new JPanel(new BorderLayout(0, 2));
        colEscuela.setOpaque(false);
        colEscuela.add(new FormularioLabel("Escuela: (Para estudiantes)"), BorderLayout.NORTH);
        JTextField txtEscuela = new JTextField();
        colEscuela.add(txtEscuela, BorderLayout.CENTER);
        
        panelAcademicos.add(colFacultad);
        panelAcademicos.add(colEscuela);
        panelFormulario.add(panelAcademicos, gbc);

        // --- TIPO DE PASAJERO ---
        gbc.gridy = 6; gbc.insets = new Insets(5, 0, 2, 0);
        panelFormulario.add(new FormularioLabel("Tipo de miembro:"), gbc);
        
        gbc.gridy = 7; gbc.insets = new Insets(0, 0, 10, 0);
        JComboBox<ItemCombo> cmbTipoPasajero = new JComboBox<>(ConsultasBD.obtenerTiposPasajero());
        panelFormulario.add(cmbTipoPasajero, gbc);

        // --- CONTRASEÑA ---
        gbc.gridy = 8; gbc.insets = new Insets(5, 0, 2, 0);
        panelFormulario.add(new FormularioLabel("Contraseña:"), gbc);
        
        gbc.gridy = 9; gbc.insets = new Insets(0, 0, 10, 0);
        JPasswordField txtClave = new JPasswordField();
        panelFormulario.add(txtClave, gbc);

        panelPrincipal.add(panelFormulario, BorderLayout.CENTER);

        // Botones inferiores instanciados desde la librería
        JPanel panelBotones = new JPanel(new GridLayout(1, 2, 15, 0));
        panelBotones.setOpaque(false);
        
        BotonSecundario btnVolver = new BotonSecundario("Volver");
        panelBotones.add(btnVolver);
        
        BotonPrimario btnRegistrar = new BotonPrimario("Registrar");
        panelBotones.add(btnRegistrar);
        
        panelPrincipal.add(panelBotones, BorderLayout.SOUTH);

        add(panelPrincipal);

        // --- EVENTO DE REGISTRO ---
        btnRegistrar.addActionListener(e -> {
            
            
            String cedulaTexto = txtCedula.getText().trim();
            String nombreTexto = txtNombre.getText().trim();
            String apellidoTexto = txtApellido.getText().trim();
            String correoTexto = txtCorreo.getText().trim();
            String facultadTexto = txtFacultad.getText().trim();
            String escuelaTexto = txtEscuela.getText().trim();
            String claveTexto = new String(txtClave.getPassword());

            if (cedulaTexto.isEmpty() || nombreTexto.isEmpty() || correoTexto.isEmpty() || facultadTexto.isEmpty() || claveTexto.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Complete todos los campos obligatorios.", "Campos Vacíos", JOptionPane.WARNING_MESSAGE);
                return; 
            }

            if (!cedulaTexto.matches("\\d+")) {
                JOptionPane.showMessageDialog(this, "La cédula debe contener únicamente números, sin puntos ni espacios.", "Formato Inválido", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (!cedulaTexto.matches("\\d{6,9}")) {
                JOptionPane.showMessageDialog(this, "La cédula debe ser válida.", "Formato Inválido", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (!correoTexto.matches("^[\\w-\\.]+@[\\w-\\.]+\\.[a-zA-Z]{2,63}$")) {
                JOptionPane.showMessageDialog(this, "La dirección de correo es inválida.", "Formato Inválido", JOptionPane.WARNING_MESSAGE);
                return;
            }

            
            ItemCombo tipoSeleccionado = (ItemCombo) cmbTipoPasajero.getSelectedItem();
            boolean estudiante = tipoSeleccionado.getNombre().equals("Estudiante");

            if (estudiante && escuelaTexto.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Como estudiante, debe indicar su Escuela.", "Campos Vacíos", JOptionPane.WARNING_MESSAGE);
                return;
            }

            
            int idTipoPasajero = tipoSeleccionado.getId();
            GestionDAO dao = new GestionDAO();

            boolean exito = dao.registrarUsuario(
                cedulaTexto,
                nombreTexto,
                apellidoTexto,
                correoTexto,
                claveTexto,
                3,
                idTipoPasajero,
                facultadTexto,
                estudiante ? escuelaTexto : ""
            );

            
            if (exito) {
                JOptionPane.showMessageDialog(this, "Registro exitoso. Ya puedes iniciar sesión.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                dispose();
                new InicioSesion().setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Error al registrar. Verifica si el correo o cédula ya existen.", "Error en BD", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnVolver.addActionListener(e -> {
            dispose();
            new InicioSesion().setVisible(true);
        });
    }
}