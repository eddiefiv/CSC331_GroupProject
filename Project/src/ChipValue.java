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