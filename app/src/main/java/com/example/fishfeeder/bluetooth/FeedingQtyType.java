package com.example.fishfeeder.bluetooth;

import android.os.Build;

import java.util.Arrays;
import java.util.Optional;

public enum FeedingQtyType {
    LOW(0),
    MEDIUM(1),
    HIGH(2);

    private final int value;
    private FeedingQtyType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static FeedingQtyType valueOf(int idx) {
        for (FeedingQtyType l : FeedingQtyType.values()) {
            if (l.value == idx)
                return l;
        }
        throw new IllegalArgumentException();
    }
}
