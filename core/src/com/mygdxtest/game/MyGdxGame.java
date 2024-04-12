package com.mygdxtest.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdxtest.game.Sprites.Ball;
import com.mygdxtest.game.States.GameStateManager;
import com.mygdxtest.game.States.MenuState;


public class MyGdxGame extends ApplicationAdapter {
	//public static int DEVICE_WIDTH = Gdx.graphics.getWidth();
	//public static int DEVICE_HEIGHT= Gdx.graphics.getHeight();
	public static int coin_add_attempts = 5;
	public static int coins_to_be_added = 5;
	public static int coins_to_skip_level = 100;
	public static int coins_to_be_rewarded = 20;
	public static int coins_to_be_added_after_level_complete = 20;

	public static int ad_cnt=0;
	public static int play_cnt = 0;
	public static int adflag = 0;
	public static int ad_freq = 4;
	public static int attempts_in_a_go = 0;
	public static int levels_in_a_go = 0;
	public static int highestScoreTemp = 0;
	public static int GRAVITY = -15;
	public static boolean isPaused = false;
	public static int JUMP_VELOCITY = 400;
	public static int BOOSTED_JUMP_VELOCITY = 400;
	public static float CAMERA_TRANSLATION = 1;
	public static final int BALL_SIZE = 50;
	public static final String a_beginner = "CgkIxYHi5q8BEAIQAg";
	public static final String a_addicted = "CgkIxYHi5q8BEAIQBA";
	public static final String a_dedication = "CgkIxYHi5q8BEAIQAw";
	public static final String a_world_class = "CgkIxYHi5q8BEAIQBg";
	public static final String a_professional = "CgkIxYHi5q8BEAIQBQ";
	public static final String a_3_levels_in_a_go = "CgkIxYHi5q8BEAIQBw";
	public static final String a_complete_level_in_single_attempt = "CgkIxYHi5q8BEAIQCA";
	public static final String a_trained = "CgkIxYHi5q8BEAIQCQ";
	public static final String a_skilled = "CgkIxYHi5q8BEAIQCg";



	//public static int difficultyFlag = 1;



	InputProcessor inputProcessor;
	GameStateManager gsm;

	SpriteBatch batch;
	public static int height, width;
	int cnt=0;

	public static AdsController adsController;
	public static PlayServices playServices;

	public MyGdxGame(AdsController adsController, PlayServices playServices){//playServices add obj here
		this.adsController = adsController;
		this.playServices = playServices;

	}



	@Override
	public void pause(){
		Gdx.app.log("paused","game");
		isPaused=true;
	}

	@Override
	public void resume(){
		Gdx.app.log("resumed","game");
		isPaused=false;
		if(adflag==1){
			adflag=0;
		}
		else{
			gsm.set(new MenuState(gsm));
		}
		if(Flags.coins_added_rewarded_video==1){
			Flags.coins_added_rewarded_video=2;
			Gdx.app.log("coins","crackers");

		}

	}


	public static float pixelToDp(float px){
		return (px * Gdx.graphics.getDensity());
	}

	@Override
	public void create() {
		Gdx.input.setCatchBackKey(true);
		JUMP_VELOCITY = Gdx.graphics.getHeight()/3;
		//JUMP_VELOCITY = 30;
		BOOSTED_JUMP_VELOCITY = Gdx.graphics.getHeight()/2;
		GRAVITY = Gdx.graphics.getHeight()/85 * -1;


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
				return false;
			}

			@Override
			public boolean touchUp(int screenX, int screenY, int pointer, int button) {
				Gdx.app.log("touched","up");
				return true;
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
		batch = new SpriteBatch();
		height=Gdx.graphics.getHeight();
		width = Gdx.graphics.getWidth();

		gsm = new GameStateManager();
		gsm.push(new MenuState(gsm));
		Gdx.gl.glClearColor(0, 0, 0.05f, 1);

	}

	@Override
	public void render () {

		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		gsm.update(Gdx.graphics.getDeltaTime());
		gsm.render(batch);

	}


	
	@Override
	public void dispose () {

	}
}
