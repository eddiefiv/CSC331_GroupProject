import java.util.*;
import java.util.stream.Collectors;

public class HandEvaluator {
    public static EnumMap<HandType, ArrayList<Card>> evaluateHand(ArrayList<Card> playerHand, ArrayList<Card> board) {
        ArrayList<Card> combined = new ArrayList<Card>();
        combined.addAll(playerHand);
        combined.addAll(board);

        // Initialize the EnumMap that will hold the final hand ranking, plus the cards that are associated with that hand (suit is irrelevant, only card value matters)
        EnumMap<HandType, ArrayList<Card>> handRankingCards = new EnumMap<>(HandType.class);

        for (Card card : playerHand) {
            HandType handType = HandType.HIGH_CARD; // Default to high card

            // Only check for royal flush, straight flush, straight, and full house if board has 3 or more cards because those hands need 5 cards to be valid
            if (board.size() >= 3) {
                HandType flushType = checkFlushes(combined);
                if (!flushType.equals(HandType.HIGH_CARD)) {
                    handRankingCards.put(handType, new );
                };
                //TODO boolean fullHouse = checkFullHouse(combined);
                //TODO boolean straight = checkStraight(combined);
            }

            if (board.size() >= 2) { // Check for four of a kind only if board has 2 or more cards because there needs to be 4 cards to be valid
                HandType fourOfAKind = checkLikeCards(combined, 4);
                if (!fourOfAKind.equals(HandType.HIGH_CARD)) return fourOfAKind;
            }
        }

        return HandType.HIGH_CARD;
    }

    // CHECKS
    private static HandType checkFlushes(ArrayList<Card> combinedHand) {
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

        HandType heartsHand = hasFlush(true, true, hearts);
        HandType diamondsHand = hasFlush(true, true, diamonds);
        HandType clubsHand = hasFlush(true, true, clubs);
        HandType spadesHand = hasFlush(true, true, spades);

        return HandType.getHandTypeFromStrengthValue(Math.max(
                HandType.getStrengthValueFromHand(heartsHand),
                Math.max(HandType.getStrengthValueFromHand(diamondsHand),
                Math.max(HandType.getStrengthValueFromHand(clubsHand),
                HandType.getStrengthValueFromHand(spadesHand)))
        ));

        return flushHand;
    }

    /**
     * Checks if there are likeCardsToCheck like cards in the provided combinedHand.
     *
     * For example, checkLikeCards(hand, 3) will check if there are 3 of a kind in the hand, similarly
     * @param combinedHand
     * @param likeCardsToCheck
     * @return
     */
    private static HandEvaluationResult checkLikeCards(ArrayList<Card> combinedHand, int likeCardsToCheck) {
        EnumMap<CardRank, Integer> frequencyTable = createEmptyRankFrequencyTable();
        List<CardRank> frequencyKeys = new ArrayList<>(frequencyTable.keySet());
        Collections.reverse(frequencyKeys); // Reverse keys to be able to iterate in reverse later

        // Add 1 to each value for every found key
        for (Card card : combinedHand) {
            frequencyTable.put(card.getRank(), frequencyTable.getOrDefault(card.getRank(), 0) + 1);
        }

        // If there are 4 or more of a single rank, then there is four of a kind
        // iterate backwards to return the highest possible pair first
        ArrayList<Card> cardsToReturn = new ArrayList<Card>();
        for (CardRank rankKey : frequencyKeys) {
            if (frequencyTable.get(rankKey) >= 4) {
                for (int i = 0; i < frequencyTable.get(rankKey); i++) {
                    cardsToReturn.add(combinedHand.get(i)); // TODO GET CARDS FROM COMBINDHAND INSTEAD OF DEFAULT CARDRANK
                }
                return new HandEvaluationResult(HandType.FOUR_OF_A_KIND, )
            }
        }

        return HandType.HIGH_CARD;
    }

    // HELPERS
    /**
     * Will check for royal flush, straight flush, or a regular flush
     */
    private static HandType hasFlush(
            boolean royal,
            boolean straight,
            boolean[] suit
    ) {
        if (royal) { // Royal flush checks for Ace(1), Ten(10), Jack(11), Queen(12), King(13)
            if (suit[1] && suit[10] && suit[11] && suit[12] && suit[13]) {
                return  HandType.ROYAL_FLUSH;
            }
        }

        if (straight) { // Straight flush checks for any in succession
            for (int idx = 0; idx < 10; idx++) {
                if (suit[idx]) { // If a true is found, check the succeeding 4 values to see if they are true too
                    boolean valid = true;
                    for (int offset = idx; offset < idx + 4; offset++) {
                        if (!suit[offset]) {
                            valid = false;
                            break;
                        }
                    }

                    if (valid) {
                        return HandType.STRAIGHT_FLUSH;
                    }
                }
            }
        } else { // Check for regular flush, only need 5 of same suit
            int count = 0;
            for (int idx = 0; idx < 14; idx++) {
                if (suit[idx]) {
                    count++;
                }
            }

            if (count >= 5) {
                return HandType.FLUSH;
            }

        }
        return HandType.HIGH_CARD; // Default if no flush
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
