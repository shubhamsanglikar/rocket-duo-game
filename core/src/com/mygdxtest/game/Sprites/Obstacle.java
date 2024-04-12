package com.mygdxtest.game.Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

import com.badlogic.gdx.math.Rectangle;

/**
 * Created by Shubham on 15-Dec-16.
 */
public class Obstacle {
    public Vector2 position,velocity,size;
    public boolean dirLeft;
    public Rectangle bounds;
    public Obstacle(float px, float py, float vx, float vy, float sx, float sy){
        Gdx.app.log("normal obs","created");
        position= new Vector2(px,py);
        velocity= new Vector2(vx,vy);
        size= new Vector2(sx,sy);
        dirLeft=false;
        bounds = new Rectangle(position.x, position.y, size.x, size.y);
    }



}
