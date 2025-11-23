public class Driver {
    public static void main(String[] args) {
        Player p1 = new Player(750);

        HandEvaluationResult hand = HandEvaluator.evaluateHand(p1.getHand(), Table.board);

        System.out.println(hand);
    }
}
