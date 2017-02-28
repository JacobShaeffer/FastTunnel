package com.shaeffer.jacob.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Trail {

    private boolean run;
    private TextureRegion[][] tr;
    private int[] pos;
    private int y;

    public Trail()
    {
        run = false;
    }

    public Trail(Texture t, int width, int height, int top)
    {
        run = true;
        tr = TextureRegion.split(t,width,5);
        pos = new int[height/5];
        for(int i=0; i<pos.length; i++)
        {
            pos[i] = 240 - (width/2);
        }
        y = 100;
    }

    public void update(int newX, float delta)//TODO: fix this with deltatime?
    {
        if(run)
        {
            int x = newX;
            for(int i=0; i<pos.length; i++)
            {
                int temp = pos[i];
                pos[i] = x;
                x = temp;
            }
        }
    }

    public void draw(SpriteBatch batch)
    {
        if(run)
        {
            //Gdx.app.log("dev output", "draw trail");
            for(int i=0; i<tr.length; i++)
                batch.draw(tr[i][0], pos[i], y-(5*i));
        }
    }
}
