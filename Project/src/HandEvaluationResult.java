/**
 * Record to keep track of the evaluated HandType and the Cards associated with that HandType
 *
 * @author Eddie Falco
 */

import java.util.ArrayList;

public class HandEvaluationResult {
    // Declare constants handType and cards
    private final HandType handType;
    private final ArrayList<Card> cards;

    /**
     * HandEvaluationResult constructor, taking in a HandType and ArrayList of cards to satisfy all attributes
     * @param handType HandType of the result
     * @param cards Cards corresponding to the result
     */
    public HandEvaluationResult(HandType handType, ArrayList<Card> cards) {
        this.handType = handType;
        this.cards = cards;
    }

    /**
     * HandEvaluationResult constructor, taking in an ArrayList of cards and assuming HandType is HIGH_CARD
     * @param card Cards corresponding to the result
     */
    public HandEvaluationResult(Card card) {
        handType = HandType.HIGH_CARD;

        // Add single card to cards
        ArrayList<Card> cards = new ArrayList<>();
        cards.add(card);

        this.cards = cards;
    }

    /**
     * HandEvaluationResult no-argument constructor, for baseline cases where one needs to be returned but attributes don't matter
     */
    public HandEvaluationResult() {
        // Default values
        this.handType = HandType.DEFAULT;
        this.cards = null;
    }

    /**
     * Returns the HandType corresponding to the result
     *
     * @return HandType the HandType of the result
     */
    public HandType getHandType() {
        return handType;
    }

    /**
     * Returns the Cards corresponding to the result
     *
     * @return Cards the Cards of the result
     */
    public ArrayList<Card> getCards() {
        return cards;
    }

    /**
     * Return a String representation of the HandEvaluationResult
     * @return String string representation of HandEvaluationResult
     */
    @Override
    public String toString() {
        return String.format(
                "HandType: %s%n" +
                "Cards: %s",
                handType,
                cards
        );
    }
}
