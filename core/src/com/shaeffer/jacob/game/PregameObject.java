package com.shaeffer.jacob.game;


import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.shaeffer.jacob.minor.GameInfoObject;

public class PregameObject{

    private final int WIDTH;
    private GlyphLayout gl;
    private ShapeRenderer shapeRenderer;
    private int center, x;
    private double now, first;
    private String time;
    private float[] smoothRoll;
    private int at;
    private GameInfoObject gio;
    private BitmapFont font32, font64;

    public PregameObject(int WIDTH, GameInfoObject gio, BitmapFont font32, BitmapFont font64){
        this.gio = gio;
        this.font32 = font32;
        this.font64 = font64;
        this.WIDTH = WIDTH;
        shapeRenderer = new ShapeRenderer();
        gl = new GlyphLayout();
        center = x = WIDTH/2;
        first = System.currentTimeMillis();
        time = "3";
        smoothRoll = new float[5];
        at = 0;
    }

    public boolean update(){
        float roll = average(add(gio.getRoll()));

        int max = center + (int)(roll*2);//(int)(Math.floor((double)roll)*4);
        int dx;
        if(roll > 0.5) dx = 1;
        else if(roll < -0.5) dx = -1;
        else dx = 0;
        x += dx;
        if(x > max) x = max;
        else if(x < max) x = max;
        if (x > center+79) x = center+79;
        else if(x < center-79) x = center-79;

        now = System.currentTimeMillis();
        if(x >= center+6 || x <= center-6){
            time = "";
            first = System.currentTimeMillis();
        }
        else{
            if(now - first > 4000) return true;
            else if(now - first > 3000) time = "GO!";
            else if(now - first > 2000) time = "1";
            else if(now - first > 1000) time = "2";
            else time = "3";
        }
        return false;
    }

    private float[] add(float f){
        if(at == smoothRoll.length-1){
            at = 0;
        }
        else at++;
        smoothRoll[at] = f;
        return smoothRoll;
    }

    private float average(float[] roll){
        float rme = 0;
        for (float f : roll){
            rme += f;
        }
        rme = rme/roll.length;
        return rme;
    }

    public void start()
    {
        first = System.currentTimeMillis();
    }

    public void render(SpriteBatch batch){
        batch.end();
        shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.BLUE);
        shapeRenderer.rect(center - 27, 375, 54, 50);
        shapeRenderer.line(center - 100, 400, center + 100, 400);
        shapeRenderer.line(center - 100, 425, center - 100, 375);
        shapeRenderer.line(center + 100, 425, center + 100, 375);
        shapeRenderer.setColor(Color.RED);
        shapeRenderer.circle(x, 400, 20, 6);
        shapeRenderer.end();
        batch.begin();
        gl.reset();
        gl.setText(font64, time);
        font64.draw(batch, gl, WIDTH/2 - gl.width/2, 700);
        gl.reset();
        gl.setText(font32, "Keep the hexagon fully in");
        float gh = gl.height;
        font32.draw(batch, gl, WIDTH/2 - gl.width/2, 500 + gh);
        gl.reset();
        gl.setText(font32, "the square to start.");
        font32.draw(batch, gl, WIDTH/2 - gl.width/2, 500 + gl.height - gh*1.6f);
    }

    public void dispose(){
        shapeRenderer.dispose();
    }
}