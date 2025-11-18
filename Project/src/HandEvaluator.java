/**
 * @author Eddie Falco
 */

import java.util.*;

public class HandEvaluator {
    public static HandEvaluationResult evaluateHand(ArrayList<Card> playerHand, ArrayList<Card> board) {
        ArrayList<Card> combined = new ArrayList<Card>();
        combined.addAll(playerHand);
        combined.addAll(board);

        for (Card card : playerHand) {
            HandType handType = HandType.HIGH_CARD; // Default to high card

            // Only check for royal flush, straight flush, straight, and full house if combined has 5 or more cards because those hands need 5 cards to be valid
            if (combined.size() >= 5) {
                HandEvaluationResult flushType = checkFlushes(combined); // Contains FLUSH, STRAIGHT_FLUSH, and ROYAL FLUSH
                HandEvaluationResult fullHouse = checkFullHouse(combined);
                HandEvaluationResult straight = checkStraight(combined);
            }

            if (combined.size() >= 4) { // Check for four of a kind only if combined 4 or more cards because there needs to be 4 cards to be valid
                HandEvaluationResult fourOfAKindResult = checkLikeCards(combined, 4);
            }
        }

        return null
    }

    // CHECKS
    private static HandEvaluationResult checkFlushes(ArrayList<Card> combinedHand) {
        // Boolean arrays to keep track of held cards in each suit
        // Each array has 14 booleans, toggled on if the corresponding card (ace-king) is present
        boolean[] hearts = new boolean[14];
        boolean[] diamonds = new boolean[14];
        boolean[] clubs =  new boolean[14];
        boolean[] spades = new boolean[14];

        for (Card card : combinedHand) {
            int cardValue = CardRank.getValueFromRank(card.getRank());

            switch (card.getSuit()) {
                case HEART:
                    hearts[cardValue] = true;
                    break;
                case DIAMOND:
                    diamonds[cardValue] = true;
                    break;
                case CLUB:
                    clubs[cardValue] = true;
                    break;
                case SPADE:
                    spades[cardValue] = true;
                    break;
            }
        }

        HandEvaluationResult heartsHand = hasFlush(true, true, hearts, CardSuit.HEART);
        HandEvaluationResult diamondsHand = hasFlush(true, true, diamonds, CardSuit.DIAMOND);
        HandEvaluationResult clubsHand = hasFlush(true, true, clubs, CardSuit.CLUB);
        HandEvaluationResult spadesHand = hasFlush(true, true, spades, CardSuit.SPADE);

        HandType flushHandType = HandType.getHandTypeFromStrengthValue(Math.max(
                HandType.getStrengthValueFromHandType(heartsHand.getHandType()),
                Math.max(HandType.getStrengthValueFromHandType(diamondsHand.getHandType()),
                Math.max(HandType.getStrengthValueFromHandType(clubsHand.getHandType()),
                HandType.getStrengthValueFromHandType(spadesHand.getHandType())))
        ));

        switch (flushHandType) {
            case ROYAL_FLUSH:

        }
    }

    /**
     * Checks if there are <i>likeCardsToCheck</i> like cards in the provided combinedHand.
     * <br>
     * For example, <i>checkLikeCards(hand, 3)</i> will check if there are 3 of a kind in the hand, similarly
     * @param combinedHand
     * @param likeCardsToCheck
     * @return
     */
    private static HandEvaluationResult checkLikeCards(ArrayList<Card> combinedHand, int likeCardsToCheck) throws IllegalArgumentException {
        // Check parameters
        if ((likeCardsToCheck != 4) && (likeCardsToCheck != 3) && (likeCardsToCheck != 2)) {
            throw new IllegalArgumentException("Invalid like cards to check. Should only be 4, 3, or 2.");
        }

        // Initialize frequencyTable for keep record of CardRanks and their occurrences in combinedHand
        EnumMap<CardRank, Integer> frequencyTable = createEmptyRankFrequencyTable();
        List<CardRank> frequencyKeys = new ArrayList<>(frequencyTable.keySet());
        Collections.reverse(frequencyKeys); // Reverse keys to be able to iterate in reverse later

        // Add 1 to each value for every found key
        for (Card card : combinedHand) {
            frequencyTable.put(card.getRank(), frequencyTable.getOrDefault(card.getRank(), 0) + 1);
        }

        // If there are likeCardsToCheck or more of a single rank, then there is that many like cards
        // iterate backwards to return the highest possible pair first
        ArrayList<Card> cardsToReturn = new ArrayList<Card>();
        for (CardRank rankKey : frequencyKeys) {
            if (frequencyTable.get(rankKey) >= likeCardsToCheck) {
                for (Card card : combinedHand){
                    if (card.getRank() == rankKey) {
                        cardsToReturn.add(card);
                    }
                }

                // Return proper type if valid
                if (likeCardsToCheck == 4) {
                    return new HandEvaluationResult(HandType.FOUR_OF_A_KIND, cardsToReturn);
                } else if (likeCardsToCheck == 3) {
                    return new HandEvaluationResult(HandType.THREE_OF_KIND, cardsToReturn);
                } else {
                    return new HandEvaluationResult(HandType.ONE_PAIR, cardsToReturn);
                }
            }
        }

        // Otherwise, return HIGH_CARD with the highest card in the hand
        return null
    }

