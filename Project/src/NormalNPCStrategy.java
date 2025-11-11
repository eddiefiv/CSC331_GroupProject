public class NormalNPCStrategy implements NPCStrategy {
    @Override
    public PlayerAction inference(NPCState state) {
        // If npc can check and hand is weak, check
        if (state.canCheck && state.handStrength < 0.2) {
            return PlayerAction.CHECK;
        }

        // If a call will be too costly, fold if hand is weak
        double potOdds = (double)state.callAmount / (state.potSize + state.callAmount);

        if (state.handStrength < potOdds) {
            return PlayerAction.FOLD;
        }
        // Randomly raise on a good hand
        if (state.handStrength > 0.75 && Math.random() < 0.3) {
            return PlayerAction.FOLD;
        }

        // Otherwise call in all other scenarios
        return PlayerAction.CALL;
    }
}
