package com.pgaf.pga.Sprites.TileObjects;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.math.Vector2;
import com.pgaf.pga.Sprites.Items.Mushroom;
import com.pgaf.pga.Sprites.Pump;
import com.pgaf.pga.PGA;
import com.pgaf.pga.Scenes.Hud;
import com.pgaf.pga.Screens.PlayScreen;
import com.pgaf.pga.Sprites.Items.ItemDef;


public class Coin extends InteractiveTileObject {
    private static TiledMapTileSet tileSet;
    private final int BLANK_COIN = 28;

    public Coin(PlayScreen screen, MapObject object){
        super(screen, object);
        tileSet = map.getTileSets().getTileSet("tileset_gutter");
        fixture.setUserData(this);
        setCategoryFilter(PGA.COIN_BIT);
    }

    @Override
    public void onHeadHit(Pump mario) {
        if(getCell().getTile().getId() == BLANK_COIN)
            PGA.manager.get("audio/sounds/bump.wav", Sound.class).play();
        else {
            if(object.getProperties().containsKey("mushroom")) {
                screen.spawnItem(new ItemDef(new Vector2(body.getPosition().x, body.getPosition().y + 16 / PGA.PPM),
                        Mushroom.class));
                PGA.manager.get("audio/sounds/powerup_spawn.wav", Sound.class).play();
            }
            else
                PGA.manager.get("audio/sounds/coin.wav", Sound.class).play();
            getCell().setTile(tileSet.getTile(BLANK_COIN));
            Hud.addScore(100);
        }
    }

}
