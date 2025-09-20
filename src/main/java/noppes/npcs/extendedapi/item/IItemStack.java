package noppes.npcs.extendedapi.item;

public interface IItemStack {
    void setUnbreakable(boolean value);

    int getNumericalId();

    /**
     * Returns the texture of the item. This method is good for items and blocks with the same texture on all faces.
     * However, if you want all the textures from a multi textured block, see
     */
    String getItemTexture();

    /**
     * Returns the textures that make up a block
     */
    String getBlockTexture(int side);

    boolean isBlock();

    int getType();
}
