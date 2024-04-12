package com.mygdxtest.game.States;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.Stack;

/**
 * Created by Shubham on 15-Dec-16.
 */
public class GameStateManager {
    protected Stack<State> stack= new Stack<State>();

    public void push(State state){
        stack.push(state);
    }
    public void pop(){
        stack.pop();
    }
    public void set(State state){
        stack.pop();
        //System.gc();
        stack.push(state);
    }

    public void render(SpriteBatch batch){
        stack.peek().render(batch);
    }
    public void update(float deltaTime){
        stack.peek().update(deltaTime);
    }
}
