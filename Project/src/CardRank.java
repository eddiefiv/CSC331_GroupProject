/**
 * Enum to represent each rank (2-Ace) of a card
 *
 * @author Eddie Falco
 */
public enum CardRank {
    ACE(1),
    TWO(2),
    THREE(3),
    FOUR(4),
    FIVE(5),
    SIX(6),
    SEVEN(7),
    EIGHT(8),
    NINE(9),
    TEN(10),
    JACK(11),
    QUEEN(12),
    KING(13);

    /**
     * Base constructor for syntactical reasons, does not implement anything
     *
     * @param value numerical value of the card
     */
    private CardRank(int value) {}


    /**
     * Takes a number and derives a CardRank from it
     *
     * @param value the number to derive a CardRank from
     * @return CardRank the rank corresponding with the number value
     */
    public static CardRank getRankFromValue(int value) {
        return switch (value) {
            case 1 -> ACE;
            case 2 -> TWO;
            case 3 -> THREE;
            case 4 -> FOUR;
            case 5 -> FIVE;
            case 6 -> SIX;
            case 7 -> SEVEN;
            case 8 -> EIGHT;
            case 9 -> NINE;
            case 10 -> TEN;
            case 11 -> JACK;
            case 12 -> QUEEN;
            case 13 -> KING;
            default -> throw new IllegalStateException("Unexpected value: " + value);
        };
    }

    /**
     * Takes a CardRank and derives a number value from it
     *
     * @param rank the CardRank to derive a number value from
     * @return int the number value corresponding with the CardRank
     */
    public static int getValueFromRank(CardRank rank) {
        return switch (rank) {
            case ACE -> 1;
            case TWO -> 2;
            case THREE -> 3;
            case FOUR -> 4;
            case FIVE -> 5;
            case SIX -> 6;
            case SEVEN -> 7;
            case EIGHT -> 8;
            case NINE -> 9;
            case TEN -> 10;
            case JACK -> 11;
            case QUEEN -> 12;
            case KING -> 13;
            default -> throw new IllegalStateException("Unexpected value: " + rank);
        };
    }
}
