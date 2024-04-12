package com.mygdxtest.game.States;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.mygdxtest.game.Flags;
import com.mygdxtest.game.LevelHandler;
import com.mygdxtest.game.MyGdxGame;
import com.mygdxtest.game.Sprites.Coin;
import com.mygdxtest.game.Sprites.ManageObstacle;
import com.mygdxtest.game.Sprites.Ball;

import com.badlogic.gdx.graphics.g2d.GlyphLayout;

import javax.naming.Context;

/**
 * Created by Shubham on 15-Dec-16.
 */
public class TwoBallsState extends State {

    int particleFlag;
    Vector2 camCoordinates;
    ParticleEffect emmission1,emmission2,snow,rain,collisionParticle;
    InputProcessor inputProcessor;
    Preferences prefs;
    public static Ball ball1,ball2;
    public static int difficulty=0;
    Texture obstacle,rocketTexture,progressTexture;
    ManageObstacle obstacleHandler;
    BitmapFont font, debugFont, levelName;
    GlyphLayout gameLabelLayout;
    public String scoreString,debugString;
    public static int score=0;
    int flag1=0,flag2=0,i=0,leftPower,rightPower;
    boolean leftTouched=false,rightTouched=false;
    float velX=0,velY=0;
    int current_obstacle=0;
    long startTime,elapsedTime;
    public static int hit=0;//hit =1 for left hit; hit = 2 for right hit;
    com.mygdxtest.game.States.GameStateManager gsm1;

    LevelHandler levelHandler;

    public TwoBallsState(com.mygdxtest.game.States.GameStateManager gsm) {
        super(gsm);
        MyGdxGame.adflag=0;
        gsm1 = gsm;
        Flags.nextLevelFlag = 0;
        prefs = Gdx.app.getPreferences("My Preferences");
        gameLabelLayout = new GlyphLayout();
        hit = 0;
        Flags.startFlag=0;
        score=0;
        difficulty=0;
        Flags.levelCompleteFlag=0;
        particleFlag=0;

        cam.setToOrtho(false, Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);//crops the world; false indicates the bottom left corner start

        inputProcessor = new InputProcessor() {
            @Override
            public boolean keyDown(int keycode) {

                if(keycode == Input.Keys.BACK || keycode == Input.Keys.ESCAPE){
                    Gdx.app.log("back","pressed");
                    gsm1.set(new MenuState(gsm1));
                    dispose();
                }

                if(keycode == Input.Keys.SPACE){
                    leftTouched=true;
                    leftPower=0;
                    //ball1.jump();
                }
                if(keycode == Input.Keys.ENTER){
                    rightTouched=true;
                    rightPower=0;
                    //ball2.jump();
                }
                return false;
            }

            @Override
            public boolean keyUp(int keycode) {
                if(keycode == Input.Keys.SPACE){
                    leftTouched=false;
                    ball1.jump(60*leftPower);
                }
                if(keycode == Input.Keys.ENTER){
                    rightTouched=false;
                    ball2.jump(60*rightPower);
                }
                return false;
            }

            @Override
            public boolean keyTyped(char character) {
                return false;
            }

            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                //Gdx.app.log("inputprocessor","touchdown");
                Flags.startFlag=1;

                if(screenX< cam.position.x + cam.viewportWidth/2 && hit==0) {
                    leftTouched=true;
                    leftPower=0;
                    if(levelHandler.getCurrentLevel()==levelHandler.LEFT_POWER_BOOST_LEVEL) {
                        ball1.jump(MyGdxGame.BOOSTED_JUMP_VELOCITY);
                        emmission1.reset();
                    }
                    else if(levelHandler.getCurrentLevel()==levelHandler.CONTROL_SWITCH_LEVEL){
                        ball2.jump(MyGdxGame.JUMP_VELOCITY);
                        emmission2.reset();
                    }
                    else if(levelHandler.getCurrentLevel()==levelHandler.OPPOSITE_CONTROLS_LEVEL){

                        ball1.jump(-Gdx.graphics.getHeight()/6);
                    }
                    else{
                        ball1.jump(MyGdxGame.JUMP_VELOCITY);
                        emmission1.reset();
                    }
                    snow.start();
                    rain.start();
                    if(particleFlag<=20)
                        particleFlag++;

                    collisionParticle.reset();
                }
                else if(screenX> cam.position.x + cam.viewportWidth/2 && hit==0){
                    rightTouched=true;
                    rightPower=0;

                    if(levelHandler.getCurrentLevel()==levelHandler.RIGHT_POWER_BOOST_LEVEL) {
                        ball2.jump(MyGdxGame.BOOSTED_JUMP_VELOCITY);
                        emmission2.reset();
                    }
                    else if(levelHandler.getCurrentLevel()==levelHandler.CONTROL_SWITCH_LEVEL){
                        ball1.jump(MyGdxGame.JUMP_VELOCITY);
                        emmission1.reset();
                    }
                    else if(levelHandler.getCurrentLevel()==levelHandler.OPPOSITE_CONTROLS_LEVEL){

                        ball2.jump(-Gdx.graphics.getHeight()/6);
                    }
                    else{
                        ball2.jump(MyGdxGame.JUMP_VELOCITY);
                        emmission2.reset();
                    }

                    if(particleFlag<=20)
                        particleFlag++;
                    collisionParticle.reset();
                }

                return false;
            }

            @Override
            public boolean touchUp(int screenX, int screenY, int pointer, int button) {
                if(screenX< cam.position.x + cam.viewportWidth/2) {
                    leftTouched=false;


                }
                else{
                    rightTouched=false;

                }
                return false;
            }

            @Override
            public boolean touchDragged(int screenX, int screenY, int pointer) {
                return false;
            }

            @Override
            public boolean mouseMoved(int screenX, int screenY) {
                return false;
            }

            @Override
            public boolean scrolled(int amount) {
                return false;
            }
        };
        Gdx.input.setInputProcessor(inputProcessor);




