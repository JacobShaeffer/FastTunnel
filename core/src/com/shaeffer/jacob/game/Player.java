package com.shaeffer.jacob.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Polygon;
import com.shaeffer.jacob.minor.GameInfoObject;

import java.util.ArrayList;
import java.util.List;

public class Player {

    private Texture ship;
    private Animations[] up, right, left;
    private Trail trail;
    private Polygon poly;
    private int shipW, shipH;

    private final int WIDTH;
    private double sensitivity;
    private double sensitivityMultiplier;//allows tuning of sensitivity
    private boolean sTR, sTL;
    private int x, y;

    private GameInfoObject gio;
    private boolean doRoll, doButton;

    public Player(GameInfoObject gio)
    {
        this.gio = gio;
        WIDTH = 480;
        sensitivityMultiplier = 1.0;
        sensitivity = gio.getSensitivity() * sensitivityMultiplier;//0.25*(gio.getSensitivity()/50);
        sTR = sTL = false;
        trail = null;
        setShip(setPaths(), gio.getSelected());
        x = WIDTH/2 - (shipW/2);//ship.getWidth()/2);
        y = 100;
        poly.setPosition(x, y);
        doRoll = gio.getRollControls();
        doButton = gio.getOnScreenControls();
    }

    public Player(int ship, int x, int y)
    {
        WIDTH = 480;
        sTR = sTL = false;
        setShip(setPaths(), ship);
        this.x = x;
        this.y = y;
        poly.setPosition(x, y);
        trail = null;
    }

    public void draw(SpriteBatch batch, boolean gameOver)
    {
        batch.draw(ship, x, y, shipW, shipH);//ship.getWidth(), ship.getHeight());
        if(!gameOver)
        {
            if(trail != null) trail.draw(batch);
            for (Animations i : up)
            {
                i.draw(batch);
            }
            if (sTR)
            {
                for (Animations i : right)
                {
                    i.draw(batch);
                }
            }
            else if (sTL)
            {
                for (Animations i : left)
                {
                    i.draw(batch);
                }
            }
        }
    }

    public void update(float delta, int button)
    {
        //x+=dx;
        //poly.setPosition(x, y);
        if(doRoll && !doButton)
            x += rollHandler(gio.getRoll(), delta);
        else if(doButton && !doRoll)
            x += rollHandler(buttonValue(button), delta);
        else if(doButton)//&& doRoll //But doRoll must be true if this elseif is hit
            x += rollHandler(gio.getRoll() + buttonValue(button), delta);
        poly.setPosition(x, y);
        updateAnimations(delta);
    }

    public void updateAnimations(float delta)
    {
        for(Animations i : up){i.update(x, y, delta);}
        for(Animations i : right){i.update(x, y, delta);}
        for(Animations i : left){i.update(x, y, delta);}
        if(trail != null) trail.update(x, delta);
        animateUp();
    }

    private float buttonValue(int button)
    {
        float ret = button * 6f;
        return ret;
    }

    private void animateUp()
    {
        for(Animations i : up){i.animate();}
    }

    public Polygon getPoly()
    {
        return poly;
    }

    private double rollHandler(float roll, float delta)
    {
        double dx;
        if(roll > 1)
        {
            if(!sTR)
            {
                for(Animations i : right){i.animate();}
            }
            sTR = true;
            sTL = false;
            dx = roll*sensitivity*delta;//TODO: fix this with deltatime... sensativity will need to be adjusted to make the movment the same
        }
        else if(roll < -1)
        {
            if(!sTL){
                for(Animations i : left){i.animate();}
            }
            sTL = true;
            sTR = false;
            dx = roll*sensitivity*delta;
        }
        else
        {
            sTL = false;
            sTR = false;
            dx = 0;
        }
        return dx;
    }

