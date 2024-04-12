package com.mygdxtest.game;

/**
 * Created by Shubham on 25-Jan-17.
 */
public class Flags {

    public static int startFlag = 0;
    public static int showWatchAdButton = 0;
    public static int skipEnabled = 0;
    public static int coins_from = -1;
    public static int coins_to = -1;
    public static int coins_added_rewarded_video = 0;
    public static String function_after_coins_animation = "null";
    public static int difficultyFlag = 1;
    public static int skipLevelFlag = 0;
    public static int levelCompleteFlag = 0;
    public static int nextLevelFlag = 0;//to change completion status on replay screen
    public static int playLevelFlag = -1;//playing level from levels screen;
    public static int isHighScoreBroken = 0;
    /*
        difficultyFlag:
            1: distance = 75% of screen
               OBSTACLE_MIN_VELOCITY = OBSTACLE_VELOCITY_1;
               OBSTACLE_MAX_VELOCITY = OBSTACLE_VELOCITY_2;

            2: distance = 50% of screen
               OBSTACLE_MIN_VELOCITY = OBSTACLE_VELOCITY_1;
               OBSTACLE_MAX_VELOCITY = OBSTACLE_VELOCITY_2;

            3: distance = 50% of screen
               OBSTACLE_MIN_VELOCITY = OBSTACLE_VELOCITY_1;
               OBSTACLE_MAX_VELOCITY = OBSTACLE_VELOCITY_3;

            4: distance = 40% of screen
               OBSTACLE_MIN_VELOCITY = OBSTACLE_VELOCITY_1;
               OBSTACLE_MAX_VELOCITY = OBSTACLE_VELOCITY_4;

     */
}
