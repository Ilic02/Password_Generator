import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.Random;

public class Main extends Application{
    private Scene scene;
    private boolean isDarkMode = false;

    protected static String password(int n, boolean useUpper, boolean useLower, boolean useNumbers, boolean useSpecial) {
        String upper = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String lower = upper.toLowerCase();
        String numbers = "0123456789";
        String special = ",./;'{}[]-+*`~";

        String chars = "";
        if(useUpper)
            chars += upper;
        if(useLower)
            chars += lower;
        if(useNumbers)
            chars += numbers;
        if(useSpecial)
            chars += special;

        if(chars.isEmpty())
            return "Please select at least one character set";

        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        while(sb.length() < n){
            int index = (int) (random.nextFloat() * chars.length());
            sb.append(chars.charAt(index));
        }

        return sb.toString();
    }

    private boolean siteExists(String website){
        try(BufferedReader reader = new BufferedReader(new FileReader("src/Passwords.txt"))){
            String line;
            while((line = reader.readLine()) != null){
                if(line.contains("Website: " + website))
                    return true;
            }
        }catch (IOException e){
            e.printStackTrace();
        }
        return false;
    }

    private void toggleTheme(){
        if(isDarkMode){
            setLightTheme();
        }
        else{
            setDarkTheme();
        }
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

    private void showInfoAlert(String title, String message){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showErrorAlert(String title, String message){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        HBox root = new HBox(10);
        root.setPadding(new Insets(10,10,10,10));

        VBox vb = new VBox(10);

        Label lblWebsite = new Label("Insert a website:");
        TextField tfWebsite = new TextField();
        tfWebsite.setMinWidth(250);

        Label lblLength = new Label("Insert password length:");
        TextField tfLength = new TextField();
        tfLength.setMinWidth(250);

        Button btnGenerate = new Button("Generate");
        btnGenerate.setPadding(new Insets(5,5,5,5));

        Label lblRandomPass = new Label("Random password:");
        TextArea ta = new TextArea();
        ta.setEditable(false);
        ta.setMinWidth(250);
        ta.setMinHeight(100);

        Button btnCopy = new Button("Copy to Clipboard");
        btnCopy.setPadding(new Insets(5,5,5,5));
        btnCopy.setDisable(true);

        Label lblInfo = new Label();
        lblInfo.setVisible(false);
        lblInfo.setTextFill(Color.GREEN);

        CheckBox cbUppercase = new CheckBox("Include Uppercase Letters (A-Z)");
        CheckBox cbLowercase = new CheckBox("Include Lowercase Letters (a-z)");
        CheckBox cbNumbers = new CheckBox("Include Numbers (0-9)");
        CheckBox cbSpecialChars = new CheckBox("Include Special Characters");

        Button btnToggleTheme = new Button("Toggle Theme");

        vb.getChildren().addAll(lblWebsite, tfWebsite, lblLength, tfLength, cbUppercase, cbLowercase, cbNumbers, cbSpecialChars, btnGenerate ,lblRandomPass, ta, btnCopy, lblInfo, btnToggleTheme);

        root.getChildren().addAll(vb);

        btnGenerate.setOnAction(e->{
            String website = tfWebsite.getText().trim();
            String length = tfLength.getText().trim();
            tfWebsite.clear();
            tfLength.clear();
            lblInfo.setVisible(false);
            ta.clear();
            btnCopy.setDisable(true);

            if(website.isEmpty()){
//                lblInfo.setText("Please insert a valid website name!");
//                lblInfo.setTextFill(Color.RED);
//                lblInfo.setVisible(true);
                showErrorAlert("Website name error", "Please enter a valid website name.");
                return;
            }

            try{
                int passLength = Integer.parseInt(length);

                if(passLength < 8 || passLength > 24){
//                    lblInfo.setText("Password length must be between 8 and 24 characters!");
//                    lblInfo.setTextFill(Color.RED);
//                    lblInfo.setVisible(true);
                    showErrorAlert("Input Error", "Password length must be between 8 and 24 characters!");
                    return;
                }

                boolean useUpper = cbUppercase.isSelected();
                boolean useLower = cbLowercase.isSelected();
                boolean useNumbers = cbNumbers.isSelected();
                boolean useSpecial = cbSpecialChars.isSelected();

                if(siteExists(website)){
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Duplicate website");
                    alert.setHeaderText("Website already exists");
                    alert.setContentText("Do you want to overwrite the existing password?");

                    Optional<ButtonType> result = alert.showAndWait();
                    if(result.isPresent() && result.get() == ButtonType.CANCEL){
                        lblInfo.setText("Password not saved.");
                        lblInfo.setTextFill(Color.RED);
                        lblInfo.setVisible(true);
                        return;
                    }
                }

                String randomPassword = password(passLength, useUpper, useLower, useNumbers, useSpecial);
                ta.appendText(randomPassword + "\n");

                btnCopy.setDisable(false);

                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
                LocalDateTime time = LocalDateTime.now();

                String savedPassword = new String("Website: " + website + " \nPassword: " + randomPassword + " \nDate: " + dtf.format(time) + "\n\n");
                try(BufferedWriter writer = new BufferedWriter(new FileWriter("src/Passwords.txt", true))){
                    writer.write(savedPassword);
//                    lblInfo.setText("Your password has been saved in file Passwords.txt!");
//                    lblInfo.setTextFill(Color.GREEN);
//                    lblInfo.setVisible(true);
                    showInfoAlert("Success", "Your password has been saved in file Passwords.txt!");
                }
            }catch (NumberFormatException ex){
                lblInfo.setText("Please insert correct number for password length!");
                lblInfo.setTextFill(Color.RED);
            }catch (IOException ex){
                System.out.println("Invalid path");
            }
        });

        btnCopy.setOnAction(e->{
            String passwordText = ta.getText().trim();
            if(!passwordText.isEmpty()){
                Clipboard clipboard = Clipboard.getSystemClipboard();
                ClipboardContent content = new ClipboardContent();
                content.putString(passwordText);
                clipboard.setContent(content);

//                lblInfo.setText("Password copied to clipboard!");
//                lblInfo.setTextFill(Color.BLUE);
//                lblInfo.setVisible(true);
                showInfoAlert("Clipboard", "Password copied to clipboard!");
            }
        });

        btnToggleTheme.setOnAction(e-> toggleTheme());

        scene = new Scene(root, 550, 550);

        scene.setOnKeyPressed(event -> {
            if(event.getCode().toString().equals("ESCAPE")){
                stage.close();
            }
        });

        stage.setScene(scene);
        stage.setTitle("Password Generator");
        stage.show();
    }
}