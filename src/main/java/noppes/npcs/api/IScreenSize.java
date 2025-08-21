package noppes.npcs.api;

/**
 * This class, as the name suggests, helps you get the user's screen size.
 * Keep in mind this is just how big their game window is and does not change based on gui scale.
 */
public interface IScreenSize {

    public int getWidth();

    public int getHeight();

    public int getWidthPercent(double percent);

    public int getHeightPercent(double percent);
}
