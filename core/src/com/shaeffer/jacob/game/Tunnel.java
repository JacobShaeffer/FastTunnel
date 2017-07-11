package com.shaeffer.jacob.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Queue;


public class Tunnel{

    private Texture rectangle;
    private static int WIDTH = 480;
    private static int rectangleCount = 161;
    private int rectHeight, rectWidth, moveX;

    private float currentTime;

    private Rectangle boundingBox;
    private Queue<Rectangle> collisionQL;
    private Queue<Rectangle> collisionQR;
    private int top, bottom;
    private Queue<Vector2> tunnelQ;


    private static final int Velocity = -300;
    private double difficulty;

    public Tunnel(int selected, Polygon playerPoly){
        switch (selected)
        {
            case 4:
                rectangle = new Texture("Sprites/Game/Water.png");
                break;
            default:
                rectangle = new Texture("Sprites/Game/Tunnel.png");
        }

        boundingBox = playerPoly.getBoundingRectangle();
        bottom = (int)boundingBox.y;
        top = bottom + (int)boundingBox.height;
        bottom -= 2*rectHeight;
        //top += rectHeight;
        collisionQL = new Queue<Rectangle>();
        collisionQR = new Queue<Rectangle>();

        difficulty = 1.0;
        moveX = 10;//average value of random movement
        rectHeight = 5;
        rectWidth = 120;
        tunnelQ = new Queue<Vector2>();
        int spaceing = (WIDTH-120)/2;
        for(int i=0; i<rectangleCount; i++)
        {
            int currentY = i*rectHeight;
            if(currentY - rectHeight <= top)
            {
                if(currentY + rectHeight >= bottom)
                {
                    collisionQR.addFirst(new Rectangle(0,currentY,spaceing,rectHeight));
                    collisionQL.addFirst(new Rectangle(spaceing+rectWidth,currentY,spaceing,rectHeight));
                }
            }
            tunnelQ.addFirst(new Vector2(spaceing,currentY));
        }
        currentTime = 0f;
    }

    public void updateDifficulty(double difficulty)
    {
        this.difficulty = difficulty;
    }//TODO: difficulty

    public void render(SpriteBatch batch){
        for(int i=0; i<tunnelQ.size; i++){
            batch.draw(rectangle, tunnelQ.get(i).x, tunnelQ.get(i).y, rectWidth, rectHeight);
        }
    }

    public boolean update(Polygon poly, float delta){
        int amplitude = 1;
        int period = 1;
        currentTime += delta;
        double sin_x = Math.sin(3.14159 * amplitude * currentTime) * period;
        //Gdx.app.log("dev", String.valueOf(sin_x));
        int delta_x = (int)((Math.random() * 2*moveX - moveX));
        int delta_y = (int)(delta * difficulty * Velocity);

        for(int i=0; i<tunnelQ.size; i++)
        {
            tunnelQ.get(i).y += delta_y;
            if(tunnelQ.get(i).y < -10)
            {
                tunnelQ.removeIndex(i);
            }
        }
        for(int i=0; i<collisionQL.size; i++)
        {
            collisionQL.get(i).setY(collisionQL.get(i).y + delta_y);
            collisionQR.get(i).setY(collisionQR.get(i).y + delta_y);
            if(collisionQL.get(i).y < bottom-rectHeight)
            {
                collisionQL.removeIndex(i);
                collisionQR.removeIndex(i);
            }
        }
        if(collisionQL.first().y < top+rectHeight)
        {
            Rectangle tempL = collisionQL.first();
            Rectangle tempR = collisionQR.first();

            int index = 0;
            float closest = 10000000;
            for (Vector2 vec : tunnelQ)
            {
                float dist = Math.abs(vec.y - (tempL.y+rectHeight));
                if(dist < closest)
                {
                    index = tunnelQ.indexOf(vec, true);
                    closest = dist;
                }
            }

            int x1 = (int)tunnelQ.get(index).x - (WIDTH-120)/2;
            int x2 = (int)tunnelQ.get(index).x + rectWidth;


            collisionQL.addFirst(new Rectangle(x1, tempL.y+rectHeight, tempL.width, tempL.height));
            collisionQR.addFirst(new Rectangle(x2, tempR.y+rectHeight, tempR.width, tempR.height));
        }
        if(tunnelQ.first().y < 810)
        {
            Vector2 temp = tunnelQ.first();
            if(temp.x+delta_x > (WIDTH-120))
            {
                delta_x = WIDTH-120 - (int)temp.x;
            }
            else if(temp.x+delta_x < 0)
            {
                delta_x = 0 - (int)temp.x;
            }
            tunnelQ.addFirst(new Vector2(temp.x + delta_x, temp.y + rectHeight));
        }
        return playing(poly);
    }

    private boolean playing(Polygon poly){
        for(Rectangle r : collisionQL){
            if(collision(poly, r)) return false;
        }
        for(Rectangle r : collisionQR){
            if(collision(poly, r)) return false;
        }
        return true;//returns true if no collision
    }

    private boolean collision(Polygon p, Rectangle r) {
        Polygon rPoly = new Polygon(new float[] { 0, 0, r.width, 0, r.width, r.height, 0, r.height });
        rPoly.setPosition(r.x, r.y);
        if (Intersector.overlapConvexPolygons(rPoly, p))
            return true;
        return false;
    }

    public void dispose(){
        rectangle.dispose();
    }
}
