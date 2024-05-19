package ch.epfl.chacun.gui;

import javafx.scene.image.ImageView;

public class ResizedImageView extends ImageView {

    public ResizedImageView(int size) {
        fitHeightProperty().set(size);
        fitWidthProperty().set(size);
    }
}
