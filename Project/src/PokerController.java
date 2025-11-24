import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;


public class PokerController {

    //Community Cards
    @FXML
    private ImageView comCard1;
    @FXML private ImageView comCard2;
    @FXML private ImageView comCard3;
    @FXML private ImageView comCard4;
    @FXML private ImageView comCard5;

    //Player Cards
    @FXML private ImageView playerCard1;
    @FXML private ImageView playerCard2;

    //NPC Cards
    @FXML private ImageView npcCard1;
    @FXML private ImageView npcCard2;

    //Buttons
    @FXML private Button dealButton;
    @FXML private Button betButton;
    @FXML private Button raiseButton;
    @FXML private Button foldButton;

    //Labels
    @FXML private Label potLabel;
    @FXML private Label playerChipLabel;
    @FXML private Label npcChipLabel;

    //Game Objects
    private Table table;
    private Player human;
    private NPC npc;

    @FXML
    private void initialize(){
        //human = new Player("Human", 1000);
        //npc = new NPC("Dealer", 1000);

        updateLabels();
        loadBackImages();
    }

    private void loadBackImages(){
        Image back = new Image("/images/back.png");
        playerCard1.setImage(back);
        playerCard2.setImage(back);
        npcCard1.setImage(back);
        npcCard2.setImage(back);

        comCard1.setImage(null);
        comCard2.setImage(null);
        comCard3.setImage(null);
        comCard4.setImage(null);
        comCard5.setImage(null);
    }

    private void updateLabels(){
        playerChipLabel.setText("Your Chips: $" + human.getChips());
        npcChipLabel.setText("NPC Chips: $" + npc.getChips());
        potLabel.setText("$" + Table.getPot());
    }

    @FXML
    private void onDeal(){

        //Player cards
        Table.gameplayLoop();
        playerCard1.setImage(new Image("/images/back.png"));
        playerCard2.setImage(new Image("/images/back.png"));

        //NPC face down
        Image back = new Image("/images/back.png");
        npcCard1.setImage(back);
        npcCard2.setImage(back);

        //Community cards blank until flop
        comCard1.setImage(null);
        comCard2.setImage(null);
        comCard3.setImage(null);
        comCard4.setImage(null);
        comCard5.setImage(null);

        updateLabels();
    }

    @FXML
    private void onBet(){
        //Table.bet(human, 50);
        updateLabels();
    }

    @FXML
    private void onRaise(){
        //Table.raise(human, 100);
        updateLabels();
    }

    @FXML
    private void onFold(){
        //Table.fold(human);
        updateLabels();
        loadBackImages();
    }
}
