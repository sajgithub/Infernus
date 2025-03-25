package PaooGame.Graphics;

import PaooGame.Game;

import java.awt.image.BufferedImage;

/*! \class public class Assets
    \brief Clasa incarca fiecare element grafic necesar jocului.

    Game assets include tot ce este folosit intr-un joc: imagini, sunete, harti etc.
 */
public class Assets {
        /// Referinte catre elementele grafice (dale) utilizate in joc.

    public static BufferedImage Brick_01;
    public static BufferedImage Tile_02;
    public static BufferedImage Tile_03;

    public static BufferedImage portalTile;
    public static BufferedImage background1;
    public static BufferedImage background2;
    public static BufferedImage background3;
    public static BufferedImage mainMenuBackground;

    public static SpriteSheet playerIdleSpriteSheet;
    public static SpriteSheet playerRunSpriteSheet;
    public static SpriteSheet playerJumpSpriteSheet;

    /*! \fn public static void Init()
        \brief Functia initializaza referintele catre elementele grafice utilizate.

        Aceasta functie poate fi rescrisa astfel incat elementele grafice incarcate/utilizate
        sa fie parametrizate. Din acest motiv referintele nu sunt finale.
     */
    public static void Init()
    {
            /// Se creaza temporar un obiect SpriteSheet initializat prin intermediul clasei ImageLoader
        //SpriteSheet sheet = new SpriteSheet(ImageLoader.LoadImage("/textures/PaooGameSpriteSheet.png"));

            /// Se obtin subimaginile corespunzatoare elementelor necesare.
        background1 = ImageLoader.LoadImage("/textures/bkgd1.png");
        background2 = ImageLoader.LoadImage("/textures/bkgd2.png");
        background3 = ImageLoader.LoadImage("/textures/bkgd3.png");
        mainMenuBackground = ImageLoader.LoadImage("/textures/mainmenubkgd.jpg");
        playerIdleSpriteSheet = new SpriteSheet(ImageLoader.LoadImage("/textures/Idle.png"));
        playerRunSpriteSheet = new SpriteSheet(ImageLoader.LoadImage("/textures/Run.png"));
        playerJumpSpriteSheet = new SpriteSheet(ImageLoader.LoadImage("/textures/Jump.png"));
        Brick_01 = ImageLoader.LoadImage("/textures/Brick_01.png");
        Tile_02 = ImageLoader.LoadImage("/textures/Tile_02.png");
        Tile_03 = ImageLoader.LoadImage("/textures/Tile_03.png");
        portalTile = ImageLoader.LoadImage("/textures/PortalTile.png");


    }
}
