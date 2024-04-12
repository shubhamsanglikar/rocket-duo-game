package com.mygdxtest.game.Sprites;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by Shubham on 15-Dec-16.
 */
public class StaticObstacle {
    public Vector2 position,velocity,size;
    public boolean dirLeft;
    public Rectangle bounds;
    public StaticObstacle(float px, float py, float sx, float sy){
        position= new Vector2(px,py);
        size= new Vector2(sx,sy);
        bounds = new Rectangle(position.x, position.y, size.x, size.y);
    }



}
