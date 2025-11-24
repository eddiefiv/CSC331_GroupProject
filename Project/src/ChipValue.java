/**
 * Enum to represent a chip's value
 *
 * @author Eddie Falco
 */

public enum ChipValue {
    ONE(1),
    FIVE(5),
    TEN(10),
    TWENTY_FIVE(25),
    ONE_HUNDRED(100),
    FIVE_HUNDRED(500),
    ONE_THOUSAND(1000);

    /**
     * Dummy constructor for syntactical reasons, no implementation
     * @param i chip's number value
     */
    private ChipValue(int i) {
    }

    /**
     * Derives a number value from a ChipValue
     *
     * @param chipValue the ChipValue to derive a number value from
     * @return int number value corresponding to a ChipValue
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