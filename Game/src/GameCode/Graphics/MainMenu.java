package PaooGame.Graphics;

import PaooGame.Game;
import PaooGame.Player;
import PaooGame.Save;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class MainMenu implements MouseListener {
    private Game game;

    public MainMenu(Game game) {
        this.game = game;
    }

    public static Rectangle playButton = new Rectangle(1200 / 2 + 120, 150, 100, 50);
    public static Rectangle helpButton = new Rectangle(1200 / 2 + 120, 250, 100, 50);
    public static Rectangle exitButton = new Rectangle(1200 / 2 + 120, 350, 100, 50);

    public static Rectangle level1Button = new Rectangle(1200 / 2 + 90, 150, 125, 50);
    public static Rectangle level2Button = new Rectangle(1200 / 2 + 90, 250, 125, 50);
    public static Rectangle level3Button = new Rectangle(1200 / 2 + 90, 350, 125, 50);

    public static void render(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        Font fnt0 = new Font("arial", Font.BOLD, 50);
        g.setFont(fnt0);
        g.setColor(Color.white);
        Font fnt1 = new Font("arial", Font.BOLD, 30);
        if (Game.state == Game.STATE.MENU) {
            g.drawString("Infernus", 1350 / 2, 100);
            g.setFont(fnt1);
            g.drawString("Play", playButton.x + 19, playButton.y + 32);
            g.drawString("Help", helpButton.x + 19, helpButton.y + 32);
            g.drawString("Exit", exitButton.x + 19, exitButton.y + 32);
            g2d.draw(playButton);
            g2d.draw(helpButton);
            g2d.draw(exitButton);
        } else if (Game.state == Game.STATE.LEVEL_SELECTION) {
            g.drawString("Select Level", 1200 / 2, 100);
            g.setFont(fnt1);
            g.drawString("Level 1", level1Button.x + 19, level1Button.y + 32);
            g.drawString("Level 2", level2Button.x + 19, level2Button.y + 32);
            g.drawString("Level 3", level3Button.x + 19, level3Button.y + 32);
            g2d.draw(level1Button);
            g2d.draw(level2Button);
            g2d.draw(level3Button);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        int mx = e.getX();
        int my = e.getY();
        int saveChecker = Save.converterData();

        if (Game.state == Game.STATE.MENU) {
            if (playButton.contains(mx, my)) {
                Game.state = Game.STATE.LEVEL_SELECTION;
            } else if (helpButton.contains(mx, my)) {
                System.out.println("Help was not yet implemented");
            } else if (exitButton.contains(mx, my)) {
                game.StopGame(); // Close the game when Exit button is clicked
                System.out.println("Game was closed!");
            }
        } else if (Game.state == Game.STATE.LEVEL_SELECTION) {
            if (level1Button.contains(mx, my)) {
                game.setCurrentLevel(1);
                Game.state = Game.STATE.GAME;
                game.getPlayer().setCoordinates(60,872);
            } else if (level2Button.contains(mx, my) && (saveChecker == 2 || saveChecker == 3)) {
                game.setCurrentLevel(2);
                Game.state = Game.STATE.GAME;
                game.getPlayer().setCoordinates(60,872);
            } else if (level3Button.contains(mx, my) && saveChecker == 3) {
                game.setCurrentLevel(3);
                Game.state = Game.STATE.GAME;
                game.getPlayer().setCoordinates(60,872);
            }
            else{
                System.out.println("Level is locked! Finish previous level(s)!");
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }
}
