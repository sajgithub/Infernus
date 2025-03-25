package PaooGame.Tiles;

import PaooGame.Graphics.Assets;
import java.awt.image.BufferedImage;

public class HauntedTile extends Tile {
    public HauntedTile(int id) {
        super(Assets.Tile_02, id);
    }

    @Override
    public boolean isSolid() {
        return true;
    }
}