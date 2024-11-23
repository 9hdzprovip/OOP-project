package com.projectoop.game.tools;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.projectoop.game.GameWorld;
import com.projectoop.game.screens.PlayScreen;
import com.projectoop.game.sprites.effectedObject.Chest1;
import com.projectoop.game.sprites.enemy.Orc;
import com.projectoop.game.sprites.enemy.Skeleton;
import com.projectoop.game.sprites.enemy.Eye;
import com.projectoop.game.sprites.effectedObject.Chest;

import com.projectoop.game.sprites.trap.Trap;


public class B2WorldCreator {
    private Array<Orc> orcs;
    private Array<Eye> eyes;
    private Array<Skeleton> skeletons;
    private Array<Chest> chests;
    private Array<Chest1> chest1s;

    public B2WorldCreator(PlayScreen screen){
        World world = screen.getWorld();
        TiledMap map = screen.getMap();

        BodyDef bdef = new BodyDef();
        PolygonShape shape = new PolygonShape();
        FixtureDef fdef = new FixtureDef();
        Body body;

        //ground_ however if we need to make it short
        for (MapObject object : map.getLayers().get(2).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject)object).getRectangle();

            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set((rect.getX() + rect.getWidth()/2)/ GameWorld.PPM, (rect.getY() + rect.getHeight()/2)/GameWorld.PPM);

            body = world.createBody(bdef);

            shape.setAsBox(rect.getWidth()/2/GameWorld.PPM, rect.getHeight()/2/GameWorld.PPM);
            fdef.shape = shape;
            fdef.filter.categoryBits = GameWorld.GROUND_BIT;//if (automatic) enemy collide with object, then change direction
            body.createFixture(fdef);
        }
        //trap
        for (MapObject object : map.getLayers().get(4).getObjects().getByType(RectangleMapObject.class)){
            new Trap(screen, object);
        }
        // brick
        // crete brick
        for(MapObject object: map.getLayers().get(5).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject)object).getRectangle();

            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set((rect.getX()+rect.getWidth()/2) / GameWorld.PPM , (rect.getY()+rect.getHeight()/2) / GameWorld.PPM);

            body= world.createBody(bdef);

            shape.setAsBox(rect.getWidth()/ 2 / GameWorld.PPM , rect.getHeight()/ 2 / GameWorld.PPM );
            fdef.shape = shape;
            body.createFixture(fdef);


        }
        // crete pipes
        for(MapObject object: map.getLayers().get(3).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject)object).getRectangle();

            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set((rect.getX()+rect.getWidth()/2) / GameWorld.PPM , (rect.getY()+rect.getHeight()/2) / GameWorld.PPM);

            body= world.createBody(bdef);

            shape.setAsBox(rect.getWidth()/ 2 / GameWorld.PPM , rect.getHeight()/ 2 / GameWorld.PPM );
            fdef.shape = shape;
            fdef.filter.categoryBits = GameWorld.OBJECT_BIT;
            body.createFixture(fdef);


        }
//        //pilar
//        for (MapObject object : map.getLayers().get(11).getObjects().getByType(RectangleMapObject.class)){
//            new Pilar(screen, object);
//        }
        //create all orcs
        orcs = new Array<>();
        for (MapObject object : map.getLayers().get(6).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject)object).getRectangle();
            orcs.add(new Orc(screen, rect.getX() / GameWorld.PPM, rect.y / GameWorld.PPM));
        }
        //create all eyes
        eyes = new Array<>();
        for (MapObject object : map.getLayers().get(8).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject)object).getRectangle();
            eyes.add(new Eye(screen, rect.getX() / GameWorld.PPM, rect.y / GameWorld.PPM));
        }
        //create all chests
        chests = new Array<>();
        for (MapObject object : map.getLayers().get(7).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject)object).getRectangle();
            chests.add(new Chest(screen, rect.getX() / GameWorld.PPM, rect.y / GameWorld.PPM));
        }
        //create all chests1
        chest1s = new Array<>();
        for (MapObject object : map.getLayers().get(9).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject)object).getRectangle();
            chest1s.add(new Chest1(screen, rect.getX() / GameWorld.PPM, rect.y / GameWorld.PPM));
        }
        //create all skeletons
        skeletons = new Array<>();
        for (MapObject object : map.getLayers().get(10).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject)object).getRectangle();
            skeletons.add(new Skeleton(screen, rect.getX() / GameWorld.PPM, rect.y / GameWorld.PPM));
        }



    }

    public Array<Orc> getOrcs() {
        return orcs;
    }
    public Array<Eye> getEyes() {
        return eyes;
    }
    public Array<Skeleton> getSkeletons() {
        return skeletons;
    }


    public Array<Chest> getChests(){
        return chests;
    }
    public Array<Chest1> getChests1(){
        return chest1s;
    }

}
