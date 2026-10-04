import javax.swing.*;

import componentes.BotonPrimario;
import componentes.BotonSecundario;
import componentes.FormularioLabel;
import componentes.Paleta;
import componentes.SubtituloLabel;
import componentes.TituloLabel;

import java.awt.*;

public class InicioSesion extends JFrame {

    public InicioSesion() {
        setTitle("Sistema de Transporte UCV - Autenticacion");
        setSize(440, 420); 
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); 
        setResizable(false);

        // Uso directo de la Paleta centralizada
        JPanel panelPrincipal = new JPanel(new BorderLayout(20, 20));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(30, 35, 30, 35));
        panelPrincipal.setBackground(Paleta.ARENA_FONDO);

        JPanel panelHeader = new JPanel(new GridLayout(2, 1, 5, 5));
        panelHeader.setOpaque(false);
        
        // Sustitución por componentes propios
        panelHeader.add(new TituloLabel("Sistema de Transporte"));
        panelHeader.add(new SubtituloLabel("Ingresa tus datos para iniciar sesion"));
        panelPrincipal.add(panelHeader, BorderLayout.NORTH);

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

        // Cédula
        gbc.gridy = 0; gbc.insets = new Insets(0, 0, 2, 0);
        panelFormulario.add(new FormularioLabel("Cedula:"), gbc);
        
        gbc.gridy = 1; gbc.insets = new Insets(0, 0, 12, 0);
        JTextField txtUsuario = new JTextField(20); 
        txtUsuario.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtUsuario.putClientProperty("JTextField.placeholderText", "Ej. 25111222");
        panelFormulario.add(txtUsuario, gbc);

        // Contraseña
        gbc.gridy = 2; gbc.insets = new Insets(0, 0, 2, 0);
        panelFormulario.add(new FormularioLabel("Contrasena:"), gbc);
        
        gbc.gridy = 3; gbc.insets = new Insets(0, 0, 5, 0);
        JPasswordField txtClave = new JPasswordField(20);
        txtClave.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtClave.putClientProperty("JTextField.placeholderText", "••••••••");
        panelFormulario.add(txtClave, gbc);

        // Botón de recuperar contraseña (Se mantiene igual por sus propiedades especiales de enlace)
        gbc.gridy = 4; gbc.insets = new Insets(5, 0, 0, 0);
        JButton btnCambiarClave = new JButton("¿Olvidaste tu contrasena o deseas cambiarla?");
        btnCambiarClave.setFont(new Font("Segoe UI", Font.ITALIC | Font.BOLD, 11));
        btnCambiarClave.setForeground(Paleta.AZUL_MEDIO);
        btnCambiarClave.setContentAreaFilled(false);
        btnCambiarClave.setBorderPainted(false);
        btnCambiarClave.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCambiarClave.setHorizontalAlignment(SwingConstants.LEFT);
        panelFormulario.add(btnCambiarClave, gbc);

        panelPrincipal.add(panelFormulario, BorderLayout.CENTER);

        // Implementación de Botones Estandarizados
        JPanel panelBotones = new JPanel(new GridLayout(1, 2, 15, 0));
        panelBotones.setOpaque(false);
        
        BotonSecundario btnRegistrar = new BotonSecundario("Registrarse");
        BotonPrimario btnIngresar = new BotonPrimario("Ingresar");

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

        //Iniciar sesión
        btnIngresar.addActionListener(e -> {
            String usuario = txtUsuario.getText().trim();
            String clave = new String(txtClave.getPassword());

            if (usuario.isEmpty() || clave.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Por favor, complete todos los campos.", "Campos Vacios", JOptionPane.WARNING_MESSAGE);
                return;
            }
            GestionDAO dao = new GestionDAO();

            Usuario user = dao.validarLogin(usuario, clave);

            if (user == null) {
                JOptionPane.showMessageDialog(this, "Las credenciales ingresadas no son válidas", "Credenciales inválidas", JOptionPane.WARNING_MESSAGE);
                return;
            }
            int id_rol = user.getIdRol();
            dispose();
            SesionActiva.setUsuario(user);

            switch (id_rol) {
                case 1:
                    Administrador pantallaAdmin = new Administrador();
                    pantallaAdmin.setVisible(true);
                    break;
                case 2:
                    
                    Conductor pantallaChofer = new Conductor();
                    pantallaChofer.setVisible(true);
                    break;
                case 3:
                    
                    new Pasajero().setVisible(true);
                    break;
            }
        });
    }
}