package PaooGame;
import java.awt.*;

public class Camera {
    private float x, y;

    public Camera(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public void tick(Player player) {
        this.x = player.getX() - 1920 / 2 + 250;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }
}