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
import com.projectoop.game.scences.EnemyHealthBar;
import com.projectoop.game.screens.PlayScreen;
import com.projectoop.game.sprites.Knight;
import com.projectoop.game.tools.AudioManager;

public abstract class GroundEnemy extends Enemy{
    public enum State {HURTING, ATTACKING, DEAD, WALKING};
    protected State currentState;
    protected State previousState;

    protected float scaleX;
    protected float scaleY;

    protected TextureAtlas atlasWalking;
    protected TextureAtlas atlasAttacking;
    protected TextureAtlas atlasHurting;
    protected TextureAtlas atlasDieing;

    protected Animation<TextureRegion> walkAnimation;
    protected Animation<TextureRegion> attackAnimation;
    protected Animation<TextureRegion> hurtAnimation;
    protected Animation<TextureRegion> dieAnimation;

    protected Sound attackSound;
    protected Sound hurtSound;
    protected Sound dieSound;

    protected boolean playSoundAttack;
    protected EnemyHealthBar healthBar;
    protected float maxHealth = 100;
    protected float currentHealth;
    protected float addYtoAnim;
    // test
    private boolean hasAttacked; // Đánh dấu trạng thái tấn công
    private float attackDamage = 50;  //
    public float getAttackDamage() {
        return attackDamage;
    }
    public Knight knight;

    public GroundEnemy(PlayScreen screen, float x, float y, float addY, float scale) {
        super(screen, x, y);
        this.addYtoAnim = addY;
        this.scaleX = this.scaleY = scale;

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
        currentHealth = maxHealth;
        // test
        hasAttacked = false;
        healthBar = new EnemyHealthBar(this, maxHealth);
    }

    protected void prepareAudio(){
        attackSound = AudioManager.manager.get(AudioManager.orgAttackAudio, Sound.class);
        dieSound = AudioManager.manager.get(AudioManager.orgDieAudio, Sound.class);
        hurtSound = AudioManager.manager.get(AudioManager.orgHurtAudio, Sound.class);
    }
    // test
    public void takeDamage(float damage) {
        currentHealth -= damage;
        healthBar.update(currentHealth);
    }

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
        shape.setAsBox(9/ GameWorld.PPM, 9 / GameWorld.PPM);
        //type bit
        fdef.filter.categoryBits = GameWorld.ENEMY_BIT;
        //Collision bit list
        fdef.filter.maskBits = GameWorld.GROUND_BIT |
            GameWorld.TRAP_BIT | GameWorld.CHEST_BIT |
            GameWorld.KNIGHT_BIT | GameWorld.ARROW_BIT| GameWorld.OBJECT_BIT| GameWorld.KNIGHT_SWORD_LEFT
        | GameWorld.KNIGHT_SWORD_RIGHT;
        fdef.shape = shape;
        b2body.createFixture(fdef).setUserData(this);
    }

    public void hurtKnockBack() {
        double crTime = System.currentTimeMillis();


        double t = 200;
        if(System.currentTimeMillis()-crTime <t) {
            if(screen.getPlayer().b2body.getPosition().x < b2body.getPosition().x)
                b2body.applyLinearImpulse(new Vector2(20,5), b2body.getWorldCenter(),true);
            else
                b2body.applyLinearImpulse(new Vector2(-20,5), b2body.getWorldCenter(),true);
        }

    }
    public boolean isAttack(){
        return currentState == State.ATTACKING;
    }
    //KNIGHT_BIT | ENEMY_BIT
    public boolean isInRangeAttack(){
        if (!screen.getPlayer().isAttack()) return false;
        //player on the left-side of enemy
        float check_dis = this.b2body.getPosition().x - screen.getPlayer().b2body.getPosition().x;
        if (check_dis < 30/GameWorld.PPM && check_dis > 0){
            //System.out.println("1");
            return true;
        }
        //right-side
        else if (-check_dis < 30/GameWorld.PPM && -check_dis > 0){
            //System.out.println("2");
            return true;
        }
        //System.out.println("non");
        return false;
    }


    @Override
    public void destroy() {
        setToDestroy = true;
    }

    @Override
    public void attackingCallBack() {
        attackSound.play();
        hasAttacked= true;
        isAttack = true;
        // Lấy sát thương từ Knight, có thể là sát thương bình thường hoặc tăng gấp đôi khi là người khổng lồ
        float attackDamage = screen.getPlayer().getAttackDamage();
        System.out.println("Knight attacks with damage: " + attackDamage);
        System.out.println("Chem chem chem");
        screen.getPlayer().hurtingCallBack();
    }

    @Override
    public void hurtingCallBack() {
        hurtSound.play();
        hurtKnockBack();
        //takeDamage(20);
        // Gây sát thương cho enemy
        float damage = screen.getPlayer().getAttackDamage();  // Lấy sát thương từ Knight
        takeDamage(damage);


        if (currentHealth <= 0)  isDie= true;
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
                //System.out.println("walk");
            default:
                frame = walkAnimation.getKeyFrame(stateTime, true);

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

        if (isHurt|| isInRangeAttack()){
            isHurt = false;
            isHurting = true;
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
                this.velocity = runningRight ? new Vector2(1, 0) : new Vector2(-1, 0);
            }
        }
        //attack code
        if (isAttack){
            isAttacking = true;
            isAttack = false;
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
                this.velocity = runningRight ? new Vector2(1, 0) : new Vector2(-1, 0);
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
            //this y + addY to move the animation stand on the ground
            setBounds(getX(), getY()+ addYtoAnim/GameWorld.PPM, frame.getRegionWidth() / GameWorld.PPM * scaleX,
                frame.getRegionHeight() / GameWorld.PPM * scaleY);
            setRegion(frame);
        }
    }

    public void draw (Batch batch){
        if (!destroyed || stateTime < 1){
            super.draw(batch);
            healthBar.draw(batch);
        }
    }

    @Override
    public void hitOnHead() {

    }
    public boolean hasAttacked() {
        return hasAttacked;
    }

    public void resetAttackState() {
        hasAttacked = false; // Reset trạng thái sau khi gây sát thương
    }
}
