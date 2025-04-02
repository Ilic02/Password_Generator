import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class PasswordStorage {
    private static final String FILE_PATH = "src/Passwords.txt";

    public String getPassword(String website) {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            StringBuilder result = new StringBuilder();
            boolean found = false;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("Website: " + website)) {
                    found = true;
                }
                if (found) {
                    result.append(line).append("\n");
                    if (line.trim().isEmpty())
                        break;
                }
            }
            return found ? result.toString() : null;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean deletePassword(String website) {
        List<String> lines = new ArrayList<>();
        boolean found = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("Website: " + website)) {
                    found = true;
                    while ((line = reader.readLine()) != null && line.trim().isEmpty()) {
                    }
                } else {
                    lines.add(line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        if (!found)
            return false;

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (String l : lines) {
                writer.write(l + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
