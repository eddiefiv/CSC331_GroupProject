public enum HandType {
    HIGH_CARD(1),
    ONE_PAIR(2),
    TWO_PAIR(3),
    THREE_OF_KIND(4),
    STRAIGHT(5),
    FLUSH(6),
    FULL_HOUSE(7),
    FOUR_OF_A_KIND(8),
    STRAIGHT_FLUSH(9),
    ROYAL_FLUSH(10);

    HandType(int strength) {}

    public static int getStrengthValueFromHandType(HandType hand) {
        return switch (hand) {
            case HIGH_CARD -> 1;
            case ONE_PAIR -> 2;
            case TWO_PAIR -> 3;
            case THREE_OF_KIND -> 4;
            case STRAIGHT -> 5;
            case FLUSH -> 6;
            case FULL_HOUSE -> 7;
            case FOUR_OF_A_KIND -> 8;
            case STRAIGHT_FLUSH -> 9;
            case ROYAL_FLUSH -> 10;
            default -> throw new IllegalStateException("Unexpected value: " + hand);
        };
    }

    public static HandType getHandTypeFromStrengthValue(int strength) {
        return switch (strength) {
            case 1 -> HIGH_CARD;
            case 2 -> ONE_PAIR;
            case 3 -> TWO_PAIR;
            case 4 -> THREE_OF_KIND;
            case 5 -> STRAIGHT;
            case 6 -> FLUSH;
            case 7 -> FULL_HOUSE;
            case 8 -> FOUR_OF_A_KIND;
            case 9 -> STRAIGHT_FLUSH;
            case 10 -> ROYAL_FLUSH;
            default -> throw new IllegalStateException("Unexpected value: " + strength);
        };
    }
}
