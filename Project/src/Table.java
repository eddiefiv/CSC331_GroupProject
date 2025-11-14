import java.util.ArrayList;

public class Table {
    private static ArrayList<Player> players = new ArrayList<Player>();
    private static int pot;
    private static ArrayList<Card> deck =  new ArrayList<Card>(52);
    public static ArrayList<Card> board = new ArrayList<Card>(5);

    public Table(int numNPCs) {
        for (int i = 0; i < numNPCs; i++) {
            players.add(new NPC()); // Add NPCs to the list of Players
        }
    }

    // GETTERS AND SETTERS
    public static ArrayList<Card> getDeck() {
        return deck;
    }

    // GAMEPLAY
    public static void gameplayLoop() {

    }

    public static void deal() {
        for (Player player : players) {
            for (Card card : deck) {
                for (int i = 0; i < 2; i++) { // Deal 2 cards to each player in the game using the deck
                    player.addCardToHand(card);
                    deck.remove(card);
                }
            }
        }
    }

    public static void evaluate() {

    }

    public static void joinTable(Player playerToJoin) {
        players.add(playerToJoin);
    }

    /**
     * Creates whole new deck. Does not shuffle
     */
    public static void newDeck() {
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
    public static void shuffleDeck() {
        ArrayList<Card> shuffled = new ArrayList<Card>(52);
        while (!deck.isEmpty()) {
            int idx = (int)(Math.random() * deck.size());
            shuffled.add(deck.remove(idx));
        }

        deck = shuffled;
    }
}
