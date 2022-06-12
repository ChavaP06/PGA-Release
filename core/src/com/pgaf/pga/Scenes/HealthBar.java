package com.pgaf.pga.Scenes;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.pgaf.pga.PGA;
import com.pgaf.pga.Screens.PlayScreen;
import com.pgaf.pga.Sprites.Pump;

public class HealthBar implements Disposable{

    //Scene2D.ui Stage and its own Viewport for HUD
    public Stage stage;
    private Viewport viewport;

    //HP Variables
    private float timeCount;
    private static Integer hp;
    Image image1;
    Image image2;
    Image image3;
    //Scene2D widgets

    private Pump player;

    public HealthBar(SpriteBatch sb){
        hp = 0;
        //setup the HUD viewport using a new camera seperate from our gamecam
        //define our stage using that viewport and our games spritebatch
        viewport = new FitViewport(PGA.V_WIDTH, PGA.V_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, sb);

        //hp
        //Texture texture = new Texture("milk_icon.png");
        //Image image1 = new Image(texture);
        //image1.setPosition(40, 155);
        //stage.addActor(image1);
        Texture texture = new Texture("milk_icon.png");
        image1 = new Image(texture);
        image1.setPosition(40, 155);
        stage.addActor(image1);

        image2 = new Image(texture);
        image2.setPosition(40 + 8 + 15, 155);
        stage.addActor(image2);

        image3 = new Image(texture);
        image3.setPosition(40 + 16 + 30, 155);
        stage.addActor(image3);

        //texture.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
        //TextureRegion textureRegion = new TextureRegion(texture);
        //textureRegion.setRegion(0,0,texture.getWidth() * hp,texture.getHeight());
        //Image image4 = new Image(textureRegion);
        //image4.setSize(200,100);
        //image4.setPosition(40,155);
        //stage.addActor(image4);
    }

    public void update(float dt){
        timeCount += dt;
        iconShow(timeCount);
        //time count
        if(timeCount >= 1){
            //iconShow();
            timeCount = 0;
        }
    }

    public static void setHP(int value){
        hp = value;
    }

    private void iconShow(float timeCount){
        switch(hp)
        {
            case 3 :
            {
                image1.setColor(image1.getColor().r, image2.getColor().g, image2.getColor().b, 1);
                image2.setColor(image2.getColor().r, image2.getColor().g, image2.getColor().b, 1);
                image3.setColor(image3.getColor().r, image2.getColor().g, image2.getColor().b, 1);
            }
            break;

            case 2 :
            {
                image1.setColor(image1.getColor().r, image1.getColor().g, image1.getColor().b, 1);
                image2.setColor(image2.getColor().r, image2.getColor().g, image2.getColor().b, 1);
                image3.setColor(image3.getColor().r, image3.getColor().g, image3.getColor().b, 0);
            }
            break;

            case 1 :
            {

                image2.setColor(image2.getColor().r, image2.getColor().g, image2.getColor().b, 0);
                image3.setColor(image3.getColor().r, image3.getColor().g, image3.getColor().b, 0);
                if(timeCount <= 0.5f){
                    image1.setColor(image1.getColor().r, image1.getColor().g, image1.getColor().b, 0);
                }
                else
                {
                    image1.setColor(image1.getColor().r, image1.getColor().g, image1.getColor().b, 1);
                }

            }
            break;

            default :
            {
                image1.setColor(image1.getColor().r, image1.getColor().g, image1.getColor().b, 0);
                image2.setColor(image2.getColor().r, image2.getColor().g, image2.getColor().b, 0);
                image3.setColor(image3.getColor().r, image3.getColor().g, image3.getColor().b, 0);
            }

        }
    }

    @Override
    public void dispose() { stage.dispose(); }
}
