package e_was.backend;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordUtil {
    public static String hashPassword(String plainText) {
        return new BCryptPasswordEncoder().encode(plainText);
    }
}