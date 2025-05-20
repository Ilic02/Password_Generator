import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.*;
import java.util.Optional;

public class Main extends Application {
    private Scene scene;
    private ThemeManager themeManager;

    public static void main(String[] args) {
        launch(args);
    }

    private void showMainUI(Stage stage) throws IOException {
        HBox root = new HBox(10);
        root.setPadding(new Insets(10, 10, 10, 10));

        VBox vb = new VBox(10);

        Label lblWebsite = new Label("Insert a website:");
        TextField tfWebsite = new TextField();
        tfWebsite.setMinWidth(250);

        Label lblLength = new Label("Insert password length:");
        TextField tfLength = new TextField();
        tfLength.setMinWidth(250);

        Button btnGenerate = new Button("Generate");
        btnGenerate.setPadding(new Insets(5, 5, 5, 5));

        Label lblRandomPass = new Label("Random password:");
        TextArea ta = new TextArea();
        ta.setEditable(false);
        ta.setMinWidth(250);
        ta.setMinHeight(100);

        Button btnCopy = new Button("Copy to Clipboard");
        btnCopy.setPadding(new Insets(5, 5, 5, 5));
        btnCopy.setDisable(true);

        Label lblInfo = new Label();
        lblInfo.setVisible(false);
        lblInfo.setTextFill(Color.GREEN);

        CheckBox cbUppercase = new CheckBox("Include Uppercase Letters (A-Z)");
        CheckBox cbLowercase = new CheckBox("Include Lowercase Letters (a-z)");
        CheckBox cbNumbers = new CheckBox("Include Numbers (0-9)");
        CheckBox cbSpecialChars = new CheckBox("Include Special Characters");

        Button btnToggleTheme = new Button("Toggle Theme");
        Button btnSearchPasswords = new Button("Search Passwords");

        HBox hbButtons = new HBox(10);
        hbButtons.setPadding(new Insets(5, 5, 5, 5));
        hbButtons.getChildren().addAll(btnToggleTheme, btnSearchPasswords);

        vb.getChildren().addAll(lblWebsite, tfWebsite, lblLength, tfLength, cbUppercase, cbLowercase, cbNumbers, cbSpecialChars, btnGenerate, lblRandomPass, ta, btnCopy, lblInfo, hbButtons);

        root.getChildren().addAll(vb);

        scene = new Scene(root, 550, 550);
        themeManager = new ThemeManager(scene);

        scene.setOnKeyPressed(event -> {
            if (event.getCode().toString().equals("ESCAPE")) {
                stage.close();
            }
        });

        btnGenerate.setOnAction(e -> {
            String website = tfWebsite.getText().trim();
            String length = tfLength.getText().trim();
            tfWebsite.clear();
            tfLength.clear();
            lblInfo.setVisible(false);
            ta.clear();
            btnCopy.setDisable(true);

            if (website.isEmpty()) {
                AlertHelper.showErrorAlert("Website name error", "Please enter a valid website name.");
                return;
            }

            int passLength;
            try {
                if (length.isEmpty()) {
                    throw new NumberFormatException("Password length is required!");
                }
                passLength = Integer.parseInt(length);
            } catch (NumberFormatException ex) {
                AlertHelper.showErrorAlert("Input error", "Please enter a valid password length.");
                return;
            }
            if (passLength < 8 || passLength > 24) {
                AlertHelper.showErrorAlert("Input Error", "Password length must be between 8 and 24 characters!");
                return;
            }

            boolean useUpper = cbUppercase.isSelected();
            boolean useLower = cbLowercase.isSelected();
            boolean useNumbers = cbNumbers.isSelected();
            boolean useSpecial = cbSpecialChars.isSelected();

            if (!useUpper && !useLower && useNumbers && !useSpecial) {
                AlertHelper.showErrorAlert("Selection Error", "Please select at least one character set");
                return;
            }

            try {
                if (FileHandler.siteExists(website)) {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Duplicate website");
                    alert.setHeaderText("Website already exists");
                    alert.setContentText("Do you want to overwrite the existing password?");

                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.isPresent() && result.get() == ButtonType.CANCEL) {
                        lblInfo.setText("Password not saved.");
                        lblInfo.setTextFill(Color.RED);
                        lblInfo.setVisible(true);
                        return;
                    }
                }

                String randomPassword = PasswordGenerator.generate(passLength, useUpper, useLower, useNumbers, useSpecial);
                if (randomPassword != null) {
                    ta.appendText(randomPassword + "\n");

                    btnCopy.setDisable(false);

                    FileHandler.savePassword(website, randomPassword);
                    AlertHelper.showInfoAlert("Success", "Your password has been saved in file Passwords.txt");
                }

            } catch (NumberFormatException ex) {
                lblInfo.setText("Please insert correct number for password length!");
                lblInfo.setTextFill(Color.RED);
            } catch (IOException ex) {
                System.out.println("Invalid path");
            }
        });

        btnCopy.setOnAction(e -> {
            ClipboardHelper.copyToClipboard(ta.getText().trim());
            AlertHelper.showInfoAlert("Clipboard", "Password copied to clipboard!");
        });

        btnToggleTheme.setOnAction(e -> themeManager.toggleTheme());

        btnSearchPasswords.setOnAction(e -> {
            try {
                new PasswordManager().start(new Stage());
            } catch (Exception ex) {
                AlertHelper.showErrorAlert("Search Error", "Failed to open Password Manager");
                ex.printStackTrace();
            }
        });

        stage.setScene(scene);
        stage.setTitle("Password Generator");
        stage.show();
        stage.centerOnScreen();
    }

    @Override
    public void start(Stage stage) {
        PinScreen.show(stage, () -> {
            try {
                showMainUI(stage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}