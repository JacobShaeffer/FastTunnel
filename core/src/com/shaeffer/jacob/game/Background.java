package com.shaeffer.jacob.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Polygon;

public class Background {
    private Texture background;
    private int y;
    //private float tempY;
    private static final int Velocity = -300;
    private double difficulty;
    private Tunnel tunnel;

    public Background(int selected)
    {
        constuctor(selected);
    }

    public Background(int selected, Tunnel tunnel)
    {
        constuctor(selected);
        this.tunnel = tunnel;
    }

    private void constuctor(int selected)
    {
        //Gdx.app.log("dev output", "background created " + selected);
        switch (selected)
        {
            case 1:
                background = new Texture("Sprites/Game/background_dirt.png");
                break;
            default:
                background = new Texture("Sprites/Game/background_480x800.png");
        }
        //tempY = 0f;
        y = 0;
        difficulty = 1.0;
    }

    public void update(float delta)
    {
        //tempY += Velocity * delta * difficulty;
        //y = (int)tempY;
        y += Velocity * delta * difficulty;
        if(y <= -800){
            y = 0;
            //tempY = 0;
        }
    }

    public boolean update(float delta, boolean init, Polygon player)
    {
        update(delta);
        if(init)
            return tunnel.update(player, delta);
        else
            return true;
    }

    public void updateDifficulty(double difficulty)
    {
        this.difficulty = difficulty;
    }//TODO: difficulty

    public void render(SpriteBatch batch)
    {
        batch.draw(background, 0, y, 480, 800);
        batch.draw(background, 0, y+background.getHeight(), 480, 800);
    }


    public void dispose(){
        background.dispose();
    }
}
