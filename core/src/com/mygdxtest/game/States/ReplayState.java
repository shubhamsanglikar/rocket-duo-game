package com.mygdxtest.game.States;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Align;
import com.mygdxtest.game.Flags;
import com.mygdxtest.game.MyGdxGame;
import com.mygdxtest.game.LevelHandler;

/**
 * Created by Shubham on 15-Dec-16.
 */
public class ReplayState extends State implements InputProcessor {

    Dialog dialog;
    ParticleEffect collisionParticle;
    Texture replay,watchAd,skip,coinsIcon;
    Vector3 touchpos;
    BitmapFont score;
    BitmapFont bestScore;
    BitmapFont levelName;
    BitmapFont totalScore;
    BitmapFont watchAdFont;
    BitmapFont coins;
    boolean btnSet=false;
    boolean skipBtnSet=false;
    boolean watchAdBtnSet=false;
    GlyphLayout levelStatusLayout,attemptsLayout,levelNameLayout,totalScoreLayout,watchAdLayout;
    int best;//getting stored bestScore
    LevelHandler lh;
    FreeTypeFontGenerator generator;
    Vector2 playBtnPos,playBtnSize,watchAdPos,skipPos,watchAdSize,skipSize,coinsPos,coinsSize;
    FreeTypeFontGenerator.FreeTypeFontParameter parameter;
    ParticleEffect snow,rain;
    boolean renderParticleFlag = false;
    public ReplayState(GameStateManager gsm){
        super(gsm);

        touchpos= new Vector3(0,0,0);
        cam.setToOrtho(false, Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);

        coinsPos = new Vector2(cam.position.x - cam.viewportWidth/2 + MyGdxGame.pixelToDp(20) ,cam.position.y + cam.viewportHeight/2 -MyGdxGame.pixelToDp(20));
        coinsSize = new Vector2(MyGdxGame.pixelToDp(15),MyGdxGame.pixelToDp(15));

        collisionParticle = new ParticleEffect();
        collisionParticle.load(Gdx.files.internal("coins_fireworks_particle"), Gdx.files.internal(""));
        collisionParticle.setPosition(coinsPos.x, coinsPos.y);

        MyGdxGame.play_cnt++;
        if(MyGdxGame.play_cnt % MyGdxGame.coin_add_attempts == 0 || Flags.isHighScoreBroken == 1){
            if(Flags.isHighScoreBroken == 1) {
                Flags.isHighScoreBroken = 0;
                MyGdxGame.adsController.showToast("\u2728 New High Score \u2728",true);
            }
            Flags.coins_from = lh.getCoins();
            Flags.coins_to = lh.getCoins() + MyGdxGame.coins_to_be_added;
            lh.addCoins(MyGdxGame.coins_to_be_added);
            //crackers
            Flags.function_after_coins_animation = "coinsAdded";

        }



        MyGdxGame.attempts_in_a_go++;
        MyGdxGame.adsController.showBannerAd();
        try {
            if(MyGdxGame.attempts_in_a_go % MyGdxGame.ad_freq == 0) {
                MyGdxGame.adsController.showInterstitialAd();
            }
        }
        catch(Exception e){
            Gdx.app.log("Exception","Exception in showInterstitialAd()");
        }



        Preferences prefs = Gdx.app.getPreferences("My Preferences");
        best=prefs.getInteger("bestScore",0);

        watchAdSize = new Vector2(cam.viewportWidth-40, (cam.viewportWidth-40)/3);
        watchAdPos = new Vector2(cam.position.x - cam.viewportWidth/2 + 20 ,cam.position.y - cam.viewportHeight/2 + 50);
        playBtnPos = new Vector2(cam.position.x - cam.viewportWidth/2 + 20,cam.position.y - cam.viewportHeight/2 + watchAdSize.y + 50 + 20);
        playBtnSize = new Vector2(cam.viewportWidth/2 - 40, cam.viewportWidth/2 - 40);
        skipPos = new Vector2(cam.position.x + 20,cam.position.y - cam.viewportHeight/2 + watchAdSize.y + 50 +20);
        skipSize = new Vector2(cam.viewportWidth/2 - 40, cam.viewportWidth/2 - 40);

        lh = new LevelHandler(cam);

        if(Flags.nextLevelFlag==0){
            replay = new Texture("buttons/play_again.png");
        }
        else{
            replay = new Texture("buttons/next_level.png");
        }
        if(lh.getCoins()>=MyGdxGame.coins_to_skip_level && lh.getCurrentLevel() == lh.getActualCurrentLevel() && lh.getCurrentLevel() != lh.LAST_LEVEL && Flags.levelCompleteFlag < 1){
            skip = new Texture("buttons/skip_level.png");
            Flags.skipEnabled = 1;
        }
        else{
            skip = new Texture("buttons/grey_skip_level_colored_coin.png");
            Flags.skipEnabled = 0;
        }
        watchAd = new Texture("buttons/watch_ad.png");
        coinsIcon = new Texture("coins.png");

        levelStatusLayout = new GlyphLayout();
        attemptsLayout = new GlyphLayout();
        levelNameLayout = new GlyphLayout();
        totalScoreLayout = new GlyphLayout();
        watchAdLayout = new GlyphLayout();



        generator = new FreeTypeFontGenerator(Gdx.files.internal("pirulen-rg.ttf"));
        parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = (int) MyGdxGame.pixelToDp(11);
        levelName = generator.generateFont(parameter);
        generator = new FreeTypeFontGenerator(Gdx.files.internal("pirulen-rg.ttf"));

        parameter.size = (int) MyGdxGame.pixelToDp(10);
        score = generator.generateFont(parameter);
        totalScore = generator.generateFont(parameter);
        bestScore = generator.generateFont(parameter);
        coins = generator.generateFont(parameter);
        parameter.size = (int) MyGdxGame.pixelToDp(8);
        watchAdFont = generator.generateFont(parameter);
        levelName.setColor(Color.ORANGE);
        totalScore.setColor(Color.WHITE);
        score.setColor(Color.WHITE);
        bestScore.setColor(Color.WHITE);
        coins.setColor(Color.YELLOW);
        watchAdFont.setColor(Color.WHITE);
        float temp=0;
        levelNameLayout.setText(levelName, lh.getLevelName());
        watchAdLayout.setText(watchAdFont,"WATCH AD");




        if(com.mygdxtest.game.Flags.levelCompleteFlag>1){
            //add coins after level complete.
            Flags.coins_from = lh.getCoins();
            Flags.coins_to = lh.getCoins() + MyGdxGame.coins_to_be_added_after_level_complete;
            lh.addCoins(MyGdxGame.coins_to_be_added_after_level_complete);
            //crackers
            Flags.function_after_coins_animation = "coinsAdded";

            if(lh.getCurrentLevel()>= lh.LAST_LEVEL - 1){
                renderParticleFlag=true;
                levelStatusLayout.setText(score, "GAME COMPLETE");
                collisionParticle = new ParticleEffect();
                collisionParticle.load(Gdx.files.internal("fireworks_particle"), Gdx.files.internal(""));
                collisionParticle.setPosition(cam.position.x, cam.position.y + cam.viewportHeight / 4);
                collisionParticle.start();
            }
            else{
                levelStatusLayout.setText(score, "LEVEL COMPLETE");
                MyGdxGame.levels_in_a_go++;
                if(lh.getLevelAttempts() == 1 && lh.getCurrentLevel()!= 0 )
                {
                    MyGdxGame.playServices.unlockAchievement(MyGdxGame.a_complete_level_in_single_attempt);//playServices
                    Gdx.app.log("playServices","achievement unlocked complete level in single attempt");
                }
            }

        }
        else {
            if(lh.getCurrentLevel() == 0){
                temp = (((float) OneBallState.score/(float)lh.getLevelCompleteScore())*100);
            }
            else{
                temp = (((float) TwoBallsState.score/(float)lh.getLevelCompleteScore())*100);
            }
            levelStatusLayout.setText(score, "COMPLETED: " + (int) temp + "%");

        }
        if(lh.getActualCurrentLevel() == lh.getCurrentLevel()){
            lh.manageLevelHighScore(lh.getCurrentLevel(),(int)temp);
            lh.updateTotalScore();
        }

        attemptsLayout.setText(bestScore, "ATTEMPTS: " + lh.getLevelAttempts());

        snow = new ParticleEffect();
        rain = new ParticleEffect();
        snow.load(Gdx.files.internal("small_snow_particle_fast"),Gdx.files.internal(""));
        rain.load(Gdx.files.internal("white_rain_particle_fast"), Gdx.files.internal(""));
        snow.start();
        rain.start();
        rain.getEmitters().first().setPosition(cam.position.x - cam.viewportWidth / 2, cam.position.y + 10 + cam.viewportHeight / 2);
        snow.getEmitters().first().setPosition(cam.position.x - cam.viewportWidth / 2, cam.position.y + cam.viewportHeight / 2);
        if(Flags.nextLevelFlag==1){//important at the end only
            lh.goToNextLevel();
            Flags.nextLevelFlag=0;
        }
        totalScoreLayout.setText(totalScore, "TOTAL SCORE: " + lh.getTotalScore());//has to be after go to next level
        if(lh.getActualCurrentLevel() == lh.PASS_THROUGH_LEVEL)
        {
            MyGdxGame.playServices.unlockAchievement(MyGdxGame.a_beginner);//playServices
            Gdx.app.log("playServices","achievement unlocked beginner");
        }
        if(lh.getLevelAttempts() == 100)
        {
            MyGdxGame.playServices.unlockAchievement(MyGdxGame.a_addicted);//playServices
            Gdx.app.log("playServices","achievement unlocked addicted");
        }
        if(lh.getActualCurrentLevel() == lh.CONTROL_SWITCH_LEVEL)
        {
            MyGdxGame.playServices.unlockAchievement(MyGdxGame.a_professional);//playServices
            Gdx.app.log("playServices","achievement unlocked professional");
        }
        if(lh.getActualCurrentLevel() == lh.CONTROL_SWITCH_LEVEL +1)
        {
            MyGdxGame.playServices.unlockAchievement(MyGdxGame.a_skilled);//playServices
            Gdx.app.log("playServices","achievement unlocked skilled");
        }
        if(MyGdxGame.attempts_in_a_go == 100)
        {
            MyGdxGame.playServices.unlockAchievement(MyGdxGame.a_dedication);//playServices
            Gdx.app.log("playServices","achievement unlocked dedication");
        }
        if(lh.getActualCurrentLevel() == lh.LAST_LEVEL-1)
        {
            MyGdxGame.playServices.unlockAchievement(MyGdxGame.a_world_class);//playServices
            Gdx.app.log("playServices","achievement unlocked world class(unbeatable)");
        }
        if(lh.getActualCurrentLevel() == lh.LAST_LEVEL-2)
        {
            MyGdxGame.playServices.unlockAchievement(MyGdxGame.a_trained);//playServices
            Gdx.app.log("playServices","achievement unlocked trained)");
        }
        if(MyGdxGame.levels_in_a_go == 3)
        {
            MyGdxGame.playServices.unlockAchievement(MyGdxGame.a_3_levels_in_a_go);//playServices
            Gdx.app.log("playServices", "achievement unlocked 3 levels in a go");
        }
        if(MyGdxGame.highestScoreTemp < lh.getTotalScore()){
            MyGdxGame.highestScoreTemp = lh.getTotalScore();
            MyGdxGame.playServices.submitScore(lh.getTotalScore());//playServices
        }


        Gdx.input.setInputProcessor(this);

        //rate game popup if current level == some nice level && not already rated the game... add a new var called is_rated in my_pref
        if(lh.getActualCurrentLevel() > lh.OPPOSITE_CONTROLS_LEVEL && Flags.levelCompleteFlag > 1 && prefs.getBoolean("isRated", false)){
            MyGdxGame.adsController.rateGameDialogBox("Enjoying Rocket Duo?!\nPlease spare a few moments to rate the game.");
        }

        MyGdxGame.adsController.loadRewardVideoAd();

    }
    @Override
    public void render(SpriteBatch batch) {
        batch.setProjectionMatrix(cam.combined);
        batch.begin();
        snow.draw(batch, Gdx.graphics.getDeltaTime());
        rain.draw(batch, Gdx.graphics.getDeltaTime());
        batch.draw(coinsIcon,coinsPos.x,coinsPos.y,coinsSize.x,coinsSize.y);
        levelName.draw(batch, levelNameLayout, cam.position.x - levelNameLayout.width / 2, (cam.viewportHeight / 6) + cam.position.y + MyGdxGame.pixelToDp(50));
        if(Flags.coins_from != -1){
            coins.draw(batch,""+Flags.coins_from,coinsPos.x + coinsSize.x + 10 , coinsPos.y + coinsSize.y*3/4);
        }
        else{
            coins.draw(batch,""+lh.getCoins(),coinsPos.x + coinsSize.x + 10 , coinsPos.y + coinsSize.y*3/4);
        }

        score.draw(batch, levelStatusLayout, cam.position.x - levelStatusLayout.width / 2, (cam.viewportHeight / 6) + cam.position.y + MyGdxGame.pixelToDp(20));
        bestScore.draw(batch, attemptsLayout, cam.position.x - attemptsLayout.width / 2, (cam.viewportHeight / 6) + cam.position.y);
        totalScore.draw(batch, totalScoreLayout, cam.position.x - totalScoreLayout.width / 2, (cam.viewportHeight / 6) + cam.position.y - MyGdxGame.pixelToDp(20));
        batch.draw(replay, playBtnPos.x, playBtnPos.y, playBtnSize.x, playBtnSize.y);
        if(Flags.showWatchAdButton == 1){
            batch.draw(watchAd, watchAdPos.x,watchAdPos.y,watchAdSize.x,watchAdSize.y );//watchAd condition
            watchAdFont.draw(batch,watchAdLayout,cam.position.x - watchAdLayout.width/2, watchAdPos.y + watchAdSize.y/2);
        }
        batch.draw(skip, skipPos.x, skipPos.y, skipSize.x, skipSize.y);
        if(lh.getCurrentLevel() >= lh.LAST_LEVEL-1 && renderParticleFlag) {
            collisionParticle.draw(batch, Gdx.graphics.getDeltaTime());
        }
        if(Flags.function_after_coins_animation == "nulled" || Flags.function_after_coins_animation == "coinsAdded"){
            Flags.function_after_coins_animation="null1";
            collisionParticle.start();

        }
        if(Flags.function_after_coins_animation=="null1"){
            collisionParticle.draw(batch,Gdx.graphics.getDeltaTime());
        }

        batch.end();
    }

