/**
* @author Emma Fox, Eddie Falco
* CSC 331 Section 001 - 002 - 003
* Date: 11/23/2025
* Purpose: Establish the value of the cards
*/

public enum CardRank {
    //List of card ranks
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

    private CardRank(int value) {}
    /**
    * Function to return the rank of a card based on its value
    * @param value a number value from 1 - 13 (int)
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
    * Function to return the value of a card based on its rank
    * @param rank the rank of a card (CardRank)
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
