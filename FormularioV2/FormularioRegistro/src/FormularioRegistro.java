import java.awt.*;
import javax.swing.*;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import javax.swing.text.PlainDocument;
import java.util.regex.Pattern;

/**
 * Clase de validación para los campos del formulario.
 */
class Validador {
    private static final Pattern PATRON_DNI = Pattern.compile("\\d{8}[A-Z]");
    private static final Pattern PATRON_CORREO = Pattern.compile("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$");
    private static final Pattern PATRON_TELEFONO = Pattern.compile("\\d{9}");

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
}

/**
 * Formulario de Registro con validación de campos.
 */
public class FormularioRegistro extends JFrame {
    // Dimensiones de la ventana
    private static final int ANCHO = 400;
    private static final int ALTO = 350;

    // Campos de registro
    private final CampoRegistro campoNombre;
    private final CampoRegistro campoApellidos;
    private final CampoRegistro campoDNI;
    private final CampoRegistro campoCorreo;
    private final CampoRegistro campoDireccion;
    private final CampoRegistro campoTelefono;

    // Botones del formulario
    private final JButton botonAceptar;
    private final JButton botonLimpiar;

    /**
     * Constructor del formulario de registro.
     */
    public FormularioRegistro() {
        // Inicializar campos con sus respectivas configuraciones
        campoNombre = new CampoRegistro("Nombre", true);
        campoApellidos = new CampoRegistro("Apellidos", true);
        campoDNI = new CampoRegistro("DNI", false);
        campoCorreo = new CampoRegistro("Correo Electrónico", false);
        campoDireccion = new CampoRegistro("Dirección", true);
        campoTelefono = new CampoRegistro("Teléfono", false);

        botonAceptar = new JButton("Aceptar");
        botonLimpiar = new JButton("Limpiar");

        inicializarComponentes();
        configurarVentana();
        configurarEventos();
    }

    /**
     * Configura los eventos de los botones.
     */
    private void configurarEventos() {
        botonLimpiar.addActionListener(e -> limpiarFormulario());
        botonAceptar.addActionListener(e -> validarFormulario());
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

        // Arreglo de campos para una inicialización más eficiente
        CampoRegistro[] campos = {
            campoNombre, campoApellidos, campoDNI, 
            campoCorreo, campoDireccion, campoTelefono
        };

        // Añadir campos al formulario
        int fila = 0;
        for (CampoRegistro campo : campos) {
            agregarComponenteFormulario(panelFormulario, campo.getEtiqueta(), gbc, 0, fila);
            agregarComponenteFormulario(panelFormulario, campo.getCampo(), gbc, 1, fila++);
        }

        // Configurar panel de botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        panelBotones.add(botonLimpiar);
        panelBotones.add(botonAceptar);

        add(panelFormulario, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);

        // Establecer foco inicial
        campoNombre.getCampo().requestFocus();
    }

    /**
     * Añade un componente al formulario con las restricciones de GridBagLayout.
     */
    private void agregarComponenteFormulario(Container container, JComponent component, 
                                            GridBagConstraints gbc, int gridx, int gridy) {
        gbc.gridx = gridx;
        gbc.gridy = gridy;
        container.add(component, gbc);
    }

    /**
     * Valida todos los campos del formulario antes de enviar.
     */
    private void validarFormulario() {
        CampoRegistro[] campos = {
            campoNombre, campoApellidos, campoDNI, 
            campoCorreo, campoDireccion, campoTelefono
        };

        // Verificar campos vacíos
        for (CampoRegistro campo : campos) {
            if (campo.getCampo().getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, 
                    "El campo " + campo.getEtiqueta().getText() + " es obligatorio", 
                    "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        // Validaciones específicas
        if (!Validador.validarDNI(campoDNI.getCampo().getText())) {
            JOptionPane.showMessageDialog(this, 
                "DNI incorrecto. Debe tener 8 números y una letra mayúscula", 
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!Validador.validarCorreo(campoCorreo.getCampo().getText())) {
            JOptionPane.showMessageDialog(this, 
                "Correo electrónico incorrecto", 
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!Validador.validarTelefono(campoTelefono.getCampo().getText())) {
            JOptionPane.showMessageDialog(this, 
                "Teléfono incorrecto. Debe tener 9 dígitos", 
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Envío exitoso
        JOptionPane.showMessageDialog(this, 
            "Formulario enviado con éxito", 
            "Éxito", JOptionPane.INFORMATION_MESSAGE);
        
        dispose();
    }

    /**
     * Limpia todos los campos del formulario.
     */
    private void limpiarFormulario() {
        CampoRegistro[] campos = {
            campoNombre, campoApellidos, campoDNI, 
            campoCorreo, campoDireccion, campoTelefono
        };

        for (CampoRegistro campo : campos) {
            campo.getCampo().setText("");
        }
    }

    /**
     * Configura las propiedades de la ventana.
     */
    private void configurarVentana() {
        setTitle("Formulario de Registro");
        setSize(ANCHO, ALTO);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Cargar ícono personalizado
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
     * Clase interna para campos de registro con etiqueta y campo de texto.
     */
    private static class CampoRegistro {
        private final JLabel etiqueta;
        private final JTextField campo;

        /**
         * Constructor de CampoRegistro.
         * @param texto Texto de la etiqueta
         * @param capitalizar Indica si se debe capitalizar el primer carácter
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
     * Método principal para iniciar la aplicación.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new FormularioRegistro().setVisible(true));
    }
}



