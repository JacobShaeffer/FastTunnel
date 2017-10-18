package com.shaeffer.jacob.minor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

public class GameInfoObject
{
    public static final int MAIN = 0;
    public static final int OPTIONS = 1;
    public static final int SCORES = 2;
    public static final int SHIP = 3;
    public static final int GAME = 4;
    public static final int RESET = 5;

    //input
    private float touchX;
    private float touchY;
    private boolean touched;
    private boolean noTouch;
    private boolean dragged;

    private float touch2X;
    private float touch2Y;
    private boolean touched2;
    private boolean noTouch2;
    private boolean dragged2;

    private char keyTyped;
    private float roll;
    //input

    //options
    private int sensitivity;
    //private int volume;
    private boolean toggleOnScreen;
    private boolean toggleRoll;
    private boolean wifi;
    private boolean preGame;
    //options

    //ships
    private int implemented = 5;//TODO: update as more ships are added.
    private int selected;
    private boolean[] unlocked;
    //ships

    //scores
    private String _id;
    private String username;
    private boolean nameSet;
    private String[] local;//storage for local highscores

    //scores

    //other
    private int state;
    private ScreenService screenService;

    public GameInfoObject(ScreenService screenService)
    {
        this.screenService = screenService;

        //Gdx.app.log("dev output", "GameInfoObject created");
        //set all values that aren't written to a file here
        touchX = -1;
        touchY = -1;
        touched = false;
        noTouch = true;
        dragged = false;
        roll = 0f;
        keyTyped = '\u0000';

        state = MAIN;
        sensitivity = 50;
        //volume = 50;
        toggleOnScreen = false;
        toggleRoll = true;
        wifi = true;
        preGame = true;
        username = "";
        nameSet = false;

        _id = "";
        selected = 0;
        unlocked = new boolean[]{true, true, true, true, true, false};
        local = new String[]{"0!0", "0!0", "0!0", "0!0", "0!0", "0!0", "0!0", "0!0", "0!0", "0!0"};
        //save();//TODO: remove me
        read();
    }

    public void update()
    {
        //if disk and memory data do not match
        //write memory data to disk
        if(!check()) save();
    }

    private void save()
    {
        //write data to game info file
        String content = sensitivity + ", " +  toggleOnScreen + ", " +  toggleRoll + ", " +  wifi + ", " +  preGame + ", " + _id + ", " + username + ", " + nameSet + ", " +  selected + ", ";//", " + volume +
        for(int i=0; i<unlocked.length-1; i++){
            content += unlocked[i] + ":";
        }
        content += unlocked[unlocked.length-1] + ", ";
        for(int i=0; i<local.length-1; i++){
            content += local[i] + ":";
        }
        content += local[local.length-1];
        FileHandle file = Gdx.files.local("GameInfo.dat");
        file.writeString(content, false);
        //Gdx.app.log("dev output", "write gameinfo: " + content);
    }

    private void read()
    {
        FileHandle handle = Gdx.files.local("GameInfo.dat");
        if(!handle.exists())
        {
            save();
            return;
        }

        String line;
        line = handle.readString();
        //Gdx.app.log("dev output", "read gameinfo: " + line);

        String[] values = line.split(",");
        for(int i=0; i<values.length; i++)
        {
            values[i] = values[i].trim();
        }
        sensitivity = Integer.parseInt(values[0]);
        //volume = Integer.parseInt(values[1]);
        toggleOnScreen = Boolean.parseBoolean(values[1]);
        toggleRoll = Boolean.parseBoolean(values[2]);
        wifi = Boolean.parseBoolean(values[3]);
        preGame = Boolean.parseBoolean(values[4]);
        _id = values[5];
        username = values[6];
        nameSet = Boolean.parseBoolean(values[7]);
        selected = Integer.parseInt(values[8]);
        String tempUnlocked[] = values[9].split(":");
        for(int i=0; i<tempUnlocked.length; i++)
        {
            unlocked[i] = Boolean.parseBoolean(tempUnlocked[i]);
        }

        String tempLocal[] = values[10].split(":");
        for(int i=0; i<tempLocal.length; i++)
        {
            local[i] = tempLocal[i];
        }
    }