    public void nextLevel(){
        lh.goToActualNextLevel();
        gsm.set(new TwoBallsState(gsm));
    }

    @Override
    public void update(float deltaTime) {
        if(Gdx.input.justTouched()){

        }
        if(Flags.skipLevelFlag==1){
            Flags.skipLevelFlag=0;
            Flags.coins_from = lh.getCoins();
            Flags.coins_to = lh.getCoins() - MyGdxGame.coins_to_skip_level;
            Flags.function_after_coins_animation = "nextLevel";
            lh.subtractCoins(MyGdxGame.coins_to_skip_level);
            lh.markLevelAsSkipped(lh.getActualCurrentLevel());
        }
        if(Flags.coins_from > Flags.coins_to){
            Flags.coins_from--;
        }
        else if(Flags.coins_from<Flags.coins_to && Flags.coins_added_rewarded_video != 1){
            Flags.coins_from++;
        }else if(Flags.coins_from == Flags.coins_to && Flags.coins_from != -1){
            Flags.coins_from = -1;
            Flags.coins_to = -1;
            if(Flags.function_after_coins_animation=="nextLevel"){
                Flags.function_after_coins_animation="null";
                nextLevel();
            }
            else if(Flags.function_after_coins_animation=="coinsRewarded"){
                Flags.function_after_coins_animation="nulled";//nulled important
                Gdx.app.log("coins", "final particle display for coins");
                collisionParticle.start();
                if(lh.getCoins()>=MyGdxGame.coins_to_skip_level && lh.getCurrentLevel() == lh.getActualCurrentLevel() && lh.getCurrentLevel() != lh.LAST_LEVEL && Flags.levelCompleteFlag < 1){
                    skip.dispose();
                    skip = new Texture("buttons/skip_level.png");
                    Flags.skipEnabled=1;
                }
            }
        }
    }

