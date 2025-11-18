public class Driver {
    public static void main(String[] args) {
        Player p1 = new Player(750);

        p1.addCardToHand(new Card(CardSuit.HEART, CardRank.TEN));
        p1.addCardToHand(new Card(CardSuit.HEART, CardRank.EIGHT));

        Table.board.add(new Card(CardSuit.HEART, CardRank.NINE));
        Table.board.add(new Card(CardSuit.SPADE, CardRank.QUEEN));
        Table.board.add(new Card(CardSuit.SPADE, CardRank.JACK));
        Table.board.add(new Card(CardSuit.HEART, CardRank.SIX));
        Table.board.add(new Card(CardSuit.HEART, CardRank.SEVEN));

        HandEvaluationResult hand = HandEvaluator.evaluateHand(p1.getHand(), Table.board);

        System.out.println(hand);
    }
}