    /**
     * Check for a full house (3 of a kind with a pair)
     * <br>
     * This can just call <i>checkLikeCards(combinedHand, 3)</i> then, after removing excess cards, <i>checkLikeCards(combinedHand, 2)</i>
     * @param combinedHand
     */
    private static HandEvaluationResult checkFullHouse(ArrayList<Card> combinedHand) {
        ArrayList<Card> combinedHandCopy = new ArrayList<>(combinedHand); // Copy combinedHand to avoid inadvertently modifying it
        HandEvaluationResult threeOfAKindResult = checkLikeCards(combinedHandCopy, 3);

        // First check for THREE_OF_A_KIND
        if (threeOfAKindResult.getHandType().equals(HandType.THREE_OF_KIND)) {
            combinedHandCopy.remove(threeOfAKindResult.getCards()); // Remove the cards

            HandEvaluationResult onePair = checkLikeCards(combinedHandCopy, 2);

            if (onePair.getHandType().equals(HandType.ONE_PAIR)) {
                // Initialize cardsToReturn and add the three of a kind and one pair cards
                ArrayList<Card> cardsToReturn = new ArrayList<Card>();
                cardsToReturn.addAll(onePair.getCards());
                cardsToReturn.addAll(threeOfAKindResult.getCards());

                return new HandEvaluationResult(HandType.FULL_HOUSE, cardsToReturn);
            }
        }

        // Return HIGH_CARD if nothing else
        return null
    }

    private static HandEvaluationResult checkStraight(ArrayList<Card> combinedHand) {
        boolean[] cardPresence = new boolean[14];

        for (Card card : combinedHand) {
            cardPresence[CardRank.getValueFromRank(card.getRank())] = true;
        }

        for (int idx = 0; idx < 10; idx++) {
            if (cardPresence[idx]) { // If a true is found, check the succeeding 4 values to see if they are true too
                ArrayList<Card> cardsToReturn = new ArrayList<>();
                boolean valid = true;
                for (int offset = idx; offset < idx + 4; offset++) {
                    if (!cardPresence[offset]) {
                        valid = false;
                        break;
                    }
                    cardsToReturn.add(new Card(CardSuit.DEFAULT, CardRank.getRankFromValue(offset))); // Return with DEFAULT suit because suit does not matter
                }

                if (valid) {
                    return new HandEvaluationResult(HandType.STRAIGHT_FLUSH, cardsToReturn);
                }
            }
        }

        return null;
    }

    // HELPERS
    /**
     * Will check for royal flush, straight flush, or a regular flush
     */
    private static HandEvaluationResult hasFlush(
            boolean royal,
            boolean straight,
            boolean[] cards,
            CardSuit suit
    ) {
        if (royal) { // Royal flush checks for Ace(1), Ten(10), Jack(11), Queen(12), King(13)
            if (cards[1] && cards[10] && cards[11] && cards[12] && cards[13]) {
                ArrayList<Card> cardsToReturn = new ArrayList<>();
                cardsToReturn.add(new Card(suit, CardRank.ACE));
                cardsToReturn.add(new Card(suit, CardRank.TEN));
                cardsToReturn.add(new Card(suit, CardRank.JACK));
                cardsToReturn.add(new Card(suit, CardRank.QUEEN));
                cardsToReturn.add(new Card(suit, CardRank.KING));
                return new HandEvaluationResult(HandType.ROYAL_FLUSH, cardsToReturn);
            }
        }

        if (straight) { // Straight flush checks for any in succession
            for (int idx = 0; idx < 10; idx++) {
                if (cards[idx]) { // If a true is found, check the succeeding 4 values to see if they are true too
                    ArrayList<Card> cardsToReturn = new ArrayList<>();
                    boolean valid = true;
                    for (int offset = idx; offset < idx + 4; offset++) {
                        if (!cards[offset]) {
                            valid = false;
                            break;
                        }
                        cardsToReturn.add(new Card(suit, CardRank.getRankFromValue(offset)));
                    }

                    if (valid) {
                        return new HandEvaluationResult(HandType.STRAIGHT_FLUSH, cardsToReturn);
                    }
                }
            }
        } else { // Check for regular flush, only need 5 of same suit
            int count = 0;
            ArrayList<Card> cardsToReturn = new ArrayList<>();
            for (int idx = 0; idx < 14; idx++) {
                if (cards[idx]) {
                    count++;
                    cardsToReturn.add(new Card(suit, CardRank.getRankFromValue(idx)));
                }
            }

            if (count >= 5) {
                return new HandEvaluationResult(HandType.STRAIGHT_FLUSH, cardsToReturn);
            }

        }
        return null // Default if no flush
    }

    private static EnumMap<CardRank, Integer> createEmptyRankFrequencyTable() {
        EnumMap<CardRank, Integer> frequencyTable = new EnumMap<>(CardRank.class);

        frequencyTable.put(
                CardRank.ACE, 0
        );
        frequencyTable.put(
                CardRank.TWO, 0
        );
        frequencyTable.put(
                CardRank.THREE, 0
        );
        frequencyTable.put(
                CardRank.FOUR, 0
        );
        frequencyTable.put(
                CardRank.FIVE, 0
        );
        frequencyTable.put(
                CardRank.SIX, 0
        );
        frequencyTable.put(
                CardRank.SEVEN, 0
        );
        frequencyTable.put(
                CardRank.EIGHT, 0
        );
        frequencyTable.put(
                CardRank.NINE, 0
        );
        frequencyTable.put(
                CardRank.TEN, 0
        );
        frequencyTable.put(
                CardRank.JACK, 0
        );
        frequencyTable.put(
                CardRank.QUEEN, 0
        );
        frequencyTable.put(
                CardRank.KING, 0
        );

        return frequencyTable;
    }
}
