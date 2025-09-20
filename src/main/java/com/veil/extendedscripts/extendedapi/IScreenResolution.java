package com.veil.extendedscripts.extendedapi;

public interface IScreenResolution {
    int getWidth();

    int getHeight();

    float getScale();

    int getWidthPercent(double percent);

    int getHeightPercent(double percent);
}
