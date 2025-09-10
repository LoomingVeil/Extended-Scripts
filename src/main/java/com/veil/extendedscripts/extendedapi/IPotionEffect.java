package com.veil.extendedscripts.extendedapi;

public interface IPotionEffect {
    String getName();

    int getID();

    void setID(int ID) throws Exception;

    int getDuration();

    void setDuration(int duration);

    int getAmplifier();

    void setAmplifier(int amplifier);
}
