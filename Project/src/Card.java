public class Card {
    private final CardSuit suit;
    private final CardRank rank;

    public Card(CardSuit suit, CardRank rank) {
        this.suit = suit;
        this.rank = rank;
    }

    // GETTERS
    public CardSuit getSuit() {
        return suit;
    }

    public CardRank getRank() {
        return rank;
    }

    // switches for picking the suit and rank of the cards
    public String getImageName() {
        String rankStr;

        switch (rank) {
            case TWO -> rankStr = "2";
            case THREE -> rankStr = "3";
            case FOUR -> rankStr = "4";
            case FIVE -> rankStr = "5";
            case SIX -> rankStr = "6";
            case SEVEN -> rankStr = "7";
            case EIGHT -> rankStr = "8";
            case NINE -> rankStr = "9";
            case TEN -> rankStr = "10";
            case JACK -> rankStr = "J";
            case QUEEN -> rankStr = "Q";
            case KING -> rankStr = "K";
            case ACE -> rankStr = "A";

            default -> throw new IllegalStateException("Unexpected value: " + rank);
        }

        String suitStr;

        switch (suit) {
            case HEART -> suitStr = "H";
            case DIAMOND -> suitStr = "D";
            case CLUB -> suitStr = "C";
            case SPADE -> suitStr = "S";
            default -> throw new IllegalStateException("Unexpected suit: " + suit);
        }
        return rankStr + suitStr;
    }

    @Override
    public String toString() {
        return String.format("Rank: %s, Suit: %s", this.rank, this.suit);
    }

}
