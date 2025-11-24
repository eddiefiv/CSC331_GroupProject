/**
 * Handles all Poker Table actions and gameplay actions
 *
 * @authors Eddie Falco, James Frink
 */

import java.util.*;

public class Table {
    // Initialize blinds
    private static final int BIG_BLIND_AMOUNT = 10;
    private static final int SMALL_BLIND_AMOUNT = 5;

    // Initialize player ArrayList
    private static ArrayList<Player> players = new ArrayList<Player>();

    // Initialize betting attributes
    private static int pot = 0;
    private static int currentBet = 0;

    // Initialize Card related atrtibutes
    private static ArrayList<Card> deck = new ArrayList<Card>(52);
    public static ArrayList<Card> board = new ArrayList<Card>(5);

    // Initialize the index position for the dealer
    private static int dealerPosition = 0;

    // Track players who have folded in current hand
    private static Set<Player> foldedPlayers = new HashSet<>();

    private static final Scanner scanner = new Scanner(System.in);

    /**
     * Initialize the Table and its values
     *
     * @param numNPCs how many NPCs to add
     * @throws IllegalArgumentException thrown if too few NPCs
     */
    public static void tableInit(int numNPCs) throws IllegalArgumentException {
        // Check to make sure there are enough NPCs added
        if (numNPCs < 2) {
            throw new IllegalArgumentException("Cannot have less than 2 NPCs");
        }

        // Add all NPCs to the Table
        for (int i = 0; i < numNPCs; i++) {
            players.add(new NPC("NPC" + i, new NormalNPCStrategy())); // Add NPCs to the list of Players
        }
    }

    // GETTERS AND SETTERS

    /**
     * Gets the Table's pot
     *
     * @return int the pot
     */
    public static int getPot() {
        return pot;
    }

    /**
     * Sets the Table's pot
     *
     * @param pot the pot to set
     */
    public static void setPot(int pot) {
        Table.pot = pot;
    }

    // GAMEPLAY

    /**
     * The main gameplay loop
     * All code that handles the gameplay actions live here, or are invoked from here
     */
    public static void gameplayLoop() {
        // Infinite loop of hands
        while (true) {
            System.out.println("=== NEW GAME STARTING ===");
            resetHandState();

            // Build & shuffle deck
            newDeck();
            shuffleDeck();

            // Post blinds
            postBlinds();

            // Deal two hole cards each (deal starting at small blind, which is dealer+1)
            deal();

            // Pre-flop betting: first to act is player after big blind
            int smallIdx = (dealerPosition + 1) % players.size();
            int bigIdx = (dealerPosition + 2) % players.size();
            int firstToActPreflop = (dealerPosition + 3) % players.size();

            bettingRound(firstToActPreflop);

            if (onlyOnePlayerLeft()) {
                handleImmediateWin();
                rotateBlinds();
                continue;
            }

            // Flop
            dealFlop();
            // evaluate() called inside dealFlop
            bettingRound(smallIdx);

            if (onlyOnePlayerLeft()) {
                handleImmediateWin();
                rotateBlinds();
                continue;
            }

            // Turn
            dealTurnCard(); // evaluate() called inside
            bettingRound(smallIdx);

            if (onlyOnePlayerLeft()) {
                handleImmediateWin();
                rotateBlinds();
                continue;
            }

            // River
            dealRiverCard(); // evaluate() called inside
            bettingRound(smallIdx);

            // Showdown
            List<Player> winners = determineWinners();
            awardPot(winners);

            // Rotate dealer button (blinds rotate)
            rotateBlinds();
        }
    }

    /**
     * Handles the betting flow each round
     * @param startingIndex which player to start from
     */
    private static void bettingRound(int startingIndex) {
        // Build set of active players who still need to act
        Set<Player> playersToAct = new LinkedHashSet<>();
        for (Player p : players) {
            if (!foldedPlayers.contains(p)) {
                playersToAct.add(p);
            }
        }

        int idx = startingIndex;

        // While there are Players to play still
        while (!playersToAct.isEmpty()) {
            Player p = players.get(idx);

            // If the player is not folded and is able to act
            if (!foldedPlayers.contains(p) && playersToAct.contains(p)) {
                int prevBet = p.getBet();

                // Handle this player's turn
                handlePlayerTurn(p);

                // Deduct contributed chips
                int contributed = p.getBet() - prevBet;
                if (contributed > 0) {
                    p.setBalance(p.getBalance() - contributed);
                }

                // Folded: remove from active set
                if (foldedPlayers.contains(p)) {
                    playersToAct.remove(p);
                }
                // Raise: reset the list of players to act (all others must match the new currentBet)
                else if (p.getBet() > currentBet) {
                    currentBet = p.getBet();
                    playersToAct.clear();
                    for (Player pl : players) {
                        if (!foldedPlayers.contains(pl) && pl != p) {
                            playersToAct.add(pl);
                        }
                    }
                }
                // Call or check: player has acted, remove from set
                else {
                    playersToAct.remove(p);
                }
            }

            idx = (idx + 1) % players.size();
        }
    }

