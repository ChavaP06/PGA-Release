package com.pgaf.pga.Tools;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.pgaf.pga.Sprites.Enemies.Enemy;
import com.pgaf.pga.PGA;
import com.pgaf.pga.Sprites.Items.Item;
import com.pgaf.pga.Sprites.Pump;
import com.pgaf.pga.Sprites.Other.FireBall;
import com.pgaf.pga.Sprites.TileObjects.InteractiveTileObject;


public class WorldContactListener implements ContactListener {
    @Override
    public void beginContact(Contact contact) {
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

        int cDef = fixA.getFilterData().categoryBits | fixB.getFilterData().categoryBits;

        switch (cDef){
            case PGA.MARIO_HEAD_BIT | PGA.BRICK_BIT:
            case PGA.MARIO_HEAD_BIT | PGA.COIN_BIT:
                if(fixA.getFilterData().categoryBits == PGA.MARIO_HEAD_BIT)
                    ((InteractiveTileObject) fixB.getUserData()).onHeadHit((Pump) fixA.getUserData());
                else
                    ((InteractiveTileObject) fixA.getUserData()).onHeadHit((Pump) fixB.getUserData());
                break;
            case PGA.ENEMY_HEAD_BIT | PGA.MARIO_BIT:
                if(fixA.getFilterData().categoryBits == PGA.ENEMY_HEAD_BIT)
                    ((Enemy)fixA.getUserData()).hitOnHead((Pump) fixB.getUserData());
                else
                    ((Enemy)fixB.getUserData()).hitOnHead((Pump) fixA.getUserData());
                break;
            case PGA.ENEMY_BIT | PGA.OBJECT_BIT:
                if(fixA.getFilterData().categoryBits == PGA.ENEMY_BIT)
                    ((Enemy)fixA.getUserData()).reverseVelocity(true, false);
                else
                    ((Enemy)fixB.getUserData()).reverseVelocity(true, false);
                break;
            case PGA.MARIO_BIT | PGA.ENEMY_BIT:
                if(fixA.getFilterData().categoryBits == PGA.MARIO_BIT)
                    ((Pump) fixA.getUserData()).hit((Enemy)fixB.getUserData());
                else
                    ((Pump) fixB.getUserData()).hit((Enemy)fixA.getUserData());
                break;
            case PGA.ENEMY_BIT | PGA.ENEMY_BIT:
                ((Enemy)fixA.getUserData()).hitByEnemy((Enemy)fixB.getUserData());
                ((Enemy)fixB.getUserData()).hitByEnemy((Enemy)fixA.getUserData());
                break;
            case PGA.ITEM_BIT | PGA.OBJECT_BIT:
                if(fixA.getFilterData().categoryBits == PGA.ITEM_BIT)
                    ((Item)fixA.getUserData()).reverseVelocity(true, false);
                else
                    ((Item)fixB.getUserData()).reverseVelocity(true, false);
                break;
            case PGA.ITEM_BIT | PGA.MARIO_BIT:
                if(fixA.getFilterData().categoryBits == PGA.ITEM_BIT)
                    ((Item)fixA.getUserData()).use((Pump) fixB.getUserData());
                else
                    ((Item)fixB.getUserData()).use((Pump) fixA.getUserData());
                break;
            case PGA.FIREBALL_BIT | PGA.OBJECT_BIT:
                if(fixA.getFilterData().categoryBits == PGA.FIREBALL_BIT)
                    ((FireBall)fixA.getUserData()).setToDestroy();
                else
                    ((FireBall)fixB.getUserData()).setToDestroy();
                break;
            case  PGA.MARIO_BIT | PGA.PIT_BIT:
                if(fixA.getFilterData().categoryBits == PGA.MARIO_BIT)
                    ((Pump) fixA.getUserData()).die();
                else
                    ((Pump) fixB.getUserData()).die();
                break;
            case PGA.ENEMY_BIT | PGA.EDGE_BIT:
                if(fixA.getFilterData().categoryBits == PGA.ENEMY_BIT)
                    ((Enemy)fixA.getUserData()).reverseVelocity(true, false);
                else
                    ((Enemy)fixB.getUserData()).reverseVelocity(true, false);
                break;
        }
    }

    @Override
    public void endContact(Contact contact) {
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {
    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