        camCoordinates = new Vector2(cam.position.x,cam.position.y);

        levelHandler = new LevelHandler(cam);
        obstacleHandler = new ManageObstacle(cam);

        obstacle = new Texture("obstacle_curved_9.png");
        rocketTexture = new Texture("rocket.png");
        progressTexture = new Texture("obstacle.png");
        if(levelHandler.getCurrentLevel()==levelHandler.OPPOSITE_CONTROLS_LEVEL){
            ball1 = new Ball(cam.position.x-(cam.viewportWidth/4)-cam.viewportWidth/16, cam.position.y, cam.viewportWidth/8, cam.viewportWidth/8,true);
            ball2 = new Ball(cam.position.x+(cam.viewportWidth/4)-cam.viewportWidth/16, cam.position.y, cam.viewportWidth/8, cam.viewportWidth/8,false);

        }
        else if(levelHandler.getCurrentLevel()==levelHandler.GUARDS_LEVEL){
            ball1 = new Ball(cam.position.x-(cam.viewportWidth/2), cam.position.y-(cam.viewportHeight/4), cam.viewportWidth/8, cam.viewportWidth/8,true);
            ball2 = new Ball(cam.position.x+(cam.viewportWidth/2)-cam.viewportWidth/8, cam.position.y-(cam.viewportHeight/4), cam.viewportWidth/8, cam.viewportWidth/8,false);

        }
        else{
            ball1 = new Ball(cam.position.x-(cam.viewportWidth/4)-cam.viewportWidth/16, cam.position.y-(cam.viewportHeight/4), cam.viewportWidth/8, cam.viewportWidth/8,true);
            ball2 = new Ball(cam.position.x+(cam.viewportWidth/4)-cam.viewportWidth/16, cam.position.y-(cam.viewportHeight/4), cam.viewportWidth/8, cam.viewportWidth/8,false);

        }



