package com.projectoop.game.sprites.enemy;

import com.badlogic.gdx.audio.Sound;
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
import com.projectoop.game.tools.AudioManager;

public class Skeleton extends Enemy{
    public enum State {HURTING, ATTACKING, DEAD, WALKING};
    private State currentState;
    private State previousState;


    private static final float scaleX = 1.5f;
    private static final float scaleY = 1.5f;

    private TextureAtlas atlasWalkting;
    private TextureAtlas atlasAttacking;
    private TextureAtlas atlasHurting;
    private TextureAtlas atlasDieing;

    private Animation<TextureRegion> walkAnimation;
    private Animation<TextureRegion> attackAnimation;
    private Animation<TextureRegion> hurtAnimation;
    private Animation<TextureRegion> dieAnimation;

    private Sound attackSound;
    private Sound hurtSound;
    private Sound dieSound;

    private boolean playSoundAttack;

    private boolean lastDirectionIsRight;
    // test
    private SkeletonHealthBar healthBar;
    private float maxHealth = 100; // Ví dụ về máu tối đa
    private float currentHealth;
//    private float attackDamage = 10;  //
//    public float getAttackDamage() {
//        return attackDamage;
//    }


    public Skeleton(PlayScreen screen, float x, float y) {
        super(screen, x, y);

        currentState = State.WALKING;
        previousState = State.WALKING;
        stateTime = 0;
        setToDestroy = false;
        destroyed = false;
        isAttack = false;
        isAttacking = false;
        isHurt = false;
        isHurting = false;
        isDie = false;
        playSoundAttack = false;

        //test
        //this.velocity = Vector2.Zero;

        currentHealth = maxHealth;
        healthBar = new SkeletonHealthBar(this, maxHealth);
    }


    protected void prepareAnimation(){
        atlasWalkting = new TextureAtlas("E_Skeleton/Pack/Walk.pack");
        atlasAttacking = new TextureAtlas("E_Skeleton/Pack/Attack.pack");
        atlasDieing = new TextureAtlas("E_Skeleton/Pack/Death.pack");
        atlasHurting = new TextureAtlas("E_Skeleton/Pack/Hurt.pack");

        walkAnimation = new Animation<TextureRegion>(0.3f, atlasWalkting.getRegions());
        attackAnimation = new Animation<TextureRegion>(0.4f, atlasAttacking.getRegions());
        dieAnimation = new Animation<TextureRegion>(0.2f, atlasDieing.getRegions());
        hurtAnimation = new Animation<TextureRegion>(0.3f, atlasHurting.getRegions());
    }
    protected void prepareAudio(){
        attackSound = AudioManager.manager.get(AudioManager.orgAttackAudio, Sound.class);
        dieSound = AudioManager.manager.get(AudioManager.orgDieAudio, Sound.class);
        hurtSound = AudioManager.manager.get(AudioManager.orgHurtAudio, Sound.class);
    }
//    // test
//    public void takeDamage(float damage) {
//        currentHealth -= damage;
//        healthBar.update(currentHealth);
//    }

    @Override
    protected void defineEnemy() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(getX(), getY());
        bdef.type = BodyDef.BodyType.DynamicBody;

