package com.metz;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
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
            //get player input

            //process player input, A.I.

            //all drawings of shapes must go between begin/end
            shapeRenderer.begin();
            shapeRenderer.end();

            //all drawing of graphics must be between being/end
            spriteBatch.begin();
            gameBoard.draw(spriteBatch);
            spriteBatch.end();
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
    
}
