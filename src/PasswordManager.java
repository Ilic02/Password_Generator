import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.Optional;

public class PasswordManager extends Application {
    private TextField tfSearch;
    private TextArea taResuluts;
    private Button btnDelete;
    private PasswordStorage passwordStorage;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        passwordStorage = new PasswordStorage();

        VBox root = new VBox(10);
        root.setPadding(new Insets(10));

        Label lblSearch = new Label("Search website");
        tfSearch = new TextField();
        Button btnSearch = new Button("Search");
        btnDelete = new Button("Delete selected");
        btnDelete.setDisable(true);
        taResuluts = new TextArea();
        taResuluts.setEditable(false);

        btnSearch.setOnAction(e -> searchPassword());
        btnDelete.setOnAction(e -> deletePassword());

        root.getChildren().addAll(lblSearch, tfSearch, btnSearch, taResuluts, btnDelete);

        Scene scene = new Scene(root, 400, 300);
        stage.setScene(scene);
        stage.setTitle("Password Manager");
        stage.show();
    }

    private void searchPassword() {
        String website = tfSearch.getText().trim();
        if (website.isEmpty()) {
            AlertHelper.showErrorAlert("ERROR", "Please enter a website name");
            return;
        }

        String result = passwordStorage.getPassword(website);
        if (result == null) {
            taResuluts.setText("No password found for " + website);
            btnDelete.setDisable(true);
        } else {
            taResuluts.setText(result);
            btnDelete.setDisable(false);
        }
    }

    private void deletePassword() {
        String website = tfSearch.getText().trim();
        if (website.isEmpty()) {
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Are you sure you want to delete the password for " + website + "?");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            boolean success = passwordStorage.deletePassword(website);

            if (success) {
                AlertHelper.showInfoAlert("Success", "Passwrod deleted successfully");
                taResuluts.clear();
                btnDelete.setDisable(true);
            } else {
                AlertHelper.showErrorAlert("ERROR", "Could not delete password.");
            }
        }
    }
}
