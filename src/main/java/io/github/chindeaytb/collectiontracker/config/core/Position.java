package io.github.chindeaytb.collectiontracker.config.core;

import com.google.gson.annotations.Expose;

public class Position {

    @Expose
    private int overlayX;
    @Expose
    private int overlayY;

    public Position(int x, int y) {
        this.overlayX = x;
        this.overlayY = y;
    }

    public int getX() {
        return overlayX;
    }

    public int getY() {
        return overlayY;
    }

    public void set(Position other) {
        this.overlayX = other.overlayX;
        this.overlayY = other.overlayY;
    }

    public void set(int x, int y) {
        this.overlayX = x;
        this.overlayY = y;
    }
}
