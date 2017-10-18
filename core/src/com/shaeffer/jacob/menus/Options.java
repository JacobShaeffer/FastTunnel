package com.shaeffer.jacob.menus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.shaeffer.jacob.Control;
import com.shaeffer.jacob.minor.GameInfoObject;


public class Options {

    private static int WIDTH = 480;
    private Texture background, overlay, cross, slider; //slider 240, 538  //cross 105, 465(-80(x2))
    private int buttonOrigX, buttonOrigY, crossesX;
    private int[] crossesY;
    private int slidersY, slidersX;
    private int[][] buttons;
    private boolean sensativitySlider;
    private boolean[] showCross;
    private Rectangle[] buttRect;
    private GameInfoObject gio;

    public Options(GameInfoObject gio){
        this.gio = gio;
        background = new Texture("Sprites/Options_Menu/Core.png");
        overlay = new Texture("Sprites/Options_Menu/Overlay.png");
        cross = new Texture("Sprites/Options_Menu/Cross.png");
        slider = new Texture("Sprites/Options_Menu/Slider.png");

        sensativitySlider = false;
        slidersX = (int)((gio.getSensitivity()*2.5f)+115);
        slidersY = 570;
        crossesY = new int[3];
        crossesX = 105;
        showCross = new boolean[3];
        showCross[0] = gio.getOnScreenControls();
        showCross[1] = gio.getRollControls();
        showCross[2] = gio.getPreGame();
        //showCross[3] = gio.getWifi();

        buttonOrigX = WIDTH/2 - 135;
        buttonOrigY = 512;
        crossesY[0] = buttonOrigY-20;
        crossesY[1] = buttonOrigY-76-40;
        crossesY[2] = buttonOrigY-149-60;
        //crossesY[3] = buttonOrigY-223;
        int returnBtnX = WIDTH/2 - 85;
        int returnBtnY = 108;

        int sliderBoxX = WIDTH/2 - 134;
        int sliderBoxY = slidersY;


        buttons = new int[6][2];
        buttons[0][0] = returnBtnX;
        buttons[0][1] = returnBtnY;
        buttons[1][0] = buttonOrigX - 5;
        buttons[1][1] = buttonOrigY - 6 - 20;
        buttons[2][0] = buttonOrigX - 5;
        buttons[2][1] = buttonOrigY - 82 - 40;
        buttons[3][0] = buttonOrigX - 5;
        buttons[3][1] = buttonOrigY - 155 - 60;
        //buttons[4][0] = buttonOrigX - 5;
        //buttons[4][1] = buttonOrigY - 229 - 60;
        buttons[4][0] = buttonOrigX + 11;
        buttons[4][1] = buttonOrigY - 305;

        buttRect = new Rectangle[6];
        buttRect[0] = new Rectangle(buttons[0][0], buttons[0][1], 170, 63);
        buttRect[1] = new Rectangle(buttons[1][0], buttons[1][1], 32, 32);
        buttRect[2] = new Rectangle(buttons[2][0], buttons[2][1], 32, 32);
        buttRect[3] = new Rectangle(buttons[3][0], buttons[3][1], 32, 32);
        //buttRect[3] = new Rectangle(buttons[4][0], buttons[4][1], 32, 32);
        buttRect[4] = new Rectangle(buttons[4][0], buttons[4][1], 251, 25);
        buttRect[5] = new Rectangle(sliderBoxX, sliderBoxY, 267, 26);
    }

    public void render(SpriteBatch batch) {
        batch.draw(background, (WIDTH-background.getWidth())/2, 0, background.getWidth(), background.getHeight());
        batch.draw(overlay, 0, 0, overlay.getWidth(), overlay.getHeight());
        batch.draw(slider, slidersX, slidersY, slider.getWidth(), slider.getHeight());
        for(int i=0; i<showCross.length; i++){
            if(showCross[i])
                batch.draw(cross, crossesX, crossesY[i], cross.getWidth(), cross.getHeight());
        }

        /*batch.end();
        ShapeRenderer sr = new ShapeRenderer();
        sr.setAutoShapeType(true);
        sr.begin();
        for(int i=0; i<buttons.length; i++)
        {
            sr.rect(buttRect[i].x, buttRect[i].y, buttRect[i].width, buttRect[i].height);
        }
        sr.end();
        sr.dispose();
        batch.begin();*/
    }

    public void update() {
        getButtonPressed();
        updateSlider();
    }

    public boolean toggleCross(int i){
        showCross[i] = !showCross[i];
        return showCross[i];
    }

    public void setSlider(int position){
        if(position < 115)
            position = 115;
        else if(position > 365)
            position = 365;
        slidersX = position-5;
        gio.setSensitivity((int) ((slidersX - 115) / 2.5f) + 2);
    }

    private void getButtonPressed()
    {//TODO: replace with state value
        if(gio.getTouched())
        {
            for (int i = 0; i < buttRect.length; i++)
            {
                if (buttRect[i].contains(gio.getTouchX(), gio.getTouchY()))
                {
                    if(i > 0){
                        Gdx.app.log("debug", "button pressed " + i);
                        if(i > 0 && i < 4 && gio.getTouched())
                        {
                            if(i == 1)
                                gio.setOnScreenControls(toggleCross(i-1));
                            if(i == 2)
                            {
                                if(gio.getPreGame() && gio.getRollControls())
                                {
                                    gio.setRollControls(toggleCross(i-1));
                                    gio.setPreGame(toggleCross(i));
                                }
                                else
                                    gio.setRollControls(toggleCross(i-1));
                            }
                            if(i == 3)
                                gio.setPreGame(toggleCross(i-1));
                            //if(i == 4)
                            //    gio.setWifi(toggleCross(i-1));
                        }
                        else if(i == 4)//6 is support button
                        {
                            //Gdx.app.log("dev output", "open the internet");
                            Gdx.net.openURI("http://jacobshaeffer.com/Fasttunnel/supportTheDeveloper.html");
                            Gdx.app.log("debug", "I am attempting to open the internet");
                        }
                        else if(i == 5)//5 is the sensitivity slider
                        {
                            setSlider((int)gio.getTouchX());
                            sensativitySlider = true;
                        }
                    }
                    else
                    {
                        gio.setState(GameInfoObject.RESET);
                    }
                }

            }
        }
        gio.setTouched(false);
    }

    private void updateSlider()
    {
        if(gio.getDragged())
        {
            if(sensativitySlider)
                setSlider((int)gio.getTouchX());
        }
        if(gio.getNoTouch())
        {
            sensativitySlider = false;
        }
    }

    public void dispose()
    {
        background.dispose();
        overlay.dispose();
        cross.dispose();
        slider.dispose();
    }
}
