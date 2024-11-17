package com.projectoop.game.scences;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.projectoop.game.GameWorld;
import com.projectoop.game.screens.PlayScreen;
import com.projectoop.game.sprites.Knight;

public class HealthBar {
    private Texture HeroHealthBar,HeroHealth;
    private Texture BossHealthBar,BossHealth;
    private PlayScreen screen;
    private TextureRegion currentRank;
    SpriteBatch batch;
    private Knight player;


    public HealthBar(PlayScreen x) {
        HeroHealthBar = new Texture("HealthBar/bg.png");
        HeroHealth =  new Texture("HealthBar/red.png");


        //get batch and player
        this.screen = x;
        batch = x.game.getBatch();
        player = x.getPlayer();

    }

    public void update(float dt) {
//        // draw Hero HeroHeroHealthBar
        batch.begin();
        float x = screen.getCamera().position.x-screen.getGamePort().getWorldWidth()-100/GameWorld.PPM;
        float y = 175/GameWorld.PPM+screen.getCamera().position.y+screen.getGamePort().getWorldHeight()/2;



        batch.draw(HeroHealthBar, x+3,y-2,HeroHealthBar.getWidth()/  GameWorld.PPM/2,HeroHealthBar.getHeight()/  GameWorld.PPM/2, 0,0,HeroHealthBar.getWidth(),HeroHealthBar.getHeight(),false,false);

        float ratio = 1.0f*player.getHealth()/player.getHealthMax();
        float HealthX = x+3+(HeroHealthBar.getWidth()-HeroHealth.getWidth())/2/  GameWorld.PPM;
        float HealthY = y-2+(HeroHealthBar.getHeight()-HeroHealth.getHeight())/2/  GameWorld.PPM;
        // draw at HeroHealthX and HeroHealthY with size (lengOfHeroHeroHealthBar * HeroHealth/MaxHeroHealth) x (Height) (!!!) and scale with PPM
        // and take portion of Texuture with size (lengOfHeroHeroHealthBar * HeroHealth/MaxHeroHealth) x (Height)


        batch.draw(HeroHealth, HealthX,HealthY,
            (HeroHealth.getWidth()/  GameWorld.PPM/2)*ratio,HeroHealth.getHeight()/  GameWorld.PPM/2,
            0,0,(int)(HeroHealth.getWidth()*ratio),HeroHealth.getHeight(),
            false,false);

        // test
        batch.end();


    }



}


