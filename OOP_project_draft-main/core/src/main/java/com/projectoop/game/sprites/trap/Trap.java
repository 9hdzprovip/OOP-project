package com.projectoop.game.sprites.trap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.MapObject;

import com.projectoop.game.GameWorld;
import com.projectoop.game.screens.PlayScreen;
import com.projectoop.game.sprites.Knight;


public class Trap extends InteractiveTileObject {
    public Trap(PlayScreen screen, MapObject object){
        super(screen, object);
        fixture.setUserData(this);
        setCategoryFilter(GameWorld.TRAP_BIT);
    }

    @Override
    public void onFootHit(Knight knight) {
        Gdx.app.log("Spike", "Collision");
        //AudioManager.manager.get(AudioManager.knightDieAudio, Sound.class).play();
        screen.getPlayer().setDie();
    }
}
