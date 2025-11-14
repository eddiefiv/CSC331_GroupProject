public class Driver {
    public static void main(String[] args) {
        Player p1 = new Player(750);

        p1.addCardToHand(new Card(CardSuit.HEART, CardRank.NINE));
        p1.addCardToHand(new Card(CardSuit.SPADE, CardRank.TEN));

        Table.board.add(new Card(CardSuit.SPADE, CardRank.NINE));
        Table.board.add(new Card(CardSuit.DIAMOND, CardRank.FOUR));
        Table.board.add(new Card(CardSuit.DIAMOND, CardRank.NINE));
        Table.board.add(new Card(CardSuit.CLUB, CardRank.SIX));
        Table.board.add(new Card(CardSuit.HEART, CardRank.KING));

        HandType hand = HandEvaluator.evaluateHand(p1.getHand(), Table.board);

        System.out.println(hand);
    }
}
