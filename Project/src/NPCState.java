import java.util.EnumMap;

public class NPCState {
    public double handStrength; // 0.0-1.0 value. How good is NPC's hand
    public int currentBet;
    public int callAmount;
    public int potSize;
    public EnumMap<ChipValue, Integer> chips;
    public int playersRemaining;
    public boolean canCheck;
}
