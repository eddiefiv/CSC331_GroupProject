import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;


public class PokerController {

    //Community Cards
    @FXML
    private ImageView houseCard1;
    @FXML
    private ImageView houseCard2;
    @FXML
    private ImageView houseCard3;
    @FXML
    private ImageView houseCard4;
    @FXML
    private ImageView houseCard5;

    //Player Cards
    @FXML
    private ImageView playerCard1;
    @FXML
    private ImageView playerCard2;

    //NPC 1 Cards
    @FXML
    private ImageView npcCard1;
    @FXML
    private ImageView npcCard2;

    //NPC 2 Cards
    @FXML
    private ImageView npc2Card1;
    @FXML
    private ImageView npc2Card2;

    //NPC 3 Cards
    @FXML
    private ImageView npc3Card1;
    @FXML
    private ImageView npc3Card2;

    //Buttons
    @FXML
    private Button callButton;
    @FXML
    private Button checkButton;
    @FXML
    private Button foldButton;

    //Labels
    @FXML
    private Label potLabel;
    @FXML
    private Label playerPot;
    @FXML
    private Label npc1Pot;
    @FXML
    private Label npc2Pot;
    @FXML
    private Label npc3Pot;


    private String rankToString(CardRank rank) {
        return switch (rank) {
            case ACE -> "A";
            case TWO -> "2";
            case THREE -> "3";
            case FOUR -> "4";
            case FIVE -> "5";
            case SIX -> "6";
            case SEVEN -> "7";
            case EIGHT -> "8";
            case NINE -> "9";
            case TEN -> "10";
            case JACK -> "J";
            case QUEEN -> "Q";
            case KING -> "K";
        };
    }

    // loads cards for game
    private Image loadCard(String cardName) {
        String path = "/cards/" + cardName + ".png";
        return new Image(getClass().getResourceAsStream(path));
    }
    // displays initial cards
    public void displayInitialCards() {

    }
}