package com.projectoop.game.sprites.items;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.projectoop.game.GameWorld;
import com.projectoop.game.screens.PlayScreen;
import com.projectoop.game.sprites.Knight;
import com.projectoop.game.tools.AudioManager;

public class Potion1 extends Item{
    private Texture texture;
    private TextureRegion frame;

    private Sound potionCollectSound;

    public Potion1(PlayScreen screen, float x, float y) {
        super(screen, x, y);
        toDestroy = false;
        destroyed = false;
        stateTime = 0;
        //the chest throw a potion with this velocity
        velocity = new Vector2(1.5f, 0.5f);

        potionCollectSound = AudioManager.manager.get(AudioManager.potionCollectAudio, Sound.class);
    }

    @Override
    protected void defineItem() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(getX(), getY());
        bdef.type = BodyDef.BodyType.DynamicBody;
        body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(6 / GameWorld.PPM);
        //type bit
        fdef.filter.categoryBits = GameWorld.ITEM_BIT;
        //collision bit list
        fdef.filter.maskBits = GameWorld.GROUND_BIT |
            GameWorld.TRAP_BIT | GameWorld.ENEMY_BIT | GameWorld.CHEST1_BIT |
            GameWorld.PILAR_BIT | GameWorld.KNIGHT_BIT | GameWorld.ARROW_BIT|GameWorld.CHEST_BIT;

        fdef.shape = shape;
        body.createFixture(fdef).setUserData(this);
    }

    @Override
    protected void prepareAnimation() {
        texture = new Texture("ice potion.png");
        frame = new TextureRegion(texture);
        setRegion(frame);
    }

    @Override
    public void use(Knight knight) {
        destroy();
        potionCollectSound.play();
        //buff player (code later)
        screen.getPlayer().buff1();

    }

    @Override
    public void update(float dt){
        stateTime += dt;
        if (toDestroy && !destroyed){
            world.destroyBody(body);
            destroyed = true;
            stateTime = 0;
        }
        else {
            setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() / 2);
            velocity.y = body.getLinearVelocity().y;
            body.setLinearVelocity(velocity);
            //when y = 0, set the potion stop
            if (velocity.y == 0) body.setLinearVelocity(0, 0);
        }
    }
}
