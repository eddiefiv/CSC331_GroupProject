import java.util.ArrayList;

public class Table {
    private ArrayList<Player> players = new ArrayList<Player>();
    private int pot;
    private ArrayList<Card> deck =  new ArrayList<Card>(52);

    public Table(int numNPCs) {
        for (int i = 0; i < numNPCs; i++) {
            players.add(new NPC()); // Add NPCs to the list of Players
        }
    }

    // GETTERS AND SETTERS
    public ArrayList<Card> getDeck() {
        return deck;
    }

    // GAMEPLAY
    public void gameplayLoop() {

    }

    public void deal() {
        for (Player player : players) {
            for (Card card : deck) {
                for (int i = 0; i < 2; i++) { // Deal 2 cards to each player in the game using the deck
                    player.addCardToHand(card);
                    deck.remove(card);
                }
            }
        }
    }

    public void evaluate() {

    }

    public void joinTable(Player playerToJoin) {
        players.add(playerToJoin);
    }

    /**
     * Creates whole new deck. Does not shuffle
     */
    public void newDeck() {
        int index = 0;

        for (int i = 1; i < 5; i++) { // Suits
            for (int j = 1; j < 14; j++) { // 1-Ace
                CardRank rank = CardRank.getRankFromValue(j);
                CardSuit suit = null;

                switch (i) {
                    case 1:
                        suit =  CardSuit.HEART;
                        break;
                    case 2:
                        suit =  CardSuit.DIAMOND;
                        break;
                    case 3:
                        suit =  CardSuit.CLUB;
                        break;
                    case 4:
                        suit =  CardSuit.SPADE;
                        break;
                }

                Card card = new Card(suit, rank);
                deck.add(card);
            }
        }
    }

    /**
     * Shuffles the current deck. Does not create a new deck
     */
    public void shuffleDeck() {
        ArrayList<Card> shuffled = new ArrayList<Card>(52);
        while (!deck.isEmpty()) {
            int idx = (int)(Math.random() * deck.size());
            shuffled.add(deck.remove(idx));
        }

        deck = shuffled;
    }
}
