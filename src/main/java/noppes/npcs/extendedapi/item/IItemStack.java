package noppes.npcs.extendedapi.item;

public interface IItemStack {
    void setUnbreakable(boolean value);

    int getNumericalId();

    String getItemTexture();

    String getBlockTexture(int side);

    boolean isBlock();
}
