package com.projectoop.game.sprites.effectedObject;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.projectoop.game.GameWorld;
import com.projectoop.game.screens.PlayScreen;
import com.projectoop.game.sprites.items.ItemDef;
import com.projectoop.game.sprites.items.Potion1;
import com.projectoop.game.tools.AudioManager;

public class Chest1 extends EffectedObject {

    private PlayScreen screen;

    private static final float scaleX = 1.5f;
    private static final float scaleY = 1.5f;

    private TextureAtlas atlasOpenChest;
    private Animation<TextureRegion> chestOpening;
    private TextureRegion usedFrame;
    private TextureRegion notuseFrame;

    private Sound chestOpeningSound;

    private boolean spawnItem;
    //test
    private Texture usedTexture;
    private Texture notuseTexture;

    public Chest1(PlayScreen screen, float x, float y) {
        super(screen, x, y);
        this.screen = screen;
        stateTime = 0;
        setToDestroy = false;
        destroyed = false;
        used = false;
        using = false;
        spawnItem = false;
        prepareAnimation();
        prepareAudio();
    }

    @Override
    protected void defineObject() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(getX(), getY());
        bdef.type = BodyDef.BodyType.StaticBody;
        //b2body is a square 10x10
        b2body = world.createBody(bdef);
        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(10/ GameWorld.PPM, 10/GameWorld.PPM);

        fdef.filter.categoryBits = GameWorld.CHEST1_BIT;
        fdef.filter.maskBits = GameWorld.GROUND_BIT | //collide list
            GameWorld.ENEMY_BIT | GameWorld.KNIGHT_BIT |GameWorld.ARROW_BIT;
        fdef.shape = shape;
        b2body.createFixture(fdef).setUserData(this);
    }

    @Override
    protected void prepareAnimation() {
//        atlasOpenChest = new TextureAtlas("Chest/Chest.pack");
//        chestOpening = new Animation<TextureRegion>(0.5f, atlasOpenChest.getRegions());
//        usedFrame = new TextureRegion(atlasOpenChest.findRegion("Chestche/sprite_45.png"));
//        notuseFrame = new TextureRegion(atlasOpenChest.findRegion("Chestche/sprite_46.png"));
        usedTexture = new Texture("Chestche/sprite_39.png");
        notuseTexture = new Texture("Chestche/sprite_3.png");

        usedFrame = new TextureRegion(usedTexture);
        notuseFrame = new TextureRegion(notuseTexture);
    }

    @Override
    protected void prepareAudio(){
        chestOpeningSound = AudioManager.manager.get(AudioManager.chestOpenAudio, Sound.class);
    }

    @Override
    public void usingCallBack() {
        using = true;
        chestOpeningSound.play();
        if (!spawnItem) {
            screen.spawnItem(new ItemDef(new Vector2(b2body.getPosition().x,
                b2body.getPosition().y + 16 / GameWorld.PPM), Potion1.class));
            spawnItem = true;
        }
    }

    @Override
    public void update(float dt) {
        stateTime += dt;
        setPosition(b2body.getPosition().x - getWidth()/2, b2body.getPosition().y - getHeight()/2);
        TextureRegion frame = getFrame(dt);

        setBounds(getX(), getY()+10/GameWorld.PPM, frame.getRegionWidth() / GameWorld.PPM * scaleX,
            frame.getRegionHeight() / GameWorld.PPM * scaleY);
        setRegion(frame);
    }

    public void draw (Batch batch){
        if (!destroyed || stateTime < 1){
            super.draw(batch);
        }
    }


    public State getState(){
        if (!used && !using){
            return State.NOTUSE;
        }
        else{
//            if (using && !chestOpening.isAnimationFinished(stateTime)) return State.USING;
//            else{
                using = false;
                used = true;
                return State.USED;
//            }
        }
    }

    public TextureRegion getFrame(float dt){
        currentState = getState();
        TextureRegion region;
        switch (currentState){
            case USED:
                region = usedFrame;
                break;
//            case USING:
//                region = (TextureRegion) chestOpening.getKeyFrame(stateTime);
//                break;
            case NOTUSE:
            default:
                region = notuseFrame;
                break;
        }


        stateTime = (currentState == previousState) ? stateTime + dt : 0;
        previousState = currentState;
        return region;
    }

    public void hitOnHead() {

    }
}
