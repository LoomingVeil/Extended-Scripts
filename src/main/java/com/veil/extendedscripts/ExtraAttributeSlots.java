package com.veil.extendedscripts;

import noppes.npcs.api.INbt;
import noppes.npcs.api.entity.IPlayer;

import java.util.ArrayList;

public class ExtraAttributeSlots {
    private static ArrayList<String[]> slotPaths = new ArrayList<>();
    private static ArrayList<INbt> itemLocations = new ArrayList<>();

    public static void addExtraSlot(String[] path) {
        slotPaths.add(path);
    }

    public static void validatePaths(IPlayer player) {
        INbt playerNbt = player.getAllNbt();
        for (String[] path : slotPaths) {
            Object result = ExtendedAPI.traverseNbt(playerNbt, path);
            if (result == null) {

            } else if (result instanceof INbt) {

            } else {

            }
        }
    }
}