        if(levelHandler.getCurrentLevel() == levelHandler.LESS_GRAVITY_LEVEL){
            MyGdxGame.GRAVITY = Gdx.graphics.getHeight() / 128 * -1;
        }
        else if(levelHandler.getCurrentLevel() == levelHandler.OPPOSITE_CONTROLS_LEVEL) {
            MyGdxGame.GRAVITY = Gdx.graphics.getHeight() / 128;
        }
        else{
            MyGdxGame.GRAVITY = Gdx.graphics.getHeight() / 85 * -1;
        }

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("pirulen-rg.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = (int) MyGdxGame.pixelToDp(10);//30

        font = generator.generateFont(parameter);
        debugFont = generator.generateFont(parameter);
        levelName = generator.generateFont(parameter);
        levelName.setColor(Color.WHITE);
        generator.dispose();
        font.setColor(Color.YELLOW);

        debugFont =  new BitmapFont();
       // debugFont.getData().setScale(2, 2);
        debugFont.setColor(Color.WHITE);

        scoreString=" 0";
        debugString="init";
        collisionParticle = new ParticleEffect();
        collisionParticle.load(Gdx.files.internal("collision_particle"), Gdx.files.internal(""));
        rain = new ParticleEffect();
        rain.load(Gdx.files.internal("white_rain_particle"), Gdx.files.internal(""));
        snow = new ParticleEffect();
        snow.load(Gdx.files.internal("small_snow_particle"), Gdx.files.internal(""));
        emmission1 = new ParticleEffect();
        emmission2 = new ParticleEffect();
        if(levelHandler.getCurrentLevel() == levelHandler.OPPOSITE_CONTROLS_LEVEL){
            emmission1.load(Gdx.files.internal("rocket_tail_continuous"), Gdx.files.internal(""));
            emmission2.load(Gdx.files.internal("rocket_tail_continuous"), Gdx.files.internal(""));
        }
        else {
            emmission1.load(Gdx.files.internal("rocket_tail_yellow_1"), Gdx.files.internal(""));
            emmission2.load(Gdx.files.internal("rocket_tail_yellow_1"), Gdx.files.internal(""));
        }


        collisionParticle.start();

       // coin1 = new Coin(cam.position.x - 20, cam.position.y , 20, 20);
       // coin2 = new Coin(cam.position.x + 20, cam.position.y , 20, 20);

        MyGdxGame.adsController.hideBannerAd();

        //MyGdxGame.adsController.loadInterstitialAd();
        //MyGdxGame.adsController.loadRewardVideoAd();
    }



