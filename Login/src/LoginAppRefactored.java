import javax.swing.SwingUtilities;

/**
 * Clase principal que inicia la aplicación de inicio de sesión
 */
public class LoginAppRefactored {
    public static void main(String[] args) {
        // Ahora creamos un AuthenticationService sin credenciales predefinidas
        AuthenticationService authService = new AuthenticationService();
        
        // Creación de la interfaz gráfica
        SwingUtilities.invokeLater(() -> {
            LoginUI loginUI = new LoginUI(authService);
            loginUI.display();
        });
    }
}
// public class LoginAppRefactored {
//     public static void main(String[] args) {
//         // Configuración de credenciales
//         AuthenticationService authService = new AuthenticationService(
//             "Emmanuel Pallares",
//             "220610"
//         );
        
//         // Creación de la interfaz gráfica
//         SwingUtilities.invokeLater(() -> {
//             LoginUI loginUI = new LoginUI(authService);
//             loginUI.display();
//         });
//     }
// }