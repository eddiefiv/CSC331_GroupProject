/**
 * Handles all Poker Table actions and gameplay actions
 *
 * @authors Eddie Falco, James Frink
 */

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

public class Table {
    private static final int BIG_BLIND_AMOUNT = 10;
    private static final int SMALL_BLIND_AMOUNT = 5;

    private static ArrayList<Player> players = new ArrayList<Player>();

    private static int pot = 0;
    private static int currentBet = 0;

    private static ArrayList<Card> deck = new ArrayList<Card>(52);
    public static ArrayList<Card> board = new ArrayList<Card>(5);

    public static void tableInit(int numNPCs) throws IllegalArgumentException {
        if (numNPCs < 2) {
            throw new IllegalArgumentException("Cannot have less than 2 NPCs");
        }
        for (int i = 0; i < numNPCs; i++) {
            players.add(new NPC("NPC" + i, new NormalNPCStrategy())); // Add NPCs to the list of Players
        }
    }

    // GETTERS AND SETTERS
    public static ArrayList<Card> getDeck() {
        return deck;
    }

    public static int getPot() {
        return pot;
    }

    public static void setPot(int pot) {
        Table.pot = pot;
    }

    // GAMEPLAY
    public static void gameplayLoop() {
        // This loop manages the whole Table's lifespan
        int currentBigBlindIdx;
        while (true) {
            // First, generate a new deck, shuffle it, then start the game
            newDeck();
            shuffleDeck();
            startGame();
            break;
            /**
            // TODO loop through each player continuously, prompting for raises, calls, folds, etc. Break (set running to false) the loop when all players fold or one wins
            boolean running = true;
            // This loop manages a single round
            while (running) {
                for (Player player : players) {

                }
            }
             */
        }

    }

    public static void startGame() {
        for (int i = 0; i < players.size(); i++) {
            // Set small and big blind first
            if (i == 0) {
                players.getFirst().setIsSmallBlind(true); // Set the first player (player to left of dealer, who is not a player but the Table itself) to be small blind
                players.getFirst().raise(SMALL_BLIND_AMOUNT);
            } else if (i == 1) {
                players.get(1).setIsBigBlind(true); // Set the player to the left of the first player to be big blind
                players.get(1).raise(BIG_BLIND_AMOUNT);
                currentBet = BIG_BLIND_AMOUNT; // Update the current bet to be the BIG_BLIND_AMOUNT

                // After blinds are posted, deal cards
                deal();
            } else {
                players.get(i).setActiveTurn(true); // Set the third player to be the active player
                handlePlayerTurn(players.get(i)); // Handle the player's turn
            }
        }
    }

    private static void handlePlayerTurn(Player player) {
        // If player is a ControllablePlayer, prompt for input, otherwise run NPC strategy
        if (player instanceof ControllablePlayer) {
            promptPlayerForTurn(player);
        } else if (player instanceof NPC) {
            handleNPCTurn(player);
        }
    }

    private static void promptPlayerForTurn(Player player) {
        System.out.println(player.getPlayerName() + "'s hand: " + player.getHand());
        System.out.println("What move would you like to make? (fold, call, raise)");


        // TODO create visual prompts to let the ControllablePlayer pick their turn
        Scanner scanner = new Scanner(System.in);

        boolean checking = true;

        while (checking) {
            String playerMove = scanner.nextLine();

            switch (playerMove.toLowerCase()) {
                case "fold":
                    player.fold();
                    checking = false;
                    break;
                case "call":
                    player.call();
                    checking = false;
                    break;
                case "raise":
                    player.raise(currentBet);
                    checking = false;
                    break;
                default:
                    System.out.printf("%s is not a valid action, available actions are 'fold', 'call', and 'raise'.", playerMove);
            }
        }
    }

    private static void handleNPCTurn(Player player) {
        System.out.println(player.getPlayerName() + "'s hand: " + player.getHand());
    }

    public static void deal() {
        int numPlayers = players.size();

        // Deal 2 rounds
        for (int round = 0; round < 2; round++) {
            for (Player player : players) {
                Card card = deck.removeFirst(); // Remove top card
                player.addCardToHand(card);
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
