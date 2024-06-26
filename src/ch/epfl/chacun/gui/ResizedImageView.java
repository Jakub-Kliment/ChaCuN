package ch.epfl.chacun.gui;

import javafx.scene.image.ImageView;

/**
 * A class decorator that decorates the java class ImageView
 * by resizing to a square of a given size because most of the
 * images in the game are of square shape.
 */
public final class ResizedImageView extends ImageView {

    /**
     * Constructs a new resized ImageView with the given size.
     *
     * @param size the size of the square (height and width)
     */
    public ResizedImageView(int size) {
        fitHeightProperty().set(size);
        fitWidthProperty().set(size);
    }
}
