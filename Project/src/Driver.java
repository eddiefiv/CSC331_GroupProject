public class Driver {
    public static void main(String[] args) {
        ControllablePlayer p1 = new ControllablePlayer("Player 1", 750);
        ControllablePlayer p2 = new ControllablePlayer("Player 2", 500);

        Table.tableInit(5);
        Table.joinTable(p1);
        Table.joinTable(p2);

        System.out.println(p1.getHand());

        Table.gameplayLoop();

        System.out.println(p1.getHand());
        System.out.println(p2.getHand());
    }
}
