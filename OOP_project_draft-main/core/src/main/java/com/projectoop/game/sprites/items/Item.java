package com.projectoop.game.sprites.items;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.projectoop.game.GameWorld;
import com.projectoop.game.screens.PlayScreen;
import com.projectoop.game.sprites.Knight;

public abstract class Item extends Sprite {
    protected PlayScreen screen;
    protected World world;
    protected Body body;
    protected Vector2 velocity;

    protected boolean toDestroy;
    protected boolean destroyed;
    protected float stateTime;

    public Item(PlayScreen screen, float x, float y){
        this.screen = screen;
        this.world = screen.getWorld();
        setPosition(x, y);
        setBounds(getX(), getY(), 16/ GameWorld.PPM, 16/GameWorld.PPM);
        defineItem();
        prepareAnimation();
    }

    protected abstract void defineItem();
    public abstract void use(Knight knight);
    protected abstract void prepareAnimation();

    public abstract void update(float dt);

    public void draw (Batch batch){
        if (!destroyed){
            super.draw(batch);
        }
    }

    public void destroy(){
        toDestroy = true;
    }
}
