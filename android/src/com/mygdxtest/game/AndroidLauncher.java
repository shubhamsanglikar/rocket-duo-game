package com.mygdxtest.game;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.google.android.gms.games.Games;
import com.google.example.games.basegameutils.GameHelper;
import com.mygdxtest.game.States.TwoBallsState;
import com.tappx.sdk.android.Tappx;
import com.tappx.sdk.android.TappxBanner;
import com.tappx.sdk.android.TappxInterstitial;

import com.chartboost.sdk.*;

import java.util.logging.Level;

public class AndroidLauncher extends AndroidApplication implements AdsController{
	/*
	// Old add unit IDs
	private static final String BANNER_AD_UNIT_ID = "ca-app-pub-3508481243290000/9908366973";
	private static final String INTERSTITIAL_AD_UNIT_ID = "ca-app-pub-3508481243290000/2545556977";
	private static final String REWARD_VIDEO_AD_UNIT_ID = "ca-app-pub-3508481243290000/3147659371";
	*/

	// Actual ad unit IDs
	private static final String BANNER_AD_UNIT_ID = "ca-app-pub-4567859816020981/9660423907";
	private static final String INTERSTITIAL_AD_UNIT_ID = "ca-app-pub-4567859816020981/1450212651";
	private static final String REWARD_VIDEO_AD_UNIT_ID = "ca-app-pub-4567859816020981/5710883685";


	/*
	// Test ad unit IDs
	private static final String BANNER_AD_UNIT_ID = "ca-app-pub-3940256099942544/6300978111";
	private static final String INTERSTITIAL_AD_UNIT_ID = "ca-app-pub-3940256099942544/1033173712";
	private static final String REWARD_VIDEO_AD_UNIT_ID = "ca-app-pub-3940256099942544/5224354917";
	*/


	AdView bannerAd;
	InterstitialAd interstitialAd;
	RewardedVideoAd mAd;
	private GameHelper gameHelper;//playServices
	private final static int requestCode = 1;
	private PlayServices playServices;
	DialogInterface.OnClickListener dialogClickListener;
	DialogInterface.OnCancelListener dialogClickListenerRateGame;
	public static int rateGameCnt = 0;
	public static String dialogBoxMessage = "Some error occurred. Sorry for the inconvenience";

	TappxInterstitial tappxInterstitial;
	TappxBanner tappxBanner;



	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Chartboost.onCreate(this);
		if(true){
			playServices= new PlayServices() {
				@Override
				public void signIn() {
					try
					{
						runOnUiThread(new Runnable()
						{
							@Override
							public void run()
							{
								gameHelper.beginUserInitiatedSignIn();
							}
						});
					}
					catch (Exception e)
					{
						Gdx.app.log("MainActivity", "Log in failed: " + e.getMessage() + ".");
					}
				}

				@Override
				public void signOut() {
					try
					{
						runOnUiThread(new Runnable()
						{
							@Override
							public void run()
							{
								gameHelper.signOut();
							}
						});
					}
					catch (Exception e)
					{
						Gdx.app.log("MainActivity", "Log out failed: " + e.getMessage() + ".");
					}
				}

				@Override
				public void rateGame() {
					String str = "https://play.google.com/store/apps/details?id=com.shubhamsanglikar.rocketduo";
					startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(str)));
				}

				@Override
				public void unlockAchievement(String ach) {
					if(isSignedIn()) {
						Games.Achievements.unlock(gameHelper.getApiClient(),
								ach);
					}
				}

				@Override
				public void submitScore(int highScore) {
					if (isSignedIn() == true)
					{
						Games.Leaderboards.submitScore(gameHelper.getApiClient(),
								getString(R.string.leaderboard_total_score), highScore);
					}
				}

				@Override
				public void showAchievement() {
					if (isSignedIn() == true)
					{
						startActivityForResult(Games.Achievements.getAchievementsIntent(gameHelper.getApiClient()), requestCode);
					}
					else
					{
						signIn();
					}
				}

				@Override
				public void showScore() {
					if (isSignedIn() == true)
					{
						startActivityForResult(Games.Leaderboards.getLeaderboardIntent(gameHelper.getApiClient(),
								getString(R.string.leaderboard_total_score)), requestCode);
					}
					else
					{
						signIn();
					}
				}

