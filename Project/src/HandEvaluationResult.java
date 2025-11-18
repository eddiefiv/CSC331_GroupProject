/**
 * Record to keep track of the evaulated HandType and the Cards associated with that HandType
 *
 * @author Eddie Falco
 */

import java.util.ArrayList;

public class HandEvaluationResult {
    private final HandType handType;
    private final ArrayList<Card> cards;

    public HandEvaluationResult(HandType handType, ArrayList<Card> cards) {
        this.handType = handType;
        this.cards = cards;
    }

    public HandEvaluationResult(Card card) {
        handType = HandType.HIGH_CARD;

        // Add single card to cards
        ArrayList<Card> cards = new ArrayList<>();
        cards.add(card);

        this.cards = cards;
    }

    public HandEvaluationResult() {
        this.handType = HandType.DEFAULT;
        this.cards = null;
    }

    public HandType getHandType() {
        return handType;
    }

    public ArrayList<Card> getCards() {
        return cards;
    }

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
