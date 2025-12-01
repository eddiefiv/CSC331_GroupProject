import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.util.ArrayList;

import java.util.concurrent.CompletableFuture;

public class PokerController {

    private String playerName;
    private boolean showHands = false;

    public void setShowHands(boolean show) {
        this.showHands = show;
    }

    // Inputs
    @FXML public TextField raiseAmountField;

    // Community Cards
    @FXML private ImageView houseCard1, houseCard2, houseCard3, houseCard4, houseCard5;

    // Player Cards
    @FXML private ImageView playerCard1, playerCard2;

    // NPC Cards
    @FXML private ImageView npc1Card1, npc1Card2, npc2Card1, npc2Card2, npc3Card1, npc3Card2;

    // Buttons
    @FXML private Button callButton, checkButton, foldButton, raiseButton;

    // Labels
    @FXML private Label potLabel, playerPot, npcPot1, npcPot2, npcPot3;

    @FXML
    public void initialize() {
        System.out.println("PokerController initialized!");

        // Create controllable player with initial balance
        ControllablePlayer player = new ControllablePlayer(playerName);

        Table.tableInit(3, this); // initialize table with 3 NPCs
        Table.joinTable(player);  // join controllable player

        // Start the game loop in background thread
        new Thread(Table::gameplayLoop).start();
    }

    // Update all UI labels and card images
    public void updateLabelsUI() {
        Platform.runLater(() -> {
            // Player cards — always visible
            if (!Table.players.getLast().getHand().isEmpty()) {
                setCardImage(playerCard1, Table.players.getLast().getHand().get(0));
                setCardImage(playerCard2, Table.players.getLast().getHand().get(1));
            } else {
                setCardImage(playerCard1, null);
                setCardImage(playerCard2, null);
            }

            // NPCs — only visible if showHands == true
            updateNPCCards(npc1Card1, npc1Card2, 0);
            updateNPCCards(npc2Card1, npc2Card2, 1);
            updateNPCCards(npc3Card1, npc3Card2, 2);

            // Community cards — clear empty slots
            ImageView[] houseCards = {houseCard1, houseCard2, houseCard3, houseCard4, houseCard5};
            for (int i = 0; i < houseCards.length; i++) {
                if (i < Table.board.size()) {
                    setCardImage(houseCards[i], Table.board.get(i));
                } else {
                    setCardImage(houseCards[i], null);
                }
            }

            // Update balances and pot
            potLabel.setText("" + Table.getPot());
            playerPot.setText("" + Table.players.getLast().getBalance());
            npcPot1.setText("" + Table.players.getFirst().getBalance());
            npcPot2.setText("" + Table.players.get(1).getBalance());
            npcPot3.setText("" + Table.players.get(2).getBalance());
        });
    }

    /**
     * Helper method to show or hide NPC hands
     */
    private void updateNPCCards(ImageView card1, ImageView card2, int playerIndex) {
        if (showHands) {
            ArrayList<Card> hand = Table.players.get(playerIndex).getHand();
            if (!hand.isEmpty()) {
                setCardImage(card1, hand.get(0));
                setCardImage(card2, hand.get(1));
            } else {
                setCardImage(card1, null);
                setCardImage(card2, null);
            }
        } else {
            // hide NPC cards
            setCardImage(card1, null);
            setCardImage(card2, null);
        }
    }

    /**
     * Asks player for action
     */
    public CompletableFuture<PlayerAction> promptPlayerAction(boolean isCheckAllowed, boolean isCallAllowed) {
        CompletableFuture<PlayerAction> futureAction = new CompletableFuture<>();

        Platform.runLater(() -> {
            checkButton.setDisable(!isCheckAllowed);
            callButton.setDisable(!isCallAllowed);
            foldButton.setDisable(false);
            raiseButton.setDisable(false);
            raiseAmountField.setDisable(false);

            checkButton.setOnAction(event -> futureAction.complete(PlayerAction.CHECK));
            callButton.setOnAction(event -> futureAction.complete(PlayerAction.CALL));
            foldButton.setOnAction(event -> futureAction.complete(PlayerAction.FOLD));
            raiseButton.setOnAction(event -> futureAction.complete(PlayerAction.RAISE));
        });

        return futureAction;
    }

    /**
     * Method for disabling player actions when not their turn
     */
    public void disablePlayerActionButtons() {
        Platform.runLater(() -> {
            checkButton.setDisable(true);
            callButton.setDisable(true);
            foldButton.setDisable(true);
            raiseButton.setDisable(true);
            raiseAmountField.setDisable(true);
        });
    }

    /**
     * Method that sets player name, scrapped option from start screen to select a name, this was made as a placeholder but ended up sticking around
     */
    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    /**
     * Helper to set the image of the card
     */
    private void setCardImage(ImageView view, Card card) {
        if (view == null) return;
        if (card == null) view.setImage(null);
        else view.setImage(new Image(getClass().getResourceAsStream(cardToFileName(card))));
    }

    /**
     * Coverts cards to file name for display
     */
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
            default -> throw new IllegalArgumentException("Unknown rank: " + card.getRank());
        };

        String suit = switch (card.getSuit()) {
            case HEART -> "H";
            case DIAMOND -> "D";
            case CLUB -> "C";
            case SPADE -> "S";
            default -> throw new IllegalArgumentException("Unknown suit: " + card.getSuit());
        };

        return "/cards/" + rank + suit + ".png";
    }

}
