package com.projectoop.game.sprites.weapons;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.projectoop.game.GameWorld;
import com.projectoop.game.screens.PlayScreen;

public abstract class Bullet extends Sprite {
    protected int direction;
    protected float stateTime;

    protected World world;
    protected PlayScreen screen;
    public Body b2body;
    public Vector2 velocity;

    protected boolean setToDestroy;
    protected boolean destroyed;
    protected boolean isShootingRight;

    public Bullet(PlayScreen screen, float x, float y, int direction) {
        this.screen = screen;
        this.world = screen.getWorld();
        setPosition(x + 15 * direction/ GameWorld.PPM, y);
        this.direction = direction;
        velocity = new Vector2(2.5f * direction, 0);
        stateTime = 0;
        prepareAnimation();
        defineBullet();

        isShootingRight = (direction == 1);
        setToDestroy = false;
        destroyed = false;
    }

    protected abstract void prepareAnimation();
    protected abstract void defineBullet();
    public abstract void destroy();
    public abstract void update(float dt);
    public abstract void dispose();
}
