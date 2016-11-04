package com.shaeffer.jacob.menus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.shaeffer.jacob.minor.GameInfoObject;
import com.shaeffer.jacob.minor.ScoreboardAPI;
import com.shaeffer.jacob.minor.ScoreInfoObject;


public class Scores
{

    private static int WIDTH = 480;
    private static int HEIGHT = 800;

    private Texture core, overlay, rank;
    private Texture local_global[];
    private GlyphLayout local[];
    private Rectangle[] buttRect;
    private ScoreboardAPI scoreboardAPI;
    private GameInfoObject gio;
    private ScoreInfoObject sio;
    private BitmapFont[] fonts;

    private int[][] buttons;
    private int internalState;

    public Scores(GameInfoObject gio){
        this.gio = gio;
        sio = new ScoreInfoObject();

        scoreboardAPI = new ScoreboardAPI(this.gio, sio);

        fonts = new BitmapFont[8];
        fonts[ScoreInfoObject._16] = new BitmapFont(Gdx.files.internal("Fonts/Razer16/Razer16pt.fnt"), false);
        fonts[ScoreInfoObject._18] = new BitmapFont(Gdx.files.internal("Fonts/Razer18/Razer18pt.fnt"), false);
        fonts[ScoreInfoObject._24] = new BitmapFont(Gdx.files.internal("Fonts/Razer24/Razer24pt.fnt"), false);
        fonts[ScoreInfoObject._26] = new BitmapFont(Gdx.files.internal("Fonts/Razer26/Razer26pt.fnt"), false);
        fonts[ScoreInfoObject._28] = new BitmapFont(Gdx.files.internal("Fonts/Razer28/Razer28pt.fnt"), false);
        fonts[ScoreInfoObject._30] = new BitmapFont(Gdx.files.internal("Fonts/Razer30/Razer30pt.fnt"), false);
        fonts[ScoreInfoObject._32] = new BitmapFont(Gdx.files.internal("Fonts/Razer32/Razer32pt.fnt"), false);
        fonts[ScoreInfoObject._64] = new BitmapFont(Gdx.files.internal("Fonts/Razer64/Razer64pt.fnt"), false);

        for(int i=0; i<fonts.length; i++)
        {
            fonts[i].setColor(Color.RED);
        }

        core = new Texture("Sprites/Scores_Menu/Core.png");
        overlay = new Texture("Sprites/Scores_Menu/Overlay.png");
        local_global = new Texture[]{new Texture("Sprites/Scores_Menu/Local.png"), new Texture("Sprites/Scores_Menu/Global.png")};
        rank = new Texture("Sprites/Scores_Menu/Rank.png");

        int returnBtnX = (WIDTH-170)/2;
        int returnBtnY = 108;

        internalState = 0;//Local, Global = 1

        setHighScores();

        buttons = new int[1][2];
        buttons[0][0] = returnBtnX;
        buttons[0][1] = returnBtnY;

        buttRect = new Rectangle[3];
        buttRect[0] = new Rectangle(buttons[0][0], buttons[0][1], 170, 63);
        buttRect[1] = new Rectangle(84, 204, 154, 42);
        buttRect[2] = new Rectangle(242, 204, 154, 42);
    }

    public void setHighScores()
    {
        String[] temp = gio.getLocal();
        for(int i=0; i<temp.length; i++)
        {
            temp[i] = temp[i].split("!")[1];
        }
        local = new GlyphLayout[temp.length];
        for(int i=0; i<local.length; i++)
        {
            local[i] = new GlyphLayout(fonts[ScoreInfoObject._64], temp[i]);
        }
    }

    private void getButtonPressed(GameInfoObject gio)//TODO: This method will need help
    {
        for (int i = 0; i < buttRect.length; i++) {
            if (buttRect[i].contains(gio.getTouchX(), gio.getTouchY())) {
                switch(i)
                {
                    case 0:
                        gio.setState(GameInfoObject.MAIN);
                        if(scoreboardAPI != null)
                            scoreboardAPI.reset();
                        internalState = 0;
                        break;
                    case 1:
                        internalState = 0;
                        break;
                    case 2:
                        internalState = 1;
                        if(sio.getInternalState() == ScoreInfoObject.IDLE)
                        {
                            sio.setInternalState(ScoreInfoObject.LOADING);
                            scoreboardAPI.getGlobalScores();
                        }
                        break;
                }
            }
        }
    }

