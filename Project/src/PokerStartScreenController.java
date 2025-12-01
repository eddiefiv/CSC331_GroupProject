import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class PokerStartScreenController {

    @FXML
    private TextField playerNameInput;

    @FXML
    private Button startBtn;

    @FXML
    private Button quitBtn;

    // Called when the "Start Game" button is clicked
    @FXML
    private void startGame() {
        try {
            // Load the Poker Table FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/PokerTable.fxml"));
            Scene nextScene = new Scene(loader.load());

            // Get the controller to pass the player name
            PokerController pokerController = loader.getController();
            pokerController.setPlayerName(playerNameInput.getText());

            // Open the new stage with the poker table scene
            Stage stage = new Stage();
            stage.setScene(nextScene);
            stage.setTitle("Poker Table");
            stage.show();

            // Optionally, close the start screen
            startBtn.getScene().getWindow().hide();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Called when the "Quit" button is clicked
    @FXML
    private void quitGame() {
        System.exit(0);
    }
}
