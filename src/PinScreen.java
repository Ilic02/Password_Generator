import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class PinScreen {
    public static void show(Stage primaryStage, Runnable onSuccess) {
        Label label = new Label(PinManager.isPinSet() ? "Enter your 4-digit PIN:" : "Set a 4-digit PIN:");
        PasswordField pinField = new PasswordField();
        pinField.setPromptText("****");
        Button confirmBtn = new Button("Confirm");
        Label message = new Label();
        message.setTextFill(Color.RED);

        confirmBtn.setOnAction(e -> {
            String pin = pinField.getText();
            if (!pin.matches("\\d{4}")) {
                message.setText("PIN must be exactly 4 digits");
                return;
            }

            try {
                if (!PinManager.isPinSet()) {
                    PinManager.setPin(pin);
                    message.setText("PIN set successfully");
                    onSuccess.run();
                } else {
                    if (PinManager.verifyPin(pin)) {
                        onSuccess.run();
                    } else {
                        message.setText("Invalid PIN");
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                message.setText("An error occurred while setting PIN");
            }
        });

        VBox vb = new VBox(50, label, pinField, confirmBtn, message);
        vb.setPadding(new Insets(10, 10, 10, 10));
        vb.setMinWidth(250);
        vb.setMinHeight(150);
        primaryStage.setScene((new Scene(vb)));
        primaryStage.setTitle("PIN Authentication");
        primaryStage.show();
        primaryStage.centerOnScreen();
    }
}
