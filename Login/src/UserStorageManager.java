import java.io.*;
import java.util.*;

public class UserStorageManager {
    private static final String USER_FILE_PATH = "users.txt";
    private static final String DELIMITER = ",";

    /**
     * Guarda un nuevo usuario en el archivo de usuarios
     * @param username Nombre de usuario
     * @param encryptedPassword Contraseña encriptada
     * @return true si el usuario se guardó con éxito, false si ya existe
     */
    public static boolean saveUser (String username, String encryptedPassword) {
        // Primero verificamos si el archivo de usuarios existe
        File file = new File(USER_FILE_PATH);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }

        // Verificamos si el usuario ya existe
        if (userExists(username)) {
            return false;
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(USER_FILE_PATH, true))) {
            // Formato: username,encryptedPassword
            writer.write(username + DELIMITER + encryptedPassword);
            writer.newLine();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Verifica si un usuario existe en el archivo
     * @param username Nombre de usuario a verificar
     * @return true si el usuario existe, false en caso contrario
     */
    public static boolean userExists(String username) {
        // Primero verificamos si el archivo de usuarios existe
        File file = new File(USER_FILE_PATH);
        if (!file.exists()) {
            return false;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(USER_FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(DELIMITER);
                if (parts[0].equals(username)) {
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Valida las credenciales de un usuario
     * @param username Nombre de usuario
     * @param encryptedPassword Contraseña encriptada
     * @return true si las credenciales son válidas, false en caso contrario
     */
    public static boolean validateUser (String username, String encryptedPassword) {
        // Primero verificamos si el archivo de usuarios existe
        File file = new File(USER_FILE_PATH);
        if (!file.exists()) {
            return false;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(USER_FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(DELIMITER);
                if (parts[0].equals(username) && parts[1].equals(encryptedPassword)) {
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}