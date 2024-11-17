package com.projectoop.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.projectoop.game.screens.PlayScreen;
import com.projectoop.game.tools.AudioManager;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class GameWorld extends Game {
    public static final int V_WIDTH = 400; //virtual width
    public static final int V_HEIGHT = 300;
    public static final float PPM = 100;//pixel per meter

    public static final short GROUND_BIT = 1;
    public static final short KNIGHT_BIT = 2;
    public static final short TRAP_BIT = 4;
    public static final short PILAR_BIT = 8;
    public static final short DESTROYED_BIT = 16;
    public static final short BRICK_BIT = 32;
    public static final short ENEMY_BIT = 64;
    //test
    public static final short ENEMY1_BIT = 65;
    public static final short ENEMY2_BIT = 4096;
    public static final short KNIGHT_FOOT_BIT = 128;
    public static final short OBJECT_BIT = 256;
    public static final short ARROW_BIT = 512;
    public static final short CHEST_BIT = 1024;
    public static final short CHEST1_BIT = 1025;
    public static final short ITEM_BIT = 2048;
    public static final short FIREBALL_BIT = 513;

    public SpriteBatch batch;//container hold all images or texture for rendering
    public SpriteBatch getBatch() {
        return batch;
    } // testv


    @Override
    public void create() {
        AudioManager.setUp();
        batch = new SpriteBatch();
        setScreen(new PlayScreen(this));
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void dispose() {
        super.dispose();
        batch.dispose();
        AudioManager.manager.dispose();
    }
}
