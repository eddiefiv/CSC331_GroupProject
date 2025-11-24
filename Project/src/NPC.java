/**
 * Subclass representing an NPC whose actions get handled automatically by the Table and not the user, unliked ControllablePlayer
 *
 * @author Eddie Falco
 */

public class NPC extends Player {
    private NPCStrategy strategy;

    /**
     * Constructor for NPC to satisfy Player(String playerName) constructor
     *
     * @param playerName NPCs display name
     * @param strategy NPCs strategy
     */
    public NPC(String playerName, NPCStrategy strategy) {
        super(playerName); // Init Player
        setStrategy(strategy);
    }

    /**
     * Constructor for NPC to satisfy Player(String playerName, int balance) constructor
     *
     * @param playerName NPCs display name
     * @param balance NPCs starting balance
     * @param strategy NPCs strategy
     */
    public NPC(String playerName, int balance, NPCStrategy strategy) {
        super(playerName, balance); // Init Player with balance
        setStrategy(strategy);
    }

    /**
     * Gets the NPCStrategy associated with this NPC
     *
     * @return NPCStrategy
     */
    public NPCStrategy getStrategy() {
        return strategy;
    }

    /**
     * Sets the NPCStrategy for this NPC
     *
     * @param strategy the NPCStrategy to set
     */
    public void setStrategy(NPCStrategy strategy) {
        this.strategy = strategy;
    }
}
