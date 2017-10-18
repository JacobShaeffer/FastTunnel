package com.shaeffer.jacob.menus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.shaeffer.jacob.minor.GameInfoObject;
import com.shaeffer.jacob.minor.ScoreInfoObject;


public class Scores
{

    private static int WIDTH = 480;
    private static int HEIGHT = 800;

    private Texture core, overlay;
    private GlyphLayout local[];
    private Rectangle buttRect;
    private GameInfoObject gio;
    private ScoreInfoObject sio;
    private BitmapFont font;


    public Scores(GameInfoObject gio){
        this.gio = gio;
        sio = new ScoreInfoObject();

        font = new BitmapFont(Gdx.files.internal("Fonts/Razer64/Razer64pt.fnt"), false);

        font.setColor(Color.RED);

        core = new Texture("Sprites/Scores_Menu/Core.png");
        overlay = new Texture("Sprites/Scores_Menu/Overlay_Simplified.png");

        int returnBtnX = (WIDTH-170)/2;
        int returnBtnY = 108;

        setHighScores();

        buttRect = new Rectangle(returnBtnX, returnBtnY, 170, 63);
    }

    public void setHighScores()
    {
        String[] temp = gio.getLocal();
        for(int i=0; i<temp.length; i++)
        {
            temp[i] = temp[i].split("!")[1];
        }
        local = new GlyphLayout[temp.length];
        for(int i=0; i<local.length; i++)
        {
            local[i] = new GlyphLayout(font, temp[i]);
        }
    }

    public void render(SpriteBatch batch)
    {
        batch.draw(core, 0, 0);
        batch.draw(overlay, 80, 200);

        int lineDistance = 40;
        int firstLine = 625;
        for(int i=0; i<local.length; i++) {
            font.draw(
                    batch,
                    local[i],
                    WIDTH / 2 - local[i].width / 2,
                    firstLine - (lineDistance * i)
            );
        }
    }

    public void update(float delta) {
        if(gio.getTouched()){
            if (buttRect.contains(gio.getTouchX(), gio.getTouchY())) {
                gio.setState(GameInfoObject.MAIN);
            }
        }
        gio.setTouched(false);
    }

    public void dispose() {
        font.dispose();
        core.dispose();
        overlay.dispose();
    }
}
