package com.mygdxtest.game.Sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by Shubham on 08-Jan-17.
 */
public class Coin {
    public Texture texture;
    public Vector2 position,velocity,size;
    public boolean dirLeft;
    public Rectangle bounds;
    public Coin(float px, float py, float sx, float sy){
        texture = new Texture("coin.png");
        position= new Vector2(px,py);
        size= new Vector2(sx,sy);
        dirLeft=false;
        bounds = new Rectangle(position.x, position.y, size.x, size.y);
    }
}