    /**
     * Handles a Player's turn, and determines whether to invoke a ControllablePlayer prompt or inference an NPC action
     *
     * @param player the Player to check
     */
    private static void handlePlayerTurn(Player player) {
        if (foldedPlayers.contains(player)) return;

        if (player instanceof ControllablePlayer) {
            promptPlayerForTurn(player);
        } else if (player instanceof NPC) {
            handleNPCTurn((NPC) player);
        } else {
            // default: treat as NPC with very simple logic (call)
            int toCall = currentBet - player.getBet();
            if (toCall > 0) {
                player.call(toCall);
            }
        }
    }

    /**
     * Prompts the ControllablePlayer (user) for what action they want to take
     *
     * @param player the ControllablePlayer to prompt for
     */
    private static void promptPlayerForTurn(Player player) {
        // Print out all information
        System.out.println("\n=== " + player.getPlayerName() + "'s Turn ===");
        System.out.println("Your cards: " + player.getHand());
        System.out.println("Current best hand: " + player.getHandEvaluation().getHandType() + ", Cards: " + player.getHandEvaluation().getCards());
        System.out.println("Pot: $" + pot);
        System.out.println("Your bet: $" + player.getBet());
        System.out.println("Current bet to match: $" + currentBet);

        boolean deciding = true;

        while (deciding) {
            // Can check only if player's bet matches currentBet
            boolean canCheck = (player.getBet() == currentBet);

            // Show action prompt
            System.out.print("Choose action (");
            if (canCheck) System.out.print("check, ");
            if (currentBet > player.getBet()) System.out.print("call, ");
            System.out.print("raise, fold): ");

            String move = scanner.nextLine().trim().toLowerCase();

            // Follow what action the Player has decided
            switch (move) {
                case "check":
                    if (!canCheck) {
                        System.out.println("You cannot check. You must call or fold.");
                        break;
                    }
                    System.out.println(player.getPlayerName() + " checks.");
                    deciding = false;
                    break;

                case "call":
                    int toCall = currentBet - player.getBet();
                    if (toCall <= 0) {
                        System.out.println("Nothing to call; you can check instead.");
                        break;
                    }
                    player.call(toCall);
                    player.setBalance(player.getBalance() - toCall);
                    System.out.println(player.getPlayerName() + " calls $" + toCall);
                    deciding = false;
                    break;

                case "raise":
                    System.out.print("Enter raise amount: ");
                    int raiseAmount;
                    try {
                        raiseAmount = Integer.parseInt(scanner.nextLine());
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid number.");
                        break;
                    }

                    if (raiseAmount <= 0) {
                        System.out.println("Raise must be positive.");
                        break;
                    }

                    // Update global currentBet
                    int totalRaise = (currentBet - player.getBet()) + raiseAmount;
                    currentBet = player.getBet() + totalRaise;

                    player.raise(totalRaise);
                    player.setBalance(player.getBalance() - totalRaise);

                    System.out.println(player.getPlayerName() + " raises to $" + currentBet);
                    deciding = false;
                    break;

                case "fold":
                    player.fold();
                    foldedPlayers.add(player);
                    System.out.println(player.getPlayerName() + " folds.");
                    deciding = false;
                    break;

                default:
                    System.out.println("Invalid action.");
            }
        }
    }