    @Override
    public void render(SpriteBatch batch) {
        batch.setProjectionMatrix(cam.combined);

        batch.begin();
        if (particleFlag >= 1) {

            snow.draw(batch, Gdx.graphics.getDeltaTime());
        }
        if (particleFlag >= 6) {

            rain.draw(batch, Gdx.graphics.getDeltaTime());
        }

        //collisionParticle.draw(batch,Gdx.graphics.getDeltaTime());
        emmission1.draw(batch);
        emmission2.draw(batch);

       // batch.draw(coin1.texture,coin1.position.x, coin1.position.y,coin1.size.x,coin1.size.y);
       // batch.draw(coin2.texture,coin2.position.x, coin2.position.y,coin2.size.x,coin2.size.y);
       // Gdx.app.log("ball1 pos",ball1.position.x+" "+ball1.position.y);
        batch.draw(rocketTexture, ball1.position.x, ball1.position.y, ball1.size.x, ball1.size.y);
        batch.draw(rocketTexture, ball2.position.x, ball2.position.y, ball2.size.x, ball2.size.y);
        batch.draw(progressTexture, cam.position.x-cam.viewportWidth/2, cam.position.y-2+cam.viewportHeight/2, getProgressSize() ,2);
        for(i=0;i<obstacleHandler.noOfObstacles;i++){

            if(levelHandler.getCurrentLevel() == levelHandler.DUAL_OBSTACLE_LEVEL){
                batch.draw(obstacle, obstacleHandler.dualObstacles[i].position.x, obstacleHandler.dualObstacles[i].position.y, obstacleHandler.dualObstacles[i].size.x, obstacleHandler.dualObstacles[i].size.y);
                batch.draw(obstacle, obstacleHandler.obstacles[i].position.x, obstacleHandler.obstacles[i].position.y, obstacleHandler.obstacles[i].size.x, obstacleHandler.obstacles[i].size.y);
            }
            else if(levelHandler.getCurrentLevel() == levelHandler.PASS_THROUGH_LEVEL){
                batch.draw(obstacle, obstacleHandler.staticObstacles[i].position.x, obstacleHandler.staticObstacles[i].position.y, obstacleHandler.staticObstacles[i].size.x, obstacleHandler.staticObstacles[i].size.y);
            }
            else if(levelHandler.getCurrentLevel() == levelHandler.GUARDS_LEVEL || levelHandler.getCurrentLevel() == levelHandler.GUARDS_2_LEVEL){
                batch.draw(obstacle, obstacleHandler.guardObstacles[i].position.x, obstacleHandler.guardObstacles[i].position.y, obstacleHandler.guardObstacles[i].size.x, obstacleHandler.guardObstacles[i].size.y);
                batch.draw(obstacle, obstacleHandler.obstacles[i].position.x, obstacleHandler.obstacles[i].position.y, obstacleHandler.obstacles[i].size.x, obstacleHandler.obstacles[i].size.y);
            }
            else{
                batch.draw(obstacle, obstacleHandler.obstacles[i].position.x, obstacleHandler.obstacles[i].position.y, obstacleHandler.obstacles[i].size.x, obstacleHandler.obstacles[i].size.y);
            }
        }
        //font.draw(batch, scoreString, 10, (cam.viewportHeight / 2) - 20 + cam.position.y);

        //debugFont.draw(batch, debugString, 30, (cam.viewportHeight / 2) - 20 + cam.position.y );
        if(Flags.levelCompleteFlag>=1){
            camCoordinates.y = cam.position.y;
            gameLabelLayout.setText(levelName, "LEVEL COMPLETE");
            levelName.draw(batch, gameLabelLayout, cam.position.x - gameLabelLayout.width / 2, camCoordinates.y + cam.viewportHeight / 3);
        }
        else{
            gameLabelLayout.setText(levelName, levelHandler.getLevelName());
            levelName.draw(batch, gameLabelLayout, cam.position.x - gameLabelLayout.width / 2, camCoordinates.y + cam.viewportHeight / 3);
        }

        if(particleFlag==30) {
           //Gdx.app.log("collision", "particle displayed");
            collisionParticle.draw(batch, Gdx.graphics.getDeltaTime());

        }

        batch.end();
    }




    void calculateScore(){
        if(levelHandler.getCurrentLevel() == levelHandler.PASS_THROUGH_LEVEL){
            if(ball1.position.y>=obstacleHandler.staticObstacles[current_obstacle].position.y + 20 && ball2.position.y>=obstacleHandler.staticObstacles[current_obstacle].position.y + 20){
                score++;
                current_obstacle=(current_obstacle+1)%obstacleHandler.noOfObstacles;

            }
        }
        else {//normal obs
            if (ball1.position.y >= obstacleHandler.obstacles[current_obstacle].position.y + 20 && ball2.position.y >= obstacleHandler.obstacles[current_obstacle].position.y + 20) {
                score++;
                current_obstacle = (current_obstacle + 1) % obstacleHandler.noOfObstacles;
            }
        }

    }

