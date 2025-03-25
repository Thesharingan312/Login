import java.awt.Color;
import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

/**
 * Clase que gestiona la interfaz de usuario para el inicio de sesión
 */
public class LoginUI {
    private final JFrame frame;
    private final AuthenticationService authService;
    final UITheme theme;
    private JTextField userField;
    private JPasswordField passField;
    private JButton loginButton;
    private Timer lockTimer;
    private JLabel lockMessageLabel;
    
    public LoginUI(AuthenticationService authService) {
        this.authService = authService;
        this.theme = new UITheme();
        
        // Configuración de la ventana principal
        
        frame = new JFrame("Inicio de Sesión");
        frame.setSize(400, 480);  // Modificación para que se vean todos los campos
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false); //Para bloquear el redimensionamiento

        // Cargar ícono personalizado
        try {
            ImageIcon icono = new ImageIcon(getClass().getResource("/icono2.png"));
            if (icono.getImage() != null) {
                frame.setIconImage(icono.getImage());
            } else {
                System.err.println("No se pudo cargar el ícono. Verifique la ruta del archivo.");
            }
        } catch (Exception e) {
            System.err.println("Error al cargar el ícono: " + e.getMessage());
        }
        
        // Creación de los componentes
        createAndSetupComponents();
    }
    
    /**
     * Muestra la ventana de inicio de sesión
     */
    public void display() {
        frame.setVisible(true);
    }
    
    /**
     * Crea y configura los componentes de la interfaz
     */
    private void createAndSetupComponents() {
        // Panel principal con bordes redondeados
        RoundedPanel mainPanel = new RoundedPanel(20, theme.getBackgroundColor());
        mainPanel.setLayout(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = GridBagConstraints.RELATIVE;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);
        
        // Título
        JLabel titleLabel = new JLabel("Bienvenido al sistema");
        theme.applyTitleStyle(titleLabel);
        
        // Campo de usuario
        JLabel userLabel = new JLabel("Usuario:");
        theme.applyLabelStyle(userLabel);
        
        userField = new JTextField(15);
        theme.applyTextFieldStyle(userField);
        
        // Cargar el nombre de usuario guardado si existe
        String savedUsername = authService.getSavedUsername();
        if (!savedUsername.isEmpty()) {
            userField.setText(savedUsername);
        }
        
        // Campo de contraseña
        JLabel passLabel = new JLabel("Contraseña:");
        theme.applyLabelStyle(passLabel);
        
        passField = new JPasswordField(15);
        passField.setEchoChar('●');
        theme.applyTextFieldStyle(passField);
        
        // Si hay un usuario guardado, poner el foco en el campo de contraseña
        if (!savedUsername.isEmpty()) {
            SwingUtilities.invokeLater(() -> passField.requestFocusInWindow());
        }
        
        // Checkbox para mostrar/ocultar contraseña
        JCheckBox showPasswordCheck = new JCheckBox("Mostrar contraseña");
        theme.applyCheckboxStyle(showPasswordCheck);
        showPasswordCheck.addActionListener(e -> 
            passField.setEchoChar(showPasswordCheck.isSelected() ? (char) 0 : '●')
        );
        
        // Checkbox para recordar usuario
        JCheckBox rememberUserCheck = new JCheckBox("Recordar usuario");
        theme.applyCheckboxStyle(rememberUserCheck);
        rememberUserCheck.setSelected(!savedUsername.isEmpty());
        
        // Mensaje de bloqueo (inicialmente invisible)
        lockMessageLabel = new JLabel("");
        lockMessageLabel.setFont(new Font("Arial", Font.BOLD, 12));
        lockMessageLabel.setForeground(Color.RED);
        lockMessageLabel.setVisible(false);
        
        // Botón de inicio de sesión
        loginButton = new JButton("Iniciar sesión");
        theme.applyButtonStyle(loginButton);
        loginButton.addActionListener(e -> handleLogin(userField.getText(), 
                                                    new String(passField.getPassword()), 
                                                    rememberUserCheck.isSelected()));
        
        // Botón para cambiar tema
        JButton toggleThemeButton = new JButton(theme.isDarkTheme() ? "Cambiar a tema claro" : "Cambiar a tema oscuro");
        toggleThemeButton.setFont(new Font("Arial", Font.PLAIN, 12));
        toggleThemeButton.setBackground(null);
        toggleThemeButton.setBorderPainted(false);
        toggleThemeButton.setForeground(theme.getAccentColor());
        toggleThemeButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        toggleThemeButton.setFocusPainted(false);
        toggleThemeButton.addActionListener(e -> toggleTheme());
        
        // Panel para el botón de tema (alineado a la derecha)
        JPanel themePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        themePanel.setBackground(theme.getBackgroundColor());
        themePanel.add(toggleThemeButton);
        
        // Agregar componentes al panel
        gbc.gridwidth = 1;
        mainPanel.add(themePanel, gbc);  // Botón de tema arriba a la derecha
        mainPanel.add(titleLabel, gbc);
        mainPanel.add(userLabel, gbc);
        mainPanel.add(userField, gbc);
        mainPanel.add(passLabel, gbc);
        mainPanel.add(passField, gbc);
        mainPanel.add(showPasswordCheck, gbc);
        mainPanel.add(rememberUserCheck, gbc);
        mainPanel.add(lockMessageLabel, gbc);
        mainPanel.add(loginButton, gbc);
        
        // Agregar panel a la ventana con color de fondo adecuado
        frame.getContentPane().setBackground(theme.getBackgroundColor());
        frame.add(mainPanel);
        
        // Comprobar si la cuenta está bloqueada inicialmente
        updateLockStatus();
    }
    
    /**
     * @return
     * @see java.lang.Object#hashCode()
     */
    
    public int hashCode() {
        return theme.hashCode();
    }

    /**
     * @return
     * @see UITheme#isDarkTheme()
     */
    
    public boolean isDarkTheme() {
        return theme.isDarkTheme();
    }

    /**
     * @return
     * @see UITheme#getBackgroundColor()
     */
    
    public Color getBackgroundColor() {
        return theme.getBackgroundColor();
    }

    /**
     * @return
     * @see UITheme#getTextColor()
     */
    
    public Color getTextColor() {
        return theme.getTextColor();
    }

    /**
     * @return
     * @see UITheme#getButtonColor()
     */
    
    public Color getButtonColor() {
        return theme.getButtonColor();
    }

    /**
     * @return
     * @see UITheme#getButtonHoverColor()
     */
    
    public Color getButtonHoverColor() {
        return theme.getButtonHoverColor();
    }

    /**
     * @return
     * @see UITheme#getBorderColor()
     */
    
    public Color getBorderColor() {
        return theme.getBorderColor();
    }

    /**
     * @param obj
     * @return
     * @see java.lang.Object#equals(java.lang.Object)
     */
    
    @Override
    public boolean equals(Object obj) {
        return theme.equals(obj);
    }

    /**
     * @param label
     * @see UITheme#applyTitleStyle(javax.swing.JLabel)
     */
    
    public void applyTitleStyle(JLabel label) {
        theme.applyTitleStyle(label);
    }

    /**
     * @param label
     * @see UITheme#applyLabelStyle(javax.swing.JLabel)
     */
    
    public void applyLabelStyle(JLabel label) {
        theme.applyLabelStyle(label);
    }

    /**
     * @param field
     * @see UITheme#applyTextFieldStyle(javax.swing.JTextField)
     */
    
    public void applyTextFieldStyle(JTextField field) {
        theme.applyTextFieldStyle(field);
    }

    /**
     * @param checkbox
     * @see UITheme#applyCheckboxStyle(javax.swing.JCheckBox)
     */
    
    public void applyCheckboxStyle(JCheckBox checkbox) {
        theme.applyCheckboxStyle(checkbox);
    }

    /**
     * @param button
     * @see UITheme#applyButtonStyle(javax.swing.JButton)
     */
    
    public void applyButtonStyle(JButton button) {
        theme.applyButtonStyle(button);
    }

    /**
     * @return
     * @see UITheme#getTitleFont()
     */
    
    public Font getTitleFont() {
        return theme.getTitleFont();
    }

    /**
     * @return
     * @see UITheme#getAccentColor()
     */
    
    public Color getAccentColor() {
        return theme.getAccentColor();
    }

    /**
     * @return
     * @see java.lang.Object#toString()
     */
    
    @Override
    public String toString() {
        return theme.toString();
    }

    /**
     * Cambia el tema de la UI y reconstruye la interfaz
     */
    private void toggleTheme() {
        theme.toggleTheme();
        
        // Guardar los datos actuales
        String username = userField.getText();
        String password = new String(passField.getPassword());
        
        // Reconstruir la interfaz con el nuevo tema
        frame.getContentPane().removeAll();
        createAndSetupComponents();
        
        // Restaurar los datos
        userField.setText(username);
        passField.setText(password);
        
        // Actualizar la UI
        SwingUtilities.updateComponentTreeUI(frame);
        frame.repaint();
    }
    
    /**
     * Actualiza el estado de bloqueo de la interfaz
     */
    private void updateLockStatus() {
        boolean isLocked = authService.isLocked();
        
        userField.setEnabled(!isLocked);
        passField.setEnabled(!isLocked);
        loginButton.setEnabled(!isLocked);
        
        if (isLocked) {
            long remainingSeconds = authService.getRemainingLockTime();
            lockMessageLabel.setText("Cuenta bloqueada. Intente de nuevo en " + remainingSeconds + " segundos.");
            lockMessageLabel.setVisible(true);
            
            // Iniciar un temporizador para actualizar el mensaje
            if (lockTimer != null && lockTimer.isRunning()) {
                lockTimer.stop();
            }
            
            lockTimer = new Timer(1000, e -> {
                long remaining = authService.getRemainingLockTime();
                if (remaining <= 0) {
                    ((Timer)e.getSource()).stop();
                    updateLockStatus();
                } else {
                    lockMessageLabel.setText("Cuenta bloqueada. Intente de nuevo en " + remaining + " segundos.");
                }
            });
            lockTimer.start();
        } else {
            lockMessageLabel.setVisible(false);
            if (lockTimer != null && lockTimer.isRunning()) {
                lockTimer.stop();
            }
        }
    }
    
    /**
     * Maneja el proceso de inicio de sesión
     */
    private void handleLogin(String username, String password, boolean rememberUser) {
        // Verificar si la cuenta está bloqueada
        if (authService.isLocked()) {
            updateLockStatus();
            return;
        }
        
        if (authService.authenticate(username, password)) {
            // Gestionar "recordar usuario"
            if (rememberUser) {
                authService.saveUsername(username);
            } else {
                authService.clearSavedUsername();
            }
            
            // Detener cualquier timer en ejecución
            if (lockTimer != null && lockTimer.isRunning()) {
                lockTimer.stop();
            }
            
            JOptionPane.showMessageDialog(
                frame, 
                "¡Bienvenido, " + authService.getUsername() + "!", 
                "Acceso exitoso", 
                JOptionPane.INFORMATION_MESSAGE
            );
            frame.dispose();
        } else {
            // Verificar si la cuenta se ha bloqueado con este intento
            updateLockStatus();
            
            if (!authService.isLocked()) {
                JOptionPane.showMessageDialog(
                    frame, 
                    "Usuario o contraseña incorrectos.", 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE
                );
            }
        }
    }
}