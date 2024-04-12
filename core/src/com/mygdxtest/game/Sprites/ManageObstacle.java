package com.mygdxtest.game.Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.mygdxtest.game.LevelHandler;
import com.mygdxtest.game.MyGdxGame;

/**
 * Created by Shubham on 15-Dec-16.
 */
public class ManageObstacle {
    public static int noOfObstacles;

    public static float OBSTACLE_MAX_WIDTH = 60;//150
    public static float OBSTACLE_MIN_WIDTH = 40;//40
    public static float OBSTACLE_HEIGHT = 30;
    public static int OBSTACLE_MIN_VELOCITY = 6;
    public static int OBSTACLE_MAX_VELOCITY = 7;//4
    public static int STATIC_OBSTACLE_SHIFT_VELOCITY = (int)MyGdxGame.pixelToDp(1);

    public static int OBSTACLE_VELOCITY_0,OBSTACLE_VELOCITY_1,OBSTACLE_VELOCITY_2,OBSTACLE_VELOCITY_3,OBSTACLE_VELOCITY_4,OBSTACLE_VELOCITY_5,OBSTACLE_VELOCITY_6,OBSTACLE_VELOCITY_7,OBSTACLE_VELOCITY_8;
    public static float OBSTACLE_WIDTH_1,OBSTACLE_WIDTH_2,OBSTACLE_WIDTH_3,OBSTACLE_WIDTH_4,OBSTACLE_WIDTH_5,OBSTACLE_WIDTH_6,OBSTACLE_WIDTH_7,OBSTACLE_WIDTH_8;

