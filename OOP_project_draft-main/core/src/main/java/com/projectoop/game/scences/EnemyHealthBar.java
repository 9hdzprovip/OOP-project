package com.projectoop.game.scences;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.projectoop.game.GameWorld;
import com.projectoop.game.sprites.enemy.GroundEnemy;

public class EnemyHealthBar {
    private Texture bgTexture;
    private Texture redTexture;
    private float maxHealth;
    private float currentHealth;
    private GroundEnemy groundEnemy;

    public EnemyHealthBar(GroundEnemy groundEnemy, float maxHealth) {
        this.groundEnemy = groundEnemy;
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
        float barWidth = 50 / GameWorld.PPM;
        float barHeight = 8 / GameWorld.PPM;

        // Vị trí thanh máu ở trên đầu orc
//        float barX = groundEnemy.getX()+45/GameWorld.PPM;
//        float barY = groundEnemy.getY() + groundEnemy.getHeight() / 2 + barHeight+ 20/ GameWorld.PPM;
        float barX = groundEnemy.b2body.getPosition().x - barWidth/2;
        float barY = groundEnemy.b2body.getPosition().y+ 20/GameWorld.PPM;         // tu xet vi tri




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
