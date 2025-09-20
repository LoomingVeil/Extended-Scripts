package noppes.npcs.extendedapi.overlay;

public interface IOverlayLabel {
    /**
     * Gets the width of the string in pixels. This can be useful for centering text.
     * Having certain non-standard special characters may produce inaccurate results.
     */
    int getStringWidth();
}
