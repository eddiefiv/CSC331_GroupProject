public class NPC extends Player {
    private NPCStrategy strategy;

    public NPC(String playerName, NPCStrategy strategy) {
        super(playerName); // Init Player
        setStrategy(strategy);
    }

    public NPC(String playerName, int balance, NPCStrategy strategy) {
        super(playerName, balance); // Init Player with balance
        setStrategy(strategy);
    }

    public NPCStrategy getStrategy() {
        return strategy;
    }

    public void setStrategy(NPCStrategy strategy) {
        this.strategy = strategy;
    }
}
