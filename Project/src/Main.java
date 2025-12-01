/**
 * Runnable file that is used to initialize the demo of the poker game for presenting.
 */
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    // loads the game, initializes and sets a name for the scene
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/PokerTable.fxml"));
        Scene scene = new Scene(loader.load());
        stage.setTitle("Poker Game Demo");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
