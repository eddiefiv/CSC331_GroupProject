/**
 * Test driver class, not the main driver for FXML, though
 *
 * @author Eddie Falco
 */

public class Driver {
    public static void main(String[] args) {

        // Create a controllable player (you)
        ControllablePlayer you = new ControllablePlayer("You");

        // Join table
        Table.joinTable(you);

        // Add 3 NPCs
        Table.tableInit(3);  // Adds NPC0, NPC1, NPC2

        // Run 2 sample hands
        System.out.println("=== Starting poker simulation ===");
        Table.gameplayLoop();

        System.out.println("=== Simulation complete ===");
    }
}
