package PaooGame.Tiles;

import PaooGame.Graphics.Assets;
import java.awt.image.BufferedImage;

public class BrickTile extends Tile {
    public BrickTile(int id) {
        super(Assets.Brick_01, id);
    }

    @Override
    public boolean isSolid() {
        return true;
    }
}