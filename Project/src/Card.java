/**
* @author Emma Fox, Eddie Falco
* CSC 331 Section 001 - 002 - 003
* Date: 11/23/2025
* Purpose: Create a builder for Card objects
*/
public class Card {
    private CardSuit suit;
    private CardRank rank;
    /**
    * Card Constructor
    * @param suit the suit of the card (CardSuit)
    * @param rank the rank of the card (CardRank)
    */
    public Card(CardSuit suit, CardRank rank) {
        this.suit = suit;
        this.rank = rank;
    }

    // GETTERS
    /**
    * Function to get the suit
    * @return suit
    */
    public CardSuit getSuit() {
        return suit;
    }
    /**
    * Function to get the rank
    * @return rank
    */
    public CardRank getRank() {
        return rank;
    }

    @Override
    public String toString() {
        return String.format("Rank: %s, Suit: %s", this.rank, this.suit);
    }
}
