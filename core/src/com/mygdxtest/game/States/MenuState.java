package com.mygdxtest.game.States;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.mygdxtest.game.MusicManager;
import com.mygdxtest.game.MyGdxGame;


/**
 * Created by Shubham on 15-Dec-16.
 */
public class MenuState extends State implements InputProcessor {
    //to be disposed..
    Texture playbtn,rateBtn,soundBtn,levelsBtn,ball1,ball2,achievementBtn,leaderboardBtn;
    //private static Music sound;
    BitmapFont nameFont1,nameFont2;
    //to be disposed end..
    GlyphLayout nameFontLayout1,nameFontLayout2;
    TextureRegion tr_ball1,tr_ball2;
    FreeTypeFontGenerator generator;
    FreeTypeFontGenerator.FreeTypeFontParameter parameter;
    Vector3 touchpos;
    Vector2 playBtnPos,playBtnSize,levelsBtnPos,levelsBtnSize,rateBtnSize,soundBtnSize,soundBtnPos,rateBtnPos,leaderboardBtnPos,leaderboardBtnSize,achievementBtnPos,achievementBtnSize;
    ParticleEffect snow,rain,emmission1, emmission2;
    int size=150;
    boolean btnSet = false,levelsBtnSet = false,soundBtnSet = false,rateBtnSet = false,achievementBtnSet=false,leaderboardBtnSet=false;

    com.mygdxtest.game.LevelHandler lh;





    public MenuState(GameStateManager gsm){
        super(gsm);

        //MusicManager.getInstance();

        //sound = Gdx.audio.newMusic(Gdx.files.internal("rocketduoaudio.mp3"));
       // sound.setVolume(0.5f);
        //sound.setLooping(true);

        //MyGdxGame.playServices.signIn();


        cam.setToOrtho(false, Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);//crops the world; false indicates the bottom left corner start
        lh = new com.mygdxtest.game.LevelHandler(cam);

        levelsBtnSize = new Vector2(cam.viewportWidth/9, cam.viewportWidth/9);
        rateBtnSize = new Vector2(cam.viewportWidth/9, cam.viewportWidth/9);
        leaderboardBtnSize = new Vector2(cam.viewportWidth/9, cam.viewportWidth/9);
        achievementBtnSize = new Vector2(cam.viewportWidth/9, cam.viewportWidth/9);
        soundBtnSize = new Vector2(cam.viewportWidth/9, cam.viewportWidth/9);
        playBtnPos = new Vector2(cam.position.x - cam.viewportWidth/4 + MyGdxGame.pixelToDp(10),cam.position.y -cam.viewportHeight/2+cam.viewportHeight/8+ MyGdxGame.pixelToDp(20));
        levelsBtnPos = new Vector2(cam.position.x - cam.viewportWidth/2 +cam.viewportWidth/4-levelsBtnSize.x/2,cam.position.y -cam.viewportHeight/2+cam.viewportHeight/10 - levelsBtnSize.y/2);
        rateBtnPos = new Vector2(cam.position.x - rateBtnSize.x/2,cam.position.y -cam.viewportHeight/2+cam.viewportHeight/15- rateBtnSize.y/2);
        soundBtnPos = new Vector2(cam.position.x + cam.viewportWidth/2 -cam.viewportWidth/4-soundBtnSize.x/2,cam.position.y -cam.viewportHeight/2+cam.viewportHeight/10- soundBtnSize.y/2);
        leaderboardBtnPos = new Vector2(cam.position.x + cam.viewportWidth/2 -cam.viewportWidth/8-leaderboardBtnSize.x/2,cam.position.y -cam.viewportHeight/2+cam.viewportHeight/5- leaderboardBtnSize.y/2);
        achievementBtnPos = new Vector2(cam.position.x - cam.viewportWidth/4 - cam.viewportWidth/8 -achievementBtnSize.x/2,cam.position.y -cam.viewportHeight/2+cam.viewportHeight/5- achievementBtnSize.y/2);

        playBtnSize = new Vector2(cam.viewportWidth/2 - MyGdxGame.pixelToDp(20), cam.viewportWidth/2 - MyGdxGame.pixelToDp(20));


        nameFontLayout1 = new GlyphLayout();
        nameFontLayout2 = new GlyphLayout();
        generator = new FreeTypeFontGenerator(Gdx.files.internal("SFFunkMaster.ttf"));
        parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = (int) MyGdxGame.pixelToDp(30);
        nameFont1= generator.generateFont(parameter);
        nameFont1.setColor(Color.WHITE);
        nameFontLayout1.setText(nameFont1, "Rocket");
        nameFont2= generator.generateFont(parameter);
        nameFont2.setColor(Color.WHITE);
        nameFontLayout2.setText(nameFont1, "Duo");
        //bottom = new Vector2(cam.position.x - cam.viewportWidth/4,cam.position.y +cam.viewportHeight/4);
        //Skin skin = new Skin(Gdx.files.internal("flat-earth/flat-earth-ui.json"));

        Gdx.input.setInputProcessor(this);
        playbtn= new Texture("buttons/btnPlay.png");
        levelsBtn= new Texture("buttons/list_white.png");
        rateBtn= new Texture("buttons/star_white.png");
        leaderboardBtn= new Texture("buttons/rank.png");
        achievementBtn= new Texture("buttons/achievements.png");



        emmission1 = new ParticleEffect();
        emmission1.load(Gdx.files.internal("rocket_tail_continuous_fast"), Gdx.files.internal(""));
        emmission2 = new ParticleEffect();
        emmission2.load(Gdx.files.internal("rocket_tail_continuous_fast"), Gdx.files.internal(""));
        snow = new ParticleEffect();
        rain = new ParticleEffect();
        snow.load(Gdx.files.internal("small_snow_particle_fast"), Gdx.files.internal(""));
        snow.getEmitters().first().setPosition(cam.position.x - cam.viewportWidth / 2, cam.position.y + 20 + cam.viewportHeight / 2);
        snow.start();
        rain.load(Gdx.files.internal("white_rain_particle_fast"), Gdx.files.internal(""));
        rain.getEmitters().first().setPosition(cam.position.x - cam.viewportWidth / 2, cam.position.y + 20 + cam.viewportHeight / 2);
        rain.start();
        emmission1.start();
        emmission2.start();
        ball1= new Texture("rocket.png");
        ball2= new Texture("rocket.png");
        tr_ball1 = new TextureRegion(ball1);
        tr_ball2 = new TextureRegion(ball1);

        touchpos = new Vector3();
        //playButton = new TextButton("Play", skin );
        //playButton.setPosition(cam.position.x - playButton.getWidth() / 2, cam.position.y - playButton.getHeight() / 2);

        if(lh.getSound()){
            MusicManager.getInstance().music.play();
            soundBtn= new Texture("buttons/speaker_on_white.png");
        }
        else{
            soundBtn = new Texture("buttons/speaker_off_white.png");
        }
        MyGdxGame.adsController.hideBannerAd();
    }



