package com.pgaf.pga.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.pgaf.pga.PGA;
import com.pgaf.pga.Scenes.HealthBar;
import com.pgaf.pga.Scenes.Hud;
import com.pgaf.pga.Sprites.Enemies.Enemy;
import com.pgaf.pga.Sprites.Items.Item;
import com.pgaf.pga.Sprites.Items.ItemDef;
import com.pgaf.pga.Sprites.Items.Mushroom;
import com.pgaf.pga.Sprites.Pump;
import com.pgaf.pga.Tools.B2WorldCreator;
import com.pgaf.pga.Tools.WorldContactListener;

import java.util.concurrent.LinkedBlockingQueue;


public class PlayScreen implements Screen{
    //Reference to our Game, used to set Screens
    private PGA game;
    private TextureAtlas atlas;
    Controller controller;
    public static boolean alreadyDestroyed = false;

    //basic playscreen variables
    private OrthographicCamera gamecam;
    private Viewport gamePort;
    private Hud hud;
    private HealthBar healthbar;

    //Tiled map variables
    private TmxMapLoader maploader;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;

    //Box2d variables
    private World world;
    private Box2DDebugRenderer b2dr;
    private B2WorldCreator creator;

    //sprites
    private Pump player;

    private Music music;

    private Array<Item> items;
    private LinkedBlockingQueue<ItemDef> itemsToSpawn;

    //fireball cd
    private int cooldown = 0;
    private int isFinish = 0;
    private int finishTimer = 0;
    private int finalScore = 0;

    private Sound sound;

    public PlayScreen(PGA game){
        //atlas = new TextureAtlas("Mario_and_Enemies.pack");
        controller = new Controller();
        atlas = new TextureAtlas("pumpgood.pack");

        this.game = game;
        //create cam used to follow mario through cam world
        gamecam = new OrthographicCamera();

        //create a FitViewport to maintain virtual aspect ratio despite screen size
        gamePort = new FitViewport(PGA.V_WIDTH / PGA.PPM, PGA.V_HEIGHT / PGA.PPM, gamecam);

        //create our game HUD for scores/timers/level info
        hud = new Hud(game.batch);
        healthbar = new HealthBar(game.batch);

        //Load our map and setup our map renderer
        maploader = new TmxMapLoader();
        map = maploader.load("PGAmap.tmx");
        renderer = new OrthogonalTiledMapRenderer(map, 1  / PGA.PPM);

        //initially set our gamcam to be centered correctly at the start of of map
        gamecam.position.set(gamePort.getWorldWidth() / 2, gamePort.getWorldHeight() / 2, 0);

        //create our Box2D world, setting no gravity in X, -10 gravity in Y, and allow bodies to sleep
        world = new World(new Vector2(0, -9), true);
        //allows for debug lines of our box2d world.
        b2dr = new Box2DDebugRenderer();

        creator = new B2WorldCreator(this);

        //create mario in our game world
        player = new Pump(this);

        //player position
        player.b2body.setTransform(3f,0.6f,0f);

        world.setContactListener(new WorldContactListener());

        music = PGA.manager.get("audio/music/pumptheme.ogg", Music.class);
        music.setLooping(true);
        music.setVolume(1f);
        music.play();

        items = new Array<Item>();
        itemsToSpawn = new LinkedBlockingQueue<ItemDef>();
    }

    public void spawnItem(ItemDef idef){
        itemsToSpawn.add(idef);
    }

    public void handleSpawningItems(){
        if(!itemsToSpawn.isEmpty()){
            ItemDef idef = itemsToSpawn.poll();
            if(idef.type == Mushroom.class){
                items.add(new Mushroom(this, idef.position.x, idef.position.y));
            }
        }
    }

    public TextureAtlas getAtlas(){
        return atlas;
    }

    @Override
    public void show() {
    }

