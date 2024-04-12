package com.mygdxtest.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.mygdxtest.game.Sprites.ManageObstacle;
import com.mygdxtest.game.States.TwoBallsState;

/**
 * Created by Shubham on 24-Jan-17.
 */
public class LevelHandler {
    public static int PASS_THROUGH_LEVEL = 2;//1
    public static int GUARDS_LEVEL = 3;//1
    public static int SWIRL_LEVEL = 4;//3
    public static int LEFT_POWER_BOOST_LEVEL = 5;//4
    public static int LESS_GRAVITY_LEVEL = 7;//7
    public static int RIGHT_POWER_BOOST_LEVEL = 8;//NA
    public static int CONTROL_SWITCH_LEVEL = 10;//5
    public static int DUAL_OBSTACLE_LEVEL = 12;//8
    public static int TOO_CLOSE = 13;//1
    public static int OPPOSITE_CONTROLS_LEVEL = 14;//11
    public static int SPEEDY_LEVEL = 15;//1
    public static int GUARDS_2_LEVEL = 17;//1
    public static int LAST_LEVEL = 18;//1
    public static Preferences prefs;
    private static int currentLevel;
    private int levelCompleteScore[] = {3,3, 3, 5, 5, 10, 20, 12, 12, 15, 10,15, 10, 15,12,20,30,7,500};
    private static int levelAttempts = 0;
    public String levelNames[] = {
            "WARMUP",//0
            "EASY",
            "ONE AT A TIME",
            "GUARDS",
            "SWIRL",
            "LEFT POWER BOOST",//5
            "LONG",
            "LESS GRAVITY",
            "RIGHT POWER BOOST",
            "PROFESSIONAL",
            "CONTROLS SWITCH",//10
            "WORLD CLASS",
            "DUAL OBSTACLE",
            "TOO CLOSE",
            "OPPOSITE CONTROLS",
            "SPEEDY",//15
            "KEEP CALM",
            "GUARDS 2",
            "ENDLESS",

    };


 public LevelHandler(OrthographicCamera cam){

     prefs = Gdx.app.getPreferences("My Preferences");
     currentLevel = prefs.getInteger("currentLevel", 0);
     //levelAttempts = prefs.getInteger("levelAttempts",0);
     Gdx.app.log("level attempts", "constructor " + prefs.getInteger("levelAttempts", 0));

     setDifficultyFlag(cam);
     Flags.playLevelFlag = getCurrentLevel();



 }

