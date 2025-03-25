package PaooGame;
import PaooGame.GameWindow.GameWindow;
import PaooGame.Tiles.BrickTile;
import PaooGame.Tiles.PortalTile;
import PaooGame.Tiles.Tile;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.sql.SQLOutput;

import static PaooGame.Graphics.Assets.*;
import static PaooGame.Tiles.Tile.*;

public class Player {
    private int x, y; // pozitia personajului - nefolosita momentan
    public static int CameraX=0,CameraY=0;
    private int jumpHeight = 200;
    public int speed = 7;
    private int jumpSpeed = 5;
    private int jumpStartY; //pozitia de inceput a sariturii pentru a se calcula pozitia in functie de aceasta pe parcursul sariturii
    private double gravity = 3; //gravitatia la cadere
    private double initialGravity = 7;
    private double maxGravity = 6; //am vrut sa implementez gravitatia care creste pe masura ce personajul cade, dar va ajunge sa fie implementata pe parcurs
    private BufferedImage[] idleFrames; // animatii stare idle
    private BufferedImage[] runFrames; // animatii stare run
    private BufferedImage[] jumpFrames; // animatii jump
    private int numberIdleFrames = 6;
    private int numberRunFrames = 8;
    private int numberJumpFrames = 12;

    private static final int frameWidth = 128;
    private static final int frameHeight = 128;
    private int playerWidth = 64;  // Lățimea jucătorului
    private int playerHeight = 64; // Înălțimea jucătorului


    private int playerHitboxWidth = 64;  // Lățimea jucătorului
    private int playerHitboxHeight = 64; // Înălțimea jucătorului
    private int currentFrame; // frame-ul curent din animatie

    private int frameDelay; //intarziere de frame-uri pentru animatii

    private int frameDelayCounter; //contor pentru a numara intarzierea de frame-uri

    private boolean isIdle;
    private boolean isRunning;
    private boolean isJumping;
    private boolean isFalling;
    private boolean isRising;

    private int groundLevel;

    private boolean movingLeft;
    private boolean movingRight;
    private Camera camera;


    public Player(Camera camera) {
        //frame-urile pentru idle sunt 6, run 8 si jump 12


        idleFrames = new BufferedImage[numberIdleFrames];
        runFrames = new BufferedImage[numberRunFrames];
        jumpFrames = new BufferedImage[numberJumpFrames];

        for (int i = 0; i < numberIdleFrames; i++) {
            idleFrames[i] = playerIdleSpriteSheet.cropMainChar(i, 0, frameWidth, frameHeight);
        }
        for (int i = 0; i < numberRunFrames; i++) {
            runFrames[i] = playerRunSpriteSheet.cropMainChar(i, 0, frameWidth, frameHeight);
        }
        for (int i = 0; i < numberJumpFrames; i++) {
            jumpFrames[i] = playerJumpSpriteSheet.cropMainChar(i, 0, frameWidth, frameHeight);
        }
        //initializare date

        currentFrame = 0; //frame-urile incep de la 0
        frameDelay = 5;     //cu un delay de 5 frame-uri
        frameDelayCounter = 0;

        isIdle = true;      //personajul la inceput este idle, nu se spawneaza fugind sau sarind
        isRunning = false;
        isJumping = false;
        movingLeft = false;
        movingRight = false;

        GameWindow gameWindow = new GameWindow("Infernus",1920,1080);

        groundLevel = gameWindow.GetWndHeight() - frameHeight - 80;
        System.out.println("Ground level: " + groundLevel); //debug
        x = 60;
        y = groundLevel;
        gravity = initialGravity;
        this.camera = camera;

    }