    /**
     * Handle the NPCs inferencing
     *
     * @param npc the NPC to inference
     */
    private static void handleNPCTurn(NPC npc) {
        // Build npcState (fields per your earlier snippet)
        NPCState npcState = new NPCState();
        npcState.callAmount = currentBet - npc.getBet();
        npcState.canCheck = currentBet == npc.getBet();
        npcState.potSize = getPot();
        npcState.currentBet = npc.getBet();
        npcState.handStrength = HandEvaluator.evaluateHand(npc.getHand(), board);
        npcState.chips = npc.getChips();
        npcState.playersRemaining = countActivePlayers();

        System.out.println(npc.getPlayerName() + " thinking...");

        // Inference the NPC
        PlayerAction action = npc.getStrategy().inference(npcState);

        // Follow the action determined by the NPCStrategy
        switch (action) {
            case FOLD:
                npc.fold();
                foldedPlayers.add(npc);
                System.out.printf("%s folded.%n", npc.getPlayerName());
                break;
            case CALL:
                int toCall = npcState.callAmount;
                if (toCall > 0) {
                    // guard against calling more than balance
                    if (toCall > npc.getBalance()) {
                        // no all-in: fold if cannot call (or you could implement partial call)
                        npc.fold();
                        foldedPlayers.add(npc);
                        System.out.printf("%s couldn't call and folded.%n", npc.getPlayerName());
                    } else {
                        npc.call(toCall);
                        System.out.printf("%s called $%d.%n", npc.getPlayerName(), toCall);
                    }
                } else {
                    // nothing to call -> check
                    System.out.printf("%s checked.%n", npc.getPlayerName());
                }
                break;
            case RAISE:
                // Simple: Raise by some fraction of chip stack, NPCStrategy can set how much, but fallback to small raise
                int raiseAmount = Math.max(1, (int) (npc.getBalance() * 0.05));
                int newTarget = currentBet + raiseAmount;
                int totalRaiseCost = newTarget - npc.getBet();
                if (totalRaiseCost > npc.getBalance()) {
                    // cannot raise: try call instead
                    int toCallDefault = currentBet - npc.getBet();
                    if (toCallDefault > 0 && toCallDefault <= npc.getBalance()) {
                        npc.call(toCallDefault);
                        System.out.printf("%s called $%d.%n", npc.getPlayerName(), toCallDefault);
                    } else {
                        npc.fold();
                        foldedPlayers.add(npc);
                        System.out.printf("%s folded.%n", npc.getPlayerName());
                    }
                } else {
                    currentBet = newTarget;
                    npc.raise(totalRaiseCost);
                    System.out.printf("%s raised $%d.%n", npc.getPlayerName(), raiseAmount);
                }
                break;
            default:
                // fallback: call if needed
                int toCallDefault = currentBet - npc.getBet();
                if (toCallDefault > 0) {
                    if (toCallDefault > npc.getBalance()) {
                        npc.fold();
                        foldedPlayers.add(npc);
                    } else {
                        npc.call(toCallDefault);
                    }
                }
        }
    }

    /**
     * Deal the cards to each Player
     */
    public static void deal() {
        int numPlayers = players.size();

        // deal starting at small blind (dealer + 1)
        for (int round = 0; round < 2; round++) {
            for (int i = 0; i < numPlayers; i++) {
                int idx = (dealerPosition + 1 + i) % numPlayers;
                Card card = deck.removeFirst();
                players.get(idx).addCardToHand(card);
            }
            evaluate(); // update after each hole card
        }
    }

    /**
     * Deal the flop
     */
    public static void dealFlop() {
        burnCard();
        board.add(deck.removeFirst());
        board.add(deck.removeFirst());
        board.add(deck.removeFirst());
        evaluate(); // immediate update
        System.out.println("Flop: " + board);
    }

    /**
     * Deal the turn
     */
    private static void dealTurnCard() {
        burnCard();
        board.add(deck.removeFirst());
        evaluate();
        System.out.println("Turn: " + board.getLast());
    }

    /**
     * Deal the river
     */
    private static void dealRiverCard() {
        burnCard();
        board.add(deck.removeFirst());
        evaluate();
        System.out.println("River: " + board.getLast());
    }

    /**
     * Burn a card from the top of the deck
     */
    private static void burnCard() {
        if (!deck.isEmpty()) deck.removeFirst();
    }

    // Blinds and rotation

    /**
     * Post the blinds and adjust the Table bets
     */
    private static void postBlinds() {
        foldedPlayers.clear();

        int smallIdx = (dealerPosition + 1) % players.size();
        int bigIdx = (dealerPosition + 2) % players.size();

        Player small = players.get(smallIdx);
        Player big = players.get(bigIdx);

        small.setIsSmallBlind(true);
        big.setIsBigBlind(true);

        // Post blinds via Player.raise (which updates Table.pot and player.bet)
        small.raise(SMALL_BLIND_AMOUNT);
        // Remove posted blind from their balance immediately
        small.setBalance(small.getBalance() - SMALL_BLIND_AMOUNT);

        big.raise(BIG_BLIND_AMOUNT);
        big.setBalance(big.getBalance() - BIG_BLIND_AMOUNT);

        // Important: currentBet must start at big blind so players cannot check without matching it
        currentBet = BIG_BLIND_AMOUNT;

        System.out.printf("%s posted small blind $%d, %s posted big blind $%d%n",
                small.getPlayerName(), SMALL_BLIND_AMOUNT, big.getPlayerName(), BIG_BLIND_AMOUNT);
    }

