package PaooGame.Graphics;

import java.awt.image.BufferedImage;

/*! \class public class SpriteSheet
    \brief Clasa retine o referinta catre o imagine formata din dale (sprite sheet)

    Metoda crop() returneaza o dala de dimensiuni fixe (o subimagine) din sprite sheet
    de la adresa (x * latimeDala, y * inaltimeDala)
 */
public class SpriteSheet
{
    private BufferedImage       spriteSheet;              /*!< Referinta catre obiectul BufferedImage ce contine sprite sheet-ul.*/
    private static final int    tileWidth   = 48;   /*!< Latimea unei dale din sprite sheet.*/
    private static final int    tileHeight  = 48;   /*!< Inaltime unei dale din sprite sheet.*/

    private static final int frameWidth = 128;

    private static final int frameHeight = 128;

    /*! \fn public SpriteSheet(BufferedImage sheet)
        \brief Constructor, initializeaza spriteSheet.

        \param img Un obiect BufferedImage valid.
     */
    public SpriteSheet(BufferedImage buffImg)
    {
            /// Retine referinta catre BufferedImage object.
        spriteSheet = buffImg;
    }

    /*! \fn public BufferedImage crop(int x, int y)
        \brief Returneaza un obiect BufferedImage ce contine o subimage (dala).

        Subimaginea este localizata avand ca referinta punctul din stanga sus.

        \param x numarul dalei din sprite sheet pe axa x.
        \param y numarul dalei din sprite sheet pe axa y.
     */
    public BufferedImage crop(int x, int y,int width,int height)
    {
            /// Subimaginea (dala) este regasita in sprite sheet specificad coltul stanga sus
            /// al imaginii si apoi latimea si inaltimea (totul in pixeli). Coltul din stanga sus al imaginii
            /// se obtine inmultind numarul de ordine al dalei cu dimensiunea in pixeli a unei dale.
        return spriteSheet.getSubimage(x * tileWidth, y * tileHeight, tileWidth, tileHeight);
    }

    public BufferedImage cropMainChar(int x,int y, int width, int height)
    {
        //debug :/   //rezolvat! x primea i * frameWidth in loc de i simplu, deoarece i lua valoarea 128,128x2,128x3 si se inmultea cu frameWidth, ceea ce cauza raster out of bounds!

        /*System.out.println("x * frameWidth: " + (x * frameWidth));
        System.out.println("y * frameHeight: " + (y * frameHeight));
        System.out.println("frameWidth: " + frameWidth);
        System.out.println("frameHeight: " + frameHeight);
        System.out.println("spriteSheet width: " + spriteSheet.getWidth());
        System.out.println("spriteSheet height: " + spriteSheet.getHeight());*/



        //In aceasta functie se da crop la sprite sheet-ul care contine personajul principal
        //totul este in pixeli, sprite-sheetul find de dimensiune Ceva x 128
        return spriteSheet.getSubimage(x*frameWidth,y*frameHeight,frameWidth,frameHeight);
    }
}
