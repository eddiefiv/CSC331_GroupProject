/**
 * Main driver for the whole program, and JavaFX to show the GUI
 *
 * @author Eddie Falco, Emma Fox
 */

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class FXMLDriver extends Application {
    /**
     * Start the FXML Loader and stage, with the stage's scene
     *
     * @param stage the JavaFX stage
     * @throws Exception base exception
     */
    @Override
    public void start(Stage stage) throws Exception{
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("PokerStartScreen.fxml")
        );

        Scene scene = new Scene(loader.load());
        stage.setScene(scene);
        stage.setTitle("Poker Game");
        stage.show();
    }

    /**
     * Program entry point
     *
     * @param args String arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}
