package com.pgaf.pga.Sprites.Items;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.pgaf.pga.PGA;
import com.pgaf.pga.Screens.PlayScreen;
import com.pgaf.pga.Sprites.Pump;


public class Flag extends Item {
    public Flag(PlayScreen screen, float x, float y) {
        super(screen, x, y);
        setRegion(screen.getAtlas().findRegion("pumpmilk_goal"), 0, 0, 48, 48);
        velocity = new Vector2(0, 0);
    }

    @Override
    public void defineItem() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(getX(), getY());
        bdef.type = BodyDef.BodyType.StaticBody;
        body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(6 / PGA.PPM);
        fdef.filter.categoryBits = PGA.ITEM_BIT;
        fdef.filter.maskBits = PGA.MARIO_BIT |
                PGA.OBJECT_BIT |
                PGA.GROUND_BIT |
                PGA.COIN_BIT |
                PGA.BRICK_BIT;

        fdef.shape = shape;
        body.createFixture(fdef).setUserData(this);
    }

    @Override
    public void use(Pump mario) {
        destroy();
        screen.finish();
    }

    @Override
    public void update(float dt) {
        super.update(dt);
        setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() / 2);
        //setPosition(body.getPosition().x, body.getPosition().y);
        //velocity.y = body.getLinearVelocity().y;
        body.setLinearVelocity(velocity);
    }
}