    float prevY;
    int obsCnt;
    LevelHandler levelHandler;
    public static float DISTANCE;
    public Obstacle obstacles[],dualObstacles[],guardObstacles[];
    public StaticObstacle staticObstacles[];
    OrthographicCamera cam;
    public ManageObstacle(OrthographicCamera cam){
        this.cam= cam;

//        OBSTACLE_VELOCITY_1 = Gdx.graphics.getWidth()/180;//(int)MyGdxGame.pixelToDp(2);
//        OBSTACLE_VELOCITY_2 = Gdx.graphics.getWidth()/144;//(int)MyGdxGame.pixelToDp(2.5);
//        OBSTACLE_VELOCITY_3 = Gdx.graphics.getWidth()/120;//(int)MyGdxGame.pixelToDp(3);
//        OBSTACLE_VELOCITY_4 = Gdx.graphics.getWidth()/100;//(int)MyGdxGame.pixelToDp(3.5);
//        OBSTACLE_VELOCITY_5 = Gdx.graphics.getWidth()/90;//(int)MyGdxGame.pixelToDp(4);
//        OBSTACLE_VELOCITY_6 = Gdx.graphics.getWidth()/80;//(int)MyGdxGame.pixelToDp(4.5);
//        OBSTACLE_VELOCITY_7 = Gdx.graphics.getWidth()/70;//(int)MyGdxGame.pixelToDp(5);

        OBSTACLE_VELOCITY_0 = (int)MyGdxGame.pixelToDp(1.5f);
        OBSTACLE_VELOCITY_1 = (int)MyGdxGame.pixelToDp(2);
        OBSTACLE_VELOCITY_2 = (int)MyGdxGame.pixelToDp(2.5f);
        OBSTACLE_VELOCITY_3 = (int)MyGdxGame.pixelToDp(3);
        OBSTACLE_VELOCITY_4 =(int)MyGdxGame.pixelToDp(3.5f);
        OBSTACLE_VELOCITY_5 = (int)MyGdxGame.pixelToDp(4);
        OBSTACLE_VELOCITY_6 = (int)MyGdxGame.pixelToDp(4.5f);
        OBSTACLE_VELOCITY_7 =(int)MyGdxGame.pixelToDp(5);

        OBSTACLE_WIDTH_1 = cam.viewportWidth/6;
        OBSTACLE_WIDTH_2 = cam.viewportWidth/5;
        OBSTACLE_WIDTH_3 = cam.viewportWidth/3;
        OBSTACLE_WIDTH_4 = cam.viewportWidth*2/5;
        OBSTACLE_WIDTH_5 = cam.viewportWidth/2;


        OBSTACLE_MIN_VELOCITY = OBSTACLE_VELOCITY_1;
        OBSTACLE_MAX_VELOCITY = OBSTACLE_VELOCITY_1;

        OBSTACLE_MIN_WIDTH = OBSTACLE_WIDTH_1;
        OBSTACLE_MAX_WIDTH = OBSTACLE_WIDTH_1;
        DISTANCE = cam.viewportHeight*3/4;
        OBSTACLE_HEIGHT = cam.viewportWidth/16;




        prevY = cam.position.y;
        obsCnt=0;

        levelHandler= new LevelHandler(cam);
        if(levelHandler.getCurrentLevel() == levelHandler.TOO_CLOSE){
            noOfObstacles = 5;
        }
        else{
            noOfObstacles = 3;
        }

        if(levelHandler.getCurrentLevel() == levelHandler.DUAL_OBSTACLE_LEVEL){
            dualObstacles = new Obstacle[noOfObstacles];
            obstacles = new Obstacle[noOfObstacles];
            for (int i = 0; i < noOfObstacles; i++) {
                //obstacle creation
                obstacles[i] = new Obstacle(getRandom(0, (int) (cam.viewportWidth - 20)), prevY + DISTANCE, getRandom(OBSTACLE_MIN_VELOCITY, OBSTACLE_MAX_VELOCITY), 0, getRandom((int) OBSTACLE_MIN_WIDTH, (int) OBSTACLE_MAX_WIDTH), OBSTACLE_HEIGHT);
                dualObstacles[i] = new Obstacle(getRandom(0, (int) (cam.viewportWidth - 20)), prevY +DISTANCE+ 2*OBSTACLE_HEIGHT, getRandom(OBSTACLE_MIN_VELOCITY, OBSTACLE_MAX_VELOCITY), 0, getRandom((int) OBSTACLE_MIN_WIDTH, (int) OBSTACLE_MAX_WIDTH), OBSTACLE_HEIGHT);
                prevY += DISTANCE;
                obsCnt++;
            }
        }
        else if(levelHandler.getCurrentLevel() == levelHandler.GUARDS_LEVEL || levelHandler.getCurrentLevel() == levelHandler.GUARDS_2_LEVEL){
            guardObstacles = new Obstacle[noOfObstacles];
            obstacles = new Obstacle[noOfObstacles];
            for (int i = 0; i < noOfObstacles; i++) {
                //obstacle creation
                obstacles[i] = new Obstacle(0, prevY + DISTANCE, getRandom(OBSTACLE_MIN_VELOCITY, OBSTACLE_MAX_VELOCITY), 0, getRandom((int) OBSTACLE_MIN_WIDTH, (int) OBSTACLE_MAX_WIDTH), OBSTACLE_HEIGHT);
                guardObstacles[i] = new Obstacle(cam.position.x +cam.viewportWidth/2 - obstacles[i].size.x, prevY +DISTANCE, obstacles[i].velocity.x, 0, obstacles[i].size.x, OBSTACLE_HEIGHT);
                guardObstacles[i].dirLeft=true;
                prevY += DISTANCE;
                obsCnt++;
            }
        }
        else if(levelHandler.getCurrentLevel() == levelHandler.PASS_THROUGH_LEVEL){
            staticObstacles = new StaticObstacle[noOfObstacles];
            for (int i = 0; i < noOfObstacles; i++) {
                //obstacle creation
                staticObstacles[i] = new StaticObstacle(0,   prevY + DISTANCE,  cam.viewportWidth/2 , OBSTACLE_HEIGHT);
                if(i%2==0){
                    staticObstacles[i].position.x = cam.viewportWidth/2;
                }
                prevY += DISTANCE;
                obsCnt++;
            }
        }
        else {
            obstacles = new Obstacle[noOfObstacles];
            for (int i = 0; i < noOfObstacles; i++) {
                //obstacle creation
                obstacles[i] = new Obstacle(getRandom(0, (int) (cam.viewportWidth - 20)), prevY + DISTANCE, getRandom(OBSTACLE_MIN_VELOCITY, OBSTACLE_MAX_VELOCITY), 0, getRandom((int) OBSTACLE_MIN_WIDTH, (int) OBSTACLE_MAX_WIDTH), OBSTACLE_HEIGHT);
                prevY += DISTANCE;
                obsCnt++;
            }
        }


    }

