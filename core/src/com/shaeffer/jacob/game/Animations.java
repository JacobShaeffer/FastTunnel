package com.shaeffer.jacob.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Animations {

    private Animation animation;
    private float stateTime;
    private int offsetX, offsetY;
    private boolean playing, sTT;
    private int x, y;

    public Animations(Texture img, int columns, int rows, int offsetX, int offsetY, float duration, char playmode){
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        stateTime = 0f;
        playing = false;
        sTT = false;

        TextureRegion[][] tmp = TextureRegion.split(img, img.getWidth() / columns, img.getHeight() / rows);
        TextureRegion[] frames = new TextureRegion[columns * rows];
        int index = 0;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                frames[index++] = tmp[i][j];
            }
        }
        Animation.PlayMode PlayMode;
        switch(playmode){
            case 'L':
                PlayMode = Animation.PlayMode.LOOP;
                break;
            case 'R':
                PlayMode = Animation.PlayMode.LOOP_RANDOM;
                break;
            case 'P':
                PlayMode = Animation.PlayMode.LOOP_PINGPONG;
                break;
            default:
                PlayMode = Animation.PlayMode.LOOP;
        }
        animation = new Animation(duration, frames);
        animation.setPlayMode(PlayMode);
    }

    public void draw(SpriteBatch batch){
        TextureRegion currentFrame = animation.getKeyFrame(stateTime, true);
        batch.draw(currentFrame, x+offsetX, y+offsetY, currentFrame.getRegionWidth(), currentFrame.getRegionHeight());
    }

    public void update(int x, int y, float delta){
        this.x = x;
        this.y = y;
        if(playing) {
            try {
                if (animation.isAnimationFinished(stateTime + delta)) {
                    playing = false;
                    if(sTT){
                        stateTime = 0f;
                    }
                }else stateTime += delta;
            }catch(NullPointerException e){e.printStackTrace();}
        }
    }

    public void setSTT(){
        sTT = true;
    }

    public void animate(){
        if(!sTT){
            stateTime = 0f;
        }
        playing = true;
    }
}
