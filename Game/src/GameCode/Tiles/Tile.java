package PaooGame.Tiles;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Tile {
    public static final int TILE_WIDTH = 48;
    public static final int TILE_HEIGHT = 48;

    public static Tile[] tiles = new Tile[256]; // Array to hold all tile types

    protected BufferedImage img;
    protected final int id;

    public Tile(BufferedImage image, int id) {
        this.img = image;
        this.id = id;
        tiles[id] = this; // Register the tile in the array
    }

    public void draw(Graphics g, int x, int y) {
        g.drawImage(img, x, y, TILE_WIDTH, TILE_HEIGHT, null);
    }

    public boolean isSolid() {
        return false;
    }

    public int getId() {
        return id;
    }
}
