import java.util.EnumMap;

public class NPCState {
    public HandEvaluationResult handStrength;
    public int currentBet;
    public int callAmount;
    public int potSize;
    public EnumMap<ChipValue, Integer> chips;
    public int playersRemaining;
    public boolean canCheck;
}