    @Override
    public void handleInput() {

    }

    @Override
    public void dispose() {
        generator.dispose();
        score.dispose();
        bestScore.dispose();
        levelName.dispose();
        totalScore.dispose();
        replay.dispose();
        watchAdFont.dispose();
        watchAd.dispose();
        coinsIcon.dispose();
        coins.dispose();
        skip.dispose();
        rain.dispose();
        snow.dispose();
        //if(lh.getCurrentLevel() >= lh.LAST_LEVEL-1 && renderParticleFlag) {
            collisionParticle.dispose();
       // }
    }

    @Override
    public boolean keyDown(int keycode) {
        if(keycode == Input.Keys.BACK || keycode == Input.Keys.ESCAPE){
            Gdx.app.log("back","pressed");
            gsm.set(new MenuState(gsm));
            dispose();
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
        else if(touchpos.x > watchAdPos.x && touchpos.y >watchAdPos.y && touchpos.x < watchAdPos.x+watchAdSize.x && touchpos.y < watchAdPos.y+watchAdSize.y){
            Gdx.app.log("watchAd", "touched");
            watchAdPos.add(10, 10);
            watchAdSize.add(-20, -20);
            watchAdBtnSet=true;
        }
        else if(touchpos.x > skipPos.x && touchpos.y >skipPos.y && touchpos.x < skipPos.x+skipSize.x && touchpos.y < skipPos.y+skipSize.y){
            if(Flags.skipEnabled==1){
                Gdx.app.log("skip", "touched");
                skipPos.add(10, 10);
                skipSize.add(-20, -20);
                skipBtnSet=true;
            }
            else{
                if(lh.getCurrentLevel() == lh.getActualCurrentLevel()) {
                    //MyGdxGame.adsController.showToast("You need " + (MyGdxGame.coins_to_skip_level - lh.getCoins()) + " more coins to skip this level!", true);
                    if(MyGdxGame.coins_to_skip_level - lh.getCoins() > 0) {
                        MyGdxGame.adsController.showToast("You need " + (MyGdxGame.coins_to_skip_level - lh.getCoins()) + " more coins to skip this level.", true);
                    }
                }
            }

        }
        else if(lh.getCurrentLevel() >= lh.LAST_LEVEL-1 && renderParticleFlag){
            collisionParticle.setPosition(touchpos.x,touchpos.y);
            collisionParticle.reset();
        }

        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        touchpos.set(screenX, screenY, 0);
        cam.unproject(touchpos);
        if(touchpos.x > playBtnPos.x && touchpos.y >playBtnPos.y && touchpos.x < playBtnPos.x+playBtnSize.x && touchpos.y < playBtnPos.y+playBtnSize.y && btnSet){
            LevelHandler lh = new LevelHandler(cam);
            dispose();


            if(lh.getCurrentLevel()==0)
                gsm.set(new OneBallState(gsm));
            else
                gsm.set(new TwoBallsState(gsm));

        }
        else if(touchpos.x > watchAdPos.x && touchpos.y >watchAdPos.y && touchpos.x < watchAdPos.x+watchAdSize.x && touchpos.y < watchAdPos.y+watchAdSize.y && watchAdBtnSet){
            watchAdPos.add(-10,-10);
            watchAdSize.add(20, 20);
            watchAdBtnSet=false;
            MyGdxGame.adsController.showRewardVideoAd();
        }
        else if(touchpos.x > skipPos.x && touchpos.y >skipPos.y && touchpos.x < skipPos.x+skipSize.x && touchpos.y < skipPos.y+skipSize.y && Flags.skipEnabled == 1 && skipBtnSet){
            skipPos.add(-10, -10);
            skipSize.add(20, 20);
            skipBtnSet=false;
            if(lh.getCoins() >= MyGdxGame.coins_to_skip_level && Flags.playLevelFlag == lh.getActualCurrentLevel()) {
                MyGdxGame.adsController.skipLevelDialogBox("Skip this level for "+MyGdxGame.coins_to_skip_level+" coins?");

            }
        }

        if(btnSet){
            playBtnPos.add(-10,-10);
            playBtnSize.add(20,20);
            btnSet=false;
        }
        if(watchAdBtnSet){
            watchAdPos.add(-10,-10);
            watchAdSize.add(20, 20);
            watchAdBtnSet=false;
        }
        if(skipBtnSet){
            skipPos.add(-10, -10);
            skipSize.add(20, 20);
            skipBtnSet=false;
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