    void collisionHandler(){
        Rectangle intersection = new Rectangle();


            if(levelHandler.getCurrentLevel()==levelHandler.DUAL_OBSTACLE_LEVEL) {
            for(int i=0;i<obstacleHandler.noOfObstacles;i++){
                if (obstacleHandler.dualObstacles[i].bounds.overlaps(ball1.bounds)) {
                    hit = 1;
                    rain.dispose();
                    rocketTexture.dispose();
                    rocketTexture = new Texture("rocket_hit.png");
                    Intersector.intersectRectangles(obstacleHandler.dualObstacles[i].bounds, ball1.bounds, intersection);
                    collisionParticle.setPosition(intersection.getX(), intersection.getY());
                    collisionParticle.reset();
                    particleFlag = 30;
                }
                if (obstacleHandler.dualObstacles[i].bounds.overlaps(ball2.bounds)) {
                    hit = 2;
                    rain.dispose();
                    rocketTexture.dispose();
                    rocketTexture = new Texture("rocket_hit.png");
                    Intersector.intersectRectangles(obstacleHandler.dualObstacles[i].bounds, ball2.bounds, intersection);
                    collisionParticle.setPosition(intersection.getX(), intersection.getY());
                    particleFlag = 30;
                    collisionParticle.reset();
                }

                //normal obs
                if (obstacleHandler.obstacles[i].bounds.overlaps(ball1.bounds)) {
                    hit = 1;
                    rain.dispose();
                    rocketTexture.dispose();
                    rocketTexture = new Texture("rocket_hit.png");
                    Intersector.intersectRectangles(obstacleHandler.obstacles[i].bounds, ball1.bounds, intersection);
                    collisionParticle.setPosition(intersection.getX(), intersection.getY());
                    collisionParticle.reset();
                    particleFlag = 30;
                }
                if (obstacleHandler.obstacles[i].bounds.overlaps(ball2.bounds)) {
                    hit = 2;
                    rain.dispose();
                    rocketTexture.dispose();
                    rocketTexture = new Texture("rocket_hit.png");
                    Intersector.intersectRectangles(obstacleHandler.obstacles[i].bounds, ball2.bounds, intersection);
                    collisionParticle.setPosition(intersection.getX(), intersection.getY());
                    particleFlag = 30;
                    collisionParticle.reset();
                }
            }

        }
            else if(levelHandler.getCurrentLevel()==levelHandler.GUARDS_LEVEL || levelHandler.getCurrentLevel()==levelHandler.GUARDS_2_LEVEL) {
                for(int i=0;i<obstacleHandler.noOfObstacles;i++){
                    if (obstacleHandler.guardObstacles[i].bounds.overlaps(ball1.bounds)) {
                        hit = 1;
                        rain.dispose();
                        rocketTexture.dispose();
                        rocketTexture = new Texture("rocket_hit.png");
                        Intersector.intersectRectangles(obstacleHandler.guardObstacles[i].bounds, ball1.bounds, intersection);
                        collisionParticle.setPosition(intersection.getX(), intersection.getY());
                        collisionParticle.reset();
                        particleFlag = 30;
                    }
                    if (obstacleHandler.guardObstacles[i].bounds.overlaps(ball2.bounds)) {
                        hit = 2;
                        rain.dispose();
                        rocketTexture.dispose();
                        rocketTexture = new Texture("rocket_hit.png");
                        Intersector.intersectRectangles(obstacleHandler.guardObstacles[i].bounds, ball2.bounds, intersection);
                        collisionParticle.setPosition(intersection.getX(), intersection.getY());
                        particleFlag = 30;
                        collisionParticle.reset();
                    }

                    //normal obs
                    if (obstacleHandler.obstacles[i].bounds.overlaps(ball1.bounds)) {
                        hit = 1;
                        rain.dispose();
                        rocketTexture.dispose();
                        rocketTexture = new Texture("rocket_hit.png");
                        Intersector.intersectRectangles(obstacleHandler.obstacles[i].bounds, ball1.bounds, intersection);
                        collisionParticle.setPosition(intersection.getX(), intersection.getY());
                        collisionParticle.reset();
                        particleFlag = 30;
                    }
                    if (obstacleHandler.obstacles[i].bounds.overlaps(ball2.bounds)) {
                        hit = 2;
                        rain.dispose();
                        rocketTexture.dispose();
                        rocketTexture = new Texture("rocket_hit.png");
                        Intersector.intersectRectangles(obstacleHandler.obstacles[i].bounds, ball2.bounds, intersection);
                        collisionParticle.setPosition(intersection.getX(), intersection.getY());
                        particleFlag = 30;
                        collisionParticle.reset();
                    }
                }

            }
            else if(levelHandler.getCurrentLevel()==levelHandler.PASS_THROUGH_LEVEL){
                for(int i=0;i<obstacleHandler.noOfObstacles;i++){
                    if(obstacleHandler.staticObstacles[i].bounds.overlaps(ball1.bounds) ){
                        hit = 1;
                        rain.dispose();
                        rocketTexture.dispose();
                        rocketTexture = new Texture("rocket_hit.png");
                        Intersector.intersectRectangles(obstacleHandler.staticObstacles[i].bounds, ball1.bounds, intersection);
                        collisionParticle.setPosition(intersection.getX(), intersection.getY());
                        collisionParticle.reset();
                        particleFlag=30;
                    }
                    if(obstacleHandler.staticObstacles[i].bounds.overlaps(ball2.bounds)){
                        hit =2;
                        rain.dispose();
                        rocketTexture.dispose();
                        rocketTexture = new Texture("rocket_hit.png");
                        Intersector.intersectRectangles(obstacleHandler.staticObstacles[i].bounds, ball2.bounds, intersection);
                        collisionParticle.setPosition(intersection.getX(), intersection.getY());
                        particleFlag=30;
                        collisionParticle.reset();

                        // rain.dispose();
                    }
                }

            }
            else{
                for(int i=0;i<obstacleHandler.noOfObstacles;i++) {
                    if (obstacleHandler.obstacles[i].bounds.overlaps(ball1.bounds)) {
                        hit = 1;
                        rain.dispose();
                        rocketTexture.dispose();
                        rocketTexture = new Texture("rocket_hit.png");
                        Intersector.intersectRectangles(obstacleHandler.obstacles[i].bounds, ball1.bounds, intersection);
                        collisionParticle.setPosition(intersection.getX(), intersection.getY());
                        collisionParticle.reset();
                        particleFlag = 30;
                    }
                    if (obstacleHandler.obstacles[i].bounds.overlaps(ball2.bounds)) {
                        hit = 2;
                        rain.dispose();
                        rocketTexture.dispose();
                        rocketTexture = new Texture("rocket_hit.png");
                        Intersector.intersectRectangles(obstacleHandler.obstacles[i].bounds, ball2.bounds, intersection);
                        collisionParticle.setPosition(intersection.getX(), intersection.getY());
                        particleFlag = 30;
                        collisionParticle.reset();
                    }
                }
            }


    }



