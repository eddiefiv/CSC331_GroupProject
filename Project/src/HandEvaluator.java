import java.util.*;
import java.util.stream.Collectors;

public class HandEvaluator {
    public static HandType evaluateHand(ArrayList<Card> playerHand, ArrayList<Card> board) {
        ArrayList<Card> combined = new ArrayList<Card>();
        combined.addAll(playerHand);
        combined.addAll(board);

        for (Card card : playerHand) {
            HandType handType = HandType.HIGH_CARD; // Default to high card

            // Only check for royal flush, straight flush, straight, and full house if board has 3 or more cards because those hands need 5 cards to be valid
            if (board.size() >= 3) {
                HandType flushType = checkFlushes(combined);
                if (!flushType.equals(HandType.HIGH_CARD)) return flushType;
                //boolean fullHouse = checkFullHouse(combined);
                //boolean straight = checkStraight(combined);
            }
        }

        return HandType.HIGH_CARD;
    }

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

        HandType flushHand = HandType.getHandTypeFromStrengthValue(Math.max(
                HandType.getStrengthValueFromHand(heartsHand),
                Math.max(HandType.getStrengthValueFromHand(diamondsHand),
                Math.max(HandType.getStrengthValueFromHand(clubsHand),
                HandType.getStrengthValueFromHand(spadesHand)))
        ));

        return flushHand;
    }

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
}