				@Override
				public boolean isSignedIn() {
					return gameHelper.isSignedIn();
				}
			};
			//play services ... start
			gameHelper = new GameHelper(this, GameHelper.CLIENT_GAMES);
			gameHelper.enableDebugLog(false);

			GameHelper.GameHelperListener gameHelperListener = new GameHelper.GameHelperListener()
			{
				@Override
				public void onSignInFailed(){ }

				@Override
				public void onSignInSucceeded(){ }
			};

			gameHelper.setup(gameHelperListener);
			//play services ...end
			//playServices 1. create obj, 2. uncomment this,3.pass obj in gameView (this,playServices), 4.do reqd steps in MyGdx game

		}

		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		config.useGL30=false;
		config.useAccelerometer=false;
		config.useCompass=false;


		//initialize(new MyGdxGame(this), config);
		View gameView = initializeForView(new MyGdxGame(this, playServices), config);// pass playServices
		setupAds();

		RelativeLayout layout = new RelativeLayout(this);
		layout.addView(gameView, ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.MATCH_PARENT);

		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		layout.addView(bannerAd, params);

		setContentView(layout);
	}

	public boolean isNetworkConnected() {
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo ni = cm.getActiveNetworkInfo();
		if (ni == null) {
			// There are no active networks.
			return false;
		} else
			return true;
	}

	@Override
	public void onStart(){
		super.onStart();
		gameHelper.onStart(this);// playServices
	}

	@Override
	public void onStop(){
		super.onStop();;
		gameHelper.onStop();//playServices
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);
		gameHelper.onActivityResult(requestCode, resultCode, data);//playServices
	}

	@Override
	protected void onPause(){
		super.onPause();
		Log.d("paused", "android");
	}

	public void setupAds() {
		bannerAd = new AdView(this);
		bannerAd.setVisibility(View.INVISIBLE);
		bannerAd.setBackgroundColor(0xff000000); // black
		bannerAd.setAdUnitId(BANNER_AD_UNIT_ID);
		bannerAd.setAdSize(AdSize.BANNER);

		interstitialAd = new InterstitialAd(this);
		interstitialAd.setAdUnitId(INTERSTITIAL_AD_UNIT_ID);
		interstitialAd.setAdListener(new AdListener() {
			@Override
			public void onAdClosed() {
				super.onAdClosed();
				Gdx.app.log("interstitial", "ad closed.");
			}
		});



		tappxInterstitial = new TappxInterstitial(this, "/120940746/Pub-20215-Android-7294");


		mAd = MobileAds.getRewardedVideoAdInstance(this);
		mAd.setRewardedVideoAdListener(new RewardedVideoAdListener() {
			@Override
			public void onRewardedVideoAdLoaded() {
				//make button visible..
				Flags.showWatchAdButton = 1;

			}

			@Override
			public void onRewardedVideoAdOpened() {
				//adflag=1
				MyGdxGame.adflag = 1;
			}

			@Override
			public void onRewardedVideoStarted() {
				//adflag=1
				MyGdxGame.adflag = 1;
			}

			@Override
			public void onRewardedVideoAdClosed() {
				//disable watch ad button.
				Flags.showWatchAdButton = 0;


			}

			@Override
			public void onRewarded(RewardItem rewardItem) {
				Flags.coins_added_rewarded_video = 1;
				Flags.coins_from = LevelHandler.getCoins();
				Flags.coins_to = LevelHandler.getCoins() + MyGdxGame.coins_to_be_rewarded;
				LevelHandler.addCoins(MyGdxGame.coins_to_be_rewarded);
				Flags.function_after_coins_animation = "coinsRewarded";
			}

			@Override
			public void onRewardedVideoAdLeftApplication() {

			}

			@Override
			public void onRewardedVideoAdFailedToLoad(int i) {

			}
		});

	}



	@Override
	public void showBannerAd() {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				if (isNetworkConnected()) {
					bannerAd.setVisibility(View.VISIBLE);


					AdRequest.Builder builder = new AdRequest.Builder();
					//builder.addTestDevice("EF78002E6FC2DAE0DD4ED5E477678107");
					AdRequest ad = builder.build();
					bannerAd.loadAd(ad);

					/*
					tappxBanner = new TappxBanner(getApplicationContext(), "/120940746/Pub-20215-Android-7294");
					bannerAd.addView(tappxBanner);
					//com.tappx.sdk.android.AdRequest tappxAdrequest = new com.tappx.sdk.android.AdRequest().keywords("games,challenging,brain games,puzzle,multitasking,addictive,sexy,hot");
					tappxBanner.loadAd();
					*/
				}

			}
		});
	}

	@Override
	public void hideBannerAd() {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				bannerAd.setVisibility(View.INVISIBLE);
			}
		});
	}

	@Override
	public void loadInterstitialAd() {

		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				if (isNetworkConnected()) {

					AdRequest.Builder builder = new AdRequest.Builder();
					//builder.addTestDevice("EF78002E6FC2DAE0DD4ED5E477678107");
					AdRequest adRequestInterstitial = builder.build();
					interstitialAd.loadAd(adRequestInterstitial);
					Gdx.app.log("interstitial", "loadad called.");


					/*
					//com.tappx.sdk.android.AdRequest tappxAdrequest = new com.tappx.sdk.android.AdRequest().keywords("games,challenging,brain games,puzzle,multitasking,addictive,sexy,hot");
					tappxInterstitial.loadAd();
					*/

				}

			}
		});

	}

	@Override
	public void showInterstitialAd() {
		Gdx.app.log("interstitial", "show ad called.before");
		MyGdxGame.ad_cnt++;
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				if(interstitialAd.isLoaded()){
					MyGdxGame.adflag=1;
					if((MyGdxGame.ad_cnt)%MyGdxGame.ad_freq == 0 || 1 == 1){
						interstitialAd.show();
						Gdx.app.log("interstitial", "show ad called.Yo");
					}

				}
				loadInterstitialAd();

				/*
				if(tappxInterstitial.isReady()){
					tappxInterstitial.show();
				}
				*/
			}
		});

	}

	@Override
	public void loadRewardVideoAd() {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				if (!mAd.isLoaded()) {
					AdRequest adRequest = new AdRequest.Builder().build();
					mAd.loadAd(REWARD_VIDEO_AD_UNIT_ID, adRequest);
				} else {
					Flags.showWatchAdButton = 1;
				}

			}
		});

	}

	@Override
	public void showRewardVideoAd() {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				if (mAd.isLoaded()) {
					mAd.show();
				}
			}
		});
	}

	@Override
	public void skipLevelDialogBox(String msg1) {
		dialogClickListener = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				switch (which){
					case DialogInterface.BUTTON_POSITIVE:
						//Yes button clicked
						Toast.makeText(AndroidLauncher.this,"Level Skipped",Toast.LENGTH_LONG).show();
						Log.d("dialog", "yes");
						Flags.skipLevelFlag = 1;
						break;

					case DialogInterface.BUTTON_NEGATIVE:
						//No button clicked
						//Toast.makeText(AndroidLauncher.this,"No touched",Toast.LENGTH_LONG).show();
						Log.d("dialog", "no");
						Flags.skipLevelFlag = -1;
						break;
				}
			}
		};
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				AlertDialog.Builder builder = new AlertDialog.Builder(AndroidLauncher.this);
				builder.setMessage("Skip this level for "+MyGdxGame.coins_to_skip_level+" coins?").setPositiveButton("Yes", dialogClickListener)
						.setNegativeButton("No", dialogClickListener).show();
			}
		});

	}


	@Override
	public void rateGameDialogBox(String msg1) {
		Preferences prefs = Gdx.app.getPreferences("My Preferences");
		if(isNetworkConnected() && prefs.getBoolean("isRated", false) == false) {
			dialogBoxMessage = msg1;
			dialogClickListener = new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					switch (which) {
						case DialogInterface.BUTTON_POSITIVE:
							//Yes button clicked

							try {
								startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.shubhamsanglikar.rocketduo")));
							} catch (android.content.ActivityNotFoundException anfe) {
								startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.shubhamsanglikar.rocketduo")));
							}
							Preferences prefs = Gdx.app.getPreferences("My Preferences");
							prefs.putBoolean("isRated", true);
							if(rateGameCnt==1) {
								int coins = prefs.getInteger("coins", 0);
								prefs.putInteger("coins", coins + 20);
							}
							prefs.flush();
							break;

						case DialogInterface.BUTTON_NEGATIVE:
							//No button clicked
							//Toast.makeText(AndroidLauncher.this,"No touched",Toast.LENGTH_LONG).show();
							if (rateGameCnt == 0) {
								rateGameCnt++;
								rateGameDialogBox("Lets make a deal:\nRate the game now and get 20 coins free. \ud83d\ude09");
							}
							break;
					}
				}
			};

			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					AlertDialog.Builder builder = new AlertDialog.Builder(AndroidLauncher.this);
					builder.setMessage(dialogBoxMessage).setPositiveButton("Rate", dialogClickListener)
							.setNegativeButton(rateGameCnt == 0 ? "Later" : "I won't take any bribe", dialogClickListener).show();
				}
			});
		}

	}

	@Override
	public boolean isInterstitialAdReady(){
		return interstitialAd.isLoaded();
		//return tappxInterstitial.isReady();
	}

	public static String toastMsg = "Not enough coins";
	public static boolean toastIsLengthLong = true;
	@Override
	public void showToast(String msg, boolean isLenghtLong) {
		toastMsg = msg;
		toastIsLengthLong=isLenghtLong;
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Toast.makeText(AndroidLauncher.this, toastMsg, (toastIsLengthLong) ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT).show();
			}
		});

	}


}
