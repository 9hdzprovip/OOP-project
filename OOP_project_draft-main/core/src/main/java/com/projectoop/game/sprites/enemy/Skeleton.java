package com.projectoop.game.sprites.enemy;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.projectoop.game.screens.PlayScreen;
public class Skeleton extends GroundEnemy{
    public Skeleton(PlayScreen screen, float x, float y) {
        super(screen, x, y, 0, 0.9f);
    }

    @Override
    protected void prepareAnimation() {
        atlasWalking = new TextureAtlas("SkeletonV2/Run.pack");
        atlasAttacking = new TextureAtlas("SkeletonV2/Attack.pack");
        atlasDieing = new TextureAtlas("SkeletonV2/Die.pack");
        atlasHurting = new TextureAtlas("SkeletonV2/Hurt.pack");

        walkAnimation = new Animation<TextureRegion>(0.3f, atlasWalking.getRegions());
        attackAnimation = new Animation<TextureRegion>(0.3f, atlasAttacking.getRegions());
        dieAnimation = new Animation<TextureRegion>(0.2f, atlasDieing.getRegions());
        hurtAnimation = new Animation<TextureRegion>(0.3f, atlasHurting.getRegions());
    }
}
