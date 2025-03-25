package PaooGame;

import PaooGame.GameWindow.GameWindow;
import PaooGame.Graphics.Assets;
import PaooGame.Graphics.MainMenu;
import PaooGame.Graphics.SpriteSheet;
import PaooGame.Tiles.BrickTile;
import PaooGame.Tiles.HauntedTile;
import PaooGame.Tiles.RedTile;
import PaooGame.Tiles.Tile;
import PaooGame.Camera;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

import static PaooGame.Graphics.Assets.*;
import static PaooGame.Graphics.ImageLoader.LoadImage;
import static PaooGame.Tiles.Tile.TILE_HEIGHT;
import static PaooGame.Tiles.Tile.TILE_WIDTH;

/*! \class Game
    \brief Clasa principala a intregului proiect. Implementeaza Game - Loop (Update -> Draw)

                ------------
                |           |
                |     ------------
    60 times/s  |     |  Update  |  -->{ actualizeaza variabile, stari, pozitii ale elementelor grafice etc.
        =       |     ------------
     16.7 ms    |           |
                |     ------------
                |     |   Draw   |  -->{ deseneaza totul pe ecran
                |     ------------
                |           |
                -------------
    Implementeaza interfata Runnable:

        public interface Runnable {
            public void run();
        }

    Interfata este utilizata pentru a crea un nou fir de executie avand ca argument clasa Game.
    Clasa Game trebuie sa aiba definita metoda "public void run()", metoda ce va fi apelata
    in noul thread(fir de executie). Mai multe explicatii veti primi la curs.

    In mod obisnuit aceasta clasa trebuie sa contina urmatoarele:
        - public Game();            //constructor
        - private void init();      //metoda privata de initializare
        - private void update();    //metoda privata de actualizare a elementelor jocului
        - private void draw();      //metoda privata de desenare a tablei de joc
        - public run();             //metoda publica ce va fi apelata de noul fir de executie
        - public synchronized void start(); //metoda publica de pornire a jocului
        - public synchronized void stop()   //metoda publica de oprire a jocului
 */
public class Game implements Runnable, KeyListener
{
    private Camera camera;
    private Save save;
    private MapLoader mapLoader;
    private int gameWidth = 1920;
    private int gameHeight = 1080;
    private int viewWidth = 800;
    private int viewHeight = 600;

    public enum STATE{
        MENU,
        GAME,
        LEVEL_SELECTION
    };
    public static STATE state = STATE.MENU;
    private Tile[][] tilesLvl1;
    private Tile[][] tilesLvl2;
    private Tile[][] tilesLvl3;

    private Tile[][] portalTile;
    private GameWindow      wnd;        /*!< Fereastra in care se va desena tabla jocului*/
    private static Player player;
    private boolean         runState;   /*!< Flag ce starea firului de executie.*/
    private Thread          gameThread; /*!< Referinta catre thread-ul de update si draw al ferestrei*/
    private BufferStrategy  bs;         /*!< Referinta catre un mecanism cu care se organizeaza memoria complexa pentru un canvas.*/
    /// Sunt cateva tipuri de "complex buffer strategies", scopul fiind acela de a elimina fenomenul de
    /// flickering (palpaire) a ferestrei.
    /// Modul in care va fi implementata aceasta strategie in cadrul proiectului curent va fi triplu buffer-at

    ///                         |------------------------------------------------>|
    ///                         |                                                 |
    ///                 ****************          *****************        ***************
    ///                 *              *   Show   *               *        *             *
    /// [ Ecran ] <---- * Front Buffer *  <------ * Middle Buffer * <----- * Back Buffer * <---- Draw()
    ///                 *              *          *               *        *             *
    ///                 ****************          *****************        ***************

    private Graphics        g;          /*!< Referinta catre un context grafic.*/

    public static int currentLevel = 1;
    private Tile tile; /*!< variabila membra temporara. Este folosita in aceasta etapa doar pentru a desena ceva pe ecran.*/

    /*! \fn public Game(String title, int width, int height)
        \brief Constructor de initializare al clasei Game.

        Acest constructor primeste ca parametri titlul ferestrei, latimea si inaltimea
        acesteia avand in vedere ca fereastra va fi construita/creata in cadrul clasei Game.

        \param title Titlul ferestrei.
        \param width Latimea ferestrei in pixeli.
        \param height Inaltimea ferestrei in pixeli.
     */

