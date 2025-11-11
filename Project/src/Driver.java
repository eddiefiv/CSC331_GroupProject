public class Driver {
    public static void main(String[] args) {
        Player player = new Player(750);
        int balance = player.calculateBalance();

        System.out.println(player.chips);
        System.out.println(balance);

        CardRank rank = CardRank.getRankFromValue(5);

        System.out.println(rank);

        Table table = new Table(0);
        table.newDeck();

        for (Card card : table.getDeck()) {
            System.out.println(card);
        }

        table.shuffleDeck();

        for (Card card : table.getDeck()) {
            System.out.println(card);
        }

        table.joinTable(player);

        table.deal();

        System.out.println(player.getHand());
    }
}
