package com.projectoop.game.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.projectoop.game.GameWorld;
import com.projectoop.game.scences.HealthBar;
import com.projectoop.game.screens.PlayScreen;
import com.projectoop.game.tools.AudioManager;

public class Knight extends Sprite {
    public enum State {FALLING, HURTING, ATTACKING1, ATTACKING2, ATTACKING3, DEAD, JUMPING, STANDING, RUNNING};
    public State currentState;
    public State previousState;
    public World world;
    public Body b2body;

    private static float scaleX = 1.5f;
    private static float scaleY = 1.5f;

    //test
    private static int deathCount = 3;

    private TextureAtlas atlasRunning;
    private TextureAtlas atlasJumping;
    private TextureAtlas atlasStanding;
    private TextureAtlas atlasAttacking1;
    private TextureAtlas atlasAttacking2;
    private TextureAtlas atlasAttacking3;
    private TextureAtlas atlasBeingHurt;
    private TextureAtlas atlasDieing;

    private Animation<TextureRegion> knightRun;
    private Animation<TextureRegion> knightJump;
    private Animation<TextureRegion> knightAttack1;
    private Animation<TextureRegion> knightAttack2;
    private Animation<TextureRegion> knightAttack3;
    private Animation<TextureRegion> knightDie;
    private Animation<TextureRegion> knightHurt;
    private Animation<TextureRegion> knightStand;

    private Sound knightRunSound;
    private Sound knightJumpSound;
    private Sound knightSwordSound;
    private Sound knightArrowSound;
    private Sound knightHurtSound;
    private Sound knightDieSound;

//    private BulletManager bulletManager;

    private float stateTimer;
    private boolean isRunningRight;
    private boolean isHurt;
    private boolean isHurting;
    private boolean isAttacking1;
    private boolean isAttacking2;
    private boolean isAttacking3;
    private boolean isDie;
    private boolean isJumping;
    private boolean endGame;

    private boolean playSound1;
    private boolean playSound2;

    // test
    public int Health;
    public int HealthMax;




    public Knight(PlayScreen screen){
        //test
        Health = 200;        // Giá trị khởi tạo cho sức khỏe hiện tại
        HealthMax = 200;

        this.world = screen.getWorld();
        currentState = State.STANDING;
        previousState = State.STANDING;
        stateTimer = 0;
        isRunningRight = true;

        prepareAnimation();
        prepareSound();
        defineKnight();

        setBounds(0, 0, 16/GameWorld.PPM, 16/GameWorld.PPM);

        isAttacking1 = false; isAttacking2 = false; isAttacking3 = false;
        isDie = false;
        isHurt = false;
        isHurting = false;
        isJumping = false;
        endGame = false;
    }
    // Giảm máu khi Orc tấn công
    public void takeDamage(float damage) {
        Health -= damage;
        if (Health <= 0) {
            isDie= true;
            endGame = true;
        }
    }



    private void prepareAnimation(){
        atlasRunning = new TextureAtlas("KnightAsset/Pack/Run.pack");
        atlasJumping = new TextureAtlas("KnightAsset/Pack/Jump.pack");
        atlasAttacking1 = new TextureAtlas("KnightAsset/Pack/Attack01.pack");
        atlasAttacking2 = new TextureAtlas("KnightAsset/Pack/Attack02.pack");
        atlasAttacking3 = new TextureAtlas("KnightAsset/Pack/Attack03.pack");
        atlasDieing = new TextureAtlas("KnightAsset/Pack/Die.pack");
        atlasBeingHurt = new TextureAtlas("KnightAsset/Pack/Hurt.pack");
        atlasStanding = new TextureAtlas("KnightAsset/Pack/Idle.pack");

        knightRun = new Animation<TextureRegion>(0.05f, atlasRunning.getRegions());
        knightJump = new Animation<TextureRegion>(0.05f, atlasJumping.getRegions());
        knightStand = new Animation<TextureRegion>(0.1f, atlasStanding.getRegions());
        knightDie = new Animation<TextureRegion>(0.5f, atlasDieing.getRegions());
        knightAttack1 = new Animation<TextureRegion>(0.05f, atlasAttacking1.getRegions());
        knightAttack2 = new Animation<TextureRegion>(0.05f, atlasAttacking2.getRegions());
        knightAttack3 = new Animation<TextureRegion>(0.1f, atlasAttacking3.getRegions());
        knightHurt = new Animation<TextureRegion>(0.5f, atlasBeingHurt.getRegions());
    }