    @Override
    public void update(float deltaTime) {


            rain.getEmitters().first().setPosition(cam.position.x - cam.viewportWidth / 2, cam.position.y + 10 + cam.viewportHeight / 2);
            snow.getEmitters().first().setPosition(cam.position.x - cam.viewportWidth / 2, cam.position.y + cam.viewportHeight / 2);
            emmission1.update(Gdx.graphics.getDeltaTime());
        emmission2.update(Gdx.graphics.getDeltaTime());
            emmission1.getEmitters().first().setPosition(ball1.position.x + ball1.size.x / 2, ball1.position.y);
            emmission2.getEmitters().first().setPosition(ball2.position.x + ball1.size.x / 2, ball2.position.y);

            if (hit == 0 && Flags.startFlag == 1) {
                handleDifficulty();
                handleCamera();
                calculateScore();
                obstacleHandler.handleObstacles();
                cam.update();
                collisionHandler();
                obstacleHandler.update(deltaTime);
                scoreString = " " + score;
                startTime = System.currentTimeMillis();
                //debugString = " obstacleWidth: "+obstacleHandler.obstacles[current_obstacle].size.x + " diff: "+difficulty;
                //debugString = " vel: "+obstacleHandler.obstacles[current_obstacle].velocity.x + " diff: "+difficulty;
                debugString = "playLevelFlag: "+ Flags.playLevelFlag;
                //debugString = " diff: "+difficulty+" "+ball1.position.y;
                 //debugString = "W: "+Gdx.graphics.getWidth()+" H:"+Gdx.graphics.getHeight()+" Density: "+Gdx.graphics.getDensity();

                if(levelHandler.getCurrentLevel()== levelHandler.PASS_THROUGH_LEVEL){
                    managePassThroughObstacles();
                }

                if (leftTouched) {
                    leftPower += 1;
                }
                if (rightTouched) {
                    rightPower += 1;
                }

            } else if (ball1.position.y == (cam.position.y - cam.viewportHeight / 2) || ball2.position.y == (cam.position.y - cam.viewportHeight / 2) || hit == 3) {
                if (score > prefs.getInteger("bestScore")) {
                    prefs.putInteger("bestScore", score);
                    Flags.isHighScoreBroken = 1;
                    prefs.flush();
                }
                //MyGdxGame.adsController.hideBannerAd();
                levelHandler.incrementLevelAttempts(1);
                gsm.set(new com.mygdxtest.game.States.ReplayState(gsm));
                dispose();

            }

        if (levelHandler.getCurrentLevel() == levelHandler.SWIRL_LEVEL) {
                ball1.update_swirl_level(deltaTime, cam);
                ball2.update_swirl_level(deltaTime, cam);
            } else {
                ball1.update(deltaTime, cam);
                ball2.update(deltaTime, cam);
            }
            manageOppositeControlsLevelsGravity();
            checkIfLevelComplete();

    }


