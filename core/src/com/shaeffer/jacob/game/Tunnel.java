package com.shaeffer.jacob.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Polygon;

public class Tunnel{

    private Texture rectangle;
    private static int WIDTH = 480;
    private int[] x, y;
    private int rectHeight, rectWidth, moveX;
    private Polygon[] collisionBoxes;
    private int xAtTop;

    private static final int Velocity = -300;
    private double difficulty;
    private float tempY;//top of the first rectangle in the stack

    public Tunnel(int selected, Polygon playerPoly){
        switch (selected)
        {
            case 4:
                rectangle = new Texture("Sprites/Game/Water.png");
                break;
            default:
                rectangle = new Texture("Sprites/Game/Tunnel.png");
        }

        difficulty = 1.0;
        moveX = 10;//average value of random movement
        rectHeight = 5;
        rectWidth = 120;
        tempY = 805f;//set origin of stack to top of screen
        x = new int[161];//TODO: small gap at bottom of stack, need to adjust values?, add one more in?
        y = new int[161];
        int spaceing = (WIDTH-120)/2;
        xAtTop = spaceing;
        for(int i=0; i<x.length; i++)
        {
            x[i] = spaceing;
            y[i] = i*rectHeight;
        }

        /*
        int bottom = (int)(playerPoly.getBoundingRectangle().getY());
        int top = bottom + (int)(playerPoly.getBoundingRectangle().getHeight());
        if(bottom % rectHeight != 0){bottom -= bottom%rectHeight;}
        if(top % rectHeight != 0){top += (rectHeight-(top%rectHeight));}
        int count = (top-bottom)/rectHeight;
        collisionBoxes = new Polygon[count*2];
        for(int i=0; i<collisionBoxes.length/2; i++){
            float[] vert1 = {0,bottom+(rectHeight*i),spaceing,bottom+(rectHeight*i),spaceing,bottom+rectHeight+(rectHeight*i),0,bottom+rectHeight+(rectHeight*i)};
            float[] vert2 = {spaceing + rectWidth,bottom+(rectHeight*i),spaceing*2 + rectWidth,bottom+(rectHeight*i),spaceing*2 + rectWidth,bottom+rectHeight+(rectHeight*i),spaceing + rectWidth,bottom+rectHeight+(rectHeight*i)};
            collisionBoxes[i] = new Polygon(vert1);
            collisionBoxes[i+collisionBoxes.length/2] = new Polygon(vert2);
        }
        xAtTop = 800/rectHeight - (800-top)/rectHeight;
        */
    }

    public void updateDifficulty(double difficulty)
    {
        this.difficulty = difficulty;
    }

    public void render(SpriteBatch batch){
        for(int i=0; i<x.length; i++){
            batch.draw(rectangle, x[i], y[i], rectWidth, rectHeight);
        }
    }

    public boolean update(Polygon poly, float delta){//TODO: fix this with deltatime
        int move = (int)((Math.random() * 2*moveX - moveX));


        if(x[x.length-1]+move > (WIDTH-120)){move = WIDTH-120 - x[x.length-1];}
        if(x[x.length-1]+move < 0){move = 0 - x[x.length-1];}

        xAtTop += move;
        int origin = moveRectangleOrigin(delta);
        updateRowPositions(origin, xAtTop);
        //System.arraycopy(x,1,x,0,x.length-1);
        //x[x.length-1] += move;

        /*
        move = x[xAtTop-1] - x[xAtTop-2];
        int j = collisionBoxes.length/2, k = collisionBoxes.length;
        for(int i=0; i<j-1; i++){
            collisionBoxes[i].setPosition(collisionBoxes[i+1].getX(),collisionBoxes[i].getY());
        }
        for(int i=j; i<k-1; i++){
            collisionBoxes[i].setPosition(collisionBoxes[i+1].getX(),collisionBoxes[i].getY());
        }
        collisionBoxes[j-1].setPosition(collisionBoxes[j-1].getX()+move, collisionBoxes[j-1].getY());
        collisionBoxes[k-1].setPosition(collisionBoxes[k-1].getX()+move, collisionBoxes[k-1].getY());
        */
        return playing(poly);
    }

    private void updateRowPositions(int origin, int reposition)
    {
        int loop = 0;
        for(int i=0; i<y.length; i++)
        {
            int newPos = origin - (i*rectHeight);
            if(newPos <= -5)
            {
                loop = 805;
                x[i] = reposition;//Not all of them are being updated here
                Gdx.app.log("Dev Output", "" + i);
            }
            newPos += loop;
            y[i] = newPos;
        }

    }

    private int moveRectangleOrigin(float delta)
    {
        tempY += Velocity * delta * difficulty;
        int origin = (int)tempY;
        if(origin <= 0){
            origin = 800;
            tempY = 800f;
        }
        return origin;
    }

    private boolean playing(Polygon poly){
        /*
        for(Polygon r : collisionBoxes){
            if(Intersector.overlapConvexPolygons(poly, r)){
                return false;
            }
        }
        */
        return true;//returns true if no collision
    }

    public void dispose(){
        rectangle.dispose();
    }
}