    public void setDifficultyFlag(OrthographicCamera cam){
        if(getCurrentLevel()<=5){
            Flags.difficultyFlag=1;
        }
        else if(getCurrentLevel()<=9){
            Flags.difficultyFlag=3;
        }
        else if(getCurrentLevel()<=13){
            Flags.difficultyFlag=4;
        }
        else if(getCurrentLevel()<=16){
            Flags.difficultyFlag=5;
        }

        if(getCurrentLevel()==DUAL_OBSTACLE_LEVEL){
            Flags.difficultyFlag=2;
        }
        if(getCurrentLevel()==GUARDS_2_LEVEL){
            Flags.difficultyFlag=0;
        }
        if(getCurrentLevel()==RIGHT_POWER_BOOST_LEVEL){
            Flags.difficultyFlag=1;
        }
        if(getCurrentLevel() == TOO_CLOSE){
            Flags.difficultyFlag=6;
        }
        if(getCurrentLevel() == SPEEDY_LEVEL){
            Flags.difficultyFlag=5;
        }
        if(getCurrentLevel() == OPPOSITE_CONTROLS_LEVEL){
            Flags.difficultyFlag=2;
        }if(getCurrentLevel() == LAST_LEVEL){
            Flags.difficultyFlag=7;
        }


        Gdx.app.log("flags","levelhandler ");
        if(Flags.difficultyFlag ==0) {
            ManageObstacle.OBSTACLE_MIN_VELOCITY = ManageObstacle.OBSTACLE_VELOCITY_0;
            ManageObstacle.OBSTACLE_MAX_VELOCITY = ManageObstacle.OBSTACLE_VELOCITY_0;
            ManageObstacle.DISTANCE = cam.viewportHeight*3/4;
        }
        if(Flags.difficultyFlag ==1) {
            ManageObstacle.OBSTACLE_MIN_VELOCITY = ManageObstacle.OBSTACLE_VELOCITY_1;
            ManageObstacle.OBSTACLE_MAX_VELOCITY = ManageObstacle.OBSTACLE_VELOCITY_1;
            ManageObstacle.DISTANCE = cam.viewportHeight*3/4;
        }
        if(Flags.difficultyFlag ==2) {//dual obs
            ManageObstacle.OBSTACLE_MIN_VELOCITY = ManageObstacle.OBSTACLE_VELOCITY_1;
            ManageObstacle.OBSTACLE_MAX_VELOCITY = ManageObstacle.OBSTACLE_VELOCITY_2;
            ManageObstacle.DISTANCE = cam.viewportHeight*3/5;
        }
        if(Flags.difficultyFlag ==3) {
            ManageObstacle.OBSTACLE_MIN_VELOCITY = ManageObstacle.OBSTACLE_VELOCITY_1;
            ManageObstacle.OBSTACLE_MAX_VELOCITY = ManageObstacle.OBSTACLE_VELOCITY_2;
            ManageObstacle.DISTANCE = cam.viewportHeight/2;
        }
        if(Flags.difficultyFlag ==4) {
            ManageObstacle.OBSTACLE_MIN_VELOCITY = ManageObstacle.OBSTACLE_VELOCITY_2;
            ManageObstacle.OBSTACLE_MAX_VELOCITY = ManageObstacle.OBSTACLE_VELOCITY_3;
            ManageObstacle.DISTANCE = cam.viewportHeight/2;
        }
        if(Flags.difficultyFlag ==5) {
            ManageObstacle.OBSTACLE_MIN_VELOCITY = ManageObstacle.OBSTACLE_VELOCITY_1;
            ManageObstacle.OBSTACLE_MAX_VELOCITY = ManageObstacle.OBSTACLE_VELOCITY_3;
            ManageObstacle.DISTANCE = cam.viewportHeight*2/5;
        }
        if(Flags.difficultyFlag ==6) {//tooclose
            ManageObstacle.OBSTACLE_MIN_VELOCITY = ManageObstacle.OBSTACLE_VELOCITY_1;
            ManageObstacle.OBSTACLE_MAX_VELOCITY = ManageObstacle.OBSTACLE_VELOCITY_3;
            ManageObstacle.DISTANCE = cam.viewportHeight*33/100;
        }
        if(Flags.difficultyFlag ==7) {//last level
            ManageObstacle.OBSTACLE_MIN_VELOCITY = ManageObstacle.OBSTACLE_VELOCITY_1;
            ManageObstacle.OBSTACLE_MAX_VELOCITY = ManageObstacle.OBSTACLE_VELOCITY_3;
            ManageObstacle.DISTANCE = cam.viewportHeight*2/5;
        }


        if(getCurrentLevel() == PASS_THROUGH_LEVEL){
            MyGdxGame.CAMERA_TRANSLATION = MyGdxGame.pixelToDp(0.8f);
        }
        else if(getCurrentLevel() == SPEEDY_LEVEL){
            ManageObstacle.OBSTACLE_MIN_VELOCITY = ManageObstacle.OBSTACLE_VELOCITY_2;
            MyGdxGame.CAMERA_TRANSLATION = MyGdxGame.pixelToDp(1.5f);
        }
        else{
            MyGdxGame.CAMERA_TRANSLATION = MyGdxGame.pixelToDp(1);
        }

    }

    public void incrementLevelAttempts(int no){

        //prefs.putInteger("levelAttempts", getLevelAttempts()+no);
       // prefs.flush();
       // Gdx.app.log("level attempts", "updated " + prefs.getInteger("levelAttempts",0));

        addAttemptsToLevel(getCurrentLevel(),no);
    }

    public int getCurrentLevel(){
        if(Flags.playLevelFlag != -1){
            return Flags.playLevelFlag;
        }
        else{
            return getActualCurrentLevel();
        }


    }

    public int getActualCurrentLevel(){
        Gdx.app.log("level gettingActuallevel", " " + prefs.getInteger("currentLevel", 0));
        return prefs.getInteger("currentLevel", 0);

    }