    public void manageOppositeControlsLevelsGravity() {
        if (levelHandler.getCurrentLevel() == levelHandler.OPPOSITE_CONTROLS_LEVEL && hit != 0) {
            MyGdxGame.GRAVITY = -Gdx.graphics.getHeight() / 50;
        }
    }

    public void managePassThroughObstacles(){
        if(ball1.position.y > obstacleHandler.staticObstacles[current_obstacle].position.y && ball2.position.y < obstacleHandler.staticObstacles[current_obstacle].position.y && obstacleHandler.staticObstacles[current_obstacle].position.x>0){
            obstacleHandler.staticObstacles[current_obstacle].position.x-= ManageObstacle.STATIC_OBSTACLE_SHIFT_VELOCITY;
        }
        if(ball1.position.y < obstacleHandler.staticObstacles[current_obstacle].position.y && ball2.position.y > obstacleHandler.staticObstacles[current_obstacle].position.y && obstacleHandler.staticObstacles[current_obstacle].position.x< cam.viewportWidth-obstacleHandler.staticObstacles[current_obstacle].size.x){
            obstacleHandler.staticObstacles[current_obstacle].position.x+= ManageObstacle.STATIC_OBSTACLE_SHIFT_VELOCITY;
        }
    }


    public void handleDifficulty(){
        //manageDistance();//actual distance is managed here ... level handler only changes flags.difficultyflag
        manageObstacle();
    }




    void manageDistance(){

            Gdx.app.log("flags","managedistance main");
            if(Flags.difficultyFlag ==1) {
                obstacleHandler.OBSTACLE_MIN_VELOCITY = obstacleHandler.OBSTACLE_VELOCITY_1;
                obstacleHandler.OBSTACLE_MAX_VELOCITY = obstacleHandler.OBSTACLE_VELOCITY_2;
                obstacleHandler.DISTANCE = cam.viewportHeight*3/4;
            }
            if(Flags.difficultyFlag ==2) {
                obstacleHandler.OBSTACLE_MIN_VELOCITY = obstacleHandler.OBSTACLE_VELOCITY_1;
                obstacleHandler.OBSTACLE_MAX_VELOCITY = obstacleHandler.OBSTACLE_VELOCITY_2;
                obstacleHandler.DISTANCE = cam.viewportHeight/2;
            }
            if(Flags.difficultyFlag ==3) {
                obstacleHandler.OBSTACLE_MIN_VELOCITY = obstacleHandler.OBSTACLE_VELOCITY_1;
                obstacleHandler.OBSTACLE_MAX_VELOCITY = obstacleHandler.OBSTACLE_VELOCITY_3;
                obstacleHandler.DISTANCE = cam.viewportHeight/2;
            }
        if(Flags.difficultyFlag ==4) {
            obstacleHandler.OBSTACLE_MIN_VELOCITY = obstacleHandler.OBSTACLE_VELOCITY_2;
            obstacleHandler.OBSTACLE_MAX_VELOCITY = obstacleHandler.OBSTACLE_VELOCITY_4;
            obstacleHandler.DISTANCE = cam.viewportHeight/2;
        }



    }

