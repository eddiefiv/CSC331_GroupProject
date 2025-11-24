/**
 * Base player class that holds all attributes and methods to allow a player to function, either as an NPC or ControllablePlayer
 *
 * @author Eddie Falco
 */
import java.util.ArrayList;
import java.util.EnumMap;

public class Player {
    // Simple metadata
    private String playerName;
    private int balance;
    private int bet;

    // Functional attributes
    public EnumMap<ChipValue, Integer> chips; // key = chip, value = number of that chip
    private ArrayList<Card> hand = new ArrayList<Card>();
    private HandEvaluationResult handEvaluation = null;

    // Boolean attributes for state checking
    private boolean isSmallBlind; // Is the player the current small blind
    private boolean isBigBlind; // Is the player the current big blind
    private boolean isActiveTurn; // Is it the player's turn
    private boolean isActivelyPlaying; // Is the player still in the game (if they folded, this is false)

    /**
     * Constructor for the Player, taking in a String playerName as a display name
     *
     * @param playerName Player's display name
     */
    public Player(String playerName) {
        int defaultBalance = 1000;

        this.playerName = playerName;

        this.chips = new EnumMap<>(ChipValue.class);

        // Update attributes
        setBalance(defaultBalance);
        setChips(defaultBalance);
        setIsSmallBlind(false);
        setIsBigBlind(false);
    }

    /**
     * Constructor for the Player, taking in a String playerName as a display name and a integer balance
     *
     * @param playerName Player's display name
     * @param initialBalance Player's initial balance
     */
    public Player(String playerName, int initialBalance) {
        this.chips = new EnumMap<>(ChipValue.class);

        this.playerName = playerName;

        // Update attributes
        setBalance(initialBalance);
        setChips(initialBalance);
        setIsSmallBlind(false);
        setIsBigBlind(false);
    }

    /**
     * Returns the player's display name
     *
     * @return Player's name
     */
    public String getPlayerName() {
        return playerName;
    }

    /**
     * Returns the player's current hand of cards
     *
     * @return ArrayList of Card objects in the player's hand
     */
    public ArrayList<Card> getHand() {
        return hand;
    }

    /**
     * Returns the amount the player has currently bet in the current round
     *
     * @return Player's current bet
     */
    public int getBet() {
        return bet;
    }

    /**
     * Sets the player's current bet for the round
     *
     * @param bet The amount to set as the player's current bet
     */
    public void setBet(int bet) {
        this.bet = bet;
    }

    /**
     * Returns the player's chips organized by chip value
     *
     * @return EnumMap mapping ChipValue to the count of that chip
     */
    public EnumMap<ChipValue, Integer> getChips() {
        return chips;
    }

    /**
     * Updates the player's chip counts based on the specified balance
     *
     * @param balance Total balance to convert into chips
     */
    public void setChips(int balance) {
        int numOfOneThousands = 0;
        int numOfFiveHundreds = 0;
        int numOfOneHundreds = 0;
        int numOfTwentyFives = 0;
        int numOfTens = 0;
        int numOfFives = 0;
        int numOfOnes = 0;

        if (balance / 1000 > 0) {
            numOfOneThousands = balance / 1000;
            balance %= 1000;
        }
        if (balance / 500 > 0) {
            numOfFiveHundreds = balance / 500;
            balance %= 500;
        }
        if (balance / 100 > 0) {
            numOfOneHundreds = balance / 100;
            balance %= 100;
        }
        if (balance / 25 > 0) {
            numOfTwentyFives = balance / 25;
            balance %= 25;
        }
        if (balance / 10 > 0) {
            numOfTens = balance / 10;
            balance %= 10;
        }
        if (balance / 5 > 0) {
            numOfFives = balance / 5;
            balance %= 5;
        }
        if (balance > 0) {
            numOfOnes = balance;
        }

        this.chips.put(ChipValue.ONE, this.chips.getOrDefault(ChipValue.ONE, 0) + numOfOnes);
        this.chips.put(ChipValue.FIVE, this.chips.getOrDefault(ChipValue.FIVE, 0) + numOfFives);
        this.chips.put(ChipValue.TEN, this.chips.getOrDefault(ChipValue.TEN, 0) + numOfTens);
        this.chips.put(ChipValue.TWENTY_FIVE, this.chips.getOrDefault(ChipValue.TWENTY_FIVE, 0) + numOfTwentyFives);
        this.chips.put(ChipValue.ONE_HUNDRED, this.chips.getOrDefault(ChipValue.ONE_HUNDRED, 0) + numOfOneHundreds);
        this.chips.put(ChipValue.FIVE_HUNDRED, this.chips.getOrDefault(ChipValue.FIVE_HUNDRED, 0) + numOfFiveHundreds);
        this.chips.put(ChipValue.ONE_THOUSAND, this.chips.getOrDefault(ChipValue.ONE_THOUSAND, 0) + numOfOneThousands);
    }

    /**
     * Returns the player's current balance
     *
     * @return Player's balance
     */
    public int getBalance() {
        return balance;
    }

    /**
     * Sets the player's balance
     *
     * @param balance The balance to set
     */
    public void setBalance(int balance) {
        this.balance = balance;
    }

    /**
     * Sets whether the player is currently the small blind
     *
     * @param isSmallBlind true if the player is the small blind, false otherwise
     */
    public void setIsSmallBlind(boolean isSmallBlind) {
        this.isSmallBlind = isSmallBlind;
    }

    /**
     * Sets whether the player is currently the big blind
     *
     * @param isBigBlind true if the player is the big blind, false otherwise
     */
    public void setIsBigBlind(boolean isBigBlind) {
        this.isBigBlind = isBigBlind;
    }

    /**
     * Sets whether it is currently the player's turn
     *
     * @param activeTurn true if it's the player's turn, false otherwise
     */
    public void setActiveTurn(boolean activeTurn) {
        isActiveTurn = activeTurn;
    }

    /**
     * Returns the evaluation of the player's hand
     *
     * @return HandEvaluationResult containing hand strength and relevant info
     */
    public HandEvaluationResult getHandEvaluation() {
        return handEvaluation;
    }

    /**
     * Sets the evaluation result of the player's hand
     *
     * @param handEvaluation HandEvaluationResult object containing hand strength and info
     */
    public void setHandEvaluation(HandEvaluationResult handEvaluation) {
        this.handEvaluation = handEvaluation;
    }

    /**
     * Adds a card to the player's hand
     *
     * @param card Card object to add to the player's hand
     */
    public void addCardToHand(Card card) {
        hand.add(card);
    }

    /**
     * Makes the player call a specified amount, adding it to the pot and their current bet
     *
     * @param howMuchToCall Amount the player needs to call
     */
    public void call(int howMuchToCall) {
        Table.setPot(Table.getPot() + howMuchToCall);
        setBet(getBet() + howMuchToCall);
    }

    /**
     * Makes the player raise by a specified amount, adding it to the pot and their current bet
     *
     * @param amountToRaise Amount the player wants to raise
     */
    public void raise(int amountToRaise) {
        Table.setPot(Table.getPot() + amountToRaise);
        setBet(getBet() + amountToRaise);
    }

    /**
     * Makes the player fold, marking them as no longer actively playing
     */
    public void fold() {
        isActivelyPlaying = false;
    }

    /**
     * Returns a string representation of the player, including their hand and chips
     *
     * @return String summarizing the player's hand and chip counts
     */
    @Override
    public String toString() {
        return String.format("Hand: %s%nChips: %s%n", hand, chips);
    }
}