    private String[][] setPaths()
    {
        FileHandle handle = Gdx.files.internal("Files/Ships.dat");
        String line = handle.readString();
        String[] arr = line.split("\r\n");
        List<List<String>> list = new ArrayList<List<String>>();
        int listIndex = -1;
        for(int i=0; i<arr.length; i++)
        {
            arr[i] = arr[i].replaceAll("\\s+","");
            if(arr[i].contains("BREAK"))
            {
                list.add(new ArrayList<String>());
                listIndex++;
            }
            else
            {
                list.get(listIndex).add(arr[i]);
            }
        }
        listIndex++;
        String[][] temp = new String[listIndex][];
        for(int i=0; i<listIndex; i++)
        {
            temp[i] = list.get(i).toArray(new String[0]);
        }
        return temp;
    }

    private boolean setShip(String[][] ships, int selected)
    {
        String[] paths = ships[selected];
        int[] num = new int[3];
        int current = -1;
        int count = 0;
        int check = 0;
        String[] temp;

        if(!paths[check].equals("up"))
        {
            temp = paths[0].split(",");
            ship = new Texture(temp[0]);

            shipW = Integer.parseInt(temp[1]);
            shipH = Integer.parseInt(temp[2]);
            check++;
        }
        for(int i = check; i<paths.length; i++)
        {

            if(paths[i].equals("up"))
            {
                current = 0;
                i++;
            }
            else if(paths[i].equals("right"))
            {
                current = 1;
                i++;
            }
            else if(paths[i].equals("left"))
            {
                current = 2;
                i++;
            }
            else if(paths[i].equals("vert") || paths[i].equals("trail"))
            {
                current = -1;
                i++;
            }

            if(current != -1)
            {
                num[current]++;
            }
        }

        up = new Animations[num[0]];
        right = new Animations[num[1]];
        left = new Animations[num[2]];
        trail = new Trail();
        for(int i=1; i<paths.length; i++)
        {
            if(paths[i].equals("up"))
            {
                current = 0;
                i++;
                count = 0;
            }
            else if(paths[i].equals("right"))
            {
                current = 1;
                i++;
                count = 0;
            }
            else if(paths[i].equals("left"))
            {
                current = 2;
                i++;
                count = 0;
            }
            else if(paths[i].equals("trail"))
            {
                current = 3;
                i++;
                count = 0;
            }
            else if(paths[i].equals("vert"))
            {
                current = 4;
                i++;
                count = 0;
            }

            temp = paths[i].split(",");
            if(current == 0)
            {
                up[count] = new Animations(new Texture(temp[0]), Integer.parseInt(temp[1]),Integer.parseInt(temp[2]),Integer.parseInt(temp[3]),Integer.parseInt(temp[4]),fracToDec(temp[5]), temp[6].toCharArray()[0]);
                count++;
            }
            else if(current == 1)
            {
                right[count] = new Animations(new Texture(temp[0]), Integer.parseInt(temp[1]),Integer.parseInt(temp[2]),Integer.parseInt(temp[3]),Integer.parseInt(temp[4]),fracToDec(temp[5]), temp[6].toCharArray()[0]);
                count++;
            }
            else if(current == 2)
            {
                left[count] = new Animations(new Texture(temp[0]), Integer.parseInt(temp[1]),Integer.parseInt(temp[2]),Integer.parseInt(temp[3]),Integer.parseInt(temp[4]),fracToDec(temp[5]), temp[6].toCharArray()[0]);
                count++;
            }
            else if(current == 3)
            {
                trail = new Trail(new Texture(temp[0]), Integer.parseInt(temp[1]), Integer.parseInt(temp[2]), Integer.parseInt(temp[3]));
            }
            else if(current == 4)
            {
                float[] vert = new float[temp.length];
                for(int j=0; j<temp.length; j+=2)
                {
                    vert[j] = Float.parseFloat(temp[j]);
                    vert[j+1] = Float.parseFloat(temp[j+1]);
                }
                poly = new Polygon(vert);
            }
        }
        for(Animations i : up){i.setSTT();}
        return true;
    }

    private float fracToDec(String str)
    {
        String[] temp = str.split("/");
        return Float.parseFloat(temp[0])/Float.parseFloat(temp[1]);
    }

    public void dispose()
    {
        ship.dispose();
    }
}
