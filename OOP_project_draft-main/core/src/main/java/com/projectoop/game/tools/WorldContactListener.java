package com.projectoop.game.tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.*;
import com.projectoop.game.GameWorld;
import com.projectoop.game.screens.PlayScreen;
import com.projectoop.game.sprites.Knight;
import com.projectoop.game.sprites.effectedObject.Chest;
import com.projectoop.game.sprites.effectedObject.Chest1;
import com.projectoop.game.sprites.enemy.Enemy;
import com.projectoop.game.sprites.enemy.Eye;
import com.projectoop.game.sprites.enemy.Orc;
import com.projectoop.game.sprites.enemy.Skeleton;
import com.projectoop.game.sprites.items.Item;
import com.projectoop.game.sprites.trap.InteractiveTileObject;
import com.projectoop.game.sprites.weapons.Arrow;

public class WorldContactListener implements ContactListener {


    @Override
    public void beginContact(Contact contact) {
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();


        //collision definition
        int cDef = fixA.getFilterData().categoryBits | fixB.getFilterData().categoryBits;


//        if (fixA.getUserData() == "head" || fixB.getUserData() == "head"){
//            Fixture head = fixA.getUserData() == "head" ? fixA : fixB;
//            Fixture object = head == fixA ? fixB : fixA;
//            //System.out.println("Head hit 1");
//
//            if (object.getUserData() != null && InteractiveTileObject.class.isAssignableFrom(object.getUserData().getClass())){
//                ((InteractiveTileObject) object.getUserData()).onHeadHit();
//                //System.out.println("Head hit 2");
//            }
//        }
//
//        if (fixA.getUserData() == "foot" || fixB.getUserData() == "foot"){
//            Fixture head = fixA.getUserData() == "foot" ? fixA : fixB;
//            Fixture object = head == fixA ? fixB : fixA;
//
//            if (object.getUserData() != null && InteractiveTileObject.class.isAssignableFrom(object.getUserData().getClass())){
//                ((InteractiveTileObject) object.getUserData()).onFootHit();
//
//            }
//        }

        switch (cDef){
            //trap collision
            case GameWorld.KNIGHT_FOOT_BIT | GameWorld.TRAP_BIT:
                if (fixA.getFilterData().categoryBits == GameWorld.KNIGHT_FOOT_BIT){
                    ((InteractiveTileObject) fixB.getUserData()).onFootHit((Knight) fixA.getUserData());
                }
                else{
                    ((InteractiveTileObject) fixA.getUserData()).onFootHit((Knight) fixB.getUserData());
                }
                break;
            //enemy collision
            case GameWorld.ENEMY_BIT | GameWorld.PILAR_BIT://enemy collide with object -> reverse
                //Gdx.app.log("Enemy", "Pilar");
                Enemy enemy = (Enemy) ((fixA.getFilterData().categoryBits == GameWorld.ENEMY_BIT) ? fixA.getUserData() : fixB.getUserData());
                enemy.reverseVelocity(true, false);
                break;
            case GameWorld.ENEMY_BIT | GameWorld.ENEMY_BIT:
                //Gdx.app.log("Orc", "Orc");
                ((Enemy)fixA.getUserData()).reverseVelocity(true, false);
                ((Enemy)fixB.getUserData()).reverseVelocity(true, false);
                break;
            case GameWorld.ENEMY_BIT| GameWorld.OBJECT_BIT:
                if(fixA.getFilterData().categoryBits == GameWorld.ENEMY_BIT)
                    ((Enemy)fixA.getUserData()).reverseVelocity(true,false);
                else
                    ((Enemy)fixB.getUserData()).reverseVelocity(true,false);
                break;
            // test
            case GameWorld.ENEMY1_BIT| GameWorld.OBJECT_BIT:
                if(fixA.getFilterData().categoryBits == GameWorld.ENEMY1_BIT)
                    ((Enemy)fixA.getUserData()).reverseVelocity(true,false);
                else
                    ((Enemy)fixB.getUserData()).reverseVelocity(true,false);
                break;
            // test
            case GameWorld.ENEMY2_BIT| GameWorld.OBJECT_BIT:
                if(fixA.getFilterData().categoryBits == GameWorld.ENEMY2_BIT)
                    ((Enemy)fixA.getUserData()).reverseVelocity(true,false);
                else
                    ((Enemy)fixB.getUserData()).reverseVelocity(true,false);
                break;
            //arrow collision
            case GameWorld.ENEMY_BIT | GameWorld.ARROW_BIT://test
                //Gdx.app.log("Arrow", "Enemy");
                if(fixA.getFilterData().categoryBits == GameWorld.ARROW_BIT) {
                    Gdx.app.log("Enemy hit", "");
                    ((Arrow)(fixA.getUserData())).destroy();
                    ((Orc)(fixB.getUserData())).hurtingCallBack();
                }
                else {
                    Gdx.app.log("Enemy hit", "");
                    ((Arrow)(fixB.getUserData())).destroy();
                    ((Orc)(fixA.getUserData())).hurtingCallBack();
                }
                break;
            // test
            case GameWorld.ENEMY1_BIT | GameWorld.ARROW_BIT://test
                //Gdx.app.log("Arrow", "Enemy");
                if(fixA.getFilterData().categoryBits == GameWorld.ARROW_BIT) {
                    Gdx.app.log("Enemy hit", "");
                    ((Arrow)(fixA.getUserData())).destroy();
                    ((Eye)(fixB.getUserData())).hurtingCallBack();
                }
                else {
                    Gdx.app.log("Enemy hit", "");
                    ((Arrow)(fixB.getUserData())).destroy();
                    ((Eye)(fixA.getUserData())).hurtingCallBack();
                }
                break;
            case GameWorld.GROUND_BIT | GameWorld.ARROW_BIT:
            case GameWorld.CHEST_BIT | GameWorld.ARROW_BIT:
                //Gdx.app.log("Arrow", "Object");
                Arrow arrow1 = (Arrow) ((fixA.getFilterData().categoryBits == GameWorld.ARROW_BIT) ? fixA.getUserData() : fixB.getUserData());
                arrow1.destroy();
                break;
            // test
            case GameWorld.CHEST1_BIT | GameWorld.ARROW_BIT:
                //Gdx.app.log("Arrow", "Object");
                Arrow arrow2 = (Arrow) ((fixA.getFilterData().categoryBits == GameWorld.ARROW_BIT) ? fixA.getUserData() : fixB.getUserData());
                arrow2.destroy();
                break;
            //chest collision
            case GameWorld.CHEST_BIT | GameWorld.KNIGHT_FOOT_BIT:
            case GameWorld.CHEST_BIT | GameWorld.KNIGHT_BIT:
                //Gdx.app.log("Knight", "Open Chest");
                Chest chest = (Chest)((fixA.getFilterData().categoryBits == GameWorld.CHEST_BIT) ? fixA.getUserData() : fixB.getUserData());
                chest.usingCallBack();
                break;
            // test
            case GameWorld.CHEST1_BIT | GameWorld.KNIGHT_FOOT_BIT:
            case GameWorld.CHEST1_BIT | GameWorld.KNIGHT_BIT:
                //Gdx.app.log("Knight", "Open Chest");
                Chest1 chest1 = (Chest1)((fixA.getFilterData().categoryBits == GameWorld.CHEST1_BIT) ? fixA.getUserData() : fixB.getUserData());
                chest1.usingCallBack();
                break;
            case GameWorld.ITEM_BIT | GameWorld.KNIGHT_BIT:
                //Gdx.app.log("Knight", "Buff");
                if(fixA.getFilterData().categoryBits == GameWorld.ITEM_BIT)
                    ((Item)fixA.getUserData()).use((Knight) fixB.getUserData());
                else
                    ((Item)fixB.getUserData()).use((Knight) fixA.getUserData());
                break;


            //knight and enemy
            // test
//            case GameWorld.ENEMY2_BIT | GameWorld.KNIGHT_BIT:
//                if(fixA.getFilterData().categoryBits == GameWorld.ENEMY2_BIT) {
//                    Gdx.app.log("Enemy hit", "");
//                    ((Skeleton) fixA.getUserData()).attackingCallBack();
//                }
//                else {
//                    Gdx.app.log("Enemy hit", "");
//                    ((Skeleton) fixB.getUserData()).attackingCallBack();
//                }
//                break;
            case GameWorld.ENEMY_BIT | GameWorld.KNIGHT_BIT:
                if (fixA.getFilterData().categoryBits == GameWorld.ENEMY_BIT) {
                    Orc orc = (Orc) fixA.getUserData();
                    orc.attackingCallBack();
                    Knight knight = (Knight) fixB.getUserData();
                    knight.takeDamage(orc.getAttackDamage()); // Orc gây sát thương cho Knight
                } else {
                    Orc orc = (Orc) fixB.getUserData();
                    orc.attackingCallBack();
                    Knight knight = (Knight) fixA.getUserData();
                    knight.takeDamage(orc.getAttackDamage()); // Orc gây sát thương cho Knight
                }
                break;
            case GameWorld.ENEMY1_BIT | GameWorld.KNIGHT_BIT:
                if (fixA.getFilterData().categoryBits == GameWorld.ENEMY1_BIT) {
                    Eye eye = (Eye) fixA.getUserData();
                    eye.attackingCallBack();
                    Knight knight = (Knight) fixB.getUserData();
                    knight.takeDamage(eye.getAttackDamage()); // Eye gây sát thương cho Knight

                } else {
                    Eye eye = (Eye) fixB.getUserData();
                    eye.attackingCallBack();
                    Knight knight = (Knight) fixA.getUserData();
                    knight.takeDamage(eye.getAttackDamage()); // Eye gây sát thương cho Knight

                }
                break;
            case GameWorld.ENEMY2_BIT | GameWorld.KNIGHT_BIT:
                if (fixA.getFilterData().categoryBits == GameWorld.ENEMY2_BIT) {
                    Skeleton skeleton = (Skeleton) fixA.getUserData();
                    skeleton.attackingCallBack();
//                    Knight knight = (Knight) fixB.getUserData();
//                    knight.takeDamage(eye.getAttackDamage()); // Eye gây sát thương cho Knight
//                    System.out.println("FixtureA: " + fixA.getUserData());
//                    System.out.println("FixtureB: " + fixB.getUserData());

                } else {
                    Skeleton skeleton = (Skeleton) fixB.getUserData();
                    skeleton.attackingCallBack();
//                    Knight knight = (Knight) fixA.getUserData();
//                    knight.takeDamage(eye.getAttackDamage()); // Eye gây sát thương cho Knight

                }
                break;
        }
    }

    @Override
    public void endContact(Contact contact) {

    }

    @Override
    public void preSolve(Contact contact, Manifold manifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse contactImpulse) {

    }
}
