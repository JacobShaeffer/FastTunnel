package com.shaeffer.jacob.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.shaeffer.jacob.minor.GameInfoObject;
import com.shaeffer.jacob.minor.submitAPI;

public class HighscoreControl{
    private final int WIDTH;
    private GameInfoObject gio;
    private GlyphLayout glyphLayout;
    private BitmapFont font64, font28;
    private String currentName;
    private Rectangle doneBounds, returnBounds, nameLineBounds;
    private Texture nameLine, done_Button, button_return;
    private int internalState;//0 = keyboard active, 1 = the other one
    private int score;

    public HighscoreControl(GameInfoObject gio, int WIDTH, Rectangle returnBounds, Texture button_return)
    {
        this.returnBounds = returnBounds;
        this.button_return = button_return;
        this.WIDTH = WIDTH;
        this.gio = gio;
        glyphLayout = new GlyphLayout();
        currentName = gio.getUsername();
        font64 = new BitmapFont(Gdx.files.internal("Fonts/Razer64/Razer64pt.fnt"), false);
        font64.setColor(Color.RED);
        font28 = new BitmapFont(Gdx.files.internal("Fonts/Razer28/Razer28pt.fnt"), false);
        font28.setColor(Color.BLACK);
        nameLine = new Texture(Gdx.files.internal("Sprites/Game/name_line.png"));
        done_Button = new Texture(Gdx.files.internal("Sprites/Game/done_button.png"));
        doneBounds = new Rectangle(183,400,115,50);
        nameLineBounds = new Rectangle(0,450,480,190);
        score = 0;
    }

    public int update()
    {
        if(gio.getTouched())
        {
            gio.setTouched(false);
            switch(touchListener())
            {
                case 1:
                    //the textLine was touched
                    if(internalState == 1)
                    {
                        Gdx.input.setOnscreenKeyboardVisible(true);
                        internalState = 0;
                    }
                    break;
                case 2:
                    //the "done" button was pressed
                    Gdx.input.setOnscreenKeyboardVisible(false);
                    internalState = 1;
                    break;
                case 3:
                    //return was touched
                    Gdx.input.setOnscreenKeyboardVisible(false);
                    contactServer();
                    gio.setNameSet(true);
                    return 99;
            }
        }
        if(internalState == 0)
            keyboardInput();
        return 1;//99 when return button pressed
    }

    private void keyboardInput()
    {
        char c = gio.getKeyTyped();
        switch (c)
        {
            case '\u0000':
                break;
            case '\b':
                if(currentName.length() > 0)
                    currentName = currentName.substring(0,currentName.length()-1);
                break;
            case '\n':
                Gdx.input.setOnscreenKeyboardVisible(false);
                internalState = 1;
                break;
            default:
                if(currentName.length() <= 14 && isAllowed(c))
                    currentName += c;
        }
    }

    private boolean isAllowed(char c)
    {
        GlyphLayout glyphLayout = new GlyphLayout(font28, c+"");
        if(glyphLayout.width > 0)
            return true;
        else
            return false;
    }

    private void contactServer()
    {
        gio.setUsername(currentName);
        gio.setNameSet(true);
        submitAPI api = new submitAPI(gio);
        String[] temp = gio.getLocal()[0].split("!");
        api.newHighScore(currentName, Integer.parseInt(temp[0]), Integer.parseInt(temp[1]));
    }

    private int touchListener()
    {
        float x = gio.getTouchX();
        float y = gio.getTouchY();
        if(internalState == 0)
        {
            if(doneBounds.contains(x, y))
            {
                return 2;
            }
        }
        if(nameLineBounds.contains(x, y))
            return 1;
        else if(returnBounds.contains(x, y))
            return 3;
        else
            return 0;
    }

    public void render(SpriteBatch batch)
    {
        float fontX, fontY;
        glyphLayout.reset();

        glyphLayout.setText(font64, String.valueOf(score));
        fontX = WIDTH/2 - glyphLayout.width/2;
        fontY = 750 + glyphLayout.height;
        font64.draw(batch, glyphLayout, fontX, fontY - glyphLayout.height);

        if(internalState == 0)
        {
            //keyboard active
            batch.draw(nameLine, nameLineBounds.getX(), nameLineBounds.getY());
            batch.draw(done_Button, doneBounds.getX(), doneBounds.getY());
        }
        else
        {
            batch.draw(nameLine, nameLineBounds.getX(), nameLineBounds.getY());
        }

        fontY = nameLineBounds.getY() + 45;
        for(int i=0; i<currentName.length(); i++)
        {
            glyphLayout.reset();
            String str = currentName.substring(i, i+1);
            glyphLayout.setText(font28, str);
            fontX = 79 + ((int)(22.3*i)); // + padding + individual glyph width adjustment
            font28.draw(batch, glyphLayout, fontX, fontY);
        }
    }

    public void newHighscore(int Score)
    {
        score = Score;

        if(gio.isNameSet())
        {
            internalState = 1;
            currentName = gio.getUsername();
        }
        else
        {
            Gdx.input.setOnscreenKeyboardVisible(true);
            internalState = 0;
        }
    }

    public void dispose()
    {
        button_return.dispose();
        done_Button.dispose();
        nameLine.dispose();
        font64.dispose();
        font28.dispose();
    }
}
