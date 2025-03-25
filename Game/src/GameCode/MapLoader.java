package PaooGame;

import PaooGame.Tiles.*;

import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class MapLoader {
    private String path;
    private Tile[][] tiles;

    public MapLoader(String path) {
        this.path = path;
        loadMap();
    }

    private void loadMap() {
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            // citire dimensiuni harta
            String line = br.readLine();
            String[] dimensions = line.split(" ");
            int width = Integer.parseInt(dimensions[0]);
            int height = Integer.parseInt(dimensions[1]);

            // initializare tile-uri
            tiles = new Tile[width][height];

            // citire tile-uri din fisier
            for (int y = 0; y < height; y++) {
                line = br.readLine();
                String[] tokens = line.split(" ");
                for (int x = 0; x < width; x++) {
                    int tileId = Integer.parseInt(tokens[x]);
                    tiles[x][y] = createTile(tileId);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Tile createTile(int tileId) {
        switch (tileId) {
            case 1:
                return new BrickTile(tileId);
            case 2:
                return new HauntedTile(tileId);
            case 3:
                return new RedTile(tileId);
            case 4:
                return new PortalTile(tileId);
            default:
                return null;
        }
    }

    public Tile[][] getTiles() {
        return tiles;
    }

    public void drawMap(Graphics g) {
        for (int y = 0; y < tiles[0].length; y++) {
            for (int x = 0; x < tiles.length; x++) {
                if (tiles[x][y] != null) {
                    tiles[x][y].draw(g, x * Tile.TILE_WIDTH, y * Tile.TILE_HEIGHT);
//                    g.setColor(Color.RED);
//                   g.drawRect(x * Tile.TILE_WIDTH, y * Tile.TILE_HEIGHT, Tile.TILE_WIDTH, Tile.TILE_HEIGHT);
                }
            }
        }
    }
}
