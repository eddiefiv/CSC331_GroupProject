public class Driver {
    public static void main(String[] args) {
        Player p1 = new Player(750);

        p1.addCardToHand(new Card(CardSuit.HEART, CardRank.TEN));
        p1.addCardToHand(new Card(CardSuit.DIAMOND, CardRank.JACK));

        Table.board.add(new Card(CardSuit.SPADE, CardRank.NINE));
        Table.board.add(new Card(CardSuit.SPADE, CardRank.TWO));
        Table.board.add(new Card(CardSuit.HEART, CardRank.ACE));
        Table.board.add(new Card(CardSuit.SPADE, CardRank.FOUR));
        Table.board.add(new Card(CardSuit.CLUB, CardRank.THREE));

        HandEvaluationResult hand = HandEvaluator.evaluateHand(p1.getHand(), Table.board);

        System.out.println(hand);
    }
}
