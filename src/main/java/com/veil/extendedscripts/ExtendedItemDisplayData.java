package com.veil.extendedscripts;

import noppes.npcs.controllers.data.ItemDisplayData;

public class ExtendedItemDisplayData extends ItemDisplayData {
    public ExtendedItemDisplayData(String defaultTexture, Integer defaultColor) {
        this.texture = defaultTexture;
        this.itemColor = defaultColor;
    }

    public ExtendedItemDisplayData(String defaultTexture) {
        this.texture = defaultTexture;
        this.itemColor = ExtendedScripts.SIGNATURE_COLOR;
    }

    public ExtendedItemDisplayData() { }
}
