package com.pgaf.pga.Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.pgaf.pga.Scenes.HealthBar;
import com.pgaf.pga.Sprites.Enemies.Enemy;
import com.pgaf.pga.PGA;
import com.pgaf.pga.Scenes.Hud;
import com.pgaf.pga.Screens.PlayScreen;
import com.pgaf.pga.Sprites.Other.FireBall;

public class Pump extends Sprite {
    public enum State { FALLING, JUMPING, STANDING, RUNNING, GROWING, DEAD, IDLING, ATTACKING, SECRET, AIR_ATTACKING};
    public State currentState;
    public State previousState;

    public World world;
    public Body b2body;

    private TextureRegion marioStand;
    private TextureRegion secret;
    private Animation <TextureRegion> marioIdle;
    private Animation <TextureRegion> marioWalk;
    private Animation <TextureRegion> marioRun;
    private Animation <TextureRegion> marioJump;
    private Animation <TextureRegion> marioJAttack;
    private Animation <TextureRegion> marioAttack;
    private TextureRegion marioDead;
    private TextureRegion bigMarioStand;
    private TextureRegion bigMarioJump;
    private TextureRegion marioFalling;
    private Animation <TextureRegion> bigMarioRun;
    private Animation <TextureRegion> growMario;

    private float stateTimer;
    private boolean runningRight;
    private boolean marioIsBig;
    private boolean runGrowAnimation;
    private boolean timeToDefineBigMario;
    private boolean timeToRedefineMario;
    private boolean marioIsDead;
    private PlayScreen screen;

    private Array<FireBall> fireballs;
    private int HP = 2;

    public Pump(PlayScreen screen){
        //initialize default values
        this.screen = screen;
        this.world = screen.getWorld();
        currentState = State.STANDING;
        previousState = State.STANDING;
        stateTimer = 0;
        runningRight = true;

        //RUNNING
        Array<TextureRegion> frames = new Array<TextureRegion>();
        //IDLE
        for(int i=1; i<=3; i++)
            frames.add(new TextureRegion(screen.getAtlas().findRegion("pumpx"), i * 48, 0, 48, 64));
        //0.1 float duration of animation
        marioIdle = new Animation(0.20f, frames);
        frames.clear();

        for(int i=4; i<=8; i++)
            frames.add(new TextureRegion(screen.getAtlas().findRegion("pumpx"), i * 48, 0, 48, 64));
        //0.1 float duration of animation
        marioWalk = new Animation(0.11f, frames);
        marioRun = new Animation(0.075f, frames);
        frames.clear();

        //JUMPING (START FROM THE NEXT SPRITE)
        for(int i=9; i<=10; i++)
            frames.add(new TextureRegion(screen.getAtlas().findRegion("pumpx"), i * 48, 0, 48, 64));
        //0.1 float duration of animation
        marioJump = new Animation(0.25f, frames);
        frames.clear();
        //FALL
        marioFalling = new TextureRegion(screen.getAtlas().findRegion("pumpx"), 10 * 48, 0, 48, 64);

        //SECRET
        secret = new TextureRegion(screen.getAtlas().findRegion("secret"), 0, 0, 48, 64);

        //Stand
        marioStand = new TextureRegion(screen.getAtlas().findRegion("pumpx"), 48, 0, 48, 64);
        //create dead mario texture region
        marioDead = new TextureRegion(screen.getAtlas().findRegion("pumpx"), 0, 0, 48, 64);

        //ATTACKING AIR
        for(int i=11; i<=13; i++)
            frames.add(new TextureRegion(screen.getAtlas().findRegion("pumpx"), i * 48, 0, 48, 64));
        //0.1 float duration of animation
        marioJAttack = new Animation(0.1f, frames);
        frames.clear();
        //ATTACKING
        for(int i=14; i<=15; i++)
            frames.add(new TextureRegion(screen.getAtlas().findRegion("pumpx"), i * 48, 0, 48, 64));
        //0.1 float duration of animation
        marioAttack = new Animation(0.15f, frames);
        frames.clear();
        //define mario in Box2d
        defineMario();

        //set initial values for marios location, width and height. And initial frame as marioStand.
        setBounds(0, 0, 48 / PGA.PPM, 64 / PGA.PPM);
        setRegion(marioStand);

        fireballs = new Array<FireBall>();

    }

    public void update(float dt){

        // time is up : too late mario dies T_T
        // the !isDead() method is used to prevent multiple invocation
        // of "die music" and jumping
        // there is probably better ways to do that but it works for now.
        if (screen.getHud().isTimeUp() && !isDead()) {
            die();
        }

        //send pump's current hp back to hud --- hp
        HealthBar.setHP(getHP());
        Hud.setHP(getHP());

        //update our sprite to correspond with the position of our Box2D body
        //if(marioIsBig)
        //    setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2 - 6 / PGA.PPM);
        //else
            setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2 + 24 / PGA.PPM);
        //update sprite with the correct frame depending on marios current action
        setRegion(getFrame(dt));
        //if(timeToDefineBigMario)
        //    defineBigMario();
        //if(timeToRedefineMario)
        //    redefineMario();

