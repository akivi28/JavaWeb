package itstep.learning.services.randomString;

import java.util.Random;

public class PasswordGenerator implements RandomStringGenerator {
    private static final String LOWERCASE = "abcdefghijklmnopqrstuvwxyz";
    private static final String UPPERCASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String DIGITS = "0123456789";
    private static final String SPECIAL_CHARS = "!@#$%^&*()-_=+[]{}|;:,.<>?/";

    @Override
    public String generate() {
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        char character;
        for (int i = 0; i < 12; i++) {
            switch (random.nextInt(4)){
                case 0:
                    character = LOWERCASE.charAt(random.nextInt(LOWERCASE.length()));
                    sb.append(character);
                    break;
                case 1:
                    character = UPPERCASE.charAt(random.nextInt(UPPERCASE.length()));
                    sb.append(character);
                    break;
                case 2:
                    character = DIGITS.charAt(random.nextInt(DIGITS.length()));
                    sb.append(character);
                    break;
                case 3:
                    character = SPECIAL_CHARS.charAt(random.nextInt(SPECIAL_CHARS.length()));
                    sb.append(character);
                    break;
            }
        }

        return sb.toString();
    }
}
