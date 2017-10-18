package com.shaeffer.jacob.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.shaeffer.jacob.minor.GameInfoObject;

public class HighscoreControl{
    private final int WIDTH;
    private GameInfoObject gio;
    private GlyphLayout glyphLayout;
    private BitmapFont font64;
    private Rectangle returnBounds;
    private Texture new_highscore, button_return;
    private int new_highscore_x, new_highscore_Y;
    private int score;

    public HighscoreControl(GameInfoObject gio, int WIDTH, Rectangle returnBounds, Texture button_return)
    {
        this.returnBounds = returnBounds;
        this.button_return = button_return;
        this.WIDTH = WIDTH;
        this.gio = gio;
        glyphLayout = new GlyphLayout();
        font64 = new BitmapFont(Gdx.files.internal("Fonts/Razer64/Razer64pt.fnt"), false);
        font64.setColor(Color.RED);
        score = 0;


        new_highscore = new Texture(Gdx.files.internal("Sprites/Game/new_highscore.png"));
        new_highscore_x = 0;
        new_highscore_Y = 600;
    }

    public int update()
    {
        if(gio.getTouched())
        {
            gio.setTouched(false);
            if(touchListener())
            {
                //return was touched
                return 99;
            }
        }
        return 1;//99 when return button pressed
    }

    private boolean touchListener()
    {
        float x = gio.getTouchX();
        float y = gio.getTouchY();
        if(returnBounds.contains(x, y))
            return true;
        else
            return false;
    }

    public void render(SpriteBatch batch)
    {
        float fontX, fontY;
        glyphLayout.reset();

        glyphLayout.setText(font64, String.valueOf(score));
        fontX = WIDTH/2 - glyphLayout.width/2;
        fontY = 550 + glyphLayout.height;
        font64.draw(batch, glyphLayout, fontX, fontY - glyphLayout.height);

        batch.draw(new_highscore, new_highscore_x, new_highscore_Y);
    }

    public void newHighscore(int score)
    {
        this.score = score;
    }

    public void dispose()
    {
        button_return.dispose();
        font64.dispose();
    }
}