    /**
     * Rotate the blinds to the preceding Players
     */
    private static void rotateBlinds() {
        dealerPosition = (dealerPosition + 1) % players.size();
    }

    /**
     * Calculates how many active (non-folded) players there currently are
     *
     * @return int how many active (non-folded) players
     */
    private static int countActivePlayers() {
        int count = 0;
        for (Player p : players) {
            if (!foldedPlayers.contains(p)) count++;
        }
        return count;
    }

    /**
     * Is there one player remaining
     *
     * @return boolean true if there is one player remaining
     */
    private static boolean onlyOnePlayerLeft() {
        return countActivePlayers() == 1;
    }

    /**
     * Handles an immediate win
     */
    private static void handleImmediateWin() {
        for (Player p : players) {
            if (!foldedPlayers.contains(p)) {
                // the remaining player already contributed to pot; simply award full pot
                p.setBalance(p.getBalance() + pot);
                System.out.printf("%s wins the pot of $%d (everyone else folded).%n", p.getPlayerName(), pot);
                pot = 0;
                break;
            }
        }
    }

    /**
     * Determine winners: returns list of winners (split pot among them). Comparison is done by HandType only.
     *
     * @return List list of Players that won
     */
    private static List<Player> determineWinners() {
        List<Player> contenders = new ArrayList<>();
        for (Player p : players) {
            if (!foldedPlayers.contains(p)) {
                contenders.add(p);
            }
        }

        if (contenders.isEmpty()) return new ArrayList<>();

        // Evaluate all contenders (should already be evaluated)
        Player best = contenders.get(0);
        HandEvaluationResult bestEval = best.getHandEvaluation();
        List<Player> winners = new ArrayList<>();
        winners.add(best);

        for (int i = 1; i < contenders.size(); i++) {
            Player cur = contenders.get(i);
            HandEvaluationResult curEval = cur.getHandEvaluation();

            if (curEval == null && bestEval == null) {
                winners.add(cur); // both default -> tie
            } else if (curEval == null) {
                // best is non-null and better
                continue;
            } else if (bestEval == null) {
                // cur is better
                best = cur;
                bestEval = curEval;
                winners.clear();
                winners.add(cur);
            } else {
                // compare by HandType ordinal
                int cmp = Integer.compare(curEval.getHandType().ordinal(), bestEval.getHandType().ordinal());
                if (cmp > 0) {
                    best = cur;
                    bestEval = curEval;
                    winners.clear();
                    winners.add(cur);
                } else if (cmp == 0) {
                    // tie on hand type -> include both (split pot)
                    winners.add(cur);
                }
            }
        }

        return winners;
    }

    /**
     * Award the pot to a winner or winners
     *
     * @param winners the winners that won the round and whom to distribute the pot to
     */
    private static void awardPot(List<Player> winners) {
        if (winners == null || winners.isEmpty()) {
            System.out.println("No winners found; clearing pot.");
            pot = 0;
            return;
        }

        int share = pot / winners.size();
        int remainder = pot % winners.size();

        for (int i = 0; i < winners.size(); i++) {
            Player w = winners.get(i);
            int awarding = share + (i == 0 ? remainder : 0); // give any remainder to first winner
            w.setBalance(w.getBalance() + awarding);
            System.out.printf("%s wins $%d%n", w.getPlayerName(), awarding);
        }

        pot = 0;
    }

    // Helper: reset state between hands
    private static void resetHandState() {
        board.clear();
        deck.clear();
        foldedPlayers.clear();
        pot = 0;
        currentBet = 0;

        // reset players
        for (Player p : players) {
            p.setBet(0);
            p.getHand().clear();
            p.setIsSmallBlind(false);
            p.setIsBigBlind(false);
            p.setHandEvaluation(null);
            p.setActiveTurn(false);
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

    /**
     * Add a Player to the Table
     *
     * @param playerToJoin the Player to add
     */
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
