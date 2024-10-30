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

        Label lblInfo = new Label("Your password has been saved in file Passwords.txt!");
        lblInfo.setVisible(false);
        lblInfo.setTextFill(Color.GREEN);

        CheckBox cbUppercase = new CheckBox("Include Uppercase Letters (A-Z)");
        CheckBox cbLowercase = new CheckBox("Include Lowercase Letters (a-z)");
        CheckBox cbNumbers = new CheckBox("Include Numbers (0-9)");
        CheckBox cbSpecialChars = new CheckBox("Include Special Characters");

        vb.getChildren().addAll(lblWebsite, tfWebsite, lblLength, tfLength, cbUppercase, cbLowercase, cbNumbers, cbSpecialChars, btnGenerate ,lblRandomPass, ta, btnCopy, lblInfo);

        root.getChildren().addAll(vb);

        btnGenerate.setOnAction(e->{
            String website = tfWebsite.getText().trim();
            String length = tfLength.getText().trim();
            tfWebsite.clear();
            tfLength.clear();
            lblInfo.setVisible(false);
            ta.clear();
            btnCopy.setDisable(true);

            try{
                int passLength = Integer.parseInt(length);

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
                    lblInfo.setText("Your password has been saved in file Passwords.txt!");
                    lblInfo.setTextFill(Color.GREEN);
                    lblInfo.setVisible(true);
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

                lblInfo.setText("Password copied to clipboard!");
                lblInfo.setTextFill(Color.BLUE);
                lblInfo.setVisible(true);
            }
        });

        Scene scene = new Scene(root, 300, 350);

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