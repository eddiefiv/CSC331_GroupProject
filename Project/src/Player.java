import java.util.ArrayList;
import java.util.EnumMap;

public class Player {
    public EnumMap<ChipValue, Integer> chips; // key = chip, value = number of that chip
    private int balance;
    private ArrayList<Card> hand = new ArrayList<Card>();

    public Player() {
        int defaultBalance = 1000;

        this.chips = new EnumMap<>(ChipValue.class);

        setBalance(defaultBalance);
        setChips(defaultBalance);
    }

    public Player(int initialBalance) {
        this.chips = new EnumMap<>(ChipValue.class);

        setBalance(initialBalance);
        setChips(initialBalance);
    }

    // GETTERS AND SETTERS

    public ArrayList<Card> getHand() {
        return hand;
    }

    public void setHand(ArrayList<Card> hand) {
        this.hand = hand;
    }

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

    public void setBalance(int balance) {
        this.balance = balance;
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

    public void raise() {

    }

    public void fold() {

    }

    public void deal() {

    }

    @Override
    public String toString() {
        return String.format("Hand: %s%nChips: %s%n", hand, chips);
    }
}
