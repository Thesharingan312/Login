import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.prefs.Preferences;
import java.time.LocalDateTime;

public class AuthenticationService {
    private  String username;
    private  String encryptedPassword;
    private int failedAttempts;
    private LocalDateTime lockUntil;
    private static final int MAX_ATTEMPTS = 3;
    private static final int LOCK_DURATION_SECONDS = 30;
    
    public AuthenticationService(String username, String password) {
        this.username = "";
        this.encryptedPassword = encryptPassword("");
        this.failedAttempts = 0;
        this.lockUntil = null;
    }
    public AuthenticationService() {
        // Inicializa con valores predeterminados
        this.username = "";
        this.encryptedPassword = encryptPassword("");
        this.failedAttempts = 0;
        this.lockUntil = null;
    }
    /**
     * Verifica las credenciales del usuario
     */
    public boolean authenticate(String inputUsername, String inputPassword) {
        // Verificar si la cuenta está bloqueada
        if (isLocked()) {
            return false;
        }
        
        // Encriptar la contraseña de entrada para comparación
        String encryptedInputPassword = encryptPassword(inputPassword);
        
        // Usar UserStorageManager para validar
        boolean isAuthenticated = UserStorageManager.validateUser(inputUsername, encryptedInputPassword);
        
        if (isAuthenticated) {
            this.username = inputUsername;
            this.encryptedPassword = encryptedInputPassword;
            // Reiniciar contador de intentos fallidos si la autenticación es exitosa
            resetFailedAttempts();
            return true;
        } else {
            // Incrementar contador de intentos fallidos
            failedAttempts++;
            
            // Bloquear la cuenta si se excede el número máximo de intentos
            if (failedAttempts >= MAX_ATTEMPTS) {
                lockAccount();
            }
            
            return false;
        }
    }
    
    /**
     * Registra un nuevo usuario
     */
    public boolean register(String username, String password) {
        // Verificar si el usuario ya existe
        if (UserStorageManager.userExists(username)) {
            return false;
        }
        
        // Encriptar la contraseña
        String encryptedPassword = encryptPassword(password);
        
        // Guardar el usuario
        return UserStorageManager.saveUser(username, encryptedPassword);
    }
    
    
    /**
     * Verifica si la cuenta está bloqueada
     */
    public boolean isLocked() {
        if (lockUntil == null) {
            return false;
        }
        
        if (LocalDateTime.now().isAfter(lockUntil)) {
            // El tiempo de bloqueo ha expirado
            lockUntil = null;
            resetFailedAttempts();
            return false;
        }
        
        return true;
    }
    
    /**
     * Bloquea la cuenta temporalmente
     */
    private void lockAccount() {
        lockUntil = LocalDateTime.now().plusSeconds(LOCK_DURATION_SECONDS);
    }
    
    /**
     * Reinicia el contador de intentos fallidos
     */
    private void resetFailedAttempts() {
        failedAttempts = 0;
    }
    
    /**
     * Obtiene el tiempo restante de bloqueo en segundos
     */
    public long getRemainingLockTime() {
        if (!isLocked() || lockUntil == null) {
            return 0;
        }
        
        return java.time.Duration.between(LocalDateTime.now(), lockUntil).getSeconds();
    }
    private String encryptPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = digest.digest(password.getBytes());
            
            // Convertir a representación hexadecimal
            StringBuilder hexString = new StringBuilder();
            for (byte hashedByte : hashedBytes) {
                String hex = Integer.toHexString(0xff & hashedByte);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error al encriptar la contraseña", e);
        }
    }
    /**
     * Devuelve el nombre de usuario actual
     * @return Nombre de usuario actual
     */
    public String getCurrentUsername() {
        return username;}
    
    /**
     * Guarda el nombre de usuario para futuras sesiones
     * @param username Nombre de usuario a guardar
     */
    public void saveUsername(String username) {
        Preferences prefs = Preferences.userNodeForPackage(AuthenticationService.class);
        prefs.put("savedUsername", username);
    }
    
    /**
     * Recupera el nombre de usuario guardado
     * @return El nombre de usuario guardado o cadena vacía si no hay ninguno
     */
    public String getSavedUsername() {
        Preferences prefs = Preferences.userNodeForPackage(AuthenticationService.class);
        return prefs.get("savedUsername", "");
    }
    
    /**
     * Elimina el nombre de usuario guardado
     */
    public void clearSavedUsername() {
        Preferences prefs = Preferences.userNodeForPackage(AuthenticationService.class);
        prefs.remove("savedUsername");
    }
}
