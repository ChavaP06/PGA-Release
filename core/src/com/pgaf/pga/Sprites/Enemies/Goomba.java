package com.pgaf.pga.Sprites.Enemies;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Array;
import com.pgaf.pga.PGA;
import com.pgaf.pga.Scenes.Hud;
import com.pgaf.pga.Screens.PlayScreen;
import com.pgaf.pga.Sprites.Pump;


public class Goomba extends Enemy
{
    private float stateTime;
    private Animation <TextureRegion> walkAnimation;
    private Array<TextureRegion> frames;
    private Animation <TextureRegion> walkAnimation2;
    private boolean setToDestroy;
    private boolean destroyed;
    float angle;


    public Goomba(PlayScreen screen, float x, float y) {
        super(screen, x, y);
        frames = new Array<TextureRegion>();

        //frame
        frames.add(new TextureRegion(screen.getAtlas().findRegion("enemy_melee"), 0 * 48, 0, 48, 64));
        frames.add(new TextureRegion(screen.getAtlas().findRegion("enemy_melee"), 1 * 48, 0, 48, 64));
        frames.add(new TextureRegion(screen.getAtlas().findRegion("enemy_melee"), 2 * 48, 0, 48, 64));
        frames.add(new TextureRegion(screen.getAtlas().findRegion("enemy_melee"), 1 * 48, 0, 48, 64));

        walkAnimation = new Animation(0.2f, frames);

        frames.clear();
        frames.add(new TextureRegion(screen.getAtlas().findRegion("enemy_melee"), 5 * 48, 0, 48, 64));
        frames.add(new TextureRegion(screen.getAtlas().findRegion("enemy_melee"), 4 * 48, 0, 48, 64));
        frames.add(new TextureRegion(screen.getAtlas().findRegion("enemy_melee"), 3 * 48, 0, 48, 64));
        frames.add(new TextureRegion(screen.getAtlas().findRegion("enemy_melee"), 4 * 48, 0, 48, 64));

        walkAnimation2 = new Animation(0.2f, frames);
        stateTime = 0;
        setBounds(getX(), getY(), 48 / PGA.PPM, 64 / PGA.PPM);
        setToDestroy = false;
        destroyed = false;
        angle = 0;
    }

    public void update(float dt){
        stateTime += dt;
        if(setToDestroy && !destroyed){
            world.destroyBody(b2body);
            destroyed = true;
            setRegion(new TextureRegion(screen.getAtlas().findRegion("enemy_melee"), 48*6, 0, 48, 64));
            stateTime = 0;
        }
        else if(!destroyed) {
            b2body.setLinearVelocity(velocity);
            setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2 + 24 / PGA.PPM);
            if(b2body.getLinearVelocity().x < 0)
            {
                setRegion(walkAnimation.getKeyFrame(stateTime, true));
            }
            else
            {
                setRegion(walkAnimation2.getKeyFrame(stateTime, true));
            }
        }


    }

    @Override
    protected void defineEnemy() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(getX(), getY());
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(6 / PGA.PPM);
        fdef.filter.categoryBits = PGA.ENEMY_BIT;
        fdef.filter.maskBits = PGA.GROUND_BIT |
                PGA.COIN_BIT |
                PGA.BRICK_BIT |
                PGA.ENEMY_BIT |
                PGA.OBJECT_BIT |
                PGA.EDGE_BIT |
                PGA.MARIO_BIT;

        fdef.shape = shape;
        b2body.createFixture(fdef).setUserData(this);

        //Create the Head here:
        PolygonShape head = new PolygonShape();
        Vector2[] vertice = new Vector2[4];
        vertice[0] = new Vector2(-5, 20).scl(1 / PGA.PPM);
        vertice[1] = new Vector2(5, 20).scl(1 / PGA.PPM);
        vertice[2] = new Vector2(-3, 3).scl(1 / PGA.PPM);
        vertice[3] = new Vector2(3, 3).scl(1 / PGA.PPM);
        head.set(vertice);

        fdef.shape = head;
        fdef.restitution = 0.5f;
        fdef.filter.categoryBits = PGA.ENEMY_HEAD_BIT;
        b2body.createFixture(fdef).setUserData(this);

    }

    public void draw(Batch batch){
        if(!destroyed || stateTime < 1)
            super.draw(batch);
    }

    @Override
    public void hitOnHead(Pump mario) {
        setToDestroy = true;
        Hud.addScore(100);
        PGA.manager.get("audio/pumpsound/stomp1.wav", Sound.class).play();
    }

    @Override
    public void hitByEnemy(Enemy enemy) {
        reverseVelocity(true,false);
    }

}
