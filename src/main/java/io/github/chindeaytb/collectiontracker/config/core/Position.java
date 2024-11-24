package io.github.chindeaytb.collectiontracker.config.core;

import com.google.gson.annotations.Expose;

public class Position {

    @Expose
    private int overlayX;
    @Expose
    private int overlayY;
    @Expose
    private int overlayWidth;
    @Expose
    private int overlayHeight;
    @Expose
    private float scaleX = 1.0f;
    @Expose
    private float scaleY = 1.0f;

    public Position(int x, int y, int width, int height) {
        this.overlayX = x;
        this.overlayY = y;
        this.overlayWidth = width;
        this.overlayHeight = height;
    }

    public int getX() {
        return overlayX;
    }

    public int getY() {
        return overlayY;
    }

    public int getWidth() {
        return overlayWidth;
    }

    public int getHeight() {
        return overlayHeight;
    }

    public float getScaleX() {
        return scaleX;
    }

    public float getScaleY() {
        return scaleY;
    }

    public void setPosition(int x, int y) {
        this.overlayX = x;
        this.overlayY = y;
    }

    public void setDimensions(int width, int height) {
        this.overlayWidth = width;
        this.overlayHeight = height;
    }

    public void setScaling(float scaleX, float scaleY) {
        this.scaleX = scaleX;
        this.scaleY = scaleY;
    }

}
