import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class PokerStartScreenController {

    // Buttons
    @FXML
    private Button startBtn;
    @FXML
    private Button quitBtn;

    // Labels
    @FXML
    private Label nameInput;

    // Actions
    @FXML
    private void startGame() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("PokerTable.fxml"));
            Scene nextScene = new Scene(loader.load());

            // Get the current stage from ANY node in the scene:
            Stage stage = (Stage) startBtn.getScene().getWindow();
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