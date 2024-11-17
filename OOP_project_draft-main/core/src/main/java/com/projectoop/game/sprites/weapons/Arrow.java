package com.projectoop.game.sprites.weapons;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.*;
import com.projectoop.game.GameWorld;
import com.projectoop.game.screens.PlayScreen;

public class Arrow extends Bullet {
    private Texture texture;
    private TextureRegion frame;

    private final float scaleX = 1.2f;
    private final float scaleY = 1.2f;

    public Arrow(PlayScreen screen, float x, float y, int direction) {
        super(screen, x, y, direction);
    }

    protected void prepareAnimation() {
        texture = new Texture("KnightAsset/Arrow.png");
        frame = new TextureRegion(texture);
    }

    protected void defineBullet() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(getX(), getY());
        bdef.type = BodyDef.BodyType.DynamicBody;
        bdef.gravityScale = 0;
        b2body = world.createBody(bdef);
        CircleShape shape = new CircleShape();
        shape.setRadius(2/GameWorld.PPM);
        FixtureDef fdef = new FixtureDef();
        fdef.filter.categoryBits = GameWorld.ARROW_BIT;
        fdef.filter.maskBits = GameWorld.GROUND_BIT | GameWorld.ENEMY_BIT |
            GameWorld.CHEST_BIT | GameWorld.ITEM_BIT;
        fdef.shape = shape;
        b2body.createFixture(fdef).setUserData(this);
    }

    @Override
    public void destroy() {
        setToDestroy = true;
    }

    public void update (float dt){
        stateTime += dt;
        if (setToDestroy && !destroyed){
            world.destroyBody(b2body);
            destroyed = true;
            stateTime = 0;
        }
        else if (!destroyed){
            setPosition(b2body.getPosition().x - getWidth()/2, b2body.getPosition().y - getHeight()/2);
            setBounds(getX(), getY(), frame.getRegionWidth() / GameWorld.PPM * scaleX,
                frame.getRegionHeight() / GameWorld.PPM * scaleY);
            if ((!isShootingRight) && !frame.isFlipX()){
                frame.flip(true, false);
                isShootingRight = false;
            }
            else if ((isShootingRight) && frame.isFlipX()){
                frame.flip(true, false);
                isShootingRight = true;
            }
            b2body.setLinearVelocity(velocity);
            setRegion(frame);  // Set texture region for rendering
        }
    }

    public void dispose() {
        if (texture != null) {
            texture.dispose();
        }
    }
}