    private void prepareAnimation1(){//anim của nguyễn bá
        atlasRunning = new TextureAtlas("KnightAsset2/Pack/Run.pack");
        atlasJumping = new TextureAtlas("KnightAsset2/Pack/Jump.pack");
        atlasAttacking1 = new TextureAtlas("KnightAsset2/Pack/Attack01.pack");
        //atlasAttacking2 = new TextureAtlas("KnightAsset/Pack/Attack02.pack");
        //atlasAttacking3 = new TextureAtlas("KnightAsset/Pack/Attack03.pack");
        atlasDieing = new TextureAtlas("KnightAsset2/Pack/Die.pack");
        atlasBeingHurt = new TextureAtlas("KnightAsset2/Pack/Hurt.pack");
        atlasStanding = new TextureAtlas("KnightAsset2/Pack/Idle.pack");

        knightRun = new Animation<TextureRegion>(0.05f, atlasRunning.getRegions());
        knightJump = new Animation<TextureRegion>(0.05f, atlasJumping.getRegions());
        knightStand = new Animation<TextureRegion>(0.1f, atlasStanding.getRegions());
        knightDie = new Animation<TextureRegion>(0.5f, atlasDieing.getRegions());
        knightAttack1 = new Animation<TextureRegion>(0.05f, atlasAttacking1.getRegions());
        //knightAttack2 = new Animation<TextureRegion>(0.05f, atlasAttacking2.getRegions());
        //knightAttack3 = new Animation<TextureRegion>(0.05f, atlasAttacking3.getRegions());
        knightHurt = new Animation<TextureRegion>(0.5f, atlasBeingHurt.getRegions());
    }

    private void prepareSound(){
        knightJumpSound = AudioManager.manager.get(AudioManager.knightJumpAudio, Sound.class);
        knightRunSound = AudioManager.manager.get(AudioManager.knightRunAudio, Sound.class);
        knightHurtSound = AudioManager.manager.get(AudioManager.knightHurtAudio, Sound.class);
        knightDieSound = AudioManager.manager.get(AudioManager.knightDieAudio, Sound.class);
        knightSwordSound = AudioManager.manager.get(AudioManager.knightSwordAudio, Sound.class);
        knightArrowSound = AudioManager.manager.get(AudioManager.knightArrowAudio, Sound.class);

        playSound1 = false;
        playSound2 = false;
    }

    public void defineKnight(){
        BodyDef bdef = new BodyDef();
        bdef.position.set(32/GameWorld.PPM, 100/GameWorld.PPM);
        bdef.type = BodyDef.BodyType.DynamicBody;

        b2body = world.createBody(bdef);
        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(6/GameWorld.PPM);
        fdef.filter.categoryBits = GameWorld.KNIGHT_BIT;
        fdef.filter.maskBits =
            GameWorld.GROUND_BIT |
            GameWorld.TRAP_BIT | GameWorld.CHEST_BIT |
            GameWorld.ENEMY_BIT | GameWorld.ITEM_BIT| GameWorld.OBJECT_BIT| GameWorld.CHEST1_BIT|GameWorld.ENEMY2_BIT;

        fdef.shape = shape;
        b2body.createFixture(fdef).setUserData(this);// chú ý cai này

        //make EdgeShape for checking head-collision
//        EdgeShape head = new EdgeShape();
//        head.set(new Vector2(-2/GameWorld.PPM, 6/GameWorld.PPM),
//                new Vector2(2/GameWorld.PPM, 6/GameWorld.PPM));
//        fdef.shape = head;
//        fdef.isSensor = true;//head is a sensor
//
//        b2body.createFixture(fdef).setUserData("head");

        //make EdgeShape for checking foot-collision
        EdgeShape foot = new EdgeShape();
        foot.set(new Vector2(-2/GameWorld.PPM, -6/GameWorld.PPM),
                new Vector2(2/GameWorld.PPM, -6/GameWorld.PPM));
        fdef.filter.categoryBits = GameWorld.KNIGHT_FOOT_BIT;
        fdef.shape = foot;
        //fdef.isSensor = true;//foot is a sensor

        b2body.createFixture(fdef).setUserData(this);
    }


    public void hurtingCallBack(){
        isHurt = true;
    }

    public boolean isEndGame(){
        return endGame;
    }

    public float getStateTimer(){
        return stateTimer;
    }

    public void setDie(){
        deathCount--;
        isDie = true;
        knightDieSound.play();
    }

    public void buff(){
        // test
        if(Health != 200){
            Health +=200;
            if (Health > HealthMax) {
                Health = HealthMax; // Giới hạn Health không vượt quá HealthMax
            }
        }
        System.out.println("Bufffffffffff");


    }
    public void buff1(){
        // test
        if(Health != 200){
            Health +=10;
        }
        if (Health > HealthMax) {
            Health = HealthMax; // Giới hạn Health không vượt quá HealthMax
        }
        System.out.println("Bufffffffffff");


    }


    public boolean isMovable(){
        return (currentState != State.DEAD && currentState != State.ATTACKING3 && currentState != State.HURTING);
    }

    public boolean isJumping(){
        return isJumping;
    }

    public void attack1CallBack(){
        isAttacking1 = true;
    }
    public void attack2CallBack(){
        isAttacking2 = true;
    }
    public void attack3CallBack(){
        isAttacking3 = true;
    }