    public void loadLevel(int level) {
        switch (level) {
            case 1:
                mapLoader = new MapLoader("src/Levels/level1.txt");
                tilesLvl1 = mapLoader.getTiles();
                portalTile = mapLoader.getTiles();
                break;
            case 2:
                mapLoader = new MapLoader("src/Levels/level2.txt");
                tilesLvl2 = mapLoader.getTiles();
                portalTile = mapLoader.getTiles();
                break;
            case 3:
                mapLoader = new MapLoader("src/Levels/level3.txt");
                tilesLvl3 = mapLoader.getTiles();
                portalTile = mapLoader.getTiles();
                break;
            default:
                throw new IllegalArgumentException("Invalid level: " + currentLevel);
        }
    }

    public void setCurrentLevel(int level)
    {
        this.currentLevel = level;
        loadLevel(level);
    }

    private Tile[][] getCurrentLevelTiles() {
        switch (currentLevel) {
            case 1:
                return tilesLvl1;
            case 2:
                return tilesLvl2;
            case 3:
                return tilesLvl3;
            case 4:
                return portalTile;
            default:
                return null;
        }
    }

    public Game(String title, int width, int height)
    {
            /// Obiectul GameWindow este creat insa fereastra nu este construita
            /// Acest lucru va fi realizat in metoda init() prin apelul
            /// functiei BuildGameWindow();
        wnd = new GameWindow(title, width, height);
            /// Resetarea flagului runState ce indica starea firului de executie (started/stoped)
        runState = false;

    }

    /*! \fn private void init()
        \brief  Metoda construieste fereastra jocului, initializeaza aseturile, listenerul de tastatura etc.

        Fereastra jocului va fi construita prin apelul functiei BuildGameWindow();
        Sunt construite elementele grafice (assets): dale, player, elemente active si pasive.

     */
    private void InitGame()
    {
        wnd = new GameWindow("Infernus", 1920, 1080);
            /// Este construita fereastra grafica.
        wnd.BuildGameWindow();
            /// Se incarca toate elementele grafice (dale)
        Assets.Init();
        save = new Save();
        save.database();
        camera = new Camera(0,0);
        player = new Player(camera);

        //adaugam un keylistener
        wnd.GetCanvas().addKeyListener(this);
        wnd.GetCanvas().addMouseListener(new MainMenu(this));

        /*for (int i = 0; i < tiles.length; i++) {
            tiles[i] = new BrickTile(i); // adaugă dala solidă în array
        }*/

    }

    /*! \fn public void run()
        \brief Functia ce va rula in thread-ul creat.

        Aceasta functie va actualiza starea jocului si va redesena tabla de joc (va actualiza fereastra grafica)
     */
    public void run()
    {
            /// Initializeaza obiectul game
        InitGame();
        long oldTime = System.nanoTime();   /*!< Retine timpul in nanosecunde aferent frame-ului anterior.*/
        long curentTime;                    /*!< Retine timpul curent de executie.*/

            /// Apelul functiilor Update() & Draw() trebuie realizat la fiecare 16.7 ms
            /// sau mai bine spus de 60 ori pe secunda.

        final int framesPerSecond   = 60; /*!< Constanta intreaga initializata cu numarul de frame-uri pe secunda.*/
        final double timeFrame      = 1000000000 / framesPerSecond; /*!< Durata unui frame in nanosecunde.*/

            /// Atat timp timp cat threadul este pornit Update() & Draw()
        while (runState == true)
        {
                /// Se obtine timpul curent
            curentTime = System.nanoTime();
                /// Daca diferenta de timp dintre curentTime si oldTime mai mare decat 16.6 ms
            if((curentTime - oldTime) > timeFrame)
            {
                if(state==STATE.GAME) {
                    /// Actualizeaza pozitiile elementelor
                    Update();
                    /// Deseneaza elementele grafica in fereastra.
                    oldTime = curentTime;
                }
                Draw();
            }
            try {
                Thread.sleep(1);
            } catch (InterruptedException e){
                e.printStackTrace();
            }
        }

    }

    /*! \fn public synchronized void start()
        \brief Creaza si starteaza firul separat de executie (thread).

        Metoda trebuie sa fie declarata synchronized pentru ca apelul acesteia sa fie semaforizat.
     */
    public synchronized void StartGame()
    {
        if(runState == false)
        {
                /// Se actualizeaza flagul de stare a threadului
            runState = true;
                /// Se construieste threadul avand ca parametru obiectul Game. De retinut faptul ca Game class
                /// implementeaza interfata Runnable. Threadul creat va executa functia run() suprascrisa in clasa Game.
            gameThread = new Thread(this);
                /// Threadul creat este lansat in executie (va executa metoda run())
            gameThread.start();
        }
        else
        {
                /// Thread-ul este creat si pornit deja
            return;
        }
    }

