/**
* @author James Frink, Eddie Falco
* CSC 331 Section 001- 002- 003
* Date: 11/23/2025
* Purpose: Create the builder for Non Player Characters (NPCs)
*/
public class NPC extends Player {
    private NPCStrategy strategy;
    /**
    * NPC Constructor
    * @param playerName player's name (String)
    * @param strategy setter for the NPC's playstile (NPCStrategy)
    */
    public NPC(String playerName, NPCStrategy strategy) {
        super(playerName); // Init Player
        setStrategy(strategy);
    }
    /**
    * NPC Constructor
    * @param playerName player's name (String)
    * @param balance the amount of money that the player starts with (int)
    * @param strategy setter for the NPC's playstile (NPCStrategy)
    */
    public NPC(String playerName, int balance, NPCStrategy strategy) {
        super(playerName, balance); // Init Player with balance
        setStrategy(strategy);
    }
    /**
    * Function to get the NPC's strategy
    * @return strategy
    */
    public NPCStrategy getStrategy() {
        return strategy;
    }
    /**
    * Function to set the NPC's strategy
    * @param strategy setter for the NPC's playstile (NPCStrategy)
    */
    public void setStrategy(NPCStrategy strategy) {
        this.strategy = strategy;
    }
}
