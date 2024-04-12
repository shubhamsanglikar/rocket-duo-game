package com.mygdxtest.game.States;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.mygdxtest.game.Flags;
import com.mygdxtest.game.LevelHandler;
import com.mygdxtest.game.MyGdxGame;
import com.mygdxtest.game.Sprites.Ball;
import com.mygdxtest.game.Sprites.ManageObstacle;

/**
 * Created by Shubham on 15-Dec-16.
 */
public class OneBallState extends State {
    int particleFlag;
    Vector2 camCoordinates;
    ParticleEffect emmission1,snow,rain,collisionParticle;
    InputProcessor inputProcessor;
    Preferences prefs;
    public static Ball ball1;

    public static int difficulty=0;
    Texture obstacle,rocketTexture,progressTexture;
    ManageObstacle obstacleHandler;
    BitmapFont font, debugFont, levelName;
    GlyphLayout gameLabelLayout;
    public String scoreString,debugString;
    public static int score=0;
    int i;
    int current_obstacle=0;
    long startTime,elapsedTime;
    public static int hit=0;//hit =1 for left hit; hit = 2 for right hit;
    LevelHandler levelHandler;

    public OneBallState(com.mygdxtest.game.States.GameStateManager gsm) {
        super(gsm);
        Flags.nextLevelFlag = 0;
        prefs = Gdx.app.getPreferences("My Preferences");
        gameLabelLayout = new GlyphLayout();
        hit=0;
        Flags.startFlag=0;
        score=0;
        difficulty=0;
        Flags.levelCompleteFlag=0;
        particleFlag=0;

        cam.setToOrtho(false, Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);//crops the world; false indicates the bottom left corner start
        inputProcessor = new InputProcessor() {
            @Override
            public boolean keyDown(int keycode) {

                return false;
            }

            @Override
            public boolean keyUp(int keycode) {

                return false;
            }

            @Override
            public boolean keyTyped(char character) {
                return false;
            }

            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                Gdx.app.log("inputprocessor","touchdown");
                Flags.startFlag=1;
                if(hit==0) {
                   // MyGdxGame.GRAVITY = Gdx.graphics.getHeight() / 85 * -1;

                    ball1.jump(MyGdxGame.JUMP_VELOCITY);
                    snow.start();
                    rain.start();
                    if (particleFlag <= 20)
                        particleFlag++;
                    emmission1.reset();
                    collisionParticle.reset();
                }
                return false;
            }

            @Override
            public boolean touchUp(int screenX, int screenY, int pointer, int button) {

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
        MyGdxGame.GRAVITY = Gdx.graphics.getHeight() / 85 * -1;
        levelHandler = new LevelHandler(cam);
        obstacle = new Texture("obstacle_curved_9.png");
        rocketTexture = new Texture("rocket.png");
        progressTexture = new Texture("obstacle.png");
        ball1 = new Ball(cam.position.x-cam.viewportWidth/16, cam.position.y-(cam.viewportHeight/4), cam.viewportWidth/8, cam.viewportWidth/8, true);

        obstacleHandler = new ManageObstacle(cam);

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("pirulen-rg.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = (int) MyGdxGame.pixelToDp(10);

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
        snow.load(Gdx.files.internal("small_snow_particle"),Gdx.files.internal(""));
        emmission1 = new ParticleEffect();
        emmission1.load(Gdx.files.internal("rocket_tail_yellow_1"), Gdx.files.internal(""));

        collisionParticle.start();

       // coin1 = new Coin(cam.position.x - 20, cam.position.y , 20, 20);
       // coin2 = new Coin(cam.position.x + 20, cam.position.y , 20, 20);

         MyGdxGame.adsController.hideBannerAd();
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

       // batch.draw(coin1.texture,coin1.position.x, coin1.position.y,coin1.size.x,coin1.size.y);
       // batch.draw(coin2.texture,coin2.position.x, coin2.position.y,coin2.size.x,coin2.size.y);
        batch.draw(rocketTexture, ball1.position.x, ball1.position.y, ball1.size.x, ball1.size.y);

        batch.draw(progressTexture, cam.position.x-cam.viewportWidth/2, cam.position.y-2+cam.viewportHeight/2, getProgressSize() ,2);
        for(i=0;i<obstacleHandler.noOfObstacles;i++){
            batch.draw(obstacle, obstacleHandler.obstacles[i].position.x, obstacleHandler.obstacles[i].position.y, obstacleHandler.obstacles[i].size.x, obstacleHandler.obstacles[i].size.y);
        }

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
            Gdx.app.log("collision", "particle displayed");
            collisionParticle.draw(batch, Gdx.graphics.getDeltaTime());
        }

        batch.end();
    }


    void calculateScore(){
        if(ball1.position.y>=obstacleHandler.obstacles[current_obstacle].position.y + 20 ){
            score++;
            current_obstacle=(current_obstacle+1)%obstacleHandler.noOfObstacles;
        }

    }

    void collisionHandler(){
        Rectangle intersection = new Rectangle();

        for(int i=0;i<obstacleHandler.noOfObstacles;i++){
            if(obstacleHandler.obstacles[i].bounds.overlaps(ball1.bounds) ){
                hit = 1;
                rain.dispose();
                rocketTexture.dispose();
                rocketTexture = new Texture("rocket_hit.png");
                Intersector.intersectRectangles(obstacleHandler.obstacles[i].bounds, ball1.bounds, intersection);
                collisionParticle.setPosition(intersection.getX(), intersection.getY());
                collisionParticle.reset();
                particleFlag=30;

            }

        }

    }


    @Override
    public void update(float deltaTime) {
        //coin1.position.x = obstacleHandler.obstacles[current_obstacle].position.x -25 ;
        //coin1.position.y = obstacleHandler.obstacles[current_obstacle].position.y ;
        //coin2.position.x = obstacleHandler.obstacles[current_obstacle].position.x +obstacleHandler.obstacles[current_obstacle].size.x+ 25 ;
       // coin2.position.y = obstacleHandler.obstacles[current_obstacle].position.y ;
        ball1.update(deltaTime, cam);

        rain.getEmitters().first().setPosition(cam.position.x - cam.viewportWidth / 2, cam.position.y + 10 + cam.viewportHeight / 2);
        snow.getEmitters().first().setPosition(cam.position.x - cam.viewportWidth / 2, cam.position.y + cam.viewportHeight / 2);
        emmission1.update(Gdx.graphics.getDeltaTime());

        emmission1.getEmitters().first().setPosition(ball1.position.x + ball1.size.x / 2, ball1.position.y);


        if(hit==0 && Flags.startFlag==1) {
            handleDifficulty();
            handleCamera();
            calculateScore();
            obstacleHandler.handleObstacles();
            cam.update();
            collisionHandler();
            obstacleHandler.update(deltaTime);
            scoreString = " "+score;
            startTime = System.currentTimeMillis();
            //debugString = " obstacleWidth: "+obstacleHandler.obstacles[current_obstacle].size.x + " diff: "+difficulty;
            //debugString = " vel: "+obstacleHandler.obstacles[current_obstacle].velocity.x + " diff: "+difficulty;
            //debugString = " diff: "+difficulty+" "+ball1.position.y;
           // debugString = " width: "+Gdx.graphics.getWidth()+" Height:"+Gdx.graphics.getHeight();


        }
        else if(ball1.position.y==(cam.position.y - cam.viewportHeight / 2)){
            if(score>prefs.getInteger("bestScore")) {
                prefs.putInteger("bestScore", score);
                prefs.flush();
            }
            //MyGdxGame.adsController.hideBannerAd();
            levelHandler.incrementLevelAttempts(1);
            gsm.set(new com.mygdxtest.game.States.ReplayState(gsm));
            dispose();
        }
        checkIfLevelComplete();
    }


    public void handleDifficulty(){
        manageDistance();
        manageObstacle();
    }




    void manageDistance(){
        for(int i=0;i<7;i++){
            if(Flags.difficultyFlag ==1) {
                if (difficulty == i && score == 10 * i + 1) {
                    difficulty++;
                    Gdx.app.log("difficulty:", " " + difficulty);
                    ManageObstacle.DISTANCE -= cam.viewportHeight/20;
                }
            }
            if(Flags.difficultyFlag ==2) {
                if (difficulty == i && score == 7 * i + 1) {
                    difficulty++;
                    Gdx.app.log("difficulty:", " " + difficulty);
                    ManageObstacle.DISTANCE -= cam.viewportHeight/20;
                }
            }
            if(Flags.difficultyFlag ==3) {
                if (difficulty == i && score == 3 * i + 1) {
                    difficulty++;
                    Gdx.app.log("difficulty:", " " + difficulty);
                    ManageObstacle.DISTANCE -= cam.viewportHeight/20;
                }
            }
        }
    }

    void manageObstacle(){
        if(score<3){//3
            ManageObstacle.OBSTACLE_MIN_WIDTH=cam.viewportWidth/6;
            ManageObstacle.OBSTACLE_MAX_WIDTH=cam.viewportWidth/5;
        }
        else if(score<8){//8
            ManageObstacle.OBSTACLE_MAX_WIDTH=cam.viewportWidth/4;
        }
        else if(score<15){//15
            ManageObstacle.OBSTACLE_MIN_WIDTH= cam.viewportWidth/4;
            ManageObstacle.OBSTACLE_MAX_WIDTH=cam.viewportWidth *2/5;
            //ManageObstacle.OBSTACLE_MIN_VELOCITY=3;
        }
        else if(score<20){//2
        // 0
            ManageObstacle.OBSTACLE_MIN_WIDTH= cam.viewportWidth*2/5;
            ManageObstacle.OBSTACLE_MAX_WIDTH=cam.viewportWidth /2;
        }
    }


    public float getProgressSize(){
        if(score == levelHandler.getLevelCompleteScore()){
            progressTexture=new Texture("progress_complete.png");
            return cam.viewportWidth;
        }
        if (score > 0) {
            Gdx.app.log("progress", "" + (cam.viewportWidth * score / levelHandler.getLevelCompleteScore()));
            return (cam.viewportWidth * score / levelHandler.getLevelCompleteScore());
        }
        else{
            return 0;
        }
    }

    public void checkIfLevelComplete(){
        Gdx.app.log("rocketduo","score, levelcompscore"+score+" "+levelHandler.getLevelCompleteScore()+" "+levelHandler.getCurrentLevel());
        if(score == levelHandler.getLevelCompleteScore() && Flags.levelCompleteFlag==0){
            Flags.levelCompleteFlag=1;
            rocketsGoAwayAnimation();

        }else if(Flags.levelCompleteFlag==1){
            if(ball1.position.y > (cam.position.y+(cam.viewportHeight/2))){
                Flags.levelCompleteFlag=2;
            }
        }
        else if(Flags.levelCompleteFlag==2){
            Flags.nextLevelFlag = 1;
            dispose();
            levelHandler.incrementLevelAttempts(1);
            gsm.set(new ReplayState(gsm));

        }

    }

    public void rocketsGoAwayAnimation(){
        MyGdxGame.GRAVITY=2;
        emmission1.load(Gdx.files.internal("rocket_tail_continuous"), Gdx.files.internal(""));
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
        emmission1.dispose();
        Flags.startFlag=0;
        rain.dispose();
        snow.dispose();

    }
}
