package org.trylma.server.game.model;

public class HexCell {
    public enum State {
        EMPTY,
        OCCUPIED
    }

    private final int x;
    private final int y;
    private State state;

    public HexCell(int x, int y, State state) {
        this.x = x;
        this.y = y;
        this.state = state;
    }

    // Getters
    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public State getState() {
        return state;
    }

    // Setters
    public void setState(State state) {
        this.state = state;
    }
}