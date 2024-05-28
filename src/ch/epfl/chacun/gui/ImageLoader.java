package ch.epfl.chacun.gui;

import javafx.scene.image.Image;

/**
 * Immutable class that creates images for the tiles and markers.
 *
 * @author Alexis Grillet-Aubert (381587)
 * @author Jakub Kliment (380660)
 */
public final class ImageLoader {

    /**
     * Pixel size of a large tile
     */
    public static final int LARGE_TILE_PIXEL_SIZE = 512;

    /**
     * Large tile fit size (with higher resolution)
     */
    public static final int LARGE_TILE_FIT_SIZE = LARGE_TILE_PIXEL_SIZE / 2;

    /**
     * Pixel size of a normal tile
     */
    public static final int NORMAL_TILE_PIXEL_SIZE = 256;

    /**
     * Normal tile fit size (with higher resolution)
     */
    public static final int NORMAL_TILE_FIT_SIZE = NORMAL_TILE_PIXEL_SIZE / 2;

    /**
     * Pixel size of the marker
     */
    public static final int MARKER_PIXEL_SIZE = 96;

    /**
     * Marker fit size (with higher resolution)
     */
    public static final int MARKER_FIT_SIZE = MARKER_PIXEL_SIZE / 2;

    /**
     * Private constructor to prevent instantiation
     */
    private ImageLoader() {}

    /**
     * Returns the normal pixel sized image with the given id.
     *
     * @param id the id of the tile
     * @return the image for the tile with the given id
     */
    public static Image normalImageForTile(int id) {
        return new Image(String.format("/%d/%02d.jpg", NORMAL_TILE_PIXEL_SIZE, id));
    }

    /**
     * Returns the large pixel sized image with the given id.
     *
     * @param id the id of the marker
     * @return the image for the marker with the given id
     */
    public static Image largeImageForTile(int id) {
        return new Image(String.format("/%d/%02d.jpg", LARGE_TILE_PIXEL_SIZE, id));
    }
}
