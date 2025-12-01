import javafx.application.Platform;
import java.util.concurrent.CompletableFuture;
import java.util.*;
/**
 * Table class manages all poker gameplay logic:
 * - Players (both controllable and NPCs)
 * - Deck, pot, blinds, betting rounds
 * - Community cards and hand evaluation
 */
public class Table {
    private static final int BIG_BLIND_AMOUNT = 10;
    private static final int SMALL_BLIND_AMOUNT = 5;

    public static ArrayList<Player> players = new ArrayList<>();
    private static int pot = 0;
    private static int currentBet = 0;
    private static ArrayList<Card> deck = new ArrayList<>();
    public static ArrayList<Card> board = new ArrayList<>(5);
    private static int dealerPosition = 0;
    private static Set<Player> foldedPlayers = new HashSet<>();
    private static PokerController pokerController;

    /**
     * Initializes the table with NPCs and sets controller
     * @param numNPCs number of NPCs to add
     * @param controller PokerController to handle UI updates
     */
    public static void tableInit(int numNPCs, PokerController controller) {
        if (numNPCs < 2) throw new IllegalArgumentException("Cannot have less than 2 NPCs");

        for (int i = 0; i < numNPCs; i++) {
            players.add(new NPC("NPC" + i, new NormalNPCStrategy()));
        }

        pokerController = controller;
    }

    // Add a player to the table
    public static void joinTable(Player playerToJoin) {
        players.add(playerToJoin);
    }

    // Get current pot
    public static int getPot() {
        return pot;
    }

    // Set pot value
    public static void setPot(int value) {
        pot = value;
    }

    /**
     * Main gameplay loop (runs indefinitely)
     * Handles:
     * - New hands
     * - Deck creation & shuffling
     * - Posting blinds
     * - Dealing cards
     * - Betting rounds
     * - Reveal NPC hands for 5 seconds
     * - Determine winners and award pot
     * - Rotate dealer/blinds
     */
    public static void gameplayLoop() {
        while (true) {
            System.out.println("=== NEW HAND ===");

            resetHandState(); // clear board, pot, hands, etc.
            updateUI();

            newDeck();
            shuffleDeck();

            postBlinds(); // Assign small and big blinds
            deal(); // Deal hole cards
            updateUI();

            // Determine betting order
            int smallIdx = (dealerPosition + 1) % players.size();
            int bigIdx = (dealerPosition + 2) % players.size();
            int firstToActPreflop = (dealerPosition + 3) % players.size();

            runBettingRound(firstToActPreflop); // Pre-flop betting

            if (onlyOnePlayerLeft()) { handleImmediateWin(); rotateBlinds(); continue; }

            dealFlop(); // Deal 3 community cards
            runBettingRound(smallIdx); // Flop betting
            if (onlyOnePlayerLeft()) { handleImmediateWin(); rotateBlinds(); continue; }

            dealTurnCard(); // Deal turn card
            runBettingRound(smallIdx);
            if (onlyOnePlayerLeft()) { handleImmediateWin(); rotateBlinds(); continue; }

            dealRiverCard(); // Deal river card
            runBettingRound(smallIdx);

            // Reveal NPC hands for 5 seconds
            if (pokerController != null) {
                pokerController.setShowHands(true);
                updateUI();
                try { Thread.sleep(5000); } catch (InterruptedException e) { e.printStackTrace(); }
                pokerController.setShowHands(false);
            }

            // Determine winners and award pot
            List<Player> winners = determineWinners();
            awardPot(winners);

            rotateBlinds(); // Move dealer position
        }
    }