    public TextureRegion getFrame(float dt){
        currentState = getState();
        TextureRegion region;
        switch (currentState){
            case JUMPING:
                knightJumpSound.play();
                region = (TextureRegion) knightJump.getKeyFrame(stateTimer,false);
                break;
            case RUNNING:
                knightRunSound.play();
                region = (TextureRegion) knightRun.getKeyFrame(stateTimer, true);
                break;
            case ATTACKING1:
                if (!playSound1){
                    playSound1 = true;
                    knightSwordSound.play();
                }
                region = (TextureRegion) knightAttack1.getKeyFrame(stateTimer, false);
                break;
            case ATTACKING2:
                if (!playSound2){
                    playSound2 = true;
                    knightSwordSound.play();
                }
                region = (TextureRegion) knightAttack2.getKeyFrame(stateTimer, false);
                break;
            case ATTACKING3://test
                region = (TextureRegion) knightAttack3.getKeyFrame(stateTimer, false);
                break;
            case DEAD:
                region = (TextureRegion) knightDie.getKeyFrame(stateTimer, false);
                break;
            case HURTING:
                region = (TextureRegion) knightHurt.getKeyFrame(stateTimer, false);
                break;
            case FALLING:
            case STANDING:
            default:
                region = (TextureRegion) knightStand.getKeyFrame(stateTimer, true);
                break;
        }

        if ((b2body.getLinearVelocity().x < 0 || !isRunningRight) && !region.isFlipX()){
            region.flip(true, false);
            isRunningRight = false;
        }
        else if ((b2body.getLinearVelocity().x > 0 || isRunningRight) && region.isFlipX()){
            region.flip(true, false);
            isRunningRight = true;
        }

        stateTimer = (currentState == previousState) ? stateTimer + dt : 0;
        previousState = currentState;
        return region;
    }

    public State getState(){
        //die and hurt code
        if (isDie) {//test
            if (!knightDie.isAnimationFinished(stateTimer)) {
                return State.DEAD;
            }
            if (deathCount <= 0) endGame = true;
            else {
                b2body.setTransform(32 / GameWorld.PPM, 100 / GameWorld.PPM, 0);
                isDie = false;
            }
        }

        if (isHurt) {
            isHurting = true;
            isAttacking1 = isAttacking2 = isAttacking3 = false;
            isHurt = false;
            System.out.println("KNIGHT HURTTTTTTTT");
            return State.HURTING;
        }

        if (isHurting) {
            if (!knightHurt.isAnimationFinished(stateTimer)) {
                System.out.println("KKKKKKKKKKKKKKKK");
                return State.HURTING;
            } else isHurting = false;
        }

        //attack code
        if (isAttacking1){//test
            if (!knightAttack1.isAnimationFinished(stateTimer)){
                return State.ATTACKING1;
            }
            else {
                isAttacking1 = false;
                playSound1 = false;
            }
        }
        else if (isAttacking2){
            if (!knightAttack2.isAnimationFinished(stateTimer)){
                return State.ATTACKING2;
            }
            else {
                isAttacking2 = false;
                playSound2 = false;
            }
        }
        else if (isAttacking3){//TEST O DAY
            if (!isJumping) b2body.setLinearVelocity(0, 0);
            if (!knightAttack3.isAnimationFinished(stateTimer)){
                return State.ATTACKING3;
            }
            else {//create arrow
                int arrowDirection = (isRunningRight) ? 1 : -1;
                PlayScreen.bulletManager.addBullet(b2body.getPosition().x, b2body.getPosition().y, arrowDirection);
                isAttacking3 = false;
                knightArrowSound.play();
            }
        }
        //movement code
        isJumping = false;
        if (b2body.getLinearVelocity().y == 0 && previousState == State.JUMPING){
            b2body.setLinearVelocity(0, 0);
        }
        if (b2body.getLinearVelocity().y > 0 || (b2body.getLinearVelocity().y < 0 && previousState == State.JUMPING)){
            isJumping = true;
            return State.JUMPING;
        }
        else if (b2body.getLinearVelocity().y < 0){
            return State.FALLING;
        }
        else if (b2body.getLinearVelocity().y < 0){
            return State.JUMPING;
        }
        else if (b2body.getLinearVelocity().x != 0){
            return State.RUNNING;
        }
        else return State.STANDING;
    }


    public void update(float dt){
        setPosition(b2body.getPosition().x - getWidth()/2, b2body.getPosition().y - getHeight()/2);
        TextureRegion frame = getFrame(dt);

        //this scale Knight 1.5 time bigger
        setBounds(getX(), getY(), frame.getRegionWidth()/GameWorld.PPM*scaleX,
            frame.getRegionHeight()/GameWorld.PPM*scaleY);
        setRegion(frame);
    }
    // test
    public int getHealth() {
        return Health;
    }

    public int getHealthMax() {
        return HealthMax;
    }





}
