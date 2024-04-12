package com.mygdxtest.game.Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Rectangle;
import com.mygdxtest.game.Flags;
import com.mygdxtest.game.LevelHandler;
import com.mygdxtest.game.MyGdxGame;
import com.mygdxtest.game.States.OneBallState;
import com.mygdxtest.game.States.TwoBallsState;

/**
 * Created by Shubham on 15-Dec-16.
 */
public class Ball {
    public Texture texture;
    public Vector2 position,velocity,size;
    public boolean goLeft,leftBall;
    public Rectangle bounds;

    public Ball(float w, float h,float sizeX, float sizeY, boolean leftBall){
       // texture = new Texture("rocket.png");
        this.leftBall=leftBall;
        if(leftBall){
            goLeft=true;
        }
        else{
            goLeft=false;
        }
        position= new Vector2(w,h);
        velocity = new Vector2(0, 0);
        size = new Vector2(sizeX,sizeY);
        bounds = new Rectangle(w+w/4, h, sizeX/2, sizeY);
       // Gdx.app.log("ball","created");
    }

    public void update(float deltaTime, OrthographicCamera cam){
        LevelHandler levelHandler= new LevelHandler(cam);

            if (position.y >= cam.position.y - cam.viewportHeight / 2 && (Flags.startFlag == 1))
                velocity.add(0, MyGdxGame.GRAVITY);


            velocity.scl(deltaTime);

            position.add(0, velocity.y);

            if (position.y < cam.position.y - cam.viewportHeight / 2) {
                position.y = cam.position.y - cam.viewportHeight / 2;
                TwoBallsState.hit = 3;
                OneBallState.hit = 3;
            }
            if (position.y > cam.position.y + cam.viewportHeight / 2 && levelHandler.getCurrentLevel() == levelHandler.OPPOSITE_CONTROLS_LEVEL && Flags.levelCompleteFlag==0) {
               position.y = cam.position.y + cam.viewportHeight / 2;
              TwoBallsState.hit = 3;
            }
            velocity.scl(1 / deltaTime);

        bounds.setPosition(position.x + size.x / 4, position.y);

    }


    public void update_swirl_level(float deltaTime, OrthographicCamera cam){
        if(leftBall){
            if(position.x<cam.position.x-(cam.viewportWidth/2)+10){
             //   Gdx.app.log("leftballgoleft","false");
                goLeft=false;
            }
            if(position.x>cam.position.x-10-size.x){
                goLeft=true;
            }
        }
        else{
            if(position.x<cam.position.x+10){
                goLeft=false;
            }
            if(position.x>cam.position.x+(cam.viewportWidth/2)-10-size.x){
                goLeft=true;
                //Gdx.app.log("nleftballgoleft","true");
            }
        }
        if(goLeft){
            position.x = position.x-1;
        }
        else{
            position.x = position.x + 1;
        }
        if(position.y >= cam.position.y - cam.viewportHeight/2 && (Flags.startFlag==1)) {
            velocity.add(0, MyGdxGame.GRAVITY);
        }
        velocity.scl(deltaTime);
        position.add(velocity.x, velocity.y);
        if(position.y<cam.position.y - cam.viewportHeight/2) {
            position.y = cam.position.y - cam.viewportHeight / 2;
            TwoBallsState.hit=3;
        }
        velocity.scl(1/deltaTime);
        bounds.setPosition(position.x+size.x/4 , position.y);
    }

    public void jump(int power){
        Gdx.app.log("jump", "called");

        //velocity.y = MyGdxGame.JUMP_VELOCITY;
        velocity.y = power;
    }


}
