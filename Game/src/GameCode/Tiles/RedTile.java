package PaooGame.Tiles;

import PaooGame.Graphics.Assets;
import java.awt.image.BufferedImage;

public class RedTile extends Tile {
    public RedTile(int id) {
        super(Assets.Tile_03, id);
    }

    @Override
    public boolean isSolid() {
        return true;
    }
}