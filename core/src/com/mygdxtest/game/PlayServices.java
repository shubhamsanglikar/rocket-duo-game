package com.mygdxtest.game;

/**
 * Created by Shubham on 13-Mar-17.
 */
public interface PlayServices {

    public void signIn();

    public void signOut();

    public void rateGame();

    public void unlockAchievement(String ach);

    public void submitScore(int highScore);

    public void showAchievement();

    public void showScore();

    public boolean isSignedIn();

}
