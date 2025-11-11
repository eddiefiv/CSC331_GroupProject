public class Driver {
    public static void main(String[] args) {
        Player p1 = new Player(750);

        Table table = new Table(0);
        table.joinTable(p1);

        p1.addCardToHand(new Card(CardSuit.HEART, CardRank.ACE));
        p1.addCardToHand(new Card(CardSuit.HEART, CardRank.QUEEN));

        table.board.add(new Card(CardSuit.DIAMOND, CardRank.EIGHT));
        table.board.add(new Card(CardSuit.HEART, CardRank.KING));
        table.board.add(new Card(CardSuit.HEART, CardRank.TEN));
        table.board.add(new Card(CardSuit.CLUB, CardRank.ACE));
        table.board.add(new Card(CardSuit.HEART, CardRank.JACK));

        HandType hand = HandEvaluator.evaluateHand(p1.getHand(), table.board);

        System.out.println(hand);
    }
}
