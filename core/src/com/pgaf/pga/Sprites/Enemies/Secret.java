package com.pgaf.pga.Sprites.Enemies;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.utils.Array;
import com.pgaf.pga.PGA;
import com.pgaf.pga.Screens.PlayScreen;
import com.pgaf.pga.Sprites.Pump;


public class Secret extends Enemy
{
    private float stateTime;
    private Animation <TextureRegion> Animation;
    private Array<TextureRegion> frames;
    private boolean setToDestroy;
    private boolean destroyed;
    float angle;


    public Secret(PlayScreen screen, float x, float y) {
        super(screen, x, y);
        frames = new Array<TextureRegion>();

        //frame
        frames.add(new TextureRegion(screen.getAtlas().findRegion("bonus"), 0 * 48, 0, 48, 48));
        frames.add(new TextureRegion(screen.getAtlas().findRegion("bonus"), 1 * 48, 0, 48, 48));
        frames.add(new TextureRegion(screen.getAtlas().findRegion("bonus"), 2 * 48, 0, 48, 48));
        frames.add(new TextureRegion(screen.getAtlas().findRegion("bonus"), 3 * 48, 0, 48, 48));
        frames.add(new TextureRegion(screen.getAtlas().findRegion("bonus"), 4 * 48, 0, 48, 48));
        frames.add(new TextureRegion(screen.getAtlas().findRegion("bonus"), 5 * 48, 0, 48, 48));
        frames.add(new TextureRegion(screen.getAtlas().findRegion("bonus"), 6 * 48, 0, 48, 48));
        frames.add(new TextureRegion(screen.getAtlas().findRegion("bonus"), 7 * 48, 0, 48, 48));
        frames.add(new TextureRegion(screen.getAtlas().findRegion("bonus"), 8 * 48, 0, 48, 48));
        frames.add(new TextureRegion(screen.getAtlas().findRegion("bonus"), 9 * 48, 0, 48, 48));
        frames.add(new TextureRegion(screen.getAtlas().findRegion("bonus"), 10 * 48, 0, 48, 48));
        frames.add(new TextureRegion(screen.getAtlas().findRegion("bonus"), 9 * 48, 0, 48, 48));
        frames.add(new TextureRegion(screen.getAtlas().findRegion("bonus"), 8 * 48, 0, 48, 48));
        frames.add(new TextureRegion(screen.getAtlas().findRegion("bonus"), 7 * 48, 0, 48, 48));
        frames.add(new TextureRegion(screen.getAtlas().findRegion("bonus"), 6 * 48, 0, 48, 48));
        frames.add(new TextureRegion(screen.getAtlas().findRegion("bonus"), 5 * 48, 0, 48, 48));
        frames.add(new TextureRegion(screen.getAtlas().findRegion("bonus"), 4 * 48, 0, 48, 48));
        frames.add(new TextureRegion(screen.getAtlas().findRegion("bonus"), 3 * 48, 0, 48, 48));
        frames.add(new TextureRegion(screen.getAtlas().findRegion("bonus"), 2 * 48, 0, 48, 48));
        frames.add(new TextureRegion(screen.getAtlas().findRegion("bonus"), 1 * 48, 0, 48, 48));


        Animation = new Animation(0.035f, frames);

        stateTime = 0;
        setBounds(getX(), getY(), 48 / PGA.PPM, 48 / PGA.PPM);
        setToDestroy = false;
        destroyed = false;
        angle = 0;
    }

    public void update(float dt){
        stateTime += dt;
        if(setToDestroy && !destroyed){
            world.destroyBody(b2body);
            destroyed = true;
            setRegion(new TextureRegion(screen.getAtlas().findRegion("secret"), 0, 0, 0, 0));
            stateTime = 0;
        }
        else if(!destroyed) {
            b2body.setLinearVelocity(0, 0);
            setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);

            setRegion(Animation.getKeyFrame(stateTime, true));
        }


    }

    @Override
    protected void defineEnemy() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(getX(), getY());
        bdef.type = BodyDef.BodyType.StaticBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(18 / PGA.PPM);
        fdef.filter.categoryBits = PGA.ENEMY_HEAD_BIT;
        fdef.filter.maskBits = PGA.GROUND_BIT |
                PGA.COIN_BIT |
                PGA.BRICK_BIT |
                PGA.ENEMY_BIT |
                PGA.OBJECT_BIT |
                PGA.EDGE_BIT |
                PGA.MARIO_BIT;

        fdef.shape = shape;
        b2body.createFixture(fdef).setUserData(this);

    }

    public void draw(Batch batch){
        if(!destroyed || stateTime < 1)
            super.draw(batch);
    }



    @Override
    public void hitOnHead(Pump mario) {
        setToDestroy = true;
        mario.grow();
        mario.grow();
        //Hud.addScore(1000);
        PGA.manager.get("audio/pumpsound/pumpdie.wav", Sound.class).play();
        //PGA.manager.get("audio/sounds/stomp.wav", Sound.class).play();
    }

    @Override
    public void hitByEnemy(Enemy enemy) {
        reverseVelocity(true,false);
    }

}
