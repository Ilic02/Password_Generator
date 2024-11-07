import java.util.Random;

public class PasswordGenerator {
    public static String generate(int n, boolean useUpper, boolean useLower, boolean useNumbers, boolean useSpecial) {
        String upper = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String lower = upper.toLowerCase();
        String numbers = "0123456789";
        String special = ",./;'{}[]-+*`~";

        String chars = "";
        if (useUpper)
            chars += upper;
        if (useLower)
            chars += lower;
        if (useNumbers)
            chars += numbers;
        if (useSpecial)
            chars += special;

        if (chars.isEmpty()) {
            AlertHelper.showErrorAlert("Selection Error", "Please select at least one character set");
            return null;
        }

        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        while (sb.length() < n) {
            int index = random.nextInt(chars.length());
            sb.append(chars.charAt(index));
        }

        return sb.toString();
    }
}
