/**
 * Dataclass holding all information needed for a NPCStrategy to make its inference, populated by the Table each time the NPC moves
 */

import java.util.EnumMap;

public class NPCState {
    public HandEvaluationResult handStrength; // NPCs hand strength (better hands = higher strength)
    public int currentBet; // NPCs current bet
    public int callAmount; // Amount the NPC would need to call during turn
    public int potSize; // Size of the pot
    public int playersRemaining; // Active (un-folded) players left in the game
    public boolean canCheck; // Can the NPC check
    public int chips; // chips used for balance
}
