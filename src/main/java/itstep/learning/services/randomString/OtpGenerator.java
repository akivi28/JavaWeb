package itstep.learning.services.randomString;

import java.util.Random;

public class OtpGenerator implements RandomStringGenerator{
    @Override
    public String generate() {
        StringBuilder sb = new StringBuilder();
        Random rand = new Random();
        for (int i = 0; i < 6; i++){
            sb.append(rand.nextInt(10));
        }
        return sb.toString();
    }
}
