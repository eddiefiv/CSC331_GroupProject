import java.util.EnumMap;
/**
* @author James Frink, Eddie Falco
* CSC 331 Section 001 - 002 - 003
* Date: 11/23/2025
* Purpose: Establish the current state of an NPC
*/
public class NPCState {
    public double handStrength; // 0.0-1.0 value. How good is NPC's hand
    public int currentBet;
    public int callAmount;
    public int potSize;
    public EnumMap<ChipValue, Integer> chips;
    public int playersRemaining;
    public boolean canCheck;
}
