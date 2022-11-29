package com.example.fishfeeder.bluetooth;

public enum FeedingFrequencyType {
    ONE_TIME(0),
    TWO_TIMES(1),
    THREE_TIMES(2);

    private final int value;
    private FeedingFrequencyType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static FeedingFrequencyType valueOf(int legIndex) {
        for (FeedingFrequencyType l : FeedingFrequencyType.values()) {
            if (l.value == legIndex)
                return l;
        }
        throw new IllegalArgumentException();
    }
}
