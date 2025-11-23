/**
* @author Emma Fox, Eddie Falco
* CSC 331 Section 001 - 002 - 003
* Date: 11/23/2025
* Purpose: Establish the values of the chips
*/
/**
* Sets the values of all chip types
*/
public enum ChipValue {
    ONE(1),
    FIVE(5),
    TEN(10),
    TWENTY_FIVE(25),
    ONE_HUNDRED(100),
    FIVE_HUNDRED(500),
    ONE_THOUSAND(1000);

    ChipValue(int i) {
        // TODO Auto-generated constructor stub
    }
    /**
    * Function to return the values of certain chips
    * @param chipValue value of a given chip (ChipValue)
    */
    public static int getChipNumberValue(ChipValue chipValue) {
        return switch (chipValue) {
            case ONE -> 1;
            case FIVE -> 5;
            case TEN -> 10;
            case TWENTY_FIVE -> 25;
            case ONE_HUNDRED -> 100;
            case FIVE_HUNDRED -> 500;
            case ONE_THOUSAND -> 1000;
            default -> 0;
        };
    }
}
