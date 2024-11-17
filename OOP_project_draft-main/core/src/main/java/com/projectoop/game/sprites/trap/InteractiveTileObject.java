package com.projectoop.game.sprites.trap;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.*;
import com.projectoop.game.GameWorld;
import com.projectoop.game.screens.PlayScreen;
import com.projectoop.game.sprites.Knight;

public abstract class InteractiveTileObject {
    protected World world;
    protected TiledMap map;
    protected TiledMapTile tile;
    protected Rectangle bounds;
    protected Body body;
    protected Fixture fixture;
    protected PlayScreen screen;
    protected MapObject object;

    public InteractiveTileObject(PlayScreen screen, MapObject object){
        this.object = object;
        this.screen = screen;
        this.world = screen.getWorld();
        this.map = screen.getMap();
        this.bounds = ((RectangleMapObject) object).getRectangle();

        BodyDef bdef = new BodyDef();
        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();

        bdef.type = BodyDef.BodyType.StaticBody;
        bdef.position.set(((bounds.getX() + bounds.getWidth()/2)/ GameWorld.PPM),
            ((bounds.getY() + bounds.getHeight()/2)/GameWorld.PPM));

        body = world.createBody(bdef);

        shape.setAsBox((bounds.getWidth()/2/GameWorld.PPM),
            (bounds.getHeight()/2/GameWorld.PPM));
        fdef.shape = shape;
        fdef.filter.maskBits = GameWorld.KNIGHT_BIT;

        fixture = body.createFixture(fdef);
    }

    //public abstract void onHeadHit();
    public abstract void onFootHit(Knight knight);

    public void setCategoryFilter(short filterBit){
        Filter filter = new Filter();
        filter.categoryBits = filterBit;
        fixture.setFilterData(filter);
    }

    public TiledMapTileLayer.Cell getCell(){
        TiledMapTileLayer layer = (TiledMapTileLayer)map.getLayers().get(1);//get cell at Layer 1
        return layer.getCell((int)(body.getPosition().x * GameWorld.PPM/16),
            (int)(body.getPosition().y * GameWorld.PPM/16));
    }
}
