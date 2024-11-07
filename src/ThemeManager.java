import javafx.scene.Scene;

public class ThemeManager {
    private boolean isDarkMode = false;
    private Scene scene;

    public ThemeManager(Scene scene) {
        this.scene = scene;
    }

    public void toggleTheme(){
        if(isDarkMode)
            setLightTheme();
        else
            setDarkTheme();

        isDarkMode = !isDarkMode;
    }

    private void setLightTheme(){
        scene.getStylesheets().clear();
        scene.getStylesheets().add("styles/light-theme.css");
    }

    private void setDarkTheme(){
        scene.getStylesheets().clear();
        scene.getStylesheets().add("styles/dark-theme.css");
    }
}
