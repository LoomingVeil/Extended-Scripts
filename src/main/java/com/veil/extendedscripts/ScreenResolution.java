package com.veil.extendedscripts;

import com.veil.extendedscripts.extendedapi.IScreenResolution;

public class ScreenResolution implements IScreenResolution {
    private int width = -1;
    private int height = -1;
    private int scale = -1;

    public void setSize(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public void setScale(int scale) {
        this.scale = scale;
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public float getScale() {
        return scale;
    }

    @Override
    public int getWidthPercent(double percent) {
        return (int) percent * width;
    }

    @Override
    public int getHeightPercent(double percent) {
        return (int) percent * height;
    }
}
