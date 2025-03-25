import java.awt.*;
import javax.swing.*;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import javax.swing.text.PlainDocument;

public class FormularioRegistro extends JFrame {

    // Dimensiones de la ventana
    private static final int ANCHO = 400;
    private static final int ALTO = 300;

    // Componentes del formulario
    private JTextField campoNombre;
    private JTextField campoApellidos;
    private JTextField campoDNI;
    private JTextField campoCorreo;
    private JTextField campoDireccion; // Se agrega el nuevo campo Dirección de Emmanuel
    private JTextField campoTelefono;
    private JButton botonAceptar;
    private JButton botonLimpiar;

    // Instancias de CapitalizeFilter para cada campo
    private CapitalizeFilter capitalizeNombreFilter = new CapitalizeFilter();
    private CapitalizeFilter capitalizeApellidosFilter = new CapitalizeFilter();
    private CapitalizeFilter capitalizeDireccionFilter = new CapitalizeFilter();

    public FormularioRegistro() {
        inicializarComponentes();
        configurarVentana();
    }

    /**
     * Método para inicializar y organizar los componentes del formulario.
     */
    private void inicializarComponentes() {
        // Usamos BorderLayout para organizar el contenido principal
        setLayout(new BorderLayout());

        // Creamos un panel para el formulario
        JPanel panelFormulario = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10); // Márgenes entre los componentes

        // Configuración común para las etiquetas (alineadas a la izquierda)
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.NONE; // No expandir las etiquetas horizontalmente
        gbc.weightx = 0; // No ocupar espacio extra en ancho

        // Añadimos las etiquetas y campos al formulario
        int fila = 0; // Variable para controlar la fila actual

        agregarComponenteFormulario(panelFormulario, new JLabel("Nombre:"), gbc, 0, fila);
        campoNombre = new JTextField(15);
        agregarComponenteFormulario(panelFormulario, campoNombre, gbc, 1, fila++);
        ((PlainDocument) campoNombre.getDocument()).setDocumentFilter(new CapitalizeFilter()); // Capitalizar Nombre

        agregarComponenteFormulario(panelFormulario, new JLabel("Apellidos:"), gbc, 0, fila);
        campoApellidos = new JTextField(15);
        agregarComponenteFormulario(panelFormulario, campoApellidos, gbc, 1, fila++);
        ((PlainDocument) campoApellidos.getDocument()).setDocumentFilter(new CapitalizeFilter()); // Capitalizar Apellidos

        agregarComponenteFormulario(panelFormulario, new JLabel("DNI:"), gbc, 0, fila);
        campoDNI = new JTextField(15);
        agregarComponenteFormulario(panelFormulario, campoDNI, gbc, 1, fila++);

        agregarComponenteFormulario(panelFormulario, new JLabel("Correo Electrónico:"), gbc, 0, fila);
        campoCorreo = new JTextField(20);
        agregarComponenteFormulario(panelFormulario, campoCorreo, gbc, 1, fila++);

        agregarComponenteFormulario(panelFormulario, new JLabel("Dirección:"), gbc, 0, fila);
        campoDireccion = new JTextField(20);
        agregarComponenteFormulario(panelFormulario, campoDireccion, gbc, 1, fila++);
        ((PlainDocument) campoDireccion.getDocument()).setDocumentFilter(capitalizeDireccionFilter); // Capitalizar Dirección


        agregarComponenteFormulario(panelFormulario, new JLabel("Teléfono:"), gbc, 0, fila);
        campoTelefono = new JTextField(12);
        agregarComponenteFormulario(panelFormulario, campoTelefono, gbc, 1, fila++);

