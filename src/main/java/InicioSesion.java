import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class InicioSesion extends JFrame {

    private final Color colorAzulOscuro = new Color(0x11, 0x18, 0x44);   // #111844
    private final Color colorAzulMedio = new Color(0x4B, 0x56, 0x94);    // #4B5694
    private final Color colorAzulGris = new Color(0x72, 0x88, 0xAE);     // #7288AE
    private final Color colorArenaFondo = new Color(0xEA, 0xE0, 0xCF);    // #EAE0CF

    public InicioSesion() {
        setTitle("Sistema de Transporte UCV - Autenticacion");
        setSize(440, 420); 
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); 
        setResizable(false);

        JPanel panelPrincipal = new JPanel(new BorderLayout(20, 20));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(30, 35, 30, 35));
        panelPrincipal.setBackground(colorArenaFondo);

        JPanel panelHeader = new JPanel(new GridLayout(2, 1, 5, 5));
        panelHeader.setBackground(colorArenaFondo);
        
        JLabel lblTitulo = new JLabel("Sistema de Transporte", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitulo.setForeground(colorAzulOscuro);

        JLabel lblSubtitulo = new JLabel("Ingresa tus datos para iniciar sesion", SwingConstants.CENTER);
        lblSubtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblSubtitulo.setForeground(colorAzulMedio);

        panelHeader.add(lblTitulo);
        panelHeader.add(lblSubtitulo);
        panelPrincipal.add(panelHeader, BorderLayout.NORTH);

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

        //usuario
        gbc.gridy = 0; gbc.insets = new Insets(0, 0, 2, 0);
        JLabel lblUser = new JLabel("Cedula o Correo:");
        lblUser.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblUser.setForeground(colorAzulOscuro);
        panelFormulario.add(lblUser, gbc);
        
        gbc.gridy = 1; gbc.insets = new Insets(0, 0, 12, 0);
        JTextField txtUsuario = new JTextField(20); 
        txtUsuario.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtUsuario.putClientProperty("JTextField.placeholderText", "Ej. 25111222 o usuario@ucv.ve");
        panelFormulario.add(txtUsuario, gbc);

        //contraseña
        gbc.gridy = 2; gbc.insets = new Insets(0, 0, 2, 0);
        JLabel lblPass = new JLabel("Contrasena:");
        lblPass.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblPass.setForeground(colorAzulOscuro);
        panelFormulario.add(lblPass, gbc);
        
        gbc.gridy = 3; gbc.insets = new Insets(0, 0, 5, 0);
        JPasswordField txtClave = new JPasswordField(20);
        txtClave.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtClave.putClientProperty("JTextField.placeholderText", "••••••••");
        panelFormulario.add(txtClave, gbc);

        //recuperar Contraseña
        gbc.gridy = 4; gbc.insets = new Insets(5, 0, 0, 0);
        JButton btnCambiarClave = new JButton("¿Olvidaste tu contrasena o deseas cambiarla?");
        btnCambiarClave.setFont(new Font("Segoe UI", Font.ITALIC | Font.BOLD, 11));
        btnCambiarClave.setForeground(colorAzulMedio);
        btnCambiarClave.setContentAreaFilled(false);
        btnCambiarClave.setBorderPainted(false);
        btnCambiarClave.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCambiarClave.setHorizontalAlignment(SwingConstants.LEFT);
        panelFormulario.add(btnCambiarClave, gbc);

        panelPrincipal.add(panelFormulario, BorderLayout.CENTER);

        JPanel panelBotones = new JPanel(new GridLayout(1, 2, 15, 0));
        panelBotones.setBackground(colorArenaFondo);
        
        JButton btnRegistrar = new JButton("Registrarse");
        btnRegistrar.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnRegistrar.setBackground(Color.WHITE);
        btnRegistrar.setForeground(colorAzulOscuro);
        btnRegistrar.setBorder(BorderFactory.createLineBorder(colorAzulGris, 1));
        
        JButton btnIngresar = new JButton("Ingresar");
        btnIngresar.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnIngresar.setBackground(colorAzulOscuro);
        btnIngresar.setForeground(Color.WHITE);
        btnIngresar.setFocusPainted(false);

        panelBotones.add(btnRegistrar);
        panelBotones.add(btnIngresar);
        panelPrincipal.add(panelBotones, BorderLayout.SOUTH);

        add(panelPrincipal);
        //abrir registro de usuario
        btnRegistrar.addActionListener(e -> {
            dispose();
            RegistroUsuario pantallaRegistro = new RegistroUsuario();
            pantallaRegistro.setVisible(true);
        });

        //evento para simular el proceso de recuperación de contraseña
        btnCambiarClave.addActionListener(e -> {
            String correo = JOptionPane.showInputDialog(this, 
                "Introduce tu correo institucional para enviarte un enlace de recuperacion:", 
                "Cambiar Contrasena", JOptionPane.QUESTION_MESSAGE);
            
            if (correo != null && !correo.trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, 
                    "Se ha enviado un codigo de verificacion a: " + correo + "\nPodras redefinir tu clave de acceso.", 
                    "Correo Enviado", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        //evento para simular el inicio de sesion y redireccionar segun el tipo de usuario
        btnIngresar.addActionListener(e -> {
            String usuario = txtUsuario.getText().trim();
            String clave = new String(txtClave.getPassword());

            if (usuario.equalsIgnoreCase("admin")) {
                dispose(); 
                Administrador pantallaAdmin = new Administrador();
                pantallaAdmin.setVisible(true);
            } else if (usuario.equalsIgnoreCase("chofer")) {
                dispose();
                Conductor pantallaChofer = new Conductor();
                pantallaChofer.setVisible(true);
            } else if (!usuario.isEmpty() && !clave.isEmpty()) {
                dispose(); 
                Pasajero pantallaPasajero = new Pasajero();
                pantallaPasajero.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Por favor, complete todos los campos.", "Campos Vacios", JOptionPane.WARNING_MESSAGE);
            }
        });
    }
}