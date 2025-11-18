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

    public HandEvaluationResult() {
        this.handType = HandType.;
        this.cards = null;
    }

    public HandType getHandType() {
        return handType;
    }

    public ArrayList<Card> getCards() {
        return cards;
    }
}
