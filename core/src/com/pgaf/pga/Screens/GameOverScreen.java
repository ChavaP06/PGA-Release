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


public class GameOverScreen implements Screen {
    private Viewport viewport;
    private Stage stage;
    private int score;
    private Game game;
    private static final String file= "pumpscore.txt";
    private static String lines;
    public GameOverScreen(Game game){
        this.game = game;
        score = Hud.getScore();
        viewport = new FitViewport(PGA.V_WIDTH, PGA.V_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, ((PGA) game).batch);

        Label.LabelStyle font = new Label.LabelStyle(new BitmapFont(), Color.WHITE);

        Table table = new Table();
        table.center();
        table.top();
        table.setFillParent(true);

        Label gameOverLabel = new Label("GAME OVER", font);
        Label scoreLabel = new Label("Your Score is", font);
        Label scoreLabel2 =new Label(String.format("%d", score), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        Label scoreLabel3 =new Label(String.format("%d", load()), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        Label highScore = new Label("*** HIGH SCORE !!! ***", font);
        Label playAgainLabel = new Label("Pump to Play Again", font);
        //if(this.score>Integer.parseInt(load()))
        //write();

        table.add(gameOverLabel).expandX().padTop(20f);
        table.row();
        table.add(scoreLabel).expandX().padTop(24f);
        table.row();
        table.add(scoreLabel2).expandX().padTop(10f);
        table.row();
        if(score > load())
        {
            table.add(highScore).expandX().padTop(10f);
            table.row();
            table.add(playAgainLabel).expandX().padTop(20f);
            write();
        }
        else
        {
            table.add(playAgainLabel).expandX().padTop(50f);
        }

        stage.addActor(table);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        if(Gdx.input.justTouched()) {
            game.setScreen(new MainMenu((PGA) game));
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

    public void write()  {
        Preferences prefs =Gdx.app.getPreferences("pumpscore");
        prefs.putInteger("score",this.score);
        prefs.flush();
    }
    public int load(){
        Preferences prefs =Gdx.app.getPreferences("pumpscore");
        return prefs.getInteger("score");
    }
}
