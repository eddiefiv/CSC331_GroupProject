/**
 * Record to hold information about a single Card
 *
 * @author Eddie Falco
 */

public class Card {
    // Initialize constants suit and rank
    private final CardSuit suit;
    private final CardRank rank;

    /**
     * Construct a Card with a CardSuit and CardRank
     *
     * @param suit
     * @param rank
     */
    public Card(CardSuit suit, CardRank rank) {
        this.suit = suit;
        this.rank = rank;
    }

    // GETTERS

    /**
     * Get the CardSuit
     *
     * @return CardSuit this card's suit
     */
    public CardSuit getSuit() {
        return suit;
    }

    /**
     * Get the CardRank
     *
     * @return CardRank this card's rank
     */
    public CardRank getRank() {
        return rank;
    }

    /**
     * Return a String of the card's CardRank and CardSuit
     * @return String string representation of Card
     */
    @Override
    public String toString() {
        return String.format("Rank: %s, Suit: %s", this.rank, this.suit);
    }
}
