import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

public class Example {

    // IMPORTANT: Replace these with your actual Twilio credentials and number
    // It's a best practice to use environment variables for these
    public static final String ACCOUNT_SID = "ACad653665dcf806e36ae35ba4e1cdafe5";
    public static final String AUTH_TOKEN = "67048a8e17a1f17c2c9a8d7a276d4197";
    public static final String TWILIO_PHONE_NUMBER = "+1 775 320 9176 "; // Your Twilio phone number

    public static void sendOtp(String recipientPhoneNumber) {
        // Initialize the Twilio client
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);

        // 1. Generate the OTP
        String otp = OtpGenerator.generateOTP();
        String messageBody = "Your verification code for the Online Voting System is: " + otp;

        try {
            // 2. Send the message
            Message message = Message.creator(
                            new PhoneNumber(recipientPhoneNumber), // TO: The user's phone number
                            new PhoneNumber(TWILIO_PHONE_NUMBER),  // FROM: Your Twilio number
                            messageBody)
                    .create();

            // The SID is a unique identifier for the message
            System.out.println("OTP sent successfully! Message SID: " + message.getSid());
            System.out.println("The OTP is: " + otp); // For testing purposes, you'd save this OTP to verify later

        } catch (Exception e) {
            System.err.println("Error sending OTP: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // A main method to test the functionality
    public static void main(String[] args) {
        // Replace with the phone number you want to send the OTP to.
        // Make sure to include the country code, e.g., "+91" for India.
        String userPhoneNumber = "+918657844569";
        sendOtp(userPhoneNumber);
    }
}