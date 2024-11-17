package com.projectoop.game.sprites.enemy;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.projectoop.game.GameWorld;

public class SkeletonHealthBar {
    private Texture bgTexture;
    private Texture redTexture;
    private float maxHealth;
    private float currentHealth;
    private Skeleton skeleton;

    public SkeletonHealthBar(Skeleton skeleton, float maxHealth) {
        this.skeleton = skeleton;
        this.maxHealth = maxHealth;
        this.currentHealth = maxHealth;

        bgTexture = new Texture("HealthBar/bg.png");
        redTexture = new Texture("HealthBar/green.png");
    }

    public void update(float health) {
        this.currentHealth = health;
    }

    public void draw(Batch batch) {
        float healthPercentage = currentHealth / maxHealth;
        float barWidth = 60 / GameWorld.PPM;
        float barHeight = 8 / GameWorld.PPM;

        // Vị trí thanh máu ở trên đầu orc
        float barX = skeleton.getX()+45/GameWorld.PPM;
        float barY = skeleton.getY() + skeleton.getHeight() / 2 + barHeight+ 20/ GameWorld.PPM;

        // Vẽ nền thanh máu
        batch.draw(bgTexture, barX, barY, barWidth, barHeight);

        // Vẽ thanh máu đỏ
        batch.draw(redTexture, barX, barY, barWidth * healthPercentage, barHeight);
    }

    public void dispose() {
        bgTexture.dispose();
        redTexture.dispose();
    }
}