    public void render(SpriteBatch batch)
    {
        batch.draw(core, 0, 0);
        batch.draw(local_global[internalState], 84, 204);
        batch.draw(overlay, 80, 200);

        if(internalState == 0)
        {
            int padding = 7;
            int margin = 30;
            for(int i=0; i<local.length; i++){
                fonts[ScoreInfoObject._64].draw(batch, local[i], WIDTH/2 - local[i].width/2, HEIGHT/2 + ((padding + local[i].height) * local.length/2) - (local[i].height * i) - (padding * i) + padding*3 + margin);
            }
        }
        else {
            {
                int printingIndex = 0;
                String[] str;
                GlyphLayout[] glyph;
                batch.draw(rank, 90, 255);
                boolean sm = sio.isUsingScoreMessage();
                int padding = 7;
                int margin = 30;
                if (sm) {
                    int test = sio.getScoresMessage();
                    if (test < 4 && test >= 0) {
                        str = new String[]{"loading"};
                        for (int i = 0; i < test; i++)
                            str[0] += ".";
                    } else {
                        switch (sio.getScoresMessage()) {
                            case 5:
                                str = new String[]{"No internet connection available."};
                                //INTERNET
                                break;
                            case 6:
                                str = new String[]{"Wifi unavailable.", "You may disable wifi only", "mode in the options menu."};
                                glyph = new GlyphLayout[]{new GlyphLayout(fonts[sio._32], str[0])};
                                fonts[sio._32].draw(
                                        batch,
                                        glyph[printingIndex],
                                        WIDTH / 2 - glyph[printingIndex].width / 2,
                                        HEIGHT / 2 + ((padding + glyph[printingIndex].height) * 3 / 2) - (glyph[printingIndex].height * printingIndex) - (padding * printingIndex) + padding * 3 + margin
                                );
                                printingIndex++;
                                //WIFI
                                break;
                            default:
                                str = new String[]{"An unexpected error", "occurred while contacting", "the server."};
                                //ERROR is done here because if it hits here something went wrong
                                break;
                        }
                    }

                    glyph = new GlyphLayout[str.length];
                    for (int i = 0; i < str.length; i++) {
                        glyph[i] = new GlyphLayout(fonts[sio.getFontSize()], str[i]);
                    }

                    for (; printingIndex < glyph.length; printingIndex++) {
                        fonts[sio.getFontSize()].draw(
                                batch,
                                glyph[printingIndex],
                                WIDTH / 2 - glyph[printingIndex].width / 2,
                                HEIGHT / 2 + ((padding + glyph[printingIndex].height) * glyph.length / 2) - (glyph[printingIndex].height * printingIndex) - (padding * printingIndex) + padding * 3 + margin
                        );
                    }
                    //Print to screen
                } else {
                    //highscores being displayed
                    //Print to screen
                    String[][] temp = sio.getScores();
                    str = new String[temp.length];
                    for(int i=0; i<temp.length; i++)
                        str[i] = temp[i][0];

                    glyph = new GlyphLayout[str.length];
                    for (int i = 0; i < str.length; i++) {
                        glyph[i] = new GlyphLayout(fonts[sio.getFontSize()], str[i]);
                    }

                    for (; printingIndex < glyph.length; printingIndex++) {
                        fonts[sio.getFontSize()].draw(
                                batch,
                                glyph[printingIndex],
                                WIDTH / 2 - glyph[printingIndex].width / 2,
                                HEIGHT / 2 + ((padding + glyph[printingIndex].height) * glyph.length / 2) - (glyph[printingIndex].height * printingIndex) - (padding * printingIndex) + padding * 3 + margin
                        );
                    }
                }
            }

            {
                String str;
                GlyphLayout glyph;
                boolean rm = sio.isUsingRankMessage();

                if (rm) {
                    int test = sio.getRankMessage();
                    if (test < 4 && test >= 0) {
                        str = "";//"loading";
                        //for (int i = 0; i < test; i++)
                        //    str += ".";
                    } else {
                        str = "N/A";
                    }
                    glyph = new GlyphLayout(fonts[sio._24], str);
                    fonts[sio._24].draw(
                            batch,
                            glyph,
                            WIDTH / 2 - 30,
                            265 + ((glyph.height) * 1 / 2)
                    );
                }
                else
                {
                    str = sio.getRank();
                    glyph = new GlyphLayout(fonts[sio.getFontSize()], str);
                    fonts[sio.getFontSize()].draw(
                            batch,
                            glyph,
                            WIDTH / 2 - glyph.width / 2,
                            265 + ((glyph.height) * 1 / 2)
                    );
                }
            }
        }
    }

    public void update(float delta) {
        if(gio.getTouched())getButtonPressed(gio);
        gio.setTouched(false);
        if(scoreboardAPI != null) scoreboardAPI.update(delta);
    }

    public void dispose() {
        for(int i=0; i<fonts.length; i++)
            fonts[i].dispose();
        core.dispose();
        overlay.dispose();
        local_global[0].dispose();
        local_global[1].dispose();
    }
}