    /*! \fn public synchronized void stop()
        \brief Opreste executie thread-ului.

        Metoda trebuie sa fie declarata synchronized pentru ca apelul acesteia sa fie semaforizat.
     */
    public synchronized void StopGame()
    {
        if(runState == true)
        {
            /// Actualizare stare thread
            runState = false;
            /// Metoda join() arunca exceptii motiv pentru care trebuie incadrata intr-un block try - catch.
            try
            {
                /// Metoda join() pune un thread in asteptare panca cand un altul isi termina executie.
                /// Totusi, in situatia de fata efectul apelului este de oprire a threadului.
                gameThread.join();
                /// Close the GameWindow when the game stops
                wnd.closeWindow();
            }
            catch(InterruptedException ex)
            {
                /// In situatia in care apare o exceptie pe ecran vor fi afisate informatii utile pentru depanare.
                ex.printStackTrace();
            }
        }
        else
        {
            /// Thread-ul este oprit deja.
            return;
        }
    }

    /*! \fn private void Update()
        \brief Actualizeaza starea elementelor din joc.

        Metoda este declarata privat deoarece trebuie apelata doar in metoda run()
     */
    private void Update()
    {

        player.update(getCurrentLevelTiles());
        camera.tick(player);

        // Verifica coliziunile jucatorului cu dalele


    }



    /*! \fn private void Draw()
        \brief Deseneaza elementele grafice in fereastra coresponzator starilor actualizate ale elementelor.

        Metoda este declarata privat deoarece trebuie apelata doar in metoda run()
     */
    private void Draw() {
        // Returnez bufferStrategy pentru canvasul existent
        bs = wnd.GetCanvas().getBufferStrategy();

        // Verific daca buffer strategy a fost construit sau nu
        if (bs == null) {
            // Se executa doar la primul apel al metodei Draw()
            try {
                // Se construieste tripul buffer
                wnd.GetCanvas().createBufferStrategy(3);
                return;
            } catch (Exception e) {
                // Afisez informatii despre problema aparuta pentru depanare.
                e.printStackTrace();
            }
        }

        // Se obtine contextul grafic curent in care se poate desena.
        g = bs.getDrawGraphics();

        // Se sterge ce era
        g.clearRect(0, 0, wnd.GetWndWidth(), wnd.GetWndHeight());

        // Deseneaza imaginea de fundal
        switch (currentLevel) {
            case 1:
                g.drawImage(Assets.background1, 0, 0, wnd.GetWndWidth(), wnd.GetWndHeight(), null);
                break;
            case 2:
                g.drawImage(Assets.background2, 0, 0, wnd.GetWndWidth(), wnd.GetWndHeight(), null);
                break;
            case 3:
                g.drawImage(Assets.background3, 0, 0, wnd.GetWndWidth(), wnd.GetWndHeight(), null);
                break;
        }

        if (state == STATE.GAME) {
            // Translatează sistemul de coordonate pentru a lua în considerare camera
            g.translate(-(int)camera.getX(), -(int)camera.getY());

            // Desenează harta și jucătorul
            mapLoader.drawMap(g);
            player.render(g);

            // Resetează translația pentru a nu afecta alte desene
            g.translate((int)camera.getX(), (int)camera.getY());
        } else if (state == STATE.MENU || state == STATE.LEVEL_SELECTION) {
            g.drawImage(mainMenuBackground, 0, 0, wnd.GetWndWidth(), wnd.GetWndHeight(), null);
            MainMenu.render(g);
        }

        // Se afiseaza pe ecran
        bs.show();

        // Elibereaza resursele de memorie aferente contextului grafic curent
        g.dispose();
    }


    //implementare metode pt keylistener
    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_A:
//                System.out.println("Left key pressed");
                player.moveLeft();
                Player.CameraX -= player.speed;
                camera.setX(player.CameraX);
                break;
            case KeyEvent.VK_D:
//                System.out.println("Right key pressed");
                player.moveRight();
                Player.CameraX += player.speed;
                camera.setX(player.CameraX);
                break;
            case KeyEvent.VK_W:
//                System.out.println("Jump key pressed");
                player.jump();
                break;
            case KeyEvent.VK_ESCAPE:
                if (state == STATE.GAME) {
                    state = STATE.MENU;
                } else if (state == STATE.MENU) {
                    state = STATE.GAME;
                }
                break;
            case KeyEvent.VK_L:
                Save.insertData(currentLevel,0,0,0,0);
                //System.out.print("Score saved");
                break;
            case KeyEvent.VK_R:
                System.out.println("Progress resetted.");
                Save.insertData(0,0,0,0,0);
                break;
        }

    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_A:
//                System.out.println("Left key released");
                player.stopRunning();
                break;
            case KeyEvent.VK_D:
//                System.out.println("Right key released");
                player.stopRunning();
                break;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    public static Player getPlayer()
    {
        return player;
    }
    public int getCurrentLevel()
    {
        return currentLevel;
    }


}

