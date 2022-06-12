package com.pgaf.pga.Tools;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.pgaf.pga.PGA;
import com.pgaf.pga.Screens.PlayScreen;
import com.pgaf.pga.Sprites.Enemies.Enemy;
import com.pgaf.pga.Sprites.Enemies.Milk;
import com.pgaf.pga.Sprites.Enemies.Milk_Flag;
import com.pgaf.pga.Sprites.Enemies.Secret;
import com.pgaf.pga.Sprites.Items.Mushroom;
import com.pgaf.pga.Sprites.TileObjects.Coin;
import com.pgaf.pga.Sprites.Enemies.Goomba;


public class B2WorldCreator {
    private Array<Goomba> goombas;
    private Array<Milk> milks;
    private Array<Mushroom> mushroom;
    private Array<Milk_Flag> flag;
    private Array<Secret> secret;
    //private Array<Turtle> turtles;

    public B2WorldCreator(PlayScreen screen){
        World world = screen.getWorld();
        TiledMap map = screen.getMap();
        //create body and fixture variables
        BodyDef bdef = new BodyDef();
        PolygonShape shape = new PolygonShape();
        FixtureDef fdef = new FixtureDef();
        Body body;

        //ground
        //create ground bodies/fixtures
        for(MapObject object : map.getLayers().get(7).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set((rect.getX() + rect.getWidth() / 2) / PGA.PPM, (rect.getY() + rect.getHeight() / 2) / PGA.PPM);

            body = world.createBody(bdef);

            shape.setAsBox(rect.getWidth() / 2 / PGA.PPM, rect.getHeight() / 2 / PGA.PPM);
            fdef.shape = shape;
            body.createFixture(fdef);
        }

        //create pipe bodies/fixtures
        for(MapObject object : map.getLayers().get(8).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set((rect.getX() + rect.getWidth() / 2) / PGA.PPM, (rect.getY() + rect.getHeight() / 2) / PGA.PPM);

            body = world.createBody(bdef);

            shape.setAsBox(rect.getWidth() / 2 / PGA.PPM, rect.getHeight() / 2 / PGA.PPM);
            fdef.shape = shape;
            fdef.filter.categoryBits = PGA.PIT_BIT;
            body.createFixture(fdef);
        }

        //edge
        for(MapObject object : map.getLayers().get(13).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set((rect.getX() + rect.getWidth() / 2) / PGA.PPM, (rect.getY() + rect.getHeight() / 2) / PGA.PPM);

            body = world.createBody(bdef);

            shape.setAsBox(rect.getWidth() / 2 / PGA.PPM, rect.getHeight() / 2 / PGA.PPM);
            fdef.shape = shape;
            fdef.filter.categoryBits = PGA.EDGE_BIT;
            body.createFixture(fdef);
        }
        //create brick bodies/fixtures
        //for(MapObject object : map.getLayers().get(10).getObjects().getByType(RectangleMapObject.class)){
        //    new Brick(screen, object);
        //}

        //create coin bodies/fixtures
        for(MapObject object : map.getLayers().get(4).getObjects().getByType(RectangleMapObject.class)){

            new Coin(screen, object);
        }

        //create all goombas
        goombas = new Array<Goomba>();
        for(MapObject object : map.getLayers().get(12).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            goombas.add(new Goomba(screen, rect.getX() / PGA.PPM, rect.getY() / PGA.PPM));
        }
        //turtles = new Array<Turtle>();
        //for(MapObject object : map.getLayers().get(12).getObjects().getByType(RectangleMapObject.class)){
        //    Rectangle rect = ((RectangleMapObject) object).getRectangle();
        //    turtles.add(new Turtle(screen, rect.getX() / PGA.PPM, rect.getY() / PGA.PPM));
        //}
        //test milk
        milks = new Array<Milk>();
        for(MapObject object : map.getLayers().get(10).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            milks.add(new Milk(screen, rect.getX() / PGA.PPM, rect.getY() / PGA.PPM));
        }

        //test goal
        flag = new Array<Milk_Flag>();
        for(MapObject object : map.getLayers().get(11).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            flag.add(new Milk_Flag(screen, rect.getX() / PGA.PPM, rect.getY() / PGA.PPM));
        }

        //test onanong
        secret = new Array<Secret>();
        for(MapObject object : map.getLayers().get(14).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            secret.add(new Secret(screen, rect.getX() / PGA.PPM, rect.getY() / PGA.PPM));
        }
        //create Item
        //mushroom = new Array<Mushroom>();
        //for(MapObject object : map.getLayers().get(10).getObjects().getByType(RectangleMapObject.class)){
        //    Rectangle rect = ((RectangleMapObject) object).getRectangle();
        //    mushroom.add(new Mushroom(screen, rect.getX() / PGA.PPM, rect.getY() / PGA.PPM));
        //}

        //create Item
        //flag = new Array<Flag>();
        //for(MapObject object : map.getLayers().get(11).getObjects().getByType(RectangleMapObject.class)){
        //    Rectangle rect = ((RectangleMapObject) object).getRectangle();
        //    flag.add(new Flag(screen, rect.getX() / PGA.PPM, rect.getY() / PGA.PPM));
       // }


    }

    public Array<Goomba> getGoombas() {
        return goombas;
    }
    public Array<Enemy> getEnemies(){
        Array<Enemy> enemies = new Array<Enemy>();
        enemies.addAll(goombas);
        enemies.addAll(milks);
        enemies.addAll(flag);
        enemies.addAll(secret);
        return enemies;
    }


}
