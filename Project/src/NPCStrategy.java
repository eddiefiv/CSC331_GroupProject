/**
 * Base interface to represent an NPCs move strategy
 *
 * @author Eddie Falco
 */

public interface NPCStrategy {
    /**
     * Determines the best move for the NPC
     *
     * @param state NPCState to go based off of
     * @return PlayerAction the action to take by the NPC
     */
    PlayerAction inference(NPCState state);
}
