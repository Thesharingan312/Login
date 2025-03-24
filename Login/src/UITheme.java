import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;
import java.util.prefs.Preferences;

/**
 * Clase que gestiona los estilos de la interfaz de usuario
 */
public class UITheme {
    // Constantes para los temas
    public static final String LIGHT_THEME = "light";
    public static final String DARK_THEME = "dark";
    
    // Colores para tema claro
    private final Color lightBackgroundColor = new Color(255, 255, 255);
    private final Color lightTextColor = new Color(51, 51, 51);
    private final Color lightButtonColor = new Color(102, 179, 255);
    private final Color lightButtonHoverColor = new Color(85, 153, 255);
    private final Color lightBorderColor = new Color(176, 176, 176);
    
    // Colores para tema oscuro
    private final Color darkBackgroundColor = new Color(50, 50, 50);
    private final Color darkTextColor = new Color(240, 240, 240);
    private final Color darkButtonColor = new Color(70, 130, 180);
    private final Color darkButtonHoverColor = new Color(65, 105, 225);
    private final Color darkBorderColor = new Color(100, 100, 100);
    
    // Fuentes
    private final Font titleFont = new Font("Arial", Font.BOLD, 24);
    private final Font normalFont = new Font("Arial", Font.PLAIN, 14);
    private final Font buttonFont = new Font("Arial", Font.BOLD, 14);
    
    // Tema actual
    private String currentTheme;
    
    /**
     * Constructor, carga las preferencias de tema
     */
    public UITheme() {
        // Cargar preferencia de tema
        currentTheme = loadThemePreference();
    }
    
    /**
     * Verifica si el tema actual es oscuro
     */
    public boolean isDarkTheme() {
        return DARK_THEME.equals(currentTheme);
    }
    
    /**
     * Cambia el tema y guarda la preferencia
     */
    public void toggleTheme() {
        currentTheme = isDarkTheme() ? LIGHT_THEME : DARK_THEME;
        saveThemePreference();
    }
    
    /**
     * Guarda la preferencia de tema
     */
    private void saveThemePreference() {
        Preferences prefs = Preferences.userNodeForPackage(UITheme.class);
        prefs.put("theme", currentTheme);
    }
    
    /**
     * Carga la preferencia de tema
     */
    private String loadThemePreference() {
        Preferences prefs = Preferences.userNodeForPackage(UITheme.class);
        return prefs.get("theme", LIGHT_THEME);  // Por defecto, tema claro
    }
    
    /**
     * Obtiene el color de fondo según el tema actual
     */
    public Color getBackgroundColor() {
        return isDarkTheme() ? darkBackgroundColor : lightBackgroundColor;
    }
    
    /**
     * Obtiene el color de texto según el tema actual
     */
    public Color getTextColor() {
        return isDarkTheme() ? darkTextColor : lightTextColor;
    }
    
    /**
     * Obtiene el color de botón según el tema actual
     */
    public Color getButtonColor() {
        return isDarkTheme() ? darkButtonColor : lightButtonColor;
    }
    
    /**
     * Obtiene el color de hover de botón según el tema actual
     */
    public Color getButtonHoverColor() {
        return isDarkTheme() ? darkButtonHoverColor : lightButtonHoverColor;
    }
    
    /**
     * Obtiene el color de borde según el tema actual
     */
    public Color getBorderColor() {
        return isDarkTheme() ? darkBorderColor : lightBorderColor;
    }
    
    /**
     * Aplica estilo a etiquetas de título
     */
    public void applyTitleStyle(JLabel label) {
        label.setFont(titleFont);
        label.setForeground(getTextColor());
        label.setHorizontalAlignment(SwingConstants.CENTER);
    }
    
    /**
     * Aplica estilo a etiquetas normales
     */
    public void applyLabelStyle(JLabel label) {
        label.setFont(normalFont);
        label.setForeground(getTextColor());
    }
    
    /**
     * Aplica estilo a campos de texto
     */
    public void applyTextFieldStyle(JTextField field) {
        field.setFont(normalFont);
        field.setBackground(getBackgroundColor());
        field.setForeground(getTextColor());
        field.setCaretColor(isDarkTheme() ? Color.WHITE : Color.BLACK);
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(getBorderColor(), 1),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
    }
    
    /**
     * Aplica estilo a checkboxes
     */
    public void applyCheckboxStyle(JCheckBox checkbox) {
        checkbox.setFont(normalFont);
        checkbox.setForeground(getTextColor());
        checkbox.setOpaque(false);
    }
    
    /**
     * Aplica estilo a botones con efecto hover
     */
    public void applyButtonStyle(JButton button) {
        button.setFont(buttonFont);
        button.setBackground(getButtonColor());
        button.setForeground(getTextColor());
        button.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        button.setFocusPainted(false);
        
        // Efecto hover
        // Eliminamos listeners anteriores para evitar duplicación
        for (java.awt.event.MouseListener ml : button.getMouseListeners()) {
            if (ml instanceof MouseAdapter) {
                button.removeMouseListener(ml);
            }
        }
        
        // Efecto hover
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(getButtonHoverColor());
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(getButtonColor());
            }
        });
    }

    public Font getTitleFont() {
        return titleFont;
    }

    private final Color lightAccentColor = new Color(255, 204, 0); // Amarillo anaranjado
private final Color darkAccentColor = new Color(255, 165, 0); // Naranja oscuro

public Color getAccentColor() {
    return isDarkTheme() ? darkAccentColor : lightAccentColor;
}

    /**
     * @param obj
     * @return
     * @see java.lang.Object#equals(java.lang.Object)
     */
    
    @Override
    public boolean equals(Object obj) {
        return equals(obj);
    }

    public Font getNormalFont() {
        return normalFont;
    }

    public Font getButtonFont() {
        return buttonFont;
    }
}