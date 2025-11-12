public class Driver {
    public static void main(String[] args) {
        Player p1 = new Player(750);

        Table table = new Table(0);
        table.joinTable(p1);

        p1.addCardToHand(new Card(CardSuit.HEART, CardRank.NINE));
        p1.addCardToHand(new Card(CardSuit.SPADE, CardRank.TEN));

        table.board.add(new Card(CardSuit.SPADE, CardRank.NINE));
        table.board.add(new Card(CardSuit.DIAMOND, CardRank.FOUR));
        table.board.add(new Card(CardSuit.DIAMOND, CardRank.NINE));
        table.board.add(new Card(CardSuit.CLUB, CardRank.SIX));
        table.board.add(new Card(CardSuit.HEART, CardRank.KING));

        HandType hand = HandEvaluator.evaluateHand(p1.getHand(), table.board);

        System.out.println(hand);
    }
}
