package com.pgaf.pga.Screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.pgaf.pga.PGA;
import com.pgaf.pga.Scenes.Hud;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class MainMenu implements Screen {
    private Viewport viewport;
    private Stage stage;
    private Game game;
    private static final String file= "pumpscore.txt";
    private static String lines;
    public MainMenu(Game game){

        this.game = game;
        viewport = new FitViewport(PGA.V_WIDTH, PGA.V_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, ((PGA) game).batch);
        Gdx.input.setInputProcessor(stage);

        Label.LabelStyle font = new Label.LabelStyle(new BitmapFont(), Color.WHITE);
        BitmapFont ohyeah = new BitmapFont();
        ohyeah.getData().setScale(0.6f,0.6f);

        Table table = new Table();
        table.center();
        table.top();
        table.setFillParent(true);
        stage.getActionsRequestRendering();
        Label gameOverLabel = new Label("Pumpgood's Adventure", font);
        Label highscoreLabel1 = new Label("HIGH SCORE", new Label.LabelStyle(ohyeah, Color.WHITE));
        Label highscoreLabel2 =new Label(String.format("%d", load()), new Label.LabelStyle(ohyeah, Color.WHITE));
        Label playAgainLabel = new Label("Pump to Start", font);

        table.add(highscoreLabel1).padTop(12f);
        table.row();
        table.add(highscoreLabel2);
        table.row();
        table.add(gameOverLabel).expandX().padBottom(10f).padTop(32f);
        table.row();
        table.add(playAgainLabel).expandX().padTop(10f);

        stage.addActor(table);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

        if(Gdx.input.justTouched()) {

            game.setScreen(new PlayScreen((PGA) game));
            dispose();
        }

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.draw();

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        stage.dispose();
    }



    public int load(){
        Preferences prefs = Gdx.app.getPreferences("pumpscore");
        return prefs.getInteger("score");
    }



}
