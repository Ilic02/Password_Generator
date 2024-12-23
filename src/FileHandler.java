import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class FileHandler {
    private static final String FILE_PATH = "src/Passwords.txt";

    public static boolean siteExists(String website) {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains("Website: " + website))
                    return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void savePassword(String website, String password) throws IOException {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime time = LocalDateTime.now();

        String savedPassword = "Website: " + website + " \nPassword: " + password + " \nDate: " + dtf.format(time) + "\n\n";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, true))) {
            writer.write(savedPassword);
        }
    }
}
