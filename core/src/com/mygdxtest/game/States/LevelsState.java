package com.mygdxtest.game.States;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.mygdxtest.game.LevelHandler;
import com.mygdxtest.game.MyGdxGame;


/**
 * Created by Shubham on 15-Dec-16.
 */
public class LevelsState extends State{


    Table scrollTable;
    Texture texture;
    LevelHandler levelHandler;
    Stage stage;
    Label labels[],header,footer;
    int cnt = 0;
    Label.LabelStyle labelStyle;

    GameStateManager gsm1;
    public LevelsState(GameStateManager gsm) {
        super(gsm);
        this.gsm1=gsm;
         stage = new Stage();

        texture = new Texture("obstacle.png");
        final Skin skin = new Skin(Gdx.files.internal("flat-earth/flat-earth-ui.json"));
        Gdx.input.setInputProcessor(stage);



        Table table = new Table();//outer main table
        table.setFillParent(true);
        //table.debug();


        labels = new Label[12];
        header = new Label("LEVELS",skin);
        footer = new Label(">",skin);
        footer.setPosition(Gdx.graphics.getWidth() - Gdx.graphics.getWidth()/5  - (MyGdxGame.pixelToDp(20)), MyGdxGame.pixelToDp(30));
        footer.setAlignment(Align.center);
        footer.setWidth(Gdx.graphics.getWidth() / 5);
        footer.setHeight(Gdx.graphics.getWidth() / 8);
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("pirulen-rg.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = (int)MyGdxGame.pixelToDp(20);//30
        BitmapFont font20 = generator.generateFont(parameter);

        parameter.size = (int)MyGdxGame.pixelToDp(10);//30
        BitmapFont font10 = generator.generateFont(parameter);
        labelStyle = new Label.LabelStyle(font20, Color.WHITE);
        footer.setStyle(labelStyle);
        //background color
        Pixmap labelColor1 = new Pixmap((int)footer.getWidth(), (int)footer.getHeight(), Pixmap.Format.RGB888);
        labelColor1.setColor(Color.valueOf("990000"));
        labelColor1.fill();
        footer.setBounds(footer.getX(),footer.getY(),footer.getWidth(),footer.getHeight());
        footer.getStyle().background = new Image(new Texture(labelColor1)).getDrawable();
        footer.addListener(new InputListener(){
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    gsm1.set(new com.mygdxtest.game.States.LevelsState2(gsm1));
                dispose();

                return true;  // must return true for touchUp event to occur
            }
        });

        labelStyle = new Label.LabelStyle(font10, Color.WHITE);

        levelHandler = new LevelHandler(cam);


        scrollTable = new Table();

        //scrollTable.debug();

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 3; j++) {
                if(levelHandler.checkIfLevelSkipped(cnt)){
                    labels[cnt] = new Label(levelHandler.levelNames[cnt] + " [skipped]", skin);
                }
                else {
                    labels[cnt] = new Label(levelHandler.levelNames[cnt], skin);
                }

                labels[cnt].setAlignment(Align.center);

                labels[cnt].setWrap(true);
                //labels[cnt].setFontScale(MyGdxGame.pixelToDp(1));
                labels[cnt].setStyle(labelStyle);
                labels[cnt].setBounds(labels[cnt].getX(), labels[cnt].getY(), labels[cnt].getWidth(), labels[cnt].getHeight());

                //background color
                Pixmap labelColor = new Pixmap((int)labels[cnt].getWidth(), (int)labels[cnt].getHeight(), Pixmap.Format.RGB888);
                labelColor.setColor(Color.valueOf("990000"));
                labelColor.fill();
                labels[cnt].getStyle().background = new Image(new Texture(labelColor)).getDrawable();

                scrollTable.add(labels[cnt]).pad(MyGdxGame.pixelToDp(10)).width(Gdx.graphics.getWidth() / 4).height(Gdx.graphics.getWidth() / 4);
                if(levelHandler.getActualCurrentLevel()<cnt){
                    labels[cnt].setColor(Color.GRAY);
                    labels[cnt].setText("LOCKED");
                }
                else{
                    labels[cnt].setColor(Color.WHITE);
                }
                cnt++;
            }
            scrollTable.row();
        }


        scrollTable.addListener(new InputListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {

                return true;  // must return true for touchUp event to occur
            }

            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                Gdx.app.log("touch down", "" + x + " " + y + " " + button);
                if (x < scrollTable.getWidth() / 3 && y < scrollTable.getHeight() / 4) {
                    Gdx.app.log("touch down", "" + labels[9].getText());
                    if (levelHandler.getActualCurrentLevel() >= 9) {
                        com.mygdxtest.game.Flags.playLevelFlag = 9;
                        gsm1.set(new TwoBallsState(gsm1));
                    }
                } else if (x < scrollTable.getWidth() * 2 / 3 && y < scrollTable.getHeight() / 4) {
                    Gdx.app.log("touch down", "" + labels[10].getText());
                    if (levelHandler.getActualCurrentLevel() >= 10) {
                        com.mygdxtest.game.Flags.playLevelFlag = 10;
                        gsm1.set(new TwoBallsState(gsm1));
                    }
                } else if (y < scrollTable.getHeight() / 4) {
                    Gdx.app.log("touch down", "" + labels[11].getText());
                    if (levelHandler.getActualCurrentLevel() >= 11) {
                        com.mygdxtest.game.Flags.playLevelFlag = 11;
                        gsm1.set(new TwoBallsState(gsm1));
                    }
                } else if (x < scrollTable.getWidth() / 3 && y < scrollTable.getHeight() / 2) {
                    Gdx.app.log("touch down", "" + labels[6].getText());
                    if (levelHandler.getActualCurrentLevel() >= 6) {
                        com.mygdxtest.game.Flags.playLevelFlag = 6;
                        gsm1.set(new TwoBallsState(gsm1));
                    }
                } else if (x < scrollTable.getWidth() * 2 / 3 && y < scrollTable.getHeight() / 2) {
                    Gdx.app.log("touch down", "" + labels[7].getText());
                    if (levelHandler.getActualCurrentLevel() >= 7) {
                        com.mygdxtest.game.Flags.playLevelFlag = 7;
                        gsm1.set(new TwoBallsState(gsm1));
                    }
                } else if (y < scrollTable.getHeight() / 2) {
                    Gdx.app.log("touch down", "" + labels[8].getText());
                    if (levelHandler.getActualCurrentLevel() >= 8) {
                        com.mygdxtest.game.Flags.playLevelFlag = 8;
                        gsm1.set(new TwoBallsState(gsm1));
                    }
                } else if (x < scrollTable.getWidth() / 3 && y < scrollTable.getHeight() * 3 / 4) {
                    Gdx.app.log("touch down", "" + labels[3].getText());
                    if (levelHandler.getActualCurrentLevel() >= 3) {
                        com.mygdxtest.game.Flags.playLevelFlag = 3;
                        gsm1.set(new TwoBallsState(gsm1));
                    }
                } else if (x < scrollTable.getWidth() * 2 / 3 && y < scrollTable.getHeight() * 3 / 4) {
                    Gdx.app.log("touch down", "" + labels[4].getText());
                    if (levelHandler.getActualCurrentLevel() >= 4) {
                        com.mygdxtest.game.Flags.playLevelFlag = 4;
                        gsm1.set(new TwoBallsState(gsm1));
                    }
                } else if (y < scrollTable.getHeight() * 3 / 4) {
                    Gdx.app.log("touch down", "" + labels[5].getText());
                    if (levelHandler.getActualCurrentLevel() >= 5) {
                        com.mygdxtest.game.Flags.playLevelFlag = 5;
                        gsm1.set(new TwoBallsState(gsm1));
                    }
                } else if (x < scrollTable.getWidth() / 3) {
                    Gdx.app.log("touch down", "" + labels[0].getText());
                    if (levelHandler.getActualCurrentLevel() >= 0) {
                        com.mygdxtest.game.Flags.playLevelFlag = 0;
                        gsm1.set(new OneBallState(gsm1));
                    }
                } else if (x < scrollTable.getWidth() * 2 / 3) {
                    Gdx.app.log("touch down", "" + labels[1].getText());
                    if (levelHandler.getActualCurrentLevel() >= 1) {
                        com.mygdxtest.game.Flags.playLevelFlag = 1;
                        gsm1.set(new TwoBallsState(gsm1));
                    }
                } else {
                    Gdx.app.log("touch down", "" + labels[2].getText());
                    if (levelHandler.getActualCurrentLevel() >= 2) {
                        com.mygdxtest.game.Flags.playLevelFlag = 2;
                        gsm1.set(new TwoBallsState(gsm1));
                    }
                }
            }

        });


        table.add(scrollTable).expand();

        stage.addActor(table);
        stage.addActor(footer);


    }

    @Override
    public void update(float deltaTime) {
        stage.act(deltaTime);

        handleInput();

    }

    @Override
    public void handleInput() {
        if(Gdx.input.isKeyJustPressed(Input.Keys.BACK) ){
            Gdx.app.log("back","pressed");
            gsm.set(new MenuState(gsm));
            dispose();
        }

    }

    @Override
    public void render(SpriteBatch batch) {
        batch.setProjectionMatrix(cam.combined);
        batch.begin();
        //table.draw(batch,1);
        stage.draw();

        batch.end();
    }

    @Override
    public void dispose() {

    }

}