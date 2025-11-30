import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputControl;
import javafx.stage.Stage;

public class PokerStartScreenController {

    // Inputs
    @FXML
    private TextInputControl playerNameInput;

    // Buttons
    @FXML
    private Button startBtn;
    @FXML
    private Button quitBtn;

    // Labels
    @FXML
    private Label nameInput;

    // Actions/Listeners
    @FXML
    private void startGame() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("PokerTable.fxml"));
            Scene nextScene = new Scene(loader.load());

            // Get PokerController to be able to set playerName
            PokerController pokerController = loader.getController();
            pokerController.setPlayerName(playerNameInput.getText());

            // Set and show the new stage and scene
            Stage stage = new Stage();
            stage.setScene(nextScene);
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void quitGame() {
        System.exit(0);
    }
}