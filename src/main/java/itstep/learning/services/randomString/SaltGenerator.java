package itstep.learning.services.randomString;

import java.security.SecureRandom;
import java.util.Base64;

public class SaltGenerator implements RandomStringGenerator {
    @Override
    public String generate() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);

        return Base64.getEncoder().encodeToString(salt);
    }
}