    public void handleObstacles(){
        for(int i=0;i<noOfObstacles;i++){
            if(levelHandler.getCurrentLevel() == levelHandler.DUAL_OBSTACLE_LEVEL) {
                dualObstacles[i].bounds.setPosition(dualObstacles[i].position);
                obstacles[i].bounds.setPosition(obstacles[i].position);
                    if(obstacles[i].position.y < cam.position.y-(cam.viewportHeight/2)-obstacles[i].size.y  &&  obsCnt < levelHandler.getLevelCompleteScore()) {
                        dualObstacles[i] = createDualObstacle();
                        obstacles[i] = createObstacle();
                        prevY+=DISTANCE;
                        obsCnt++;
                        Gdx.app.log("Obstacle","updated");
                    }
                }
            else if(levelHandler.getCurrentLevel() == levelHandler.GUARDS_LEVEL || levelHandler.getCurrentLevel() == levelHandler.GUARDS_2_LEVEL) {
                guardObstacles[i].bounds.setPosition(guardObstacles[i].position);
                obstacles[i].bounds.setPosition(obstacles[i].position);
                if(obstacles[i].position.y < cam.position.y-(cam.viewportHeight/2)-obstacles[i].size.y  &&  obsCnt < levelHandler.getLevelCompleteScore()) {
                    obstacles[i] = new Obstacle(0, prevY + DISTANCE, getRandom(OBSTACLE_MIN_VELOCITY, OBSTACLE_MAX_VELOCITY), 0, getRandom((int) OBSTACLE_MIN_WIDTH, (int) OBSTACLE_MAX_WIDTH), OBSTACLE_HEIGHT);
                    guardObstacles[i] = new Obstacle(cam.position.x +cam.viewportWidth/2 - obstacles[i].size.x, prevY +DISTANCE, obstacles[i].velocity.x, 0, obstacles[i].size.x, OBSTACLE_HEIGHT);
                    guardObstacles[i].dirLeft=true;
                    prevY+=DISTANCE;
                    obsCnt++;
                    Gdx.app.log("Obstacle","updated");
                }
            }
            else if(levelHandler.getCurrentLevel() == levelHandler.PASS_THROUGH_LEVEL){
                staticObstacles[i].bounds.setPosition(staticObstacles[i].position);
                if(staticObstacles[i].position.y < cam.position.y-(cam.viewportHeight/2)-staticObstacles[i].size.y  &&  obsCnt < levelHandler.getLevelCompleteScore()) {
                    staticObstacles[i]=createStaticObstacle();
                    if(i%2==0){
                        staticObstacles[i].position.x = cam.viewportWidth/2;
                    }
                    prevY+=DISTANCE;
                    obsCnt++;
                    Gdx.app.log("Obstacle","updated");
                }
            }
            else{
                obstacles[i].bounds.setPosition(obstacles[i].position);
                if(obstacles[i].position.y < cam.position.y-(cam.viewportHeight/2)-obstacles[i].size.y  &&  obsCnt < levelHandler.getLevelCompleteScore()) {
                    obstacles[i] = createObstacle();
                    prevY+=DISTANCE;
                    obsCnt++;
                    Gdx.app.log("Obstacle","updated");
                }
            }

        }
    }

