import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
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
                boolean royalFlush = checkRoyalFlush(combined);
                if (royalFlush) return HandType.ROYAL_FLUSH;
                //boolean straightFlush = checkStraightFlush(combined);
                //boolean fullHouse = checkFullHouse(combined);
                //boolean straight = checkStraight(combined);
            }
        }

        return HandType.HIGH_CARD;
    }

    private static boolean checkRoyalFlush(ArrayList<Card> combinedHand) {
        HashMap<CardSuit, ArrayList<Card>> cardsBySuit = new HashMap<CardSuit, ArrayList<Card>>();

        for (Card card : combinedHand) {
            cardsBySuit
                    .computeIfAbsent(card.getSuit(), k -> new ArrayList<>())
                    .add(card);
        }

        for (ArrayList<Card> suited : cardsBySuit.values()) {
            if (suited.size() < 5) {
                continue;
            }

            Set<CardRank> ranks = suited.stream().map(Card::getRank).collect(Collectors.toSet());

            if (
                ranks.contains(CardRank.ACE)
                && ranks.contains(CardRank.TEN)
                && ranks.contains(CardRank.JACK)
                && ranks.contains(CardRank.QUEEN)
                && ranks.contains(CardRank.KING)) {
                return true;
            }
        }

        return false;
    }
}
