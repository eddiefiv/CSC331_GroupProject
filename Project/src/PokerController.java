import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.concurrent.CompletableFuture;


public class PokerController {

    // Private attributes
    private String playerName;

    // Inputs
    @FXML
    public TextField raiseAmountField;

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
    @FXML
    private Button raiseButton;

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
        ControllablePlayer player = new ControllablePlayer(playerName, 750);

        // Create a new table with 4 players (1 human + 3 NPCs)
        Table.tableInit(3, this);
        Table.joinTable(player);

        // Start gameplay loop
        Table.gameplayLoop();
    }

    public void updateLabelsUI() {
        // DISCLAIMER ---- THIS CODE IS VERY UNCLEAN AND NOT NEAT,
        // IT WAS IN A RUSH TO GET STUFF WORKING SINCE GROUP MEMBERS (not Brayden and Eddie) DON'T HELP :(

        // Update ControllablePlayer UI
        if (!Table.players.getLast().getHand().isEmpty()) { // Get ControllablePlayer, only update if Player has a hand
            playerCard1.setImage(new Image(cardToFileName(Table.players.getLast().getHand().getFirst())));
            playerCard2.setImage(new Image(cardToFileName(Table.players.getLast().getHand().getLast())));
        } else { // If Player has no hand, remove card images if any
            playerCard1.setImage(null);
            playerCard2.setImage(null);
        }

        // Update NPCs UI
        // NPC 1
        if (!Table.players.getFirst().getHand().isEmpty()) {
            npcCard1.setImage(new Image(cardToFileName(Table.players.getFirst().getHand().getFirst())));
            npcCard2.setImage(new Image(cardToFileName(Table.players.getFirst().getHand().getLast())));
        }
        // NPC 2
        if (!Table.players.get(1).getHand().isEmpty()) {
            npc2Card1.setImage(new Image(cardToFileName(Table.players.get(1).getHand().getFirst())));
            npc2Card2.setImage(new Image(cardToFileName(Table.players.get(1).getHand().getLast())));
        }
        // NPC 3
        if (!Table.players.get(2).getHand().isEmpty()) {
            npc3Card1.setImage(new Image(cardToFileName(Table.players.get(2).getHand().getFirst())));
            npc3Card2.setImage(new Image(cardToFileName(Table.players.get(2).getHand().getLast())));
        }

        // Update Player balances and Table pot UI
        potLabel.setText("" + Table.getPot());
        playerPot.setText("" + Table.players.getLast().getBalance());
        npc1Pot.setText("" + Table.players.getFirst().getBalance());
        npc2Pot.setText("" + Table.players.get(1).getBalance());
        npc3Pot.setText("" + Table.players.get(2).getBalance());

        // Community cards if any are available
        if (!Table.board.isEmpty()) {
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
        }
    }

    public CompletableFuture<PlayerAction> promptPlayerAction(boolean isCheckAllowed, boolean isCallAllowed) {
        CompletableFuture<PlayerAction> futureAction = new CompletableFuture<>();

        // If check is allowed
        if (isCheckAllowed) {
            checkButton.setDisable(false);
            checkButton.setOnAction(event -> {
                futureAction.complete(PlayerAction.CHECK);
            });
        }

        // If call is allowed
        if (isCallAllowed) {
            callButton.setDisable(false);
            callButton.setOnAction(event -> {
                futureAction.complete(PlayerAction.CALL);
            });
        }

        // Enable buttons
        foldButton.setDisable(false);
        raiseButton.setDisable(false);
        raiseAmountField.setDisable(false);

        // Set future
        callButton.setOnAction(event -> {
            futureAction.complete(PlayerAction.CALL);
        });
        foldButton.setOnAction(event -> {
            futureAction.complete(PlayerAction.FOLD);
        });
        raiseButton.setOnAction(event -> {
            futureAction.complete(PlayerAction.RAISE);
        });

        return futureAction;
    }

    public void disablePlayerActionButtons() {
        callButton.setDisable(true);
        checkButton.setDisable(true);
        foldButton.setDisable(true);
        raiseButton.setDisable(true);
        raiseAmountField.setDisable(true);
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
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

        return String.format("../../cards/%s%s", rank, suit);
    }
}