package ch.epfl.chacun.gui;

import javafx.scene.image.Image;

/**
 * Immutable class that creates images for the tiles and markers.
 *
 * @author Alexis Grillet-Aubert (381587)
 * @author Jakub Kliment (380660)
 */
public class ImageLoader {

    /**
     * Large tile pixel size
     */
    public static final int LARGE_TILE_PIXEL_SIZE = 512;

    /**
     * Large tile fit size
     */
    public static final int LARGE_TILE_FIT_SIZE = 256;

    /**
     * Normal tile pixel size
     */
    public static final int NORMAL_TILE_PIXEL_SIZE = 256;

    /**
     * Normal tile fit size
     */
    public static final int NORMAL_TILE_FIT_SIZE = 128;

    /**
     * Marker pixel size
     */
    public static final int MARKER_PIXEL_SIZE = 96;

    /**
     * Marker fit size
     */
    public static final int MARKER_FIT_SIZE = 48;

    /**
     * Private constructor to prevent instantiation
     */
    private ImageLoader() {}

    /**
     * Returns the normal size image for the tile with the given id.
     *
     * @param id the id of the tile
     * @return the image for the tile with the given id
     */
    public static Image normalImageForTile(int id) {
        return new Image(STR."/256/\{id<10 ? STR."0\{id}" : id}.jpg");
    }

    /**
     * Returns large the image for the marker with the given id.
     *
     * @param id the id of the marker
     * @return the image for the marker with the given id
     */
    public static Image largeImageForTile(int id) {
        return new Image(STR."/512/\{id<10 ? STR."0\{id}" : id}.jpg");
    }
}
