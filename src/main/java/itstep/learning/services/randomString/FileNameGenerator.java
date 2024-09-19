package itstep.learning.services.randomString;

import java.util.Random;

public class FileNameGenerator implements RandomStringGenerator {
    @Override
    public String generate() {
        StringBuilder result = new StringBuilder();
        result.append("file-");

        Random rand = new Random();
        for (int i = 0; i < 10; i++) {
            result.append((char) ('a' + rand.nextInt(26)));
        }
        return result.toString();
    }
}
