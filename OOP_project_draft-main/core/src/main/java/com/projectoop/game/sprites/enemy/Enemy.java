package com.projectoop.game.sprites.enemy;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.projectoop.game.screens.PlayScreen;
import com.projectoop.game.sprites.effectedObject.EffectedObject;

public abstract class Enemy extends Sprite {

    protected float stateTime;
    protected boolean setToDestroy;
    protected boolean destroyed;
    protected boolean runningRight;
    protected boolean isAttacking;
    protected boolean isHurt;
    protected boolean isDie;
    protected boolean isHurting;
    protected boolean isAttack;

    protected World world;
    protected PlayScreen screen;
    public Body b2body;
    public Vector2 velocity;



    public Enemy(PlayScreen screen, float x, float y){

        this.world = screen.getWorld();
        this.screen = screen;
        setPosition(x, y);
        defineEnemy();
        prepareAnimation();
        prepareAudio();
        velocity = new Vector2(1, 0);
        runningRight = true;
        b2body.setActive(false);
    }

    protected abstract void defineEnemy();
    protected abstract void prepareAnimation();
    protected abstract void prepareAudio();
    public abstract void update(float dt);
    public abstract void hitOnHead();
    public abstract void destroy();
    public abstract void attackingCallBack();
    public abstract void hurtingCallBack();
    public abstract float getAttackDamage();
    public abstract  boolean hasAttacked();
    public abstract  void resetAttackState();

    public void reverseVelocity(boolean x, boolean y){
        if (x){
            velocity.x = -velocity.x;
        }
        if (y){
            velocity.y = -velocity.y;
        }
    }


}