        // Configuración de los botones "Limpiar" y "Aceptar"
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));

        botonLimpiar = new JButton("Limpiar");
        botonLimpiar.addActionListener(e -> limpiarFormulario());
        panelBotones.add(botonLimpiar);

        botonAceptar = new JButton("Aceptar");
        botonAceptar.addActionListener(e -> validarFormulario());
        panelBotones.add(botonAceptar);

        gbc.gridx = 0;
        gbc.gridy = fila++;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        panelFormulario.add(panelBotones, gbc);

        // Añadimos el panel al centro del BorderLayout
        add(panelFormulario, BorderLayout.CENTER);

        // Establecer el foco inicial en el campo "Nombre"
        campoNombre.requestFocus();
    }

    /**
     * Método auxiliar para añadir componentes al panel con GridBagLayout.
     * @param container El contenedor al que se añade el componente.
     * @param component El componente a añadir.
     * @param gbc Las restricciones de GridBagLayout para el componente.
     * @param gridx La columna en la que se añade el componente.
     * @param gridy La fila en la que se añade el componente.
     */
    private void agregarComponenteFormulario(Container container, JComponent component, GridBagConstraints gbc, int gridx, int gridy) {
        gbc.gridx = gridx;
        gbc.gridy = gridy;
        container.add(component, gbc);
    }

    /**
     * Método que valida los datos ingresados antes de cerrar el formulario.
     */
    private void validarFormulario() {
        String nombre = campoNombre.getText().trim();
        String apellidos = campoApellidos.getText().trim();
        String dni = campoDNI.getText().trim();
        String correo = campoCorreo.getText().trim();
        String direccion = campoDireccion.getText().trim(); //se obtiene el valor del nuevo campo Dirección
        String telefono = campoTelefono.getText().trim();

        // Verificar si algún campo está vacío
        if (nombre.isEmpty() || apellidos.isEmpty() || dni.isEmpty() || correo.isEmpty() || direccion.isEmpty() || telefono.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Verificar si el DNI tiene el formato correcto (8 números seguidos de una letra mayúscula)
        if (!validarDNI(dni)) {
            JOptionPane.showMessageDialog(this, "DNI incorrecto. Debe tener 8 números y una letra mayúscula al final.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Verificar si el correo electrónico tiene el formato correcto
        if (!validarCorreo(correo)) {
            JOptionPane.showMessageDialog(this, "Correo electrónico incorrecto. Debe tener el formato nombre@dominio.com", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Verificar si el teléfono tiene el formato correcto (9 dígitos)
        if (!validarTelefono(telefono)) {
            JOptionPane.showMessageDialog(this, "Teléfono incorrecto. Debe tener 9 dígitos.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Si todo está correcto, mostrar mensaje de éxito y cerrar la ventana
        JOptionPane.showMessageDialog(this, "Formulario enviado con éxito", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        dispose();
    }

    /**
     * Valida que el DNI tenga el formato correcto (8 números seguidos de una letra mayúscula).
     * @param dni El DNI a validar.
     * @return true si el DNI es válido, false en caso contrario.
     */
    private boolean validarDNI(String dni) {
        if (!dni.matches("\\d{8}[A-Z]")) {
            return false;
        }

        // Algoritmo para validar la letra del DNI
        String letras = "TRWAGMYFPDXBNJZSQVHLCKE";
        int numeroDNI = Integer.parseInt(dni.substring(0, 8));
        char letraCalculada = letras.charAt(numeroDNI % 23);
        char letraIntroducida = dni.charAt(8);

        return letraCalculada == letraIntroducida;
    }

    /**
     * Valida que el correo electrónico tenga el formato correcto.
     * @param correo El correo electrónico a validar.
     * @return true si el correo es válido, false en caso contrario.
     */
    private boolean validarCorreo(String correo) {
        return correo.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$");
    }

    /**
     * Valida que el teléfono tenga el formato correcto (9 dígitos).
     * @param telefono El teléfono a validar.
     * @return true si el teléfono es válido, false en caso contrario.
     */
    private boolean validarTelefono(String telefono) {
        return telefono.matches("\\d{9}");
    }

    /**
     * Método para limpiar los campos del formulario.
     */
    private void limpiarFormulario() {
        campoNombre.setText("");
        campoApellidos.setText("");
        campoDNI.setText("");
        campoCorreo.setText("");
        campoDireccion.setText(""); // Se limpia el campo de Dirección
        campoTelefono.setText("");

        // Resetear los filtros de capitalización
        capitalizeNombreFilter.reset();
        capitalizeApellidosFilter.reset();
        capitalizeDireccionFilter.reset();
    }

    /**
     * Configura las propiedades principales de la ventana.
     */
    private void configurarVentana() {
        setTitle("Formulario de registro Compartido");          // Título de la ventana
        setSize(ANCHO, ALTO);                        // Dimensiones de la ventana
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Cerrar aplicación al cerrar ventana
        setLocationRelativeTo(null);                // Centrar ventana en pantalla
        setResizable(false);                        // Evitar redimensionamiento manual

        // Establecer un icono personalizado para la ventana
        try {
            setIconImage(new ImageIcon(getClass().getResource("/icono.png")).getImage());
        } catch (Exception e) {
            System.err.println("No se pudo cargar el icono: " + e.getMessage());
        }
    }

    /**
     * Clase interna para capitalizar la primera letra de cada palabra en un JTextField.
     */
    private static class CapitalizeFilter extends DocumentFilter {
        private boolean isFirstLetter = true;

        @Override
        public void insertString(FilterBypass fb, int offset, String text, AttributeSet attr) throws BadLocationException {
            if (text != null && text.length() > 0 && isFirstLetter) {
                text = text.substring(0, 1).toUpperCase() + text.substring(1);
                isFirstLetter = false; // Desactivar después de la primera letra
            }
            super.insertString(fb, offset, text, attr);
        }

        @Override
        public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
            if (text != null && text.length() > 0 && isFirstLetter) {
                text = text.substring(0, 1).toUpperCase() + text.substring(1);
                isFirstLetter = false; // Desactivar después de la primera letra
            }
            super.replace(fb, offset, length, text, attrs);
        }

        // Reiniciar el estado cuando se limpie el campo
        public void reset() {
            isFirstLetter = true;
        }
    }

    /**
     * Método principal.
     * @param args Los argumentos de la línea de comandos.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            FormularioRegistro formulario = new FormularioRegistro();
            formulario.setVisible(true);
        });
    }
}
