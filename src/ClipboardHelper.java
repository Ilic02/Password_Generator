import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;

public class ClipboardHelper {
    public static void copyToClipboard(String text) {
        Clipboard clipboard = Clipboard.getSystemClipboard();
        ClipboardContent content = new ClipboardContent();
        content.putString(text);
        clipboard.setContent(content);
    }
}
