import javax.swing.*;
import java.awt.*;

public class RegistroUsuario extends JFrame {

    private final Color colorAzulOscuro = new Color(0x11, 0x18, 0x44);
    private final Color colorAzulMedio = new Color(0x4B, 0x56, 0x94);
    private final Color colorAzulGris = new Color(0x72, 0x88, 0xAE);
    private final Color colorArenaFondo = new Color(0xEA, 0xE0, 0xCF);

    public RegistroUsuario() {
        setTitle("Sistema de Transporte UCV - Crear Cuenta");
        setSize(500, 600); 
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel panelPrincipal = new JPanel(new BorderLayout(20, 20));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(25, 35, 25, 35));
        
        panelPrincipal.setBackground(colorArenaFondo); 

        // Encabezado
        JPanel panelHeader = new JPanel(new GridLayout(2, 1, 2, 2));
        panelHeader.setOpaque(false);
        JLabel lblTitulo = new JLabel("Registro de Usuario", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitulo.setForeground(colorAzulOscuro);
        JLabel lblSub = new JLabel("Crea tu cuenta para el sistema de transporte", SwingConstants.CENTER);
        lblSub.setForeground(colorAzulMedio);
        panelHeader.add(lblTitulo);
        panelHeader.add(lblSub);
        panelPrincipal.add(panelHeader, BorderLayout.NORTH);

        // Formulario
        JPanel panelFormulario = new JPanel(new GridBagLayout());
        panelFormulario.setBackground(Color.WHITE);
        panelFormulario.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(colorAzulGris, 1),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.weightx = 1.0;

        // --- CÉDULA ---
        gbc.gridy = 0; gbc.insets = new Insets(5, 0, 2, 0);
        JLabel lblCedula = new JLabel("Cedula:");
        lblCedula.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblCedula.setForeground(colorAzulOscuro);
        panelFormulario.add(lblCedula, gbc);
        gbc.gridy = 1; gbc.insets = new Insets(0, 0, 10, 0);
        JTextField txtCedula = new JTextField();
        panelFormulario.add(txtCedula, gbc);

        // --- NOMBRE Y APELLIDO ---
        gbc.gridy = 2; gbc.insets = new Insets(0, 0, 10, 0);
        
        JPanel panelNombres = new JPanel(new GridLayout(1, 2, 10, 0));
        panelNombres.setOpaque(false);
        
        //  (Nombre)
        JPanel colNombre = new JPanel(new BorderLayout(0, 2));
        colNombre.setOpaque(false);
        JLabel lblNombre = new JLabel("Nombre:");
        lblNombre.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblNombre.setForeground(colorAzulOscuro);
        colNombre.add(lblNombre, BorderLayout.NORTH);
        JTextField txtNombre = new JTextField();
        colNombre.add(txtNombre, BorderLayout.CENTER);
        
        //  (Apellido)
        JPanel colApellido = new JPanel(new BorderLayout(0, 2));
        colApellido.setOpaque(false);
        JLabel lblApellido = new JLabel("Apellido:");
        lblApellido.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblApellido.setForeground(colorAzulOscuro);
        colApellido.add(lblApellido, BorderLayout.NORTH);
        JTextField txtApellido = new JTextField();
        colApellido.add(txtApellido, BorderLayout.CENTER);
        
        
        panelNombres.add(colNombre);
        panelNombres.add(colApellido);
        panelFormulario.add(panelNombres, gbc);

        // --- CORREO ---
        gbc.gridy = 3; gbc.insets = new Insets(5, 0, 2, 0);
        JLabel lblCorreo = new JLabel("Correo UCV:");
        lblCorreo.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblCorreo.setForeground(colorAzulOscuro);
        panelFormulario.add(lblCorreo, gbc);
        gbc.gridy = 4; gbc.insets = new Insets(0, 0, 10, 0);
        JTextField txtCorreo = new JTextField();
        panelFormulario.add(txtCorreo, gbc);

        // --- FACULTAD Y ESCUELA (Paneles Anidados lado a lado) ---
        gbc.gridy = 5; gbc.insets = new Insets(0, 0, 10, 0);
        JPanel panelAcademicos = new JPanel(new GridLayout(1, 2, 10, 0));
        panelAcademicos.setOpaque(false);
        
        JPanel colFacultad = new JPanel(new BorderLayout(0, 2));
        colFacultad.setOpaque(false);
        JLabel lblFacu = new JLabel("Facultad:");
        lblFacu.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblFacu.setForeground(colorAzulOscuro);
        colFacultad.add(lblFacu, BorderLayout.NORTH);
        JTextField txtFacultad = new JTextField();
        colFacultad.add(txtFacultad, BorderLayout.CENTER);
        
        JPanel colEscuela = new JPanel(new BorderLayout(0, 2));
        colEscuela.setOpaque(false);
        JLabel lblEscu = new JLabel("Escuela: (Para estudiantes)");
        lblEscu.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblEscu.setForeground(colorAzulOscuro);
        colEscuela.add(lblEscu, BorderLayout.NORTH);
        JTextField txtEscuela = new JTextField();
        colEscuela.add(txtEscuela, BorderLayout.CENTER);
        
        panelAcademicos.add(colFacultad);
        panelAcademicos.add(colEscuela);
        panelFormulario.add(panelAcademicos, gbc);

        // --- TIPO DE PASAJERO ---
        gbc.gridy = 6; gbc.insets = new Insets(5, 0, 2, 0);
        JLabel lblTipo = new JLabel("Tipo de miembro:");
        lblTipo.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblTipo.setForeground(colorAzulOscuro);
        panelFormulario.add(lblTipo, gbc);
        gbc.gridy = 7; gbc.insets = new Insets(0, 0, 10, 0);
        
        
        JComboBox<ItemCombo> cmbTipoPasajero = new JComboBox<>(ConsultasBD.obtenerTiposPasajero());
        panelFormulario.add(cmbTipoPasajero, gbc);

        // --- CONTRASEÑA ---
        gbc.gridy = 8; gbc.insets = new Insets(5, 0, 2, 0);
        JLabel lblcontra = new JLabel("Contraseña:");
        lblcontra.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblcontra.setForeground(colorAzulOscuro);
        panelFormulario.add(lblcontra, gbc);
        gbc.gridy = 9; gbc.insets = new Insets(0, 0, 10, 0);
        JPasswordField txtClave = new JPasswordField();
        panelFormulario.add(txtClave, gbc);

        panelPrincipal.add(panelFormulario, BorderLayout.CENTER);

        // Botones inferiores
        JPanel panelBotones = new JPanel(new GridLayout(1, 2, 15, 0));
        panelBotones.setOpaque(false);
        JButton btnVolver = new JButton("Volver");
        btnVolver.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnVolver.setBackground(Color.WHITE);
        btnVolver.setForeground(colorAzulOscuro);
        btnVolver.setBorder(BorderFactory.createLineBorder(colorAzulGris, 1));
        panelBotones.add(btnVolver);
        JButton btnRegistrar = new JButton("Registrar");
        btnRegistrar.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnRegistrar.setBackground(colorAzulOscuro);
        btnRegistrar.setForeground(Color.WHITE);
        btnRegistrar.setFocusPainted(false);
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

            if (!correoTexto.matches("^[\\w-\\.]+@[\\w-\\.]+\\.com$")) {
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