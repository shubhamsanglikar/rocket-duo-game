package com.mygdxtest.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;

/**
 * Created by Shubham on 16-Feb-17.
 */
public class MusicManager {
    static MusicManager musicManager;
    public static Music music;

    public static MusicManager getInstance(){
        if(musicManager==null){
            musicManager= new MusicManager();
            musicManager.music = Gdx.audio.newMusic(Gdx.files.internal("rocketduoaudio.mp3"));
            musicManager.music.setVolume(0.5f);
            musicManager.music.setLooping(true);
            return musicManager;
        }
        else{
            return musicManager;
        }
    }

    public void stopMusic(){
        getInstance().music.stop();
    }

    public void playMusic(){
        getInstance().music.play();
    }

}
