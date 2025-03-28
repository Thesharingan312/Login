package registro;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import javax.swing.*;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import javax.swing.text.PlainDocument;

/**
 * Clase de validación para los campos del formulario.
 */
class Validador {
    private static final Pattern PATRON_DNI = Pattern.compile("\\d{8}[A-Z]");
    private static final Pattern PATRON_CORREO = Pattern.compile("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,40}$");
    private static final Pattern PATRON_TELEFONO = Pattern.compile("\\d{9}");
    // Patrón para contraseña: mínimo 8 caracteres, al menos 1 mayúscula, 1 minúscula, 1 dígito y 1 carácter especial.
    private static final Pattern PATRON_CONTRASENA = Pattern.compile("^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?]).{8,}$");

    /**
     * Valida el DNI con el algoritmo de letra de control.
     * @param dni Número de identificación a validar
     * @return true si el DNI es válido, false en caso contrario
     */
    public static boolean validarDNI(String dni) {
        if (!PATRON_DNI.matcher(dni).matches()) return false;
        
        String letras = "TRWAGMYFPDXBNJZSQVHLCKE";
        int numeroDNI = Integer.parseInt(dni.substring(0, 8));
        char letraCalculada = letras.charAt(numeroDNI % 23);
        char letraIntroducida = dni.charAt(8);

        return letraCalculada == letraIntroducida;
    }

    /**
     * Valida el formato del correo electrónico.
     * @param correo Correo electrónico a validar
     * @return true si el correo tiene un formato válido
     */
    public static boolean validarCorreo(String correo) {
        return PATRON_CORREO.matcher(correo).matches();
    }

    /**
     * Valida el formato del número de teléfono.
     * @param telefono Número de teléfono a validar
     * @return true si el teléfono tiene 9 dígitos
     */
    public static boolean validarTelefono(String telefono) {
        return PATRON_TELEFONO.matcher(telefono).matches();
    }
    
    /**
     * Valida la contraseña asegurando un mínimo de 8 caracteres, 
     * al menos una mayúscula, una minúscula, un dígito y un carácter especial.
     * @param contrasena La contraseña a validar.
     * @return true si la contraseña cumple el estándar.
     */
    public static boolean validarContrasena(String contrasena) {
        return PATRON_CONTRASENA.matcher(contrasena).matches();
    }
}

/**
 * Formulario de Registro con validación de campos.
 */
public class FormularioRegistro extends JFrame {
    // Dimensiones de la ventana
    private static final int ANCHO = 400;
    private static final int ALTO = 400; // Aumentado para incluir nuevos campos

    // Campos de registro originales
    private final CampoRegistro campoNombre;
    private final CampoRegistro campoApellidos;
    private final CampoRegistro campoDNI;
    private final CampoRegistro campoCorreo;
    private final CampoRegistro campoDireccion;
    private final CampoRegistro campoTelefono;

    // Nuevos campos para contraseña
    private final JPasswordField campoContrasena;
    private final JPasswordField campoConfirmarContrasena;
    private final JCheckBox checkMostrarContrasena;

    // Botones del formulario
    private final JButton botonAceptar;
    private final JButton botonLimpiar;

    /**
     * Constructor del formulario de registro.
     */
    public FormularioRegistro() {
        // Inicializar campos originales
        campoNombre = new CampoRegistro("Nombre", true);
        campoApellidos = new CampoRegistro("Apellidos", true);
        campoDNI = new CampoRegistro("DNI", false);
        campoCorreo = new CampoRegistro("Correo Electrónico", false);
        campoDireccion = new CampoRegistro("Dirección", true);
        campoTelefono = new CampoRegistro("Teléfono", false);

        // Inicializar nuevos campos de contraseña
        campoContrasena = new JPasswordField(15);
        campoConfirmarContrasena = new JPasswordField(15);
        // Usamos el carácter '●' (Unicode 25CF) para ocultar la contraseña
        campoContrasena.setEchoChar('\u25CF');
        campoConfirmarContrasena.setEchoChar('\u25CF');
        checkMostrarContrasena = new JCheckBox("Mostrar contraseñas");

        botonAceptar = new JButton("Aceptar");
        botonLimpiar = new JButton("Limpiar");

        // MEJORA AÑADIDA: Aplicar filtros de formato a DNI y Teléfono
        aplicarFiltrosEspeciales();

        // Inicializar y organizar los componentes, respetando el diseño original
        inicializarComponentes();
        configurarVentana();
        configurarEventos();
    }

    /**
     * Configura los eventos de los botones y componentes.
     */
    private void configurarEventos() {
        botonLimpiar.addActionListener(e -> limpiarFormulario());
        botonAceptar.addActionListener(e -> validarFormulario());
        checkMostrarContrasena.addActionListener(e -> alternarVisibilidadContrasena());
    }
    
    /**
     * Alterna la visibilidad de los campos de contraseña.
     * Si está seleccionado, muestra la contraseña en texto normal; de lo contrario, la oculta con '●'.
     */
    private void alternarVisibilidadContrasena() {
        char echoChar = checkMostrarContrasena.isSelected() ? '\u0000' : '\u25CF';
        campoContrasena.setEchoChar(echoChar);
        campoConfirmarContrasena.setEchoChar(echoChar);
    }