    public void update(Tile[][] tiles) {
        if (tiles == null) {
            System.out.println("Tiles array is null in update method.");
            return;
        }

        // Reset movement flags
        isIdle = true;
        isRunning = false;

        if (movingLeft) {
            int newX = x - speed;
            if (!checkCollision(tiles, newX, y)) {
                x = newX;
//                System.out.println("Moving left to " + x);
                camera.setX(x - 400);
                isIdle = false;
                isRunning = true;
            } else {
//                System.out.println("Collision detected on left");
            }
        }
        if (movingRight) {
            int newX = x + speed;
            if (!checkCollision(tiles, newX, y)) {
                x = newX;
//                System.out.println("Moving right to " + x);
                camera.setX(x - 400);
                isIdle = false;
                isRunning = true;
            } else {
//                System.out.println("Collision detected on right");
            }
        }

        if (isJumping) {
            if (isRising ) {
                y -= jumpSpeed;
                if (y <= jumpStartY - jumpHeight) {
                    isRising = false;
                    isFalling = true;
                }
            } else if (isFalling && !checkCollision(tiles, x, y)) {
                y += gravity;
                if (y >= groundLevel) {
                    y = groundLevel;
                    isJumping = false;
                    isFalling = false;
                    isIdle = true;
                }
            }
            else {
                isJumping = false;
                isFalling = false;
                isIdle = true;
            }
        }
        updateAnimation();

    }
    private void updateAnimation() {
        frameDelayCounter++;
        if (frameDelayCounter >= frameDelay) {
            frameDelayCounter = 0;
            currentFrame++;
            if (isIdle) {
                currentFrame %= numberIdleFrames;
            } else if (isRunning) {
                currentFrame %= numberRunFrames;
            } else if (isJumping) {
                currentFrame %= numberJumpFrames;
            }
        }
    }

    public void render(Graphics g)
    {
        //System.out.println("Render - currentFrame: " + currentFrame); //debug pentru ca frame-urile totale depaseau frame-urile de pe ecran
        // Desenare player pe ecran
        // verificare ca frame-ul curent sa nu treaca out of bounds

        if (isIdle && currentFrame >= 0 && currentFrame < idleFrames.length) {
            g.drawImage(idleFrames[currentFrame], x, y, null);
        } else if (isRunning && currentFrame >= 0 && currentFrame < runFrames.length) {
            g.drawImage(runFrames[currentFrame], x, y, null);
        } else if (isJumping && currentFrame >= 0 && currentFrame < jumpFrames.length) {
            g.drawImage(jumpFrames[currentFrame], x, y, null);
        }
//        g.setColor(Color.GREEN);
//        g.drawRect(x+50, y+30, frameWidth-100, frameHeight-30);
//        System.out.println(x + " " + y);

    }



    private boolean checkCollision(Tile[][] tiles, int newX, int newY) {
        if (tiles == null) {
            System.out.println("Tiles array is null in checkCollision method.");
        } else {
            Rectangle playerBounds = new Rectangle(newX + 50, newY + 30, frameWidth - 100, frameHeight - 30);
            for (int row = 0; row < tiles.length; row++) {
                for (int col = 0; col < tiles[row].length; col++) {
                    Tile tile = tiles[row][col];
                    if (tile != null && tile.isSolid()) {
                        int tileX = row * Tile.TILE_WIDTH;
                        int tileY = col * Tile.TILE_HEIGHT;
                        Rectangle tileBounds = new Rectangle(tileX, tileY, Tile.TILE_WIDTH, Tile.TILE_HEIGHT);

                        // Verificăm coliziunile pe ambele axe (Ox și Oy)
                        if (playerBounds.intersects(tileBounds) && tile instanceof PortalTile) {
//                            System.out.println("Collision detected with tile at (" + col + ", " + row + ")");
                            ((PortalTile) tile).onCollision();
                            return true;
                        }
                        else if (playerBounds.intersects(tileBounds))
                        {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }
    public void moveLeft() {
        movingLeft = true;
        movingRight = false;
        isIdle = false;
        isRunning = true;
    }

    public void moveRight() {
        movingLeft = false;
        movingRight = true;
        isIdle = false;
        isRunning = true;
    }

    public void stopRunning() {
        movingLeft = false;
        movingRight = false;
        isRunning = false;
        isIdle = true;
    }

    public void jump() {
        if (!isJumping) {
//            System.out.println("Jump initiated"); // Mesaj de debug
            isIdle = false;
            isRunning = false;
            isJumping = true;
            isRising = true;
            jumpStartY = y;
        }
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public void setCoordinates(float x, float y) {
        this.x = (int) x;
        this.y = groundLevel;
    }



}
