package com.shaeffer.jacob;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.shaeffer.jacob.game.Background;
import com.shaeffer.jacob.game.GameControl;
import com.shaeffer.jacob.menus.Main;
import com.shaeffer.jacob.menus.Options;
import com.shaeffer.jacob.menus.Scores;
import com.shaeffer.jacob.menus.Ship;
import com.shaeffer.jacob.minor.GameInfoObject;
import com.shaeffer.jacob.minor.NativePlatform;


public class Control implements ApplicationListener, InputProcessor
{
    public final int HEIGHT = 800;
    public final int WIDTH = 480;

    private GameInfoObject gameInfoObject;
    private SpriteBatch batch;
    private OrthographicCamera camera;

    private Main mainMenu;
    private Options options;
    private Scores scores;
    private Ship ship;
    private GameControl gameControl;
    private Background background;

    private boolean reset;
    private float timeSinceStart;
    private float initTime;
    private int frames;
    private NativePlatform nativePlatform;

    public Control(NativePlatform nativePlatform)
    {
        this.nativePlatform =  nativePlatform;
    }

    @Override
    public void create ()
    {
        //nativePlatform.run_on_the_ui_thread();

        //Gdx.app.log("dev output", "app started");
        Gdx.app.setLogLevel(Application.LOG_DEBUG);
        Gdx.input.setInputProcessor(this);

        gameInfoObject = new GameInfoObject(nativePlatform);
        gameInfoObject.update();
        background = new Background(0);
        batch = new SpriteBatch();

        camera = new OrthographicCamera(WIDTH, HEIGHT);
        camera.position.set(WIDTH/2, HEIGHT/2, 0);

        mainMenu = new Main(gameInfoObject);
        options = new Options(gameInfoObject);
        scores = new Scores(gameInfoObject);
        ship = new Ship(gameInfoObject);
        gameControl = new GameControl(gameInfoObject);
        reset = true;
        timeSinceStart = 0f;
        initTime = System.currentTimeMillis();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void render ()
    {
        float delta = Gdx.graphics.getDeltaTime();

        update(delta);

        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        switch(gameInfoObject.getState())
        {
            case(GameInfoObject.OPTIONS):
                background.render(batch);
                options.render(batch);
                break;
            case(GameInfoObject.SCORES):
                background.render(batch);
                scores.render(batch);
                break;
            case(GameInfoObject.SHIP):
                background.render(batch);
                ship.render(batch);
                break;
            case(GameInfoObject.GAME):
                gameControl.render(batch);
                break;
            default:
                background.render(batch);
                mainMenu.render(batch);
                break;
        }
        batch.end();
    }

    private void update(float delta)
    {
        getRoll();
        switch(gameInfoObject.getState())
        {
            case(GameInfoObject.MAIN):
                background.update(delta);
                mainMenu.update();
                break;
            case(GameInfoObject.OPTIONS):
                background.update(delta);
                options.update();
                break;
            case(GameInfoObject.SCORES):
                background.update(delta);
                scores.update(delta);
                break;
            case(GameInfoObject.SHIP):
                background.update(delta);
                ship.update(delta);
                break;
            case(GameInfoObject.GAME):
                if(reset)
                {
                    reset = false;
                    gameControl.start();
                    gameControl.update(delta);
                }
                else
                    gameControl.update(delta);
                break;
            case(GameInfoObject.RESET):
                background.update(delta);
                reset = true;
                gameControl.quickRecreate();
                scores.setHighScores();
                gameInfoObject.setState(GameInfoObject.MAIN);
                break;
        }
    }

    private void getRoll(){
        gameInfoObject.setRoll(Gdx.input.getRoll());
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button)
    {
        if(pointer == 0){
            Vector3 vec = new Vector3();
            camera.unproject(vec.set(screenX, screenY, 0));
            gameInfoObject.setTouchX(vec.x);
            gameInfoObject.setTouchY(vec.y);
            gameInfoObject.setTouched(true);
            gameInfoObject.setNoTouch(false);
        }
        else if(pointer == 1)
        {
            Vector3 vec = new Vector3();
            camera.unproject(vec.set(screenX, screenY, 0));
            gameInfoObject.setTouch2X(vec.x);
            gameInfoObject.setTouch2Y(vec.y);
            gameInfoObject.setTouched2(true);
            gameInfoObject.setNoTouch2(false);
        }

        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button)
    {
        if(pointer == 0)
        {
            Vector3 vec = new Vector3();
            camera.unproject(vec.set(screenX, screenY, 0));
            gameInfoObject.setTouchX(vec.x);
            gameInfoObject.setTouchY(vec.y);
            gameInfoObject.setTouched(false);
            gameInfoObject.setDragged(false);
            gameInfoObject.setNoTouch(true);
        }
        else if(pointer == 1)
        {
            Vector3 vec = new Vector3();
            camera.unproject(vec.set(screenX, screenY, 0));
            gameInfoObject.setTouch2X(vec.x);
            gameInfoObject.setTouch2Y(vec.y);
            gameInfoObject.setTouched2(false);
            gameInfoObject.setDragged2(false);
            gameInfoObject.setNoTouch2(true);
        }

        return true;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer)
    {
        if(pointer == 0){
            Vector3 vec = new Vector3();
            camera.unproject(vec.set(screenX, screenY, 0));
            gameInfoObject.setTouchX(vec.x);
            gameInfoObject.setTouchY(vec.y);
            gameInfoObject.setTouched(false);
            gameInfoObject.setDragged(true);
        }
        else if(pointer == 1)
        {
            Vector3 vec = new Vector3();
            camera.unproject(vec.set(screenX, screenY, 0));
            gameInfoObject.setTouch2X(vec.x);
            gameInfoObject.setTouch2Y(vec.y);
            gameInfoObject.setTouched2(false);
            gameInfoObject.setDragged2(true);
        }
        return true;
    }
    @Override public boolean mouseMoved(int screenX, int screenY) {return false;}
    @Override public boolean scrolled(int amount) {return false;}
    @Override public boolean keyDown(int keycode) {return false;}
    @Override public boolean keyUp(int keycode) {return false;}
    @Override public boolean keyTyped(char character)
    {
        gameInfoObject.setKeyTyped(character);
        return true;
    }

    @Override
    public void pause()
    {

    }
    @Override
    public void resume()
    {

    }

    @Override
    public void dispose()
    {
        mainMenu.dispose();
        options.dispose();
        scores.dispose();
        ship.dispose();
        gameControl.dispose();
    }
}
