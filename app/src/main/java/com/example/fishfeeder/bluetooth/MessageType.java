package com.example.fishfeeder.bluetooth;

public enum MessageType {
    POST(0),
    GET(1),
    SYNC_TIME(2);

    private final int value;
    private MessageType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