    public void handleInput(float dt) {
        // UP BUTTON
        if(isFinish != 1)
        {
            if(Gdx.input.isKeyJustPressed(Input.Keys.Z) && Gdx.input.isKeyPressed(Input.Keys.X) && player.b2body.getLinearVelocity().y == 0) //impulse is instantaneous change /force is diff. getworldcenter is where on body we want to apply force.
            {
                // of center will give a torque
                PGA.manager.get("audio/pumpsound/jump.wav", Sound.class).play();
                if(player.b2body.getLinearVelocity().x > 1.1f && Gdx.input.isKeyPressed(Input.Keys.RIGHT))
                    player.b2body.applyLinearImpulse(new Vector2(0, 3.7f + (player.b2body.getLinearVelocity().x)/10.0f), player.b2body.getWorldCenter(), true);
                else if (player.b2body.getLinearVelocity().x < -1.1f && Gdx.input.isKeyPressed(Input.Keys.LEFT))
                    player.b2body.applyLinearImpulse(new Vector2(0, 3.7f + ((player.b2body.getLinearVelocity().x)*-1)/10.0f), player.b2body.getWorldCenter(), true);
                else
                    player.b2body.applyLinearImpulse(new Vector2(0, 3.7f ), player.b2body.getWorldCenter(), true);

            }
            else if(Gdx.input.isKeyJustPressed(Input.Keys.Z) && player.b2body.getLinearVelocity().y == 0) {
                player.b2body.applyLinearImpulse(new Vector2(0, 3.5f), player.b2body.getWorldCenter(), true);
              PGA.manager.get("audio/pumpsound/jump.wav", Sound.class).play();
            }

            // RIGHT BUTTON
            if(Gdx.input.isKeyPressed(Input.Keys.RIGHT) && Gdx.input.isKeyPressed(Input.Keys.X) && player.b2body.getLinearVelocity().x <= 1.5)
                player.b2body.applyLinearImpulse(new Vector2(0.075f, 0), player.b2body.getWorldCenter(), true);
            else if(Gdx.input.isKeyPressed(Input.Keys.RIGHT) && player.b2body.getLinearVelocity().x <= 1)
                player.b2body.applyLinearImpulse(new Vector2(0.06f, 0), player.b2body.getWorldCenter(), true);
            // LEFT BUTTON
            if(Gdx.input.isKeyPressed(Input.Keys.LEFT) && Gdx.input.isKeyPressed(Input.Keys.X) && player.b2body.getLinearVelocity().x >= -1.5)
                player.b2body.applyLinearImpulse(new Vector2(-0.075f, 0), player.b2body.getWorldCenter(), true);
            else if(Gdx.input.isKeyPressed(Input.Keys.LEFT) && player.b2body.getLinearVelocity().x >= -1)
                player.b2body.applyLinearImpulse(new Vector2(-0.06f, 0), player.b2body.getWorldCenter(), true);
        }

        /*
        //Controller
        if(isFinish != 1)
        {
        if(controller.isRightPressed() && controller.isRunPressed() && player.b2body.getLinearVelocity().y == 0) //impulse is instantaneous change /force is diff. getworldcenter is where on body we want to apply force.
        {
            // of center will give a torque
            if(player.b2body.getLinearVelocity().x > 1.1f && Gdx.input.isKeyPressed(Input.Keys.RIGHT))
                player.b2body.applyLinearImpulse(new Vector2(0, 3.7f + (player.b2body.getLinearVelocity().x)/10.0f), player.b2body.getWorldCenter(), true);
            else if (player.b2body.getLinearVelocity().x < -1.1f && Gdx.input.isKeyPressed(Input.Keys.LEFT))
                player.b2body.applyLinearImpulse(new Vector2(0, 3.7f + ((player.b2body.getLinearVelocity().x)*-1)/10.0f), player.b2body.getWorldCenter(), true);
            else
                player.b2body.applyLinearImpulse(new Vector2(0, 3.7f ), player.b2body.getWorldCenter(), true);

        }
        else if(controller.isJumpPressed() && player.b2body.getLinearVelocity().y == 0)
            player.b2body.applyLinearImpulse(new Vector2(0, 3.35f), player.b2body.getWorldCenter(), true);


        // RIGHT BUTTON
        if(controller.isRightPressed() && controller.isRunPressed() && player.b2body.getLinearVelocity().x <= 1.5)
            player.b2body.applyLinearImpulse(new Vector2(0.075f, 0), player.b2body.getWorldCenter(), true);
        else if(controller.isRightPressed() && player.b2body.getLinearVelocity().x <= 1)
            player.b2body.applyLinearImpulse(new Vector2(0.06f, 0), player.b2body.getWorldCenter(), true);
        // LEFT BUTTON
        if(controller.isLeftPressed() && controller.isRunPressed() && player.b2body.getLinearVelocity().x >= -1.5)
            player.b2body.applyLinearImpulse(new Vector2(-0.075f, 0), player.b2body.getWorldCenter(), true);
        else if(controller.isLeftPressed() && player.b2body.getLinearVelocity().x >= -1)
            player.b2body.applyLinearImpulse(new Vector2(-0.06f, 0), player.b2body.getWorldCenter(), true);
        */
        //}
        // RIGHT BUTTON
        if(controller.isJumpPressed() && player.b2body.getLinearVelocity().y == 0 && cooldown == 0)
        {
            player.b2body.applyLinearImpulse(new Vector2(0, 3.6f), player.b2body.getWorldCenter(), true);
            cooldown = 20;
            PGA.manager.get("audio/pumpsound/jump.wav", Sound.class).play();
        }

        if(cooldown != 0)
        {
            cooldown--;
        }
        if(controller.isRightPressed() && player.b2body.getLinearVelocity().x <= 1.3)
            player.b2body.applyLinearImpulse(new Vector2(0.075f, 0), player.b2body.getWorldCenter(), true);
        else if(controller.isLeftPressed() && player.b2body.getLinearVelocity().x >= -1.3)
            player.b2body.applyLinearImpulse(new Vector2(-0.075f, 0), player.b2body.getWorldCenter(), true);

    }

