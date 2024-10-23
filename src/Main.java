import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

public class Main extends Application{
    protected static String password(int n){
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890,./;'{}[]-+*`~";
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        while(sb.length() < n){
            int index = (int) (random.nextFloat() * chars.length());
            sb.append(chars.charAt(index));
        }
        String pass = sb.toString();
        return pass;
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

        Label lblInfo = new Label("Your password has been saved in file Passwords.txt!");
        lblInfo.setVisible(false);
        lblInfo.setTextFill(Color.GREEN);

        vb.getChildren().addAll(lblWebsite, tfWebsite, lblLength, tfLength, btnGenerate ,lblRandomPass, ta, lblInfo);

        root.getChildren().addAll(vb);

        btnGenerate.setOnAction(e->{
            String website = tfWebsite.getText().trim();
            String length = tfLength.getText().trim();
            tfWebsite.clear();
            tfLength.clear();
            lblInfo.setVisible(false);
            ta.clear();

            String randomPassword = password(Integer.parseInt(length));
            ta.appendText(randomPassword);
            ta.appendText("\n");

            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
            LocalDateTime time = LocalDateTime.now();

            String savedPassword = new String("Website: " + website + " \nPassword: " + randomPassword + " \nDate: " + dtf.format(time) + "\n\n");
            if(Integer.parseInt(length) <= 0) {
                lblInfo.setVisible(true);
                lblInfo.setText("Please insert correct number for password length!");
                lblInfo.setTextFill(Color.RED);
                ta.appendText("\n ERROR - INCORRECT LENGTH FORMAT");
            }
            else {
                try {
                    BufferedWriter writer = new BufferedWriter(new FileWriter("src/Passwords.txt", true));
                    writer.write(savedPassword);
                    System.out.println("Your password has been saved in Passwords.txt!");
                    lblInfo.setVisible(true);
                    writer.close();
                } catch (IOException ex) {
                    System.out.println("Invalid path!");
                }
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