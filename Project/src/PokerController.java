import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;


public class PokerController {

    // Community Cards
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

    // Player Cards
    @FXML
    private ImageView playerCard1;
    @FXML
    private ImageView playerCard2;

    // NPC 1 Cards
    @FXML
    private ImageView npcCard1;
    @FXML
    private ImageView npcCard2;

    // NPC 2 Cards
    @FXML
    private ImageView npc2Card1;
    @FXML
    private ImageView npc2Card2;

    // NPC 3 Cards
    @FXML
    private ImageView npc3Card1;
    @FXML
    private ImageView npc3Card2;

    // Buttons
    @FXML
    private Button callButton;
    @FXML
    private Button checkButton;
    @FXML
    private Button foldButton;

    // Labels
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

    // Actions
    @FXML
    public void initialize() {
        System.out.println("PokerController initialized!");

        // Create a new Player
        ControllablePlayer player = new ControllablePlayer("Eddie", 750);

        // Create a new table with 4 players (1 human + 3 NPCs)
        Table.tableInit(3); // however you start the game
        Table.joinTable(player);

        // Example: Deal first hand
        Table.gameplayLoop();

        // Now update UI with the initial card images
        updateUI(player);
    }

    @FXML
    private void updateUI(Player player) {
        // Example card image loading
        // You should replace these with your actual card image paths:
        // Example: "/cards/2H.png"
        if (!Table.board.isEmpty()) {
            playerCard1.setImage(new Image(cardToFileName(player.getHand().getFirst())));
            playerCard2.setImage(new Image(cardToFileName(player.getHand().getLast())));
        }

        // Community cards if any are available
        for (int i = 0; i < Table.board.size(); i++) {
            Image img = new Image(cardToFileName(Table.board.get(i)));
            switch (i) {
                case 0 -> houseCard1.setImage(img);
                case 1 -> houseCard2.setImage(img);
                case 2 -> houseCard3.setImage(img);
                case 3 -> houseCard4.setImage(img);
                case 4 -> houseCard5.setImage(img);
            }
        }

        // Update pot and balances
        potLabel.setText("" + Table.getPot());
        playerPot.setText("" + player.getBalance());
        npc1Pot.setText("" + Table.players.getFirst().getBalance());
        npc2Pot.setText("" + Table.players.get(1).getBalance());
        npc3Pot.setText("" + Table.players.get(2).getBalance());
    }

    private String cardToFileName(Card card) {
        String rank = switch (card.getRank()) {
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

        String suit = switch (card.getSuit()) {
            case HEART -> "H";
            case DIAMOND -> "D";
            case CLUB -> "C";
            case SPADE -> "S";
            case DEFAULT -> null;
        };

        return rank + suit;
    }

}