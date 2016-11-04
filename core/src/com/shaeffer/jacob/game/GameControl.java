package com.shaeffer.jacob.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.shaeffer.jacob.minor.GameInfoObject;

public class GameControl {
    private final int WIDTH = 480, HEIGHT = 800;
    private Background background;
    private Player player;
    private Tunnel tunnel;
    private PregameObject pregameObj;
    private int Score;
    private boolean S;
    private double initTime;
    private boolean gameOver;
    private BitmapFont font32, font64;
    private final int paddingY = 5;
    private boolean initiated;
    private String[] highscores;
    private boolean scoreUpdates;
    private GlyphLayout glyphLayout;
    private GameInfoObject gio;
    //private int button;
    private int onScreenButtonValues[];
    private boolean drawOnScreenButtons;
    private Rectangle[] onScreenButtonBounds;
    private Rectangle returnButtonBounds;
    private Texture onScreenButtons, returnButton;
    private int internalState;// 0 is playscreen, 1 is highscore screen
    private HighscoreControl highscoreControl;

    public GameControl(GameInfoObject gio){
        this.gio = gio;
        glyphLayout = new GlyphLayout();

        onScreenButtonBounds = new Rectangle[2];
        onScreenButtonBounds[0] = new Rectangle(0, HEIGHT/2 - 55, 55, 110);
        onScreenButtonBounds[1] = new Rectangle(WIDTH - 55, HEIGHT/2 - 55, 55, 110);
        onScreenButtons = new Texture(Gdx.files.internal("Sprites/Game/onScreenButtons.png"));
        onScreenButtonValues = new int[] {-1, 1};

        returnButtonBounds = new Rectangle(155, 108, 170, 63);
        returnButton = new Texture(Gdx.files.internal("Sprites/Game/return.png"));

        recreate();
    }

    public void update(float delta)
    {
        if(internalState == 0 || internalState == 99)
        {
            if(!gameOver) {
                if(initiated)
                {
                    gameOver = !background.update(delta, initiated, player.getPoly());
                    Gdx.app.log("dev output", gameOver + "");
                    if(drawOnScreenButtons)
                        player.update(delta, getButtonInput());
                    else
                        player.update(delta, 0);
                    if (!S) initTime = System.currentTimeMillis();
                    else
                    {
                        double now = System.currentTimeMillis();
                        if (now - initTime > 100)
                        {
                            Score++;
                            initTime = System.currentTimeMillis();
                        }
                    }
                    S = true;
                    //gameOver = !tunnel.update(player.getPoly(), delta);
                    if (gameOver)
                    {
                        scoreUpdates = updateScores();
                        //TODO: if scoreUpdates false, check if there is a pending score to send to server
                        if(scoreUpdates) internalState = 1;
                        gio.setLocal(highscores);
                        highscoreControl.newHighscore(Score);
                    }
                }
                else
                {
                    background.update(delta, initiated, null);
                    initiated = pregameObj.update();
                    player.updateAnimations(delta);
                }
            }
            else
            {
                if (gio.getTouched() || internalState == 99)
                {
                    if (returnButtonBounds.contains(gio.getTouchX(), gio.getTouchY()) || internalState == 99)
                    {
                        gio.setState(GameInfoObject.RESET);
                        gio.setTouched(false);
                        recreate();
                    }
                }
            }
        }
        else
        {
            internalState = highscoreControl.update();
        }
    }

    private boolean updateScores(){
        boolean retMe;
        //int[] ships = new int[highscores.length];
        int[] scores = new int[highscores.length];

        for (int i=0; i<highscores.length; i++)
        {
            String[] temp = highscores[i].split("!");
            //ships[i] = Integer.parseInt(temp[0]);
            scores[i] = Integer.parseInt(temp[1]);
        }

        if(Score > scores[0]){
            for(int i=highscores.length-1; i>0; i--)
            {
                highscores[i] = highscores[i - 1];
            }
            highscores[0] = String.valueOf(gio.getSelected()) +"!"+ String.valueOf(Score);
            retMe = true;
        }
        else{
            for(int i=0; i<scores.length; i++){
                if(Score > scores[i]){
                    for(int j=highscores.length-1; j>i+1; j--)
                    {
                        highscores[j] = highscores[j - 1];
                    }
                    highscores[i] = String.valueOf(gio.getSelected()) +"!"+ String.valueOf(Score);;
                    i=highscores.length;
                }
            }
            retMe = false;
        }
        return retMe;
    }

