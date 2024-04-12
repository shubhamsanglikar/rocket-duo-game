package com.mygdxtest.game;


import com.mygdxtest.game.States.TwoBallsState;

import javax.naming.Context;

/**
 * Created by Shubham on 06-Jan-17.
 */

//interstitial ad unit id: ca-app-pub-3508481243290000/2545556977
public interface AdsController {

    public void showBannerAd();
    public void hideBannerAd();

    public void loadInterstitialAd();
    public boolean isInterstitialAdReady();
    public void showInterstitialAd();

    public void loadRewardVideoAd();
    public void showRewardVideoAd();

    public void skipLevelDialogBox(String msg);
    public void rateGameDialogBox(String msg);

    public void showToast(String msg,boolean isLengthLong);
}