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
import com.pgaf.pga.Scenes.Hud;
import com.pgaf.pga.Screens.PlayScreen;
import com.pgaf.pga.Sprites.Pump;

public class Milk extends Enemy
{
    private float stateTime;
    private Animation <TextureRegion> Animation;
    private Array<TextureRegion> frames;
    private boolean setToDestroy;
    private boolean destroyed;
    float angle;

    public Milk(PlayScreen screen, float x, float y) {
        super(screen, x, y);
        frames = new Array<TextureRegion>();

        //frame
        frames.add(new TextureRegion(screen.getAtlas().findRegion("pumpmilk_sprite"), 0 * 48, 0, 48, 48));
        frames.add(new TextureRegion(screen.getAtlas().findRegion("pumpmilk_sprite"), 1 * 48, 0, 48, 48));
        frames.add(new TextureRegion(screen.getAtlas().findRegion("pumpmilk_sprite"), 2 * 48, 0, 48, 48));
        frames.add(new TextureRegion(screen.getAtlas().findRegion("pumpmilk_sprite"), 3 * 48, 0, 48, 48));
        frames.add(new TextureRegion(screen.getAtlas().findRegion("pumpmilk_sprite"), 4 * 48, 0, 48, 48));
        frames.add(new TextureRegion(screen.getAtlas().findRegion("pumpmilk_sprite"), 5 * 48, 0, 48, 48));
        frames.add(new TextureRegion(screen.getAtlas().findRegion("pumpmilk_sprite"), 6 * 48, 0, 48, 48));
        frames.add(new TextureRegion(screen.getAtlas().findRegion("pumpmilk_sprite"), 7 * 48, 0, 48, 48));

        Animation = new Animation(0.1f, frames);

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
            setRegion(new TextureRegion(screen.getAtlas().findRegion("pumpmilk_sprite"), 0, 0, 0, 0));
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
        fdef.shape = shape;
       fdef.filter.categoryBits = PGA.ENEMY_HEAD_BIT;
       // fdef.filter.maskBits = PGA.MARIO_BIT;

        b2body.createFixture(fdef).setUserData(this);

        /*
        //Create the Head here:
        PolygonShape head = new PolygonShape();
        Vector2[] vertice = new Vector2[4];
        vertice[0] = new Vector2(-5, 8).scl(1 / PGA.PPM);
        vertice[1] = new Vector2(5, 8).scl(1 / PGA.PPM);
        vertice[2] = new Vector2(-3, 3).scl(1 / PGA.PPM);
        vertice[3] = new Vector2(3, 3).scl(1 / PGA.PPM);
        head.set(vertice);

        fdef.shape = head;
        fdef.restitution = 0.5f;
        fdef.filter.categoryBits = PGA.ENEMY_HEAD_BIT;
        b2body.createFixture(fdef).setUserData(this);
        */
    }

    public void draw(Batch batch){
        if(!destroyed || stateTime < 1)
            super.draw(batch);
    }



    @Override
    public void hitOnHead(Pump mario) {
        setToDestroy = true;
        mario.grow();
        Hud.addScore(1000);
        PGA.manager.get("audio/pumpsound/powerup1.wav", Sound.class).play();

    }

    @Override
    public void hitByEnemy(Enemy enemy) {
        //if(enemy instanceof Turtle && ((Turtle) enemy).currentState == Turtle.State.MOVING_SHELL)
        //   setToDestroy = true;
        //else
        //    reverseVelocity(true, false);
        reverseVelocity(true,false);
    }

}
