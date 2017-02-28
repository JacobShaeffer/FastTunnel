package com.shaeffer.jacob.menus;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.shaeffer.jacob.Control;
import com.shaeffer.jacob.minor.GameInfoObject;


public class Main
{
    private Texture texture;
    private static int WIDTH = 480;
    int buttonOrigX, buttonOrigY;
    int[][] buttons;
    Rectangle[] buttRect;
    int[] buttonState = {GameInfoObject.GAME, GameInfoObject.SHIP, GameInfoObject.SCORES, GameInfoObject.OPTIONS};
    private GameInfoObject gio;

    public Main(GameInfoObject gio){
        this.gio = gio;
        texture = new Texture("Sprites/Game/Main_Menu.png");

        buttonOrigX = (WIDTH-250)/2;
        buttonOrigY = 250;

        buttons = new int[4][2];
        buttons[0][0] = buttonOrigX;
        buttons[0][1] = buttonOrigY+87+40+87+40;
        buttons[1][0] = buttonOrigX;
        buttons[1][1] = buttonOrigY+87+40;
        buttons[2][0] = buttonOrigX;
        buttons[2][1] = buttonOrigY;
        buttons[3][0] = buttonOrigX;
        buttons[3][1] = buttonOrigY-87-40;

        buttRect = new Rectangle[4];
        buttRect[0] = new Rectangle(buttons[0][0], buttons[0][1], 250, 87);
        buttRect[1] = new Rectangle(buttons[1][0], buttons[1][1], 250, 87);
        buttRect[2] = new Rectangle(buttons[2][0], buttons[2][1], 250, 87);
        buttRect[3] = new Rectangle(buttons[3][0], buttons[3][1], 250, 87);
    }

    private void getButtonPressed(GameInfoObject gio) {
        for (int i = 0; i < buttRect.length; i++) {
            if (buttRect[i].contains(gio.getTouchX(), gio.getTouchY())) {
                gio.setState(buttonState[i]);
            }
        }
    }

    public void render(SpriteBatch batch) {
        batch.draw(texture, (WIDTH-texture.getWidth())/2, 0, texture.getWidth(), texture.getHeight());
    }

    public void update() {
        if(gio.getTouched())getButtonPressed(gio);
        gio.setTouched(false);
    }

    public void dispose() {
        texture.dispose();
    }
}
