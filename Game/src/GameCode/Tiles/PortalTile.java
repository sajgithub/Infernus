package PaooGame.Tiles;

import PaooGame.Graphics.Assets;
import PaooGame.Player;
import PaooGame.Game;
import PaooGame.Save;

import java.awt.*;
import java.awt.image.BufferedImage;


public class PortalTile extends Tile {


    public PortalTile(int id) {
        super(Assets.portalTile, id);
    }

    @Override
    public boolean isSolid() {
        return true; // Portal tile is not solid
    }

    public void onCollision() {
        if(Game.currentLevel == 1)
        {
            Save.insertData(2,0,0,0,0);
        }
        else if(Game.currentLevel == 2)
        {
            Save.insertData(3,0,0,0,0);
        }
        Game.state = Game.STATE.MENU;

    }
}
