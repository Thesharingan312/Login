import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.prefs.Preferences;
import java.time.LocalDateTime;

public class AuthenticationService {
    private final String username;
    private final byte[] encryptedPassword;
    private int failedAttempts;
    private LocalDateTime lockUntil;
    private static final int MAX_ATTEMPTS = 3;
    private static final int LOCK_DURATION_SECONDS = 30;
    
    public AuthenticationService(String username, String password) {
        this.username = username;
        this.encryptedPassword = encryptPassword(password);
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
        
        boolean isAuthenticated = inputUsername.equals(username) && 
                Arrays.equals(encryptPassword(inputPassword), encryptedPassword);
        
        if (isAuthenticated) {
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
    
    /**
     * Encripta la contraseña utilizando SHA-256
     */
    private byte[] encryptPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            return digest.digest(password.getBytes());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error al encriptar la contraseña", e);
        }
    }
    
    public String getUsername() {
        return username;
    }
    
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