    void manageObstacle(){
        if(score<3){//3
            ManageObstacle.OBSTACLE_MIN_WIDTH= ManageObstacle.OBSTACLE_WIDTH_1;
            ManageObstacle.OBSTACLE_MAX_WIDTH= ManageObstacle.OBSTACLE_WIDTH_2;
        }
        else if(score<8){//8
            ManageObstacle.OBSTACLE_MAX_WIDTH= ManageObstacle.OBSTACLE_WIDTH_3;
        }
        else if(score<15){//15
            ManageObstacle.OBSTACLE_MIN_WIDTH= ManageObstacle.OBSTACLE_WIDTH_2;
            ManageObstacle.OBSTACLE_MAX_WIDTH= ManageObstacle.OBSTACLE_WIDTH_4;
            //ManageObstacle.OBSTACLE_MIN_VELOCITY=3;
        }
        else if(score<20){//20
            ManageObstacle.OBSTACLE_MIN_WIDTH= ManageObstacle.OBSTACLE_WIDTH_3;
            ManageObstacle.OBSTACLE_MAX_WIDTH= ManageObstacle.OBSTACLE_WIDTH_5;
        }
        //overrides (special cases)
        if(levelHandler.getCurrentLevel()==levelHandler.TOO_CLOSE){
            ManageObstacle.OBSTACLE_MIN_WIDTH= ManageObstacle.OBSTACLE_WIDTH_1;
            ManageObstacle.OBSTACLE_MAX_WIDTH= ManageObstacle.OBSTACLE_WIDTH_3;
        }

    }


    public float getProgressSize(){
        if(score == levelHandler.getLevelCompleteScore()){
            progressTexture.dispose();
            progressTexture=new Texture("progress_complete.png");
            return cam.viewportWidth;
        }
        if (score > 0) {
            //Gdx.app.log("progress", "" + (cam.viewportWidth * score / levelHandler.getLevelCompleteScore()));
            return (cam.viewportWidth * score / levelHandler.getLevelCompleteScore());
        }
        else{
            return 0;
        }
    }

    public void checkIfLevelComplete(){
        if(score == levelHandler.getLevelCompleteScore() && Flags.levelCompleteFlag==0){
            Flags.levelCompleteFlag=1;
            rocketsGoAwayAnimation();



        }else if(Flags.levelCompleteFlag==1){
            if(ball1.position.y > (cam.position.y+(cam.viewportHeight/2)) && ball2.position.y > (cam.position.y+(cam.viewportHeight/2))){
                Flags.levelCompleteFlag=2;
            }
        }
        else if(Flags.levelCompleteFlag==2){
            Flags.nextLevelFlag = 1;
            levelHandler.incrementLevelAttempts(1);
            gsm.set(new ReplayState(gsm));
            dispose();
        }

    }

    public void rocketsGoAwayAnimation(){
        if (levelHandler.getCurrentLevel() == levelHandler.OPPOSITE_CONTROLS_LEVEL) {

        }
        else {
            MyGdxGame.GRAVITY = 2;
            emmission1.load(Gdx.files.internal("rocket_tail_continuous"), Gdx.files.internal(""));
            emmission2.load(Gdx.files.internal("rocket_tail_continuous"), Gdx.files.internal(""));
        }

    }


    public void handleCamera(){

        cam.translate(0, MyGdxGame.CAMERA_TRANSLATION);


        /*
        if(ball1.position.y> cam.position.y + cam.viewportHeight*0.25){
            flag1 = 1;
           // Gdx.app.log("camera","flag=1");
        }
        if(ball2.position.y> cam.position.y + cam.viewportHeight*0.25){
            flag2 = 1;
            // Gdx.app.log("camera","flag=1");
        }
        if(flag1==1){
            cam.translate(0,MyGdxGame.CAMERA_TRANSLATION);
            Gdx.app.log("camera", "translating...(1)");
            if(ball1.position.y < cam.position.y){
                flag1 = 0;
               // Gdx.app.log("camera","flag=0");
            }
        }
        if(flag2==1){
            cam.translate(0,MyGdxGame.CAMERA_TRANSLATION);
            Gdx.app.log("camera", "translating...(2)");
            if(ball2.position.y < cam.position.y){
                flag2 = 0;
                // Gdx.app.log("camera","flag=0");
            }
        }
        */
    }


    @Override
    public void handleInput() {

    }

    @Override
    public void dispose() {
        rocketTexture.dispose();
        progressTexture.dispose();
        obstacle.dispose();

        font.dispose();
        debugFont.dispose();
        levelName.dispose();

        emmission1.dispose();
        emmission2.dispose();

        rain.dispose();
        snow.dispose();
        collisionParticle.dispose();

    }
}
