public class NormalNPCStrategy implements NPCStrategy {
    @Override
    public PlayerAction inference(NPCState state) {
        // If npc can check and hand is weak, check
        if (state.canCheck && HandType.getStrengthValueFromHandType(state.handStrength.getHandType()) < HandType.getStrengthValueFromHandType(HandType.ONE_PAIR)) {
            return PlayerAction.CHECK;
        }

        // If a call will be too costly, fold if hand is weak
        double potOdds = (double)state.callAmount / (state.potSize + state.callAmount);

        if (HandType.getStrengthValueFromHandType(state.handStrength.getHandType()) < potOdds) {
            return PlayerAction.FOLD;
        }

        // Randomly raise on a bad hand
        if (HandType.getStrengthValueFromHandType(state.handStrength.getHandType()) > HandType.getStrengthValueFromHandType(HandType.ONE_PAIR) && Math.random() < 0.2) {
            return PlayerAction.RAISE;
        }

        // Randomly raise on a good hand
        if (HandType.getStrengthValueFromHandType(state.handStrength.getHandType()) > HandType.getStrengthValueFromHandType(HandType.THREE_OF_KIND) && Math.random() < 0.5) {
            return PlayerAction.RAISE;
        }

        // Randomly raise on a great hand
        if (HandType.getStrengthValueFromHandType(state.handStrength.getHandType()) > HandType.getStrengthValueFromHandType(HandType.FLUSH) && Math.random() < 0.9) {
            return PlayerAction.RAISE;
        }

        // Otherwise call in all other scenarios
        return PlayerAction.CALL;
    }
}