    public void render(SpriteBatch batch)
    {
        background.render(batch);
        tunnel.render(batch);
        player.draw(batch, gameOver);
        if(!gameOver)
        {
            printScoreToScreen(batch);
            if(drawOnScreenButtons)
            {
                batch.draw(onScreenButtons, 0, 345);
            }
        }
        else
        {
            if(internalState == 1 || internalState == 99)
            {
                highscoreControl.render(batch);
            }
            else
            {
                ScoreScreen(batch);
            }
            batch.draw(returnButton, 155, 108);
        }
        if(!initiated)
        {
            pregameObj.render(batch);
        }
    }

    private int getButtonInput()
    {
        int button = 0;

        if(!gio.getNoTouch())
        {
            for(int i=0; i<onScreenButtonBounds.length; i++)
            {
                if(onScreenButtonBounds[i].contains(gio.getTouchX(), gio.getTouchY()))
                {
                    button += onScreenButtonValues[i];
                    break;
                }
            }
        }

        if(!gio.getNoTouch2())
        {
            for(int i=0; i<onScreenButtonBounds.length; i++)
            {
                if(onScreenButtonBounds[i].contains(gio.getTouch2X(), gio.getTouch2Y()))
                {
                    button += onScreenButtonValues[i];
                    break;
                }
            }
        }
        return button;
    }

    public void recreate()
    {
        glyphLayout.reset();
        S = false;
        Score = 0;
        font32 = new BitmapFont(Gdx.files.internal("Fonts/Razer32/Razer32pt.fnt"), false);
        font64 = new BitmapFont(Gdx.files.internal("Fonts/Razer64/Razer64pt.fnt"), false);
        font32.setColor(Color.RED);
        font64.setColor(Color.RED);
        gameOver = false;
        scoreUpdates = false;
        highscores = gio.getLocal();
        quickRecreate();
    }

    public void quickRecreate()
    {
        if(gio.getPreGame())
        {
            initiated = false;
            pregameObj = new PregameObject(WIDTH, gio, font32, font64);
        }
        else
            initiated = true;
        drawOnScreenButtons = gio.getOnScreenControls();

        if(player != null) player.dispose();
        if(background != null) background.dispose();
        if(tunnel != null) tunnel.dispose();

        //Gdx.app.log("dev output", "new player created");
        player = new Player(gio);
        tunnel = new Tunnel(gio.getSelected(), player.getPoly());
        background = new Background(gio.getSelected(), tunnel);
        highscoreControl = new HighscoreControl(gio, WIDTH, returnButtonBounds, returnButton);

        internalState = 0;
    }

    public void start()
    {
        initTime = System.currentTimeMillis();
        if(gio.getPreGame())
            pregameObj.start();
    }

    private void printScoreToScreen(SpriteBatch batch){
        glyphLayout.reset();
        glyphLayout.setText(font32, String.valueOf(Score));
        float fontX = WIDTH - glyphLayout.width - 5;
        float fontY = 800 - paddingY;
        font32.draw(batch, glyphLayout, fontX, fontY);
    }

    private void ScoreScreen(SpriteBatch batch){
        glyphLayout.reset();
        float fontX, fontY;
        glyphLayout.setText(font64, "Your Score Was");
        fontX = WIDTH/2 - glyphLayout.width/2;
        fontY = 600 + glyphLayout.height;
        font64.draw(batch, glyphLayout, fontX, fontY);

        glyphLayout.setText(font64, String.valueOf(Score));
        fontX = WIDTH/2 - glyphLayout.width/2;
        fontY = 500 + glyphLayout.height;
        font64.draw(batch, glyphLayout, fontX, fontY - glyphLayout.height - paddingY);
    }

    public void dispose(){
        player.dispose();
        background.dispose();
        tunnel.dispose();
        pregameObj.dispose();
        font32.dispose();
        font64.dispose();
    }


}
