package com.shaeffer.jacob.menus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.shaeffer.jacob.game.Player;
import com.shaeffer.jacob.minor.GameInfoObject;

public class Ship {

    private Texture core, selected, unselected, disabled, grayedOut;
    private Player[] ships;
    private boolean[] enabled;
    private boolean[] green;
    private static int WIDTH = 480;
    private int numberOfShips;
    private int[][] buttons;
    private Rectangle[] buttRect;
    private GameInfoObject gio;

    public Ship(GameInfoObject gio){
        this.gio = gio;
        core = new Texture("Sprites/Ship_Menu/Core.png");
        selected = new Texture("Sprites/Ship_Menu/Selected.png");
        unselected = new Texture("Sprites/Ship_Menu/Unselected.png");
        disabled = new Texture("Sprites/Ship_Menu/Disabled.png");
        grayedOut = new Texture("Sprites/Ship_Menu/GrayedOut.png");
        int HEIGHT = 800;

        numberOfShips = gio.getImplemented();
        ships = new Player[numberOfShips];
        enabled = new boolean[numberOfShips];
        green = new boolean[numberOfShips];

        int selected = gio.getSelected();
        for(int i=0; i<numberOfShips; i++){
            if(selected == i)
                green[i] = true;
            else
                green[i] = false;
        }

        boolean unlocked[] = gio.getUnlocked();
        for(int i=0; i<numberOfShips; i++){
            enabled[i] = unlocked[i];
        }

        int buttonOrigX = WIDTH/2;
        int buttonOrigY = HEIGHT/2;

        int returnBtnX = (WIDTH-170)/2;
        int returnBtnY = 108;

        buttons = new int[6][2];//0 is return //1-6 are ship selection buttons
        buttons[0][0] = returnBtnX;
        buttons[0][1] = returnBtnY;

        buttons[1][0] = buttonOrigX-100;
        buttons[1][1] = buttonOrigY+95;
        buttons[2][1] = buttonOrigY+95;
        buttons[2][0] = buttonOrigX+20;
        buttons[3][1] = buttonOrigY;
        buttons[3][0] = buttonOrigX-100;
        buttons[4][1] = buttonOrigY;
        buttons[4][0] = buttonOrigX+20;
        buttons[5][1] = buttonOrigY-95;
        buttons[5][0] = buttonOrigX-40;
        //buttons[6][1] = buttonOrigY-95;
        //buttons[6][0] =

        int offsets[][] = new int[5][2];
        //Old Faithful
        offsets[0][0] = 24;
        offsets[0][1] = 20;
        //A.N.T.
        offsets[1][0] = 36;
        offsets[1][1] = 20;
        //F-35
        offsets[2][0] = 18;
        offsets[2][1] = 18;
        //Pride
        offsets[3][0] = 24;
        offsets[3][1] = 20;
        //El Capitan
        offsets[4][0] = 22;
        offsets[4][1] = 12;
        //unimplemented to be F.I.S.H.
        //offsets[5][0] = 24;
        //offsets[5][1] = 20;


        shipSetup(offsets);

        buttRect = new Rectangle[6];
        buttRect[0] = new Rectangle(buttons[0][0], buttons[0][1], 170, 63);//return
        buttRect[1] = new Rectangle(buttons[1][0], buttons[1][1], 80, 80);//ship 1
        buttRect[2] = new Rectangle(buttons[2][0], buttons[2][1], 80, 80);//ship 2
        buttRect[3] = new Rectangle(buttons[3][0], buttons[3][1], 80, 80);//ship 3
        buttRect[4] = new Rectangle(buttons[4][0], buttons[4][1], 80, 80);//ship 4
        buttRect[5] = new Rectangle(buttons[5][0], buttons[5][1], 80, 80);//ship 5
        //buttRect[6] = new Rectangle(buttons[6][0], buttons[6][1], 80, 80);//ship 6
    }

    public void render(SpriteBatch batch) {
        batch.draw(core, (WIDTH-core.getWidth())/2, 0, core.getWidth(), core.getHeight());
        for(int i=0; i<numberOfShips; i++){
            if(enabled[i]){
                if(green[i])
                    batch.draw(selected, buttons[i+1][0], buttons[i+1][1], selected.getWidth(), selected.getHeight());
                else
                    batch.draw(unselected, buttons[i+1][0], buttons[i+1][1], unselected.getWidth(), unselected.getHeight());
                ships[i].draw(batch, false);
            }
            else{
                batch.draw(disabled, buttons[i+1][0], buttons[i+1][1], selected.getWidth(), selected.getHeight());
                ships[i].draw(batch, true);
                batch.draw(grayedOut, buttons[i+1][0], buttons[i+1][1], selected.getWidth(), selected.getHeight());
            }
        }
    }

    private boolean shipSelected(int i){
        boolean ret = false;
        if(enabled[i-1]){
            for(int j=0; j<numberOfShips; j++){
                green[j] = false;
            }
            green[i-1] = true;
            ret = true;
        }
        return ret;
    }

    public void update(float delta) {
        if(gio.getTouched())getButtonPressed(gio);//TODO: update gio, do things with return value in this class
        for(Player p : ships)
            p.updateAnimations(delta);
    }

    private void getButtonPressed(GameInfoObject gio) {
        for (int i = 0; i < buttRect.length; i++) {
            if (buttRect[i].contains(gio.getTouchX(), gio.getTouchY())) {
                if(i > 0){
                    if(shipSelected(i))
                        gio.setSelected(i-1);
                }
                else
                    gio.setState(GameInfoObject.RESET);
            }
        }
        gio.setTouched(false);
    }

    private void shipSetup(int offsets[][])
    {
        for(int i=0; i<ships.length; i++)
        {
            ships[i] = new Player(i, buttons[i+1][0] + offsets[i][0], buttons[i+1][1] + offsets[i][1]);
        }
    }

    public void dispose()
    {
        core.dispose();
        selected.dispose();
        unselected.dispose();
        disabled.dispose();
        grayedOut.dispose();
        for(Player p : ships)
            p.dispose();
    }
}