    Obstacle createObstacle(){
        //obstacle creation
        Obstacle obs = new Obstacle(getRandom(0, (int) (cam.viewportWidth - 20)), (prevY+DISTANCE), getRandom(OBSTACLE_MIN_VELOCITY,OBSTACLE_MAX_VELOCITY), 0, getRandom((int)OBSTACLE_MIN_WIDTH,(int)OBSTACLE_MAX_WIDTH), OBSTACLE_HEIGHT);
        return obs;
    }

    Obstacle createDualObstacle(){
        //obstacle creation
        Obstacle obs = new Obstacle(getRandom(0, (int) (cam.viewportWidth - 20)), (prevY+DISTANCE+2*OBSTACLE_HEIGHT), getRandom(OBSTACLE_MIN_VELOCITY,OBSTACLE_MAX_VELOCITY), 0, getRandom((int)OBSTACLE_MIN_WIDTH,(int)OBSTACLE_MAX_WIDTH), OBSTACLE_HEIGHT);
        //prevY+=DISTANCE;
        //obsCnt++;
        return obs;
    }



    StaticObstacle createStaticObstacle(){
        StaticObstacle obs = new StaticObstacle(0,prevY+DISTANCE,cam.viewportWidth/2,OBSTACLE_HEIGHT);
        return obs;
    }

    int getRandom(int min, int max){
        return(min + (int)(Math.random() * (max-min)));
    }

    public void update(float deltaTime){

        if(levelHandler.getCurrentLevel() == levelHandler.DUAL_OBSTACLE_LEVEL) {
            updateDualObstacle();
            updateObstacles();
        }
        else if(levelHandler.getCurrentLevel() == levelHandler.GUARDS_LEVEL || levelHandler.getCurrentLevel() == levelHandler.GUARDS_2_LEVEL) {
            updateGuardObstacles();
            updateObstacles();
        }
        else if(levelHandler.getCurrentLevel() == levelHandler.PASS_THROUGH_LEVEL) {
            //updateStaticObstacles();
        }
        else{
            updateObstacles();
        }


    }

    public void updateObstacles(){
        for(int i=0;i<noOfObstacles;i++){
            if(obstacles[i].position.x<=0){
                obstacles[i].dirLeft=false;
            }
            if(obstacles[i].position.x >= cam.viewportWidth/2+cam.position.x-obstacles[i].size.x){
                obstacles[i].dirLeft=true;
            }
            if(obstacles[i].dirLeft){
                obstacles[i].position.x = obstacles[i].position.x-obstacles[i].velocity.x;
            }
            else{
                obstacles[i].position.x = obstacles[i].position.x+obstacles[i].velocity.x;
            }
        }

    }

    public void updateGuardObstacles(){
        for(int i=0;i<noOfObstacles;i++){
            if(guardObstacles[i].position.x<=0){
                guardObstacles[i].dirLeft=false;
            }
            if(guardObstacles[i].position.x >= cam.viewportWidth/2+cam.position.x-guardObstacles[i].size.x){
                guardObstacles[i].dirLeft=true;
            }
            if(guardObstacles[i].dirLeft){
                guardObstacles[i].position.x = guardObstacles[i].position.x-guardObstacles[i].velocity.x;
            }
            else{
                guardObstacles[i].position.x = guardObstacles[i].position.x+guardObstacles[i].velocity.x;
            }
        }

    }

    public void updateDualObstacle(){
        for (int i = 0; i < noOfObstacles; i++) {
            if (dualObstacles[i].position.x <= 0) {
                dualObstacles[i].dirLeft = false;
            }
            if (dualObstacles[i].position.x >= cam.viewportWidth / 2 + cam.position.x - dualObstacles[i].size.x) {
                dualObstacles[i].dirLeft = true;
            }
            if (dualObstacles[i].dirLeft) {
                dualObstacles[i].position.x = dualObstacles[i].position.x - dualObstacles[i].velocity.x;
            } else {
                dualObstacles[i].position.x = dualObstacles[i].position.x + dualObstacles[i].velocity.x;
            }
        }
    }




}