    /**
     * Aplica filtros de formato a campos específicos (DNI y Teléfono).
     */
    private void aplicarFiltrosEspeciales() {
        // Filtro para DNI: 8 números + 1 letra mayúscula
        ((AbstractDocument) campoDNI.getCampo().getDocument()).setDocumentFilter(new DniFilter());
        // Filtro para Teléfono: Solo permite dígitos
        ((AbstractDocument) campoTelefono.getCampo().getDocument()).setDocumentFilter(new TelefonoFilter());
    }

    /**
     * Valida todos los campos del formulario, incluidos los nuevos de contraseña.
     */
    private void validarFormulario() {
        List<String> errores = new ArrayList<>();
        // Validar que todos los campos originales sean completados
        CampoRegistro[] obligatorios = {campoNombre, campoApellidos, campoDNI, campoCorreo, campoDireccion, campoTelefono};
        for (CampoRegistro campo : obligatorios) {
            if (campo.getCampo().getText().trim().isEmpty()) {
                errores.add("Campo obligatorio: " + campo.getEtiqueta().getText());
            }
        }
        // Validar que el campo de dirección tenga al menos 5 caracteres
        if (campoDireccion.getCampo().getText().length() < 5) {
            errores.add("La dirección debe tener al menos 5 caracteres.");
        }
        // Obtener el nombre completo y las contraseñas
        String nombre = campoNombre.getCampo().getText();
        String apellidos = campoApellidos.getCampo().getText();
        String nombreCompleto = nombre + " " + apellidos;
        
        String contrasena = new String(campoContrasena.getPassword());
        String confirmarContrasena = new String(campoConfirmarContrasena.getPassword());
        // Validaciones específicas de los campos originales
        if (!campoDNI.getCampo().getText().isEmpty() && !Validador.validarDNI(campoDNI.getCampo().getText())) {
            errores.add("DNI inválido. Formato: 8 números + letra mayúscula.");
        }
        if (!campoCorreo.getCampo().getText().isEmpty() && !Validador.validarCorreo(campoCorreo.getCampo().getText())) {
            errores.add("Formato de correo electrónico inválido.");
        }
        if (!campoTelefono.getCampo().getText().isEmpty() && !Validador.validarTelefono(campoTelefono.getCampo().getText())) {
            errores.add("Teléfono debe tener 9 dígitos.");
        }
        // Validar que los campos de contraseña sean completados
        String contrasena = new String(campoContrasena.getPassword());
        String confirmarContrasena = new String(campoConfirmarContrasena.getPassword());
        if (contrasena.trim().isEmpty()) {
            errores.add("Campo obligatorio: Contraseña.");
        }
        if (confirmarContrasena.trim().isEmpty()) {
            errores.add("Campo obligatorio: Confirmar Contraseña.");
        }
        // Validar la seguridad de la contraseña y que ambas coincidan
        if (!contrasena.trim().isEmpty() && !Validador.validarContrasena(contrasena)) {
            errores.add("La contraseña debe tener al menos 8 caracteres, incluir mayúsculas, minúsculas, números y un carácter especial.");
        }
        if (!contrasena.equals(confirmarContrasena)) {
            errores.add("Las contraseñas no coinciden.");
        }
        
        if (!errores.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                String.join("\n", errores), 
                "Errores de validación", 
                JOptionPane.ERROR_MESSAGE);
        } else {
            // Crear servicio de autenticación para registro
            AuthenticationService authService = new AuthenticationService();
            
            // Intentar registrar al usuario
            boolean registroExitoso = authService.register(nombreCompleto, contrasena);
            
            if (registroExitoso) {
                JOptionPane.showMessageDialog(this, 
                    "Formulario enviado con éxito. Usuario registrado.", 
                    "Éxito", JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, 
                    "El usuario ya existe. Por favor, elija otro nombre.", 
                    "Error de Registro", 
                    JOptionPane.ERROR_MESSAGE);}
        }
    }
    /**
     * Inicializa y organiza los componentes del formulario.
     */
    private void inicializarComponentes() {
        setLayout(new BorderLayout());
        JPanel panelFormulario = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Agregar los campos originales
        CampoRegistro[] campos = {campoNombre, campoApellidos, campoDNI, campoCorreo, campoDireccion, campoTelefono};
        int fila = 0;
        for (CampoRegistro campo : campos) {
            agregarComponenteFormulario(panelFormulario, campo.getEtiqueta(), gbc, 0, fila);
            agregarComponenteFormulario(panelFormulario, campo.getCampo(), gbc, 1, fila++);
        }
        // Agregar el campo de Contraseña
        agregarComponenteFormulario(panelFormulario, new JLabel("Contraseña:"), gbc, 0, fila);
        agregarComponenteFormulario(panelFormulario, campoContrasena, gbc, 1, fila++);
        // Agregar el campo de Confirmar Contraseña
        agregarComponenteFormulario(panelFormulario, new JLabel("Confirmar Contraseña:"), gbc, 0, fila);
        agregarComponenteFormulario(panelFormulario, campoConfirmarContrasena, gbc, 1, fila++);
        // Agregar checkbox para mostrar/ocultar contraseña
        agregarComponenteFormulario(panelFormulario, checkMostrarContrasena, gbc, 1, fila++);
        
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        panelBotones.add(botonLimpiar);
        panelBotones.add(botonAceptar);
        
        add(panelFormulario, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);
        campoNombre.getCampo().requestFocus();
    }

    /**
     * Agrega un componente al contenedor con las restricciones de GridBagLayout.
     */
    private void agregarComponenteFormulario(Container container, JComponent component, GridBagConstraints gbc, int gridx, int gridy) {
        gbc.gridx = gridx;
        gbc.gridy = gridy;
        container.add(component, gbc);
    }

    /**
     * Limpia todos los campos del formulario.
     */
    private void limpiarFormulario() {
        CampoRegistro[] campos = {campoNombre, campoApellidos, campoDNI, campoCorreo, campoDireccion, campoTelefono};
        for (CampoRegistro campo : campos) {
            campo.getCampo().setText("");
        }
        campoContrasena.setText("");
        campoConfirmarContrasena.setText("");
    }

    /**
     * Configura las propiedades de la ventana, incluyendo el título, tamaño y el ícono.
     */
    private void configurarVentana() {
        setTitle("Formulario de Registro");
        setSize(ANCHO, ALTO);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        try {
            ImageIcon icono = new ImageIcon(getClass().getResource("/icono.png"));
            if (icono.getImage() != null) {
                setIconImage(icono.getImage());
            } else {
                System.err.println("No se pudo cargar el ícono. Verifique la ruta del archivo.");
            }
        } catch (Exception e) {
            System.err.println("Error al cargar el ícono: " + e.getMessage());
        }
    }

    /**
     * Método principal para iniciar la aplicación.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new FormularioRegistro().setVisible(true));
    }

    // ================== CLASES INTERNAS ================== //

    /**
     * Filtro para DNI: Permite 8 números seguidos de 1 letra mayúscula.
     */
    private static class DniFilter extends DocumentFilter {
        @Override
        public void insertString(FilterBypass fb, int offset, String text, AttributeSet attr) throws BadLocationException {
            String newText = text.replaceAll("[^\\dA-Za-z]", "").toUpperCase();
            super.insertString(fb, offset, newText, attr);
            formatearDNI(fb);
        }
        @Override
        public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
            String newText = text.replaceAll("[^\\dA-Za-z]", "").toUpperCase();
            super.replace(fb, offset, length, newText, attrs);
            formatearDNI(fb);
        }
        private void formatearDNI(FilterBypass fb) throws BadLocationException {
            String contenido = fb.getDocument().getText(0, fb.getDocument().getLength());
            if (contenido.length() > 8) {
                String numeros = contenido.substring(0, 8).replaceAll("[^\\d]", "");
                String letra = contenido.substring(8).replaceAll("[^A-Z]", "");
                super.replace(fb, 0, fb.getDocument().getLength(), numeros + letra, null);
            }
        }
    }

    /**
     * Filtro para teléfono: Solo permite dígitos.
     */
    private static class TelefonoFilter extends DocumentFilter {
        @Override
        public void insertString(FilterBypass fb, int offset, String text, AttributeSet attr) throws BadLocationException {
            super.insertString(fb, offset, text.replaceAll("[^\\d]", ""), attr);
        }
        @Override
        public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
            super.replace(fb, offset, length, text.replaceAll("[^\\d]", ""), attrs);
        }
    }

    /**
     * Filtro para capitalizar el primer carácter de los campos.
     */
    private static class CapitalizeFilter extends DocumentFilter {
        @Override
        public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
            if (string != null && fb.getDocument().getLength() == 0) {
                string = string.toUpperCase();
            }
            super.insertString(fb, offset, string, attr);
        }
        @Override
        public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
            if (text != null && fb.getDocument().getLength() == 0) {
                text = text.toUpperCase();
            }
            super.replace(fb, offset, length, text, attrs);
        }
    }

    /**
     * Clase interna para campos de registro que contiene una etiqueta y un campo de texto.
     */
    private static class CampoRegistro {
        private final JLabel etiqueta;
        private final JTextField campo;
        /**
         * Constructor de CampoRegistro.
         * @param texto Texto para la etiqueta.
         * @param capitalizar Si es true, capitaliza el primer carácter del campo.
         */
        public CampoRegistro(String texto, boolean capitalizar) {
            etiqueta = new JLabel(texto + ":");
            campo = new JTextField(15);
            if (capitalizar) {
                PlainDocument doc = (PlainDocument) campo.getDocument();
                doc.setDocumentFilter(new CapitalizeFilter());
            }
        }
        public JLabel getEtiqueta() { return etiqueta; }
        public JTextField getCampo() { return campo; }
    }
}
