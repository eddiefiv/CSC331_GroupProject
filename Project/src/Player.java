import java.util.ArrayList;
import java.util.EnumMap;

/**
 * @author James Frink, Eddie Falco
 * CSC 331 Section 001 - 002 - 003
 * Date: 11/23/2025
 * Purpose: Create a builder for the Players
 */
public class Player {
    private String playerName;
    public EnumMap<ChipValue, Integer> chips; // key = chip, value = number of that chip
    private int balance;
    private ArrayList<Card> hand = new ArrayList<Card>();
    private HandEvaluationResult handEvaluation = null;

    // Boolean attributes for state checking
    private boolean isSmallBlind; // Is the player the current small blind
    private boolean isBigBlind; // Is the player the current big blind
    private boolean isActiveTurn; // Is it the player's turn
    private boolean isActivelyPlaying; // Is the player still in the game (if they folded, this is false)
    /**
     * Player Constructor
     * @param playerName player's name (String)
     */
    public Player(String playerName) {
        int defaultBalance = 1000;

        this.playerName = playerName;

        this.chips = new EnumMap<>(ChipValue.class);

        setBalance(defaultBalance);
        setChips(defaultBalance);
        setIsSmallBlind(false);
        setIsBigBlind(false);
    }
    /**
     * Player Constructor
     * @param playerName player's name (String)
     * @param initialBalance amount of chips the player starts with (int)
     */
    public Player(String playerName, int initialBalance) {
        this.chips = new EnumMap<>(ChipValue.class);

        this.playerName = playerName;

        setBalance(initialBalance);
        setChips(initialBalance);
        setIsSmallBlind(false);
        setIsBigBlind(false);
    }

    // GETTERS AND SETTERS
    /**
     * Function to get playerName
     * @return playerName
     */
    public String getPlayerName() {
        return playerName;
    }
    /**
     * Function to set hand
     * @param hand the cards the Player has (ArrayList<Card>)
     */
    public ArrayList<Card> getHand() {
        return hand;
    }
    /**
     * Function to set hand
     * @param hand the cards the Player has (ArrayList<Card>)
     */
    public void setHand(ArrayList<Card> hand) {
        this.hand = hand;
    }
    /**
     * Function to return chips
     * @return chips
     */
    public EnumMap<ChipValue, Integer> getChips() {
        return chips;
    }
    /**
     * Function to set chips
     * @param balance balance (int)
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
            numOfOneThousands = balance / 1000; // Set chip count and update balance
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
        this.chips.put(ChipValue.FIVE, this.chips.getOrDefault(ChipValue.ONE, 0) + numOfFives);
        this.chips.put(ChipValue.TEN, this.chips.getOrDefault(ChipValue.ONE, 0) + numOfTens);
        this.chips.put(ChipValue.TWENTY_FIVE, this.chips.getOrDefault(ChipValue.ONE, 0) + numOfTwentyFives);
        this.chips.put(ChipValue.ONE_HUNDRED, this.chips.getOrDefault(ChipValue.ONE_HUNDRED, 0) + numOfOneHundreds);
        this.chips.put(ChipValue.FIVE_HUNDRED, this.chips.getOrDefault(ChipValue.FIVE_HUNDRED, 0) + numOfFiveHundreds);
        this.chips.put(ChipValue.ONE_THOUSAND, this.chips.getOrDefault(ChipValue.ONE_THOUSAND, 0) + numOfOneThousands);
    }
    /**
     * Function to get the balance
     * @return balance
     */
    public int getBalance() {
        return balance;
    }
    /**
    * Function to set the balance
    * @param balance balance (int)
    */
    public void setBalance(int balance) {
        this.balance = balance;
    }
    /**
    * Function to see if it is a small blind
    * @return isSmallBlind
    */
    public boolean isSmallBlind() {
        return isSmallBlind;
    }
    /**
    * Function to set the blind as a small blind
    * @param isSmallBlind isSmallBlind (boolean)
    */
    public void setIsSmallBlind(boolean isSmallBlind) {
        this.isSmallBlind = isSmallBlind;
    }
    /**
    * Function to see if it is a big blind
    * @return isBigBlind
    */
    public boolean isBigBlind() {
        return isBigBlind;
    }
    /**
    * Function to set the blind to a big blind
    * @param isBigBlind isBigBlind (boolean)
    */
    public void setIsBigBlind(boolean isBigBlind) {
        this.isBigBlind = isBigBlind;
    }
    
    public boolean isActiveTurn() {
        return isActiveTurn;
    }

    public void setActiveTurn(boolean activeTurn) {
        isActiveTurn = activeTurn;
    }

    public HandEvaluationResult getHandEvaluation() {
        return handEvaluation;
    }

    public void setHandEvaluation(HandEvaluationResult handEvaluation) {
        this.handEvaluation = handEvaluation;
    }

    // UTILITIES
    public void addChip(ChipValue chipValue, int amount) {
        this.chips.put(chipValue, this.chips.getOrDefault(chipValue, 0) + amount);
    }

    public void addCardToHand(Card card) {
        hand.add(card);
    }

    public int calculateBalance() {
        int total = 0;

        for (ChipValue chipValue : chips.keySet()) {
            int frequency = chips.getOrDefault(chipValue, 0);

            total += (ChipValue.getChipNumberValue(chipValue) * frequency);
        }

        return total;
    }

    // ---- GAMEPLAY ----
    public void call() {

    }

    public void raise(int amountToRaise) {
        // First prompt the user if they want to raise
        Table.setPot(Table.getPot() + amountToRaise);
    }

    public void fold() {
        isActivelyPlaying = false;
    }

    @Override
    public String toString() {
        return String.format("Hand: %s%nChips: %s%n", hand, chips);
    }
}
