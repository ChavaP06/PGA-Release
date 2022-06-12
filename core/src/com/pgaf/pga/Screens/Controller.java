package com.pgaf.pga.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.pgaf.pga.PGA;

public class Controller {
    private float buttsize = (float) (110);
    Viewport viewport;
    Stage stage;
    boolean upPressed, downPressed, leftPressed, rightPressed,jumpPressed,runPressed;
    OrthographicCamera cam;

    public Controller(){
        cam = new OrthographicCamera();
        viewport = new FitViewport(800,480,cam);
        stage = new Stage(viewport, PGA.batch);
        Gdx.input.setInputProcessor(stage);

        Table table = new Table();
        table.left().bottom();

        Image upImg = new Image(new Texture("keys/upArrow.png"));
        upImg.setSize(buttsize,buttsize);
        upImg.addListener(new InputListener(){

            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button){
                upPressed = true;
                return true;
            }
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button){
                upPressed = false;

            }


        });

        Image downImg = new Image(new Texture("keys/downArrow.png"));
        downImg.setSize(buttsize,buttsize);
        downImg.addListener(new InputListener(){

            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button){
                downPressed = true;
                return true;
            }
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button){
                downPressed = false;

            }


        });
        Image rightImg = new Image(new Texture("keys/rightArrow.png"));
        rightImg.setSize(buttsize,buttsize);
        rightImg.addListener(new InputListener(){

            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button){
                rightPressed = true;
                return true;
            }
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button){
                rightPressed = false;

            }


        });

        Image leftImg = new Image(new Texture("keys/leftArrow.png"));
        leftImg.setSize(buttsize,buttsize);
        leftImg.addListener(new InputListener(){

            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button){
                leftPressed = true;
                return true;
            }
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button){
                leftPressed = false;

            }


        });

        Image jumpImg = new Image(new Texture("keys/run.png"));
        jumpImg.setSize(buttsize,buttsize);
        jumpImg.addListener(new InputListener(){

            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button){
                jumpPressed = true;
                return true;
            }
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button){
                jumpPressed = false;

            }


        });

        table.row().pad(5,5,35,5);
        table.add(leftImg).size(leftImg.getWidth(),leftImg.getHeight());
        table.add(rightImg).size(rightImg.getWidth(),rightImg.getHeight());
        table.add();
        table.add();
        table.add();
        table.add();
        table.add();
        table.add();
        table.add();
        table.add();
        table.add();
        table.add();
        table.add();
        table.add();  table.add(); table.add(); table.add(); table.add(); table.add();
        table.add(); table.add(); table.add(); table.add(); table.add(); table.add();
        table.add(); table.add(); table.add(); table.add(); table.add(); table.add();
        table.add(); table.add(); table.add(); table.add(); table.add(); table.add();
        table.add(); table.add();
        table.add(); table.add(); table.add(); table.add(); table.add(); table.add();table.add(jumpImg).size(upImg.getWidth(),upImg.getHeight()).center().right();

        stage.addActor(table);
    }

    public void draw(){
        stage.draw();
    }

    public boolean isUpPressed() {
        return upPressed;
    }

    public boolean isDownPressed() {
        return downPressed;
    }

    public boolean isLeftPressed() {
        return leftPressed;
    }

    public boolean isRightPressed() {
        return rightPressed;
    }

    public boolean isJumpPressed() {
        return jumpPressed;
    }

    public boolean isRunPressed() {
        return runPressed;
    }

    public void resize(int width, int height){
        viewport.update(width, height);
    }


}
