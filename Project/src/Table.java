import java.util.ArrayList;

public class Table {
    private final int BIG_BLIND_AMOUNT = 10;
    private final int SMALL_BLIND_AMOUNT = 5;

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
        boolean running = true;

        while (running) {
            // TODO loop through each player continuously, prompting for raises, calls, folds, etc. Break (set running to false) the loop when all players fold or one wins
        }

    }

    private static void startGame() {
        // Set small and big blind first
        players.get(0).setIsSmallBlind(true); // Set the first player (player to left of dealer, who is not a player but the Table itself) to be small blind
        players.get(0).raise()
        players.get(1).setIsBigBlind(true); // Set the player to the left of the first player to be big blind

        players.get(2).setActiveTurn(true); // Set the third player to be the active player

        // TODO initial setup
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

    /**
     * To be run every time a new card is added to the community card hand, or the player receives their hand
     */
    public static void evaluate() {
        for (Player player : players) {
            player.setHandEvaluation(HandEvaluator.evaluateHand(player.getHand(), Table.board));
        }
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
                CardSuit suit = switch (i) {
                    case 1 -> CardSuit.HEART;
                    case 2 -> CardSuit.DIAMOND;
                    case 3 -> CardSuit.CLUB;
                    case 4 -> CardSuit.SPADE;
                    default -> null;
                };

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