    public void update(float dt){
        //handle user input first
        handleInput(dt);
        handleSpawningItems();

        //takes 1 step in the physics simulation(60 times per second)
        world.step(1 / 60f, 6, 2);

        player.update(dt);
        for(Enemy enemy : creator.getEnemies()) {
            enemy.update(dt);
            if(enemy.getX() < player.getX() + 224 / PGA.PPM) {
                enemy.b2body.setActive(true);
            }
        }

        for(Item item : items)
            item.update(dt);

        hud.update(dt);
        healthbar.update(dt);

        if(isFinish == 1)
            finishTimer++;

        //attach our gamecam to our players.x coordinate
        if(player.currentState != Pump.State.DEAD) {
            gamecam.position.x = player.b2body.getPosition().x;
        }

        //update our gamecam with correct coordinates after changes
        gamecam.update();
        //tell our renderer to draw only what our camera can see in our game world.
        renderer.setView(gamecam);
    }


    @Override
    public void render(float delta) {
        //separate our update logic from render
        update(delta);

        //Clear the game screen with Black
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //render our game map
        renderer.render();
        //renderer our Box2DDebugLines
        //b2dr.render(world, gamecam.combined);

        game.batch.setProjectionMatrix(gamecam.combined);
        game.batch.begin();
        player.draw(game.batch);
        for (Enemy enemy : creator.getEnemies())
            enemy.draw(game.batch);
        for (Item item : items)
            item.draw(game.batch);
        game.batch.end();
        controller.draw();
        //Set our batch to now draw what the Hud camera sees.
        game.batch.setProjectionMatrix(hud.stage.getCamera().combined);
        game.batch.setProjectionMatrix(healthbar.stage.getCamera().combined);
        hud.stage.draw();
        healthbar.stage.draw();

        if(gameOver()){
            game.setScreen(new GameOverScreen(game));
            dispose();
        }

        if(stageClear() && finishTimer == 60){
            PGA.manager.get("audio/music/pumptheme.ogg", Music.class).stop();
            finalScore += Hud.getTime() * 10;
            if(player.getHP() == 3)
                finalScore += 2000;
            else
                finalScore += player.getHP() * 250;

            Hud.addScore(finalScore);
            game.setScreen(new GameOverScreen(game));
            dispose();
        }
        //Stage Clear


    }

    public void dmgTaken(){
        player.b2body.applyLinearImpulse(new Vector2(0, 3.35f ), player.b2body.getWorldCenter(), true);
    }
    public boolean gameOver(){
        if(player.currentState == Pump.State.DEAD && player.getStateTimer() > 3){
            return true;
        }
        return false;
    }

    public void finish(){
        isFinish = 1;
    }
    public boolean stageClear(){
        if(isFinish == 1){
            return true;
        }
        return false;
    }

    @Override
    public void resize(int width, int height) {
        //updated our game viewport
        gamePort.update(width,height);
        controller.resize(width,height);
    }

    public TiledMap getMap(){
        return map;
    }
    public World getWorld(){
        return world;
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
        //dispose of all our opened resources
        map.dispose();
        renderer.dispose();
        world.dispose();
        b2dr.dispose();
        hud.dispose();
        healthbar.dispose();
    }

    public Hud getHud(){ return hud; }
    public HealthBar getHealthbar(){ return healthbar; }

}