    @Override
    public void update(float deltaTime) {

        handleInput();
        emmission1.update(Gdx.graphics.getDeltaTime());
        emmission2.update(Gdx.graphics.getDeltaTime());
        emmission1.getEmitters().first().setPosition(cam.position.x - cam.viewportWidth/8, cam.position.y +cam.viewportHeight/4 +20);
        emmission2.getEmitters().first().setPosition(cam.position.x + cam.viewportWidth/8, cam.position.y +cam.viewportHeight/5 +20);


    }

    @Override
    public void handleInput() {

        if(Gdx.input.justTouched()){


//            if(Gdx.input.getX()<200){
//                lh.goToPrevLevel();
//            }
//            else if(Gdx.input.getX()<500){
//
//            }
//            else if(Gdx.input.getX()>500){
//                lh.goToNextLevel();
//            }
//            Gdx.app.log("difficulty set to"," "+Flags.difficultyFlag);
//
//
//            LevelHandler lh = new LevelHandler(cam);
//            if(lh.getCurrentLevel()==0)
//                gsm.set(new OneBallState(gsm));
//            else
//                gsm.set(new TwoBallsState(gsm));
//            dispose();
      }
    }

    @Override
    public void render(SpriteBatch batch) {
        batch.setProjectionMatrix(cam.combined);
        batch.begin();



        //batch.draw(gameNameFont,cam.position.x - cam.viewportWidth/4 ,cam.position.y, cam.viewportWidth/2, cam.viewportWidth/4);
        nameFont1.draw(batch, nameFontLayout1, cam.position.x - nameFontLayout1.width/2, cam.position.y + MyGdxGame.pixelToDp(50));
        nameFont2.draw(batch, nameFontLayout2, cam.position.x - nameFontLayout2.width/2, cam.position.y + MyGdxGame.pixelToDp(20));
        snow.draw(batch, Gdx.graphics.getDeltaTime());
        rain.draw(batch, Gdx.graphics.getDeltaTime());
        emmission1.draw(batch);
        emmission2.draw(batch);
        batch.draw(playbtn, playBtnPos.x, playBtnPos.y , playBtnSize.x, playBtnSize.y );
        batch.draw(levelsBtn, levelsBtnPos.x, levelsBtnPos.y , levelsBtnSize.x, levelsBtnSize.y );
        batch.draw(rateBtn, rateBtnPos.x, rateBtnPos.y , rateBtnSize.x, rateBtnSize.y );
        batch.draw(soundBtn, soundBtnPos.x, soundBtnPos.y , soundBtnSize.x, soundBtnSize.y );
        batch.draw(leaderboardBtn, leaderboardBtnPos.x, leaderboardBtnPos.y , leaderboardBtnSize.x, leaderboardBtnSize.y );
        batch.draw(achievementBtn, achievementBtnPos.x, achievementBtnPos.y , achievementBtnSize.x, achievementBtnSize.y );

        batch.draw(tr_ball1, cam.position.x - cam.viewportWidth/4, cam.position.y +cam.viewportHeight/4, cam.viewportWidth/4, cam.viewportWidth/4);
        batch.draw(tr_ball2, cam.position.x  , cam.position.y +cam.viewportHeight/5 , cam.viewportWidth/4, cam.viewportWidth/4);
       // batch.draw(tr_ball2, cam.position.x + 75 , cam.position.y, cam.viewportWidth/8, cam.viewportWidth/8,150,150,1,1,-30);
        batch.end();
    }

