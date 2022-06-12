package com.pgaf.pga.Sprites.TileObjects;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.maps.MapObject;
import com.pgaf.pga.Sprites.Pump;
import com.pgaf.pga.PGA;
import com.pgaf.pga.Scenes.Hud;
import com.pgaf.pga.Screens.PlayScreen;


public class Brick extends InteractiveTileObject {
    public Brick(PlayScreen screen, MapObject object){
        super(screen, object);
        fixture.setUserData(this);
        setCategoryFilter(PGA.BRICK_BIT);
    }

    @Override
    public void onHeadHit(Pump mario) {
        if(mario.isBig()) {
            setCategoryFilter(PGA.DESTROYED_BIT);
            getCell().setTile(null);
            Hud.addScore(200);
            PGA.manager.get("audio/sounds/breakblock.wav", Sound.class).play();
        }
        PGA.manager.get("audio/sounds/bump.wav", Sound.class).play();
    }

}