    private boolean check()
    {
        FileHandle handle = Gdx.files.local("GameInfo.dat");
        if(!handle.exists())
        {
            return false;
        }

        String line;
        line = handle.readString();

        String[] values = line.split(",");
        for(int i=0; i<values.length; i++)
        {
            values[i] = values[i].trim();
        }
        if(sensitivity != Integer.parseInt(values[0]))
        {
            //Gdx.app.log("dev output", "difference in sensitivity");
            return false;
        }
        //if(volume != Integer.parseInt(values[1]))
        //{
        //    Gdx.app.log("dev output", "difference in volume");
        //    return false;
        //}
        if(toggleOnScreen != Boolean.parseBoolean(values[1]))
        {
            //Gdx.app.log("dev output", "difference in on screen controls");
            return false;
        }
        if(toggleRoll != Boolean.parseBoolean(values[2]))
        {
            //Gdx.app.log("dev output", "difference in roll controls");
            return false;
        }
        if(wifi != Boolean.parseBoolean(values[3]))
        {
            //Gdx.app.log("dev output", "difference in wifi");
            return false;
        }
        if(preGame != Boolean.parseBoolean(values[4]))
        {
            //Gdx.app.log("dev output", "difference in preGame");
            return false;
        }
        if(!_id.equals(values[5]))
        {
            //Gdx.app.log("dev output", "difference in _id");
            return false;
        }
        if(!username.equals(values[6]))
        {
            //Gdx.app.log("dev output", "difference in username");
            return false;
        }
        if(nameSet != Boolean.parseBoolean(values[7]))
        {
            //Gdx.app.log("dev output", "difference in nameSet");
            return false;
        }
        if(selected != Integer.parseInt(values[8]))
        {
            //Gdx.app.log("dev output", "difference in selected");
            return false;
        }

        String tempUnlocked[] = values[9].split(":");
        for(int i=0; i<tempUnlocked.length; i++)
        {
            if(unlocked[i] != Boolean.parseBoolean(tempUnlocked[i]))
            {
                //Gdx.app.log("dev output", "difference in unlock " + i);
                return false;
            }
        }

        String tempLocal[] = values[10].split(":");
        for(int i=0; i<tempLocal.length; i++)
        {
            if(!local[i].equals(tempLocal[i]))
            {
                //Gdx.app.log("dev output", "difference in highscore " + i);
                return false;
            }
        }

        return true;
    }

    //Get functions
    public int getSensitivity() { return sensitivity; }
    //public int getVolume() { return volume; }
    public boolean getOnScreenControls() { return toggleOnScreen; }
    public boolean getRollControls() { return toggleRoll; }
    public boolean getWifi() { return wifi; }
    public String get_id() { return _id; }
    public String getUsername() { return username; }
    public boolean isNameSet() { return nameSet; }
    public int getSelected() { return selected; }
    public int getImplemented() { return implemented; }
    public boolean[] getUnlocked() { return unlocked.clone(); }
    public String[] getLocal() { return local.clone(); }
    public int getState() { return state; }
    public float getTouchX() { return touchX; }
    public float getTouchY() { return touchY; }
    public boolean getTouched()
    {
        return touched;
    }
    public boolean getNoTouch()
    {
        return noTouch;
    }
    public boolean getDragged() { return dragged; }
    public float getTouch2X() { return touch2X; }
    public float getTouch2Y() { return touch2Y; }
    public boolean getTouched2()
    {
        return touched2;
    }
    public boolean getNoTouch2()
    {
        return noTouch2;
    }
    public boolean getDragged2() { return dragged2; }
    public float getRoll() { return roll; }
    public boolean getPreGame() { return preGame; }

    //Set functions
    public void setSensitivity(int s) { sensitivity = s; }
    //public void setVolume(int v) { volume = v; }
    public void setOnScreenControls(boolean o) { toggleOnScreen = o; }
    public void setRollControls(boolean r) { toggleRoll = r; }
    public void setWifi(boolean w) { wifi = w; }
    public void set_id(String i) { _id = i; }
    public void setUsername(String u) { username = u; }
    public void setNameSet(boolean s) { nameSet = s; }
    public void setSelected(int s) { selected = s; }
    public void setUnlocked(boolean[] u) { unlocked = u; }
    public void setLocal(String[] l) { local = l; }
    public void setState(int s)
    {
        if(s == 4)
        {
            screenService.keepScreenOn(true);
        }
        else
        {
            screenService.keepScreenOn(false);
        }
        state = s;
        update();
    }
    public void setTouchX(float x) { touchX = x; }
    public void setTouchY(float y) { touchY = y; }
    public void setTouched(boolean t) { touched = t; }
    public void setNoTouch(boolean n) { noTouch = n; }
    public void setDragged(boolean d) { dragged = d; }
    public void setTouch2X(float x) { touch2X = x; }
    public void setTouch2Y(float y) { touch2Y = y; }
    public void setTouched2(boolean t) { touched2 = t; }
    public void setNoTouch2(boolean n) { noTouch2 = n; }
    public void setDragged2(boolean d) { dragged2 = d; }
    public void setRoll(float r) { roll = r; }
    public void setPreGame(boolean p) { preGame = p; }
}