    @Override
    public void dispose() {
        playbtn.dispose();
        rain.dispose();
        snow.dispose();
        emmission2.dispose();
        emmission1.dispose();
        generator.dispose();
        nameFont1.dispose();
        nameFont2.dispose();
        ball1.dispose();
        ball2.dispose();
        rateBtn.dispose();
        achievementBtn.dispose();
        leaderboardBtn.dispose();
        levelsBtn.dispose();
        soundBtn.dispose();

    }

    @Override
    public boolean keyDown(int keycode) {
        if(keycode == Input.Keys.BACK){
            Gdx.app.exit();
        }
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
        touchpos.set(screenX, screenY, 0);
        cam.unproject(touchpos);
        if(touchpos.x > playBtnPos.x && touchpos.y >playBtnPos.y && touchpos.x < playBtnPos.x+playBtnSize.x && touchpos.y < playBtnPos.y+playBtnSize.y){
            Gdx.app.log("playbtn", "touched");
            playBtnPos.add(10, 10);
            playBtnSize.add(-20,-20);
            btnSet=true;
        }
        else if(touchpos.x > levelsBtnPos.x && touchpos.y >levelsBtnPos.y && touchpos.x < levelsBtnPos.x+levelsBtnSize.x && touchpos.y < levelsBtnPos.y+levelsBtnSize.y){
            levelsBtnPos.add(-10,-10);
            levelsBtnSize.add(20, 20);
            levelsBtnSet=true;

        }
        else if(touchpos.x > rateBtnPos.x && touchpos.y >rateBtnPos.y && touchpos.x < rateBtnPos.x+rateBtnSize.x && touchpos.y < rateBtnPos.y+rateBtnSize.y){
            rateBtnPos.add(-10,-10);
            rateBtnSize.add(20, 20);
            rateBtnSet=true;

        }
        else if(touchpos.x > soundBtnPos.x && touchpos.y >soundBtnPos.y && touchpos.x < soundBtnPos.x+soundBtnSize.x && touchpos.y < soundBtnPos.y+soundBtnSize.y){
            soundBtnPos.add(-10,-10);
            soundBtnSize.add(20, 20);
            soundBtnSet=true;

        }
        else if(touchpos.x > leaderboardBtnPos.x && touchpos.y >leaderboardBtnPos.y && touchpos.x < leaderboardBtnPos.x+leaderboardBtnSize.x && touchpos.y < leaderboardBtnPos.y+leaderboardBtnSize.y){
            leaderboardBtnPos.add(-10,-10);
            leaderboardBtnSize.add(20, 20);
            leaderboardBtnSet=true;

        }
        else if(touchpos.x > achievementBtnPos.x && touchpos.y >achievementBtnPos.y && touchpos.x < achievementBtnPos.x+achievementBtnSize.x && touchpos.y < achievementBtnPos.y+achievementBtnSize.y){
            achievementBtnPos.add(-10,-10);
            achievementBtnSize.add(20, 20);
            achievementBtnSet=true;

        }
        else if(touchpos.x<cam.position.x){
            //lh.goToPrevLevel();
            //MyGdxGame.playServices.signIn();
        }
        else{
           // lh.goToNextLevel();
            //MyGdxGame.playServices.showScore();//playServices
        }
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        touchpos.set(screenX, screenY, 0);
        cam.unproject(touchpos);

        if(touchpos.x > playBtnPos.x && touchpos.y >playBtnPos.y && touchpos.x < playBtnPos.x+playBtnSize.x && touchpos.y < playBtnPos.y+playBtnSize.y && btnSet){
            com.mygdxtest.game.LevelHandler lh = new com.mygdxtest.game.LevelHandler(cam);
            if(lh.getCurrentLevel()==0)
                gsm.set(new OneBallState(gsm));
            else
                gsm.set(new TwoBallsState(gsm));
            dispose();
        }
        else if(touchpos.x > levelsBtnPos.x && touchpos.y >levelsBtnPos.y && touchpos.x < levelsBtnPos.x+levelsBtnSize.x && touchpos.y < levelsBtnPos.y+levelsBtnSize.y && levelsBtnSet){
            gsm.set(new LevelsState(gsm));
            dispose();
        }
        else if(touchpos.x > rateBtnPos.x && touchpos.y >rateBtnPos.y && touchpos.x < rateBtnPos.x+rateBtnSize.x && touchpos.y < rateBtnPos.y+rateBtnSize.y && rateBtnSet){
            rateBtnPos.add(10,10);
            rateBtnSize.add(-20, -20);
            rateBtnSet=false;
            Gdx.net.openURI("https://play.google.com/store/apps/details?id=com.shubhamsanglikar.rocketduo");
        }
        else if(touchpos.x > leaderboardBtnPos.x && touchpos.y >leaderboardBtnPos.y && touchpos.x < leaderboardBtnPos.x+leaderboardBtnSize.x && touchpos.y < leaderboardBtnPos.y+leaderboardBtnSize.y && leaderboardBtnSet){
            leaderboardBtnPos.add(10,10);
            leaderboardBtnSize.add(-20, -20);
            leaderboardBtnSet=false;
            MyGdxGame.playServices.showScore();//playServices
        }
        else if(touchpos.x > achievementBtnPos.x && touchpos.y >achievementBtnPos.y && touchpos.x < achievementBtnPos.x+achievementBtnSize.x && touchpos.y < achievementBtnPos.y+achievementBtnSize.y && achievementBtnSet){
            achievementBtnPos.add(10,10);
            achievementBtnSize.add(-20, -20);
            achievementBtnSet=false;
            MyGdxGame.playServices.showAchievement();
        }
        else if(touchpos.x > soundBtnPos.x && touchpos.y >soundBtnPos.y && touchpos.x < soundBtnPos.x+soundBtnSize.x && touchpos.y < soundBtnPos.y+soundBtnSize.y){
            soundBtnPos.add(10,10);
            soundBtnSize.add(-20, -20);
            soundBtnSet=false;
            //soundBtn.dispose();
            if(lh.getSound()){
                soundBtn = new Texture("buttons/speaker_off_white.png");
                //sound.pause();
                MusicManager.getInstance().music.pause();
                lh.setSound(false);
            }
            else{
                soundBtn = new Texture("buttons/speaker_on_white.png");
                //sound.play();
                MusicManager.getInstance().music.play();

                lh.setSound(true);
            }


        }


        if(btnSet){
            playBtnPos.add(-10,-10);
            playBtnSize.add(20,20);
            btnSet=false;
        }
        if(levelsBtnSet){
            levelsBtnPos.add(10,10);
            levelsBtnSize.add(-20, -20);
            levelsBtnSet=false;
        }
        if(rateBtnSet){
            rateBtnPos.add(10,10);
            rateBtnSize.add(-20,-20);
            rateBtnSet=false;
        }
        if(leaderboardBtnSet){
            leaderboardBtnPos.add(10,10);
            leaderboardBtnSize.add(-20,-20);
            leaderboardBtnSet=false;
        }
        if(achievementBtnSet){
            achievementBtnPos.add(10,10);
            achievementBtnSize.add(-20,-20);
            achievementBtnSet=false;
        }
        if(soundBtnSet){
            soundBtnPos.add(10,10);
            soundBtnSize.add(-20,-20);
            soundBtnSet=false;
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
}