        b2body = world.createBody(bdef);
        FixtureDef fdef = new FixtureDef();
//        CircleShape shape = new CircleShape();
//        shape.setRadius(9/GameWorld.PPM);
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(9 / GameWorld.PPM, 9 / GameWorld.PPM);
        //type bit
        fdef.filter.categoryBits = GameWorld.ENEMY2_BIT;
        //Collision bit list
        fdef.filter.maskBits = GameWorld.GROUND_BIT |
            GameWorld.TRAP_BIT | GameWorld.ENEMY2_BIT | GameWorld.CHEST_BIT |
            GameWorld.PILAR_BIT | GameWorld.KNIGHT_BIT | GameWorld.ARROW_BIT| GameWorld.OBJECT_BIT| GameWorld.ENEMY_BIT| GameWorld.ENEMY1_BIT;
        fdef.shape = shape;
        b2body.createFixture(fdef).setUserData(this);
    }

    @Override
    public void destroy() {
        setToDestroy = true;
    }

    @Override
    public void attackingCallBack() {
        isAttack = true;

    }

    @Override
    public void hurtingCallBack() {
//        takeDamage(20);
//        if (currentHealth <= 0) isDie = true;
        isHurt = true;
    }

    public TextureRegion getFrame(float dt){
        currentState = getState();
        TextureRegion frame;

        switch (currentState){
            case DEAD:
                frame = (TextureRegion) dieAnimation.getKeyFrame(stateTime, false);
                //System.out.println("dead");
                break;
            case HURTING:
                frame = (TextureRegion) hurtAnimation.getKeyFrame(stateTime, false);
                //System.out.println("hurt");
                break;
            case ATTACKING:
                frame = (TextureRegion) attackAnimation.getKeyFrame(stateTime, false);
                //System.out.println("attack");
                break;
            case WALKING:
            default:
                frame = walkAnimation.getKeyFrame(stateTime, true);
                //System.out.println("walk");

                break;
        }

        if ((b2body.getLinearVelocity().x < 0 || !runningRight) && !frame.isFlipX()){
            frame.flip(true, false);
            runningRight = false;
        }
        else if ((b2body.getLinearVelocity().x > 0 || runningRight) && frame.isFlipX()){
            frame.flip(true, false);
            runningRight = true;
        }

        stateTime = (currentState == previousState) ? stateTime + dt : 0;
        previousState = currentState;
        return frame;
    }

    public State getState(){
        //die and hurt code
        if (isDie){//test
            if (dieAnimation.isAnimationFinished(stateTime)) {
                destroy();
            }
            //call destroy
            return State.DEAD;
        }

        if (isHurt){
            isHurt = false;
            isHurting = true;
            lastDirectionIsRight = runningRight;
            this.velocity = new Vector2(0, 0);
            return State.HURTING;
        }
        if(isHurting) {//test
            if(!hurtAnimation.isAnimationFinished(stateTime)) {
                System.out.println("hihihihihihihh");
                return State.HURTING;
            }
            else {
                isHurting = false;
                this.velocity = lastDirectionIsRight ? new Vector2(1, 0) : new Vector2(-1, 0);
            }
        }
        //attack code
        if (isAttack){
            isAttacking = true;
            isAttack = false;
            lastDirectionIsRight = runningRight;
            this.velocity = new Vector2(0, 0);
            return State.ATTACKING;
        }
        if (isAttacking){//test
            if (!attackAnimation.isAnimationFinished(stateTime)){
                System.out.println("attacking");
                return State.ATTACKING;
            }
            else {
                isAttacking = false;
                this.velocity = lastDirectionIsRight ? new Vector2(1, 0) : new Vector2(-1, 0);
                //playSound1 = false;
            }
        }

        return State.WALKING;
    }

    public void update(float dt){
        stateTime += dt;
        if (setToDestroy && !destroyed){
            world.destroyBody(b2body);
            destroyed = true;
            stateTime = 0;
        }
        else if (!destroyed){
            TextureRegion frame = getFrame(dt);
            b2body.setLinearVelocity(velocity);
            setPosition(b2body.getPosition().x - getWidth()/2, b2body.getPosition().y - getHeight()/2);
            setBounds(getX(), getY(), frame.getRegionWidth() / GameWorld.PPM * scaleX,
                frame.getRegionHeight() / GameWorld.PPM * scaleY);
            setRegion(frame);
        }
        healthBar.update(currentHealth);
    }

    public void draw (Batch batch){
        if (!destroyed || stateTime < 1){
            super.draw(batch);
            healthBar.draw(batch); // Vẽ thanh máu
        }
    }

    @Override
    public void hitOnHead() {

    }
}


