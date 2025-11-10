public class Driver {
    public static void main(String[] args) {
        Player player = new Player(750);
        int balance = player.calculateBalance();

        System.out.println(player.chips);
        System.out.println(balance);
    }
}