    /**
     * Executes a betting round starting from a given player index
     * Handles folding, calling, and raising logic
     */
    private static void runBettingRound(int startingIndex) {
        Set<Player> playersToAct = new LinkedHashSet<>();
        for (Player p : players) if (!foldedPlayers.contains(p)) playersToAct.add(p);

        int idx = startingIndex;
        while (!playersToAct.isEmpty()) {
            Player p = players.get(idx);
            if (!foldedPlayers.contains(p) && playersToAct.contains(p)) {
                handlePlayerTurn(p);

                updateUI();

                if (foldedPlayers.contains(p)) {
                    playersToAct.remove(p);
                } else if (p.getBet() > currentBet) {
                    currentBet = p.getBet();
                    playersToAct.clear();
                    for (Player pl : players) if (!foldedPlayers.contains(pl) && pl != p) playersToAct.add(pl);
                } else {
                    playersToAct.remove(p);
                }
            }
            idx = (idx + 1) % players.size();
        }
    }
    /** Handles a player's turn (human or NPC) */
    private static void handlePlayerTurn(Player player) {
        if (foldedPlayers.contains(player)) return;

        if (player instanceof ControllablePlayer) {
            promptPlayerTurn((ControllablePlayer) player);
        } else if (player instanceof NPC) {
            handleNPCTurn((NPC) player);
        } else {
            int toCall = currentBet - player.getBet();
            if (toCall > 0) player.call(toCall);
        }
    }
    /**
     * Handles a controllable player's turn using CompletableFuture
     * Blocks the gameplay loop until the player chooses an action
     */
    private static void promptPlayerTurn(ControllablePlayer player) {
        boolean canCheck = (player.getBet() == currentBet);
        boolean canCall = (currentBet > player.getBet());

        CompletableFuture<PlayerAction> future = pokerController.promptPlayerAction(canCheck, canCall);
        PlayerAction action = future.join(); // block background thread

        switch (action) {
            case CHECK -> { /* do nothing */ }
            case CALL -> {
                int toCall = currentBet - player.getBet();
                player.call(toCall);
                player.setBalance(player.getBalance() - toCall);
            }
            case RAISE -> {
                int raiseAmount;
                try { raiseAmount = Integer.parseInt(pokerController.raiseAmountField.getText()); }
                catch (NumberFormatException e) { raiseAmount = 0; }

                if (raiseAmount > 0) {
                    int totalRaise = (currentBet - player.getBet()) + raiseAmount;
                    currentBet = player.getBet() + totalRaise;
                    player.raise(totalRaise);
                    player.setBalance(player.getBalance() - totalRaise);
                } else player.call(0);
            }
            case FOLD -> {
                player.fold();
                foldedPlayers.add(player);
            }
        }
        pokerController.disablePlayerActionButtons();
    }
    //Handles NPCs turn
    private static void handleNPCTurn(NPC npc) {
        NPCState npcState = new NPCState();
        npcState.callAmount = currentBet - npc.getBet();
        npcState.canCheck = currentBet == npc.getBet();
        npcState.potSize = getPot();
        npcState.currentBet = npc.getBet();
        npcState.handStrength = HandEvaluator.evaluateHand(npc.getHand(), board);
        npcState.chips = npc.getBalance();
        npcState.playersRemaining = countActivePlayers();

        PlayerAction action = npc.getStrategy().inference(npcState);

        switch (action) {
            case FOLD -> { npc.fold(); foldedPlayers.add(npc); }
            case CALL -> { int toCall = npcState.callAmount; if (toCall > 0) npc.call(Math.min(toCall, npc.getBalance())); }
            case RAISE -> {
                int raiseAmount = Math.max(1, (int) (npc.getBalance() * 0.05));
                int totalRaise = (currentBet - npc.getBet()) + raiseAmount;
                if (totalRaise > npc.getBalance()) npc.call(Math.min(currentBet - npc.getBet(), npc.getBalance()));
                else {
                    currentBet = npc.getBet() + totalRaise;
                    npc.raise(totalRaise);
                    npc.setBalance(npc.getBalance() - totalRaise);
                }
            }
        }
    }
    //post smalls and big blinds
    private static void postBlinds() {
        foldedPlayers.clear();
        int smallIdx = (dealerPosition + 1) % players.size();
        int bigIdx = (dealerPosition + 2) % players.size();

        Player small = players.get(smallIdx);
        Player big = players.get(bigIdx);

        small.setIsSmallBlind(true);
        big.setIsBigBlind(true);

        small.raise(SMALL_BLIND_AMOUNT); small.setBalance(small.getBalance() - SMALL_BLIND_AMOUNT);
        big.raise(BIG_BLIND_AMOUNT); big.setBalance(big.getBalance() - BIG_BLIND_AMOUNT);

        currentBet = BIG_BLIND_AMOUNT;
    }
    // deals out cards
    private static void deal() {
        int numPlayers = players.size();
        for (int round = 0; round < 2; round++)
            for (int i = 0; i < numPlayers; i++) {
                int idx = (dealerPosition + 1 + i) % numPlayers;
                players.get(idx).addCardToHand(deck.remove(0));
            }
        evaluate();
    }
    //deals with flop, turing and river
    private static void dealFlop() { burnCard(); for (int i = 0; i < 3; i++) board.add(deck.remove(0)); evaluate(); }
    private static void dealTurnCard() { burnCard(); board.add(deck.remove(0)); evaluate(); }
    private static void dealRiverCard() { burnCard(); board.add(deck.remove(0)); evaluate(); }
    private static void burnCard() { if (!deck.isEmpty()) deck.remove(0); }
    // rotates blinds clockwise
    private static void rotateBlinds() { dealerPosition = (dealerPosition + 1) % players.size(); }
    // counts active players
    private static int countActivePlayers() { return (int) players.stream().filter(p -> !foldedPlayers.contains(p)).count(); }
    // returns if one player is left
    private static boolean onlyOnePlayerLeft() { return countActivePlayers() == 1; }
    // handles if win is immediate after everyone folds.
    private static void handleImmediateWin() {
        for (Player p : players) {
            if (!foldedPlayers.contains(p)) {
                p.setBalance(p.getBalance() + pot);
                pot = 0;
                break;
            }
        }
        updateUI();
    }
    // determines winners of hand
    private static List<Player> determineWinners() {
        List<Player> contenders = new ArrayList<>();
        for (Player p : players) if (!foldedPlayers.contains(p)) contenders.add(p);

        if (contenders.isEmpty()) return new ArrayList<>();
        Player best = contenders.get(0);
        HandEvaluationResult bestEval = best.getHandEvaluation();
        List<Player> winners = new ArrayList<>();
        winners.add(best);

        for (int i = 1; i < contenders.size(); i++) {
            Player cur = contenders.get(i);
            HandEvaluationResult curEval = cur.getHandEvaluation();
            if (curEval != null && (bestEval == null || curEval.getHandType().ordinal() > bestEval.getHandType().ordinal())) {
                best = cur; bestEval = curEval; winners.clear(); winners.add(cur);
            } else if (curEval != null && bestEval != null && curEval.getHandType().ordinal() == bestEval.getHandType().ordinal()) {
                winners.add(cur);
            }
        }
        return winners;
    }
    // awards pot to winners
    private static void awardPot(List<Player> winners) {
        if (winners == null || winners.isEmpty()) { pot = 0; return; }
        int share = pot / winners.size();
        int remainder = pot % winners.size();

        for (int i = 0; i < winners.size(); i++) {
            Player w = winners.get(i);
            int awarding = share + (i == 0 ? remainder : 0);
            w.setBalance(w.getBalance() + awarding);
        }

        pot = 0;
        updateUI();
    }
    // resets hand state to default
    private static void resetHandState() {
        board.clear(); deck.clear(); foldedPlayers.clear(); pot = 0; currentBet = 0;
        for (Player p : players) {
            p.setBet(0); p.getHand().clear(); p.setIsSmallBlind(false); p.setIsBigBlind(false);
            p.setHandEvaluation(null); p.setActiveTurn(false);
        }
    }
    // evaluate hands for winner
    public static void evaluate() {
        for (Player p : players) p.setHandEvaluation(HandEvaluator.evaluateHand(p.getHand(), board));
    }
    // sets new deck
    private static void newDeck() {
        deck.clear();
        for (int suit = 1; suit <= 4; suit++)
            for (int rank = 1; rank <= 13; rank++)
                deck.add(new Card(switch (suit) {
                    case 1 -> CardSuit.HEART;
                    case 2 -> CardSuit.DIAMOND;
                    case 3 -> CardSuit.CLUB;
                    case 4 -> CardSuit.SPADE;
                    default -> null;
                }, CardRank.getRankFromValue(rank)));
    }
    // shuffles deck to redo
    private static void shuffleDeck() {
        Collections.shuffle(deck);
    }
    // updates UI
    private static void updateUI() {
        if (pokerController != null)
            Platform.runLater(() -> pokerController.updateLabelsUI());
    }
}
