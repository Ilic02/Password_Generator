import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PinManager {
    private static final String PIN_FILE = "pin.txt";

    private static String hash(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = md.digest(input.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : hashedBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Hashing failed", e);
        }
    }

    public static boolean isPinSet() {
        return new File(PIN_FILE).exists();
    }

    public static void setPin(String pin) throws IOException {
        String hashed = hash(pin);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(PIN_FILE))) {
            writer.write(hashed);
        }
    }

    public static boolean verifyPin(String pin) throws IOException {
        String hashed = hash(pin);
        String savedHash;
        try (BufferedReader reader = new BufferedReader(new FileReader(PIN_FILE))) {
            savedHash = reader.readLine();
        }
        return hashed.equals(savedHash);
    }
}