        for(FireBall  ball : fireballs) {
            ball.update(dt);
            if(ball.isDestroyed())
                fireballs.removeValue(ball, true);
        }

    }

    public TextureRegion getFrame(float dt){
        //get marios current state. ie. jumping, running, standing...
        currentState = getState();

        TextureRegion region;

        //depending on the state, get corresponding animation keyFrame.
        switch(currentState){
            case DEAD:
                region = marioDead;
                break;
            //case GROWING:
            //region = growMario.getKeyFrame(stateTimer);
            //    if(growMario.isAnimationFinished(stateTimer)) {
            //        runGrowAnimation = false;
            //   }
            //break;
            case JUMPING:
                //region = marioIsBig ? bigMarioJump : marioJump;
                region = marioJump.getKeyFrame(stateTimer);
                break;
            case RUNNING:
                //region = marioIsBig ? bigMarioRun.getKeyFrame(stateTimer, true) : marioRun.getKeyFrame(stateTimer, true);
                region = marioRun.getKeyFrame(stateTimer, true);
                break;
            case FALLING:
                region = marioFalling; break;
            case IDLING: region = (TextureRegion) marioIdle.getKeyFrame(stateTimer, true); break;
            case ATTACKING: region = (TextureRegion) marioAttack.getKeyFrame(stateTimer); break;
            case AIR_ATTACKING: region = (TextureRegion) marioJAttack.getKeyFrame(stateTimer); break;
            case SECRET: region = secret; break;
            default:
                //region = marioIsBig ? bigMarioStand : marioStand;
                region = marioStand;
                break;
        }

        //if mario is running left and the texture isnt facing left... flip it.
        if((b2body.getLinearVelocity().x < 0 || !runningRight) && !region.isFlipX()){
            region.flip(true, false);
            runningRight = false;
        }

        //if mario is running right and the texture isnt facing right... flip it.
        else if((b2body.getLinearVelocity().x > 0 || runningRight) && region.isFlipX()){
            region.flip(true, false);
            runningRight = true;
        }

        //if the current state is the same as the previous state increase the state timer.
        //otherwise the state has changed and we need to reset timer.
        stateTimer = currentState == previousState ? stateTimer + dt : 0;
        //update previous state
        previousState = currentState;
        //return our final adjusted frame
        return region;

    }

    public State getState(){
        //Test to Box2D for velocity on the X and Y-Axis
        //if mario is going positive in Y-Axis he is jumping... or if he just jumped and is falling remain in jump state
        if(marioIsDead)
            return State.DEAD;
        //else if(Gdx.input.isKeyPressed(Input.Keys.X) && previousState == State.JUMPING || previousState == State.FALLING)
        //    return State.AIR_ATTACKING;
        //else if(runGrowAnimation)
        //    return State.GROWING;
        else if(Gdx.input.isKeyPressed(Input.Keys.DOWN) && Gdx.input.isKeyPressed(Input.Keys.Z) && Gdx.input.isKeyPressed(Input.Keys.X))
            return State.SECRET;
        else if(b2body.getLinearVelocity().y > 0 || (b2body.getLinearVelocity().y < 0 && previousState == State.JUMPING) && previousState != State.AIR_ATTACKING)//he is going up
            return  State.JUMPING;
        //if negative in Y-Axis mario is falling
        else if(b2body.getLinearVelocity().y < 0 && previousState != State.AIR_ATTACKING)
            return State.FALLING;
        //if mario is positive or negative in the X axis he is running
        else if(b2body.getLinearVelocity().x != 0)
            return State.RUNNING;
        else if(b2body.getLinearVelocity().x == 0 && b2body.getLinearVelocity().y == 0)
            return State.IDLING;
        //else if(Gdx.input.isKeyJustPressed(Input.Keys.X) || previousState == State.ATTACKING)
        //    return State.ATTACKING;
        //if none of these return then he must be standing
        else
            return State.STANDING;
    }

    public void grow(){
        /*
        if( !isBig() ) {
            runGrowAnimation = true;
            marioIsBig = true;
            timeToDefineBigMario = true;
            setBounds(getX(), getY(), getWidth(), getHeight() * 2);
            PGA.manager.get("audio/sounds/powerup.wav", Sound.class).play();
        }

         */

        if(HP < 3)
        {
            HP += 1;
            PGA.manager.get("audio/pumpsound/powerup1.wav", Sound.class).play();
        }
    }

    public void die() {

        if (!isDead()) {

            PGA.manager.get("audio/music/pumptheme.ogg", Music.class).stop();
            PGA.manager.get("audio/pumpsound/pumpdie.wav", Sound.class).play();
            marioIsDead = true;
            Filter filter = new Filter();
            filter.maskBits = PGA.NOTHING_BIT;
            HP = 0;

            for (Fixture fixture : b2body.getFixtureList()) {
                fixture.setFilterData(filter);
            }

            b2body.applyLinearImpulse(new Vector2(0, 4f), b2body.getWorldCenter(), true);
        }
    }

    public boolean isOnGround() {
        if(getState() == State.STANDING || getState() == State.RUNNING || getState() == State.IDLING)
            return true;
        else
            return false;
    }

    public boolean isDead(){
        return marioIsDead;
    }

    public float getStateTimer(){
        return stateTimer;
    }

    public boolean isBig(){
        return marioIsBig;
    }

    public int getHP() {
        return HP;
    }

    public void jump(){
        if ( currentState != State.JUMPING ) {
            b2body.applyLinearImpulse(new Vector2(0, 4f), b2body.getWorldCenter(), true);
            currentState = State.JUMPING;
        }
    }

    public void hit(Enemy enemy){
        if(HP > 1)
        {

            HP = HP - 1;
            screen.dmgTaken();
            PGA.manager.get("audio/pumpsound/powerdown.wav", Sound.class).play();

        }
        else
        {

            HP = HP - 1;
            die();

        }
    }

    /*
    public void redefineMario(){
        Vector2 position = b2body.getPosition();
        world.destroyBody(b2body);

        BodyDef bdef = new BodyDef();
        bdef.position.set(position);
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(6 / PGA.PPM);
        fdef.filter.categoryBits = PGA.MARIO_BIT;
        fdef.filter.maskBits = PGA.GROUND_BIT |
                PGA.COIN_BIT |
                PGA.BRICK_BIT |
                PGA.ENEMY_BIT |
                PGA.OBJECT_BIT |
                PGA.ENEMY_HEAD_BIT |
                PGA.ITEM_BIT ;


        fdef.shape = shape;
        b2body.createFixture(fdef).setUserData(this);

        EdgeShape head = new EdgeShape();
        head.set(new Vector2(-2 / PGA.PPM, 6 / PGA.PPM), new Vector2(2 / PGA.PPM, 6 / PGA.PPM));
        fdef.filter.categoryBits = PGA.MARIO_HEAD_BIT;
        fdef.shape = head;
        fdef.isSensor = true;

        b2body.createFixture(fdef).setUserData(this);

        timeToRedefineMario = false;

    }

    public void defineBigMario(){
        Vector2 currentPosition = b2body.getPosition();
        world.destroyBody(b2body);

        BodyDef bdef = new BodyDef();
        bdef.position.set(currentPosition.add(0, 10 / PGA.PPM));
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(6 / PGA.PPM);
        fdef.filter.categoryBits = PGA.MARIO_BIT;
        fdef.filter.maskBits = PGA.GROUND_BIT |
                PGA.COIN_BIT |
                PGA.BRICK_BIT |
                PGA.ENEMY_BIT |
                PGA.OBJECT_BIT |
                PGA.ENEMY_HEAD_BIT |
                PGA.ITEM_BIT;

        fdef.shape = shape;
        b2body.createFixture(fdef).setUserData(this);
        shape.setPosition(new Vector2(0, -14 / PGA.PPM));
        b2body.createFixture(fdef).setUserData(this);

        EdgeShape head = new EdgeShape();
        head.set(new Vector2(-2 / PGA.PPM, 6 / PGA.PPM), new Vector2(2 / PGA.PPM, 6 / PGA.PPM));
        fdef.filter.categoryBits = PGA.MARIO_HEAD_BIT;
        fdef.shape = head;
        fdef.isSensor = true;

        b2body.createFixture(fdef).setUserData(this);
        timeToDefineBigMario = false;
    }*/

    public void defineMario(){
        BodyDef bdef = new BodyDef();
        bdef.position.set(32 / PGA.PPM, 32 / PGA.PPM);
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(6 / PGA.PPM);

        // define circular shape
       //FixtureDef fdef = new FixtureDef();
        //PolygonShape shape = new PolygonShape();
        //shape.setAsBox(0,0.09f);

        shape.setRadius(6/ PGA.PPM);
        fdef.filter.categoryBits = PGA.MARIO_BIT;
        fdef.filter.maskBits = PGA.GROUND_BIT |
                PGA.COIN_BIT |
                PGA.BRICK_BIT |
                PGA.ENEMY_BIT |
                PGA.OBJECT_BIT |
                PGA.ENEMY_HEAD_BIT |
                PGA.PIT_BIT |
                PGA.ITEM_BIT;

        fdef.shape = shape;
        b2body.createFixture(fdef).setUserData(this);

        EdgeShape head = new EdgeShape();
        head.set(new Vector2(-2 / PGA.PPM, 6 / PGA.PPM), new Vector2(2 / PGA.PPM, 6 / PGA.PPM));
        fdef.filter.categoryBits = PGA.MARIO_HEAD_BIT;
        fdef.shape = head;
        fdef.isSensor = true;

        b2body.createFixture(fdef).setUserData(this);
    }

    public void fire(){
        fireballs.add(new FireBall(screen, b2body.getPosition().x, b2body.getPosition().y, runningRight ? true : false));
    }

    public void draw(Batch batch){
        super.draw(batch);
        for(FireBall ball : fireballs)
            ball.draw(batch);
    }
}