    public void goToNextLevel(){
        if(Flags.playLevelFlag < getActualCurrentLevel()){
            Flags.playLevelFlag++;
           // prefs.putInteger("levelAttempts", 0);//think....
            prefs.flush();

            //Gdx.app.log("level goToNextLevel", " " + prefs.getInteger("currentLevel", 0));
           // Gdx.app.log("level Flag & actcurrentLevel", Flags.playLevelFlag + " " + prefs.getInteger("currentLevel", 0));
        }
        else if(Flags.playLevelFlag == getActualCurrentLevel()){
            goToActualNextLevel();

        }

    }


    public void goToActualNextLevel(){
        if(getActualCurrentLevel()<levelCompleteScore.length-1) {
            updateTotalScoreOnComplete((getActualCurrentLevel() + 1) * 100);//before playLevelFlag++;
            Flags.playLevelFlag++;
            int temp = getActualCurrentLevel() + 1;
            prefs.putInteger("currentLevel", temp);
           // prefs.putInteger("levelAttempts", 0);

            prefs.flush();
            Gdx.app.log("level goToActualNextLevel", " " + prefs.getInteger("currentLevel",0));

        }
    }



    public void goToPrevLevel(){//actual prev level
        if(getActualCurrentLevel()> 0) {
            int temp = getActualCurrentLevel() - 1;
            prefs.putInteger("currentLevel", temp);
            //prefs.putInteger("levelAttempts", 0);

            prefs.flush();
            Gdx.app.log("go to actual prev level", " " + prefs.getInteger("currentLevel",0));
        }

    }

    public void addAttemptsToLevel(int level,int no){
        int attempts=prefs.getInteger("attempts"+level,0);
        prefs.putInteger("attempts"+level,attempts+no);
        prefs.flush();
    }

    public int getLevelAttempts(){
        int level = getCurrentLevel();
        return prefs.getInteger("attempts"+level,0);
    }

    public int getLevelCompleteScore(){
        return levelCompleteScore[Flags.playLevelFlag];
    }

    public String getLevelName(){
        return levelNames[Flags.playLevelFlag];
    }


    public int getTotalScore(){
        return prefs.getInteger("totalScore",0);
    }
    public void addTotalScore(int no){
        prefs.putInteger("totalScore",getTotalScore()+no);
        prefs.flush();
    }
    public void updateTotalScoreOnComplete(int no){
        prefs.putInteger("totalScore",no);

        prefs.flush();
        Gdx.app.log("updated score", "" + getTotalScore());
    }

    public int getLevelHighScore(int level){
        return prefs.getInteger("levelHighScore"+level,0);
    }
    public void manageLevelHighScore(int level,int completedPercentage){
        int lhs = getLevelHighScore(level);
        if(lhs<completedPercentage){
            prefs.putInteger("levelHighScore"+level,completedPercentage);
            prefs.flush();
            Gdx.app.log("level high score updated",""+getLevelHighScore(level));
        }
    }
    public void updateTotalScore(){
        int ts = ((getActualCurrentLevel())*100)+getLevelHighScore(getActualCurrentLevel());
        prefs.putInteger("totalScore",ts);
        prefs.flush();

    }

    public boolean getSound(){
        return prefs.getBoolean("playSound",true);
    }

    public void setSound(boolean sound){
        prefs.putBoolean("playSound",sound);
        prefs.flush();
    }

    public static int getCoins(){
        return prefs.getInteger("coins",0);
    }

    public static void putCoins(int c){
        prefs.putInteger("coins",c);
        prefs.flush();
    }

    public static void addCoins(int c){
        putCoins(getCoins() + c);
    }

    public static void subtractCoins(int c){
        putCoins(getCoins() - c);
    }

    public static void markLevelAsSkipped(int level){
        prefs.putInteger("isSkipped"+level,1);
        prefs.flush();
    }

    public static void unmarkLevelAsSkipped(int level){
        prefs.putInteger("isSkipped"+level,0);
        prefs.flush();
    }

    public static boolean checkIfLevelSkipped(int level){
        if(prefs.getInteger("isSkipped"+level,-1)!= -1){
            return true;
        }
        else{
            return false;
        }
    }



}
