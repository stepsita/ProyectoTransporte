import javax.swing.*;
import java.awt.*;

public class RegistroUsuario extends JFrame {

    private final Color colorAzulOscuro = new Color(0x11, 0x18, 0x44);
    private final Color colorAzulMedio = new Color(0x4B, 0x56, 0x94);
    private final Color colorAzulGris = new Color(0x72, 0x88, 0xAE);
    private final Color colorArenaFondo = new Color(0xEA, 0xE0, 0xCF);

    public RegistroUsuario() {
        setTitle("Sistema de Transporte UCV - Crear Cuenta");
        setSize(450, 480);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel panelPrincipal = new JPanel(new BorderLayout(20, 20));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(25, 35, 25, 35));
        panelPrincipal.setBackground(colorArenaFondo);

        // Encabezado
        JPanel panelHeader = new JPanel(new GridLayout(2, 1, 2, 2));
        panelHeader.setBackground(colorArenaFondo);
        JLabel lblTitulo = new JLabel("Registro de Usuario", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitulo.setForeground(colorAzulOscuro);
        
        JLabel lblSub = new JLabel("Crea tu cuenta para el sistema de transporte", SwingConstants.CENTER);
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblSub.setForeground(colorAzulMedio);
        panelHeader.add(lblTitulo);
        panelHeader.add(lblSub);
        panelPrincipal.add(panelHeader, BorderLayout.NORTH);

        // Formulario (GridBagLayout)
        JPanel panelFormulario = new JPanel(new GridBagLayout());
        panelFormulario.setBackground(Color.WHITE);
        panelFormulario.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(colorAzulGris, 1),
            BorderFactory.createEmptyBorder(15, 20, 15, 20)
        ));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.weightx = 1.0;

        // Cédula
        gbc.gridy = 0; gbc.insets = new Insets(5, 0, 2, 0);
        JLabel lblCedula = new JLabel("Cedula:");
        lblCedula.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblCedula.setForeground(colorAzulOscuro);
        panelFormulario.add(lblCedula, gbc);
        
        gbc.gridy = 1; gbc.insets = new Insets(0, 0, 10, 0);
        JTextField txtCedula = new JTextField();
        txtCedula.putClientProperty("JTextField.placeholderText", "Ej. 26111222");
        panelFormulario.add(txtCedula, gbc);

        // Nombre Completo
        gbc.gridy = 2; gbc.insets = new Insets(5, 0, 2, 0);
        JLabel lblNombre = new JLabel("Nombre Completo:");
        lblNombre.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblNombre.setForeground(colorAzulOscuro);
        panelFormulario.add(lblNombre, gbc);
        
        gbc.gridy = 3; gbc.insets = new Insets(0, 0, 10, 0);
        JTextField txtNombre = new JTextField();
        txtNombre.putClientProperty("JTextField.placeholderText", "Ej. Stephanie Salazar");
        panelFormulario.add(txtNombre, gbc);

        // Tipo de Usuario
        gbc.gridy = 4; gbc.insets = new Insets(5, 0, 2, 0);
        JLabel lblTipo = new JLabel("Tipo de Usuario:");
        lblTipo.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblTipo.setForeground(colorAzulOscuro);
        panelFormulario.add(lblTipo, gbc);
        
        gbc.gridy = 5; gbc.insets = new Insets(0, 0, 10, 0);
        String[] opcionesUsuario = {"Estudiante", "Chofer"};
        JComboBox<String> cmbTipoUsuario = new JComboBox<>(opcionesUsuario);
        panelFormulario.add(cmbTipoUsuario, gbc);

        // Contraseña
        gbc.gridy = 6; gbc.insets = new Insets(5, 0, 2, 0);
        JLabel lblClave = new JLabel("Definir Contrasena:");
        lblClave.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblClave.setForeground(colorAzulOscuro);
        panelFormulario.add(lblClave, gbc);
        
        gbc.gridy = 7; gbc.insets = new Insets(0, 0, 5, 0);
        JPasswordField txtClave = new JPasswordField();
        txtClave.putClientProperty("JTextField.placeholderText", "••••••••");
        panelFormulario.add(txtClave, gbc);

        panelPrincipal.add(panelFormulario, BorderLayout.CENTER);

        // Botones inferiores
        JPanel panelBotones = new JPanel(new GridLayout(1, 2, 15, 0));
        panelBotones.setBackground(colorArenaFondo);
        
        JButton btnVolver = new JButton("Volver al Login");
        btnVolver.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnVolver.setBackground(Color.WHITE);
        btnVolver.setForeground(colorAzulOscuro);
        btnVolver.setBorder(BorderFactory.createLineBorder(colorAzulGris, 1));
        
        JButton btnRegistrar = new JButton("Registrar");
        btnRegistrar.setBackground(colorAzulOscuro);
        btnRegistrar.setForeground(Color.WHITE);
        btnRegistrar.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnRegistrar.setFocusPainted(false);

        panelBotones.add(btnVolver);
        panelBotones.add(btnRegistrar);
        panelPrincipal.add(panelBotones, BorderLayout.SOUTH);

        add(panelPrincipal);

        // --- EVENTOS ---
        btnVolver.addActionListener(e -> {
            dispose();
            new InicioSesion().setVisible(true);
        });

        btnRegistrar.addActionListener(e -> {
            if (!txtCedula.getText().trim().isEmpty() && !txtNombre.getText().trim().isEmpty() && txtClave.getPassword().length > 0) {
                JOptionPane.showMessageDialog(this, "Registro exitoso en el sistema. Ya puedes iniciar sesion.", "Registro Exitoso", JOptionPane.INFORMATION_MESSAGE);
                dispose();
                new InicioSesion().setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Por favor, complete todos los campos obligatorios.", "Campos Vacios", JOptionPane.WARNING_MESSAGE);
            }
        });
    }
}