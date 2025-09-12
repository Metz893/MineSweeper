package com.metz;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class GamePlayScreen implements Screen {

    //Object that draws all our sprite graphics: jpgs, pngs, etc.
    private SpriteBatch spriteBatch;

    //Object that draws shapes: rectangles, ovals, lines, etc.
    private ShapeRenderer shapeRenderer;

    //private to view the virtual world
    private Camera camera;

    //This controls how the camera view the world
    //zoom in/out? keep everything scaled
    private Viewport viewport;

    private GameBoard gameBoard;
    private BitmapFont defaultFont = new BitmapFont();

    private long gameTimer; //The game time counting up
    private long startTime; //Time stamp of start of game

    private Music music;
    private Music loseMusic;
    private Music winMusic;
    
    /*
     * Runs one time, at the very beginning
     * all set up should happen here
     *         loading graphics
     *         start values for variables
     *         etc. 
     */
    @Override
    public void show() {
        //OrthographicCamera is a 2D camera
        camera = new OrthographicCamera();

        //set the camera position to the middle of the world
        camera.position.set(1280/2, 720/2, 0);

        //required to save and update the camera changes above
        camera.update();

        //freeze my view to 1280x720
        //no matter the resolutioin of the window
        //camera will always show the same amount of world space
        viewport = new FitViewport(1280,720);

        //empty initialization of objets that will draw graphics for us
        spriteBatch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();

        //???, I just know that this was the solution to an annoying problem I had
        shapeRenderer.setAutoShapeType(true);

        gameBoard = new GameBoard(this);

        startTime = TimeUtils.nanoTime();

        music = Gdx.audio.newMusic((Gdx.files.internal("Music.mp3")));
        music.setLooping(true);

        loseMusic = Gdx.audio.newMusic((Gdx.files.internal("lost.mp3")));
        loseMusic.setLooping(false);
        loseMusic.setVolume(0.75f);

        winMusic = Gdx.audio.newMusic((Gdx.files.internal("win.mp3")));
        winMusic.setLooping(false);
        winMusic.setVolume(0.75f);
    }

    private void handlemouseClick() {
        //if there is a left/Right click, fires one time per click
        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            gameBoard.tileLeftClick(gameBoard.getTileAt(Gdx.input.getX(), Gdx.input.getY()));
        }
        if (Gdx.input.isButtonJustPressed(Input.Buttons.RIGHT)) {
            gameBoard.tileRightClick(gameBoard.getTileAt(Gdx.input.getX(), Gdx.input.getY()));            
        }
    }

    private void handleButtonClicks() {
        if (Gdx.input.isKeyJustPressed(Keys.SPACE)) {
            gameBoard.restart();
        }
    }

    //Draw Graphical User Interface
    private void drawGUI() {
        if (!gameBoard.getGameOver() && !gameBoard.getGameWon())
            gameTimer = TimeUtils.nanoTime() - startTime;
        defaultFont.draw(spriteBatch, "Time: " + (((gameTimer) / 100_000_000) / 10.) , 650, 650);
        defaultFont.draw(spriteBatch, "Total Bombs Left: " + (gameBoard.getTotalBombs() - gameBoard.getNumFlagsPlaced()), 430, 650);
        defaultFont.draw(spriteBatch, "Total Flags placed: " + gameBoard.getNumFlagsPlaced(), 770, 650);
        defaultFont.draw(spriteBatch, "Press SPACE to restart", 590, 200);
        defaultFont.getData().setScale(3f); // make it 3x bigger
        defaultFont.draw(spriteBatch, "Leaderboard", 30, 675);
        defaultFont.getData().setScale(2f); // make it 3x bigger

        for (int i = 0; i < LeaderBoard.getLeaderBoard().size(); i ++) {
            if (i < 10)
                defaultFont.draw(spriteBatch, i + 1 + ": " + LeaderBoard.getLeaderBoard().get(i), 80, 600 - i * 50);
        }

        defaultFont.getData().setScale(1f); // make it 3x bigger
    }

    private void drawGameOver() {
        spriteBatch.draw(gameBoard.getgameoverGraphic(), 400,200);
    }

    private void drawgameWon() {
        spriteBatch.draw(gameBoard.getWinGraphic(), 425,220);
    }

    private void clearScreen () {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    /*
     * this method will run as fst as it can (or set to a set FPS)
     * repeatedly, constantly looped
     * Things to include
     *          process user imput
     *          any A.I.
     *          draw all graphics
     */
    @Override
    public void render(float delta) {
        //Clear screen before each game move
        clearScreen();

        //get player input
        handlemouseClick();
        handleButtonClicks();

        //process player input, A.I.

        //all drawings of shapes must go between begin/end
        shapeRenderer.begin();
        shapeRenderer.end();

        //all drawing of graphics must be between being/end
        spriteBatch.begin();
        drawGUI();
        gameBoard.draw(spriteBatch);
        if (gameBoard.getGameOver()) {
            drawGameOver();
        }

        else if (gameBoard.getGameWon()) {
            drawgameWon();
        }

        spriteBatch.end();

        if (!gameBoard.getGameWon() && !gameBoard.getGameOver()) {
            loseMusic.stop();
            winMusic.stop();
            music.play();
        }
        else {
            music.pause();
        }
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        spriteBatch.dispose();
        shapeRenderer.dispose();
    }

  
    public void restartGameTimer() {
        startTime = TimeUtils.nanoTime();
    }

    public double getExactTime() {
        return ((gameTimer) / 100_000_000) / 10.;
    }

    public Music getLoseMusic () {
        return loseMusic;
    }


    public Music getwinMusic () {
        return winMusic;
    }
    
}
