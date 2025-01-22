package org.trylma.common.enums;

public enum PlayerCount {
    TWO(2),
    THREE(3),
    FOUR(4),
    SIX(6);

    private final int count;

    PlayerCount(int count) {
        this.count = count;
    }

    public int getCount() {
        return count;
    }
}