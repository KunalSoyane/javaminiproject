import java.util.Random;

public class OtpGenerator {

    public static String generateOTP() {
        // Generate a 6-digit random number
        Random random = new Random();
        int otpNumber = 100000 + random.nextInt(900000);
        return String.valueOf(otpNumber);
    }
}