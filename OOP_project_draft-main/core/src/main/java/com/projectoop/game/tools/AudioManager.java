package com.projectoop.game.tools;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class AudioManager {
    public static AssetManager manager;
    //audio
    public static String backgroundMusic = "Audio/GameMusic/Map2Music.mp3";

    public static String knightRunAudio = "Audio/SoundEffect/HeroRun.mp3";
    public static String knightHurtAudio = "Audio/SoundEffect/hero_hurt.mp3";
    public static String knightJumpAudio = "Audio/SoundEffect/Jump.wav";
    public static String knightDieAudio = "Audio/SoundEffect/hero_die.mp3";
    public static String knightSwordAudio = "Audio/SoundEffect/hero_attack_sword.mp3";
    public static String knightArrowAudio = "Audio/SoundEffect/hero_arrow.mp3";

    public static String chestOpenAudio = "Audio/SoundEffect/chest_opening.mp3";
    public static String potionCollectAudio = "Audio/SoundEffect/potion_collect.mp3";

    public static String orgDieAudio = "Audio/SoundEffect/org_die.mp3";
    public static String orgHurtAudio = "Audio/SoundEffect/org_hurt.mp3";
    public static String orgAttackAudio = "Audio/SoundEffect/org_attack.mp3";

    public static void setUp(){
        manager = new AssetManager();
        manager.load(backgroundMusic, Music.class);

        manager.load(knightHurtAudio, Sound.class);
        manager.load(knightJumpAudio, Sound.class);
        manager.load(knightRunAudio, Sound.class);
        manager.load(knightDieAudio, Sound.class);
        manager.load(knightSwordAudio, Sound.class);
        manager.load(knightArrowAudio, Sound.class);

        manager.load(chestOpenAudio, Sound.class);
        manager.load(potionCollectAudio, Sound.class);

        manager.load(orgDieAudio, Sound.class);
        manager.load(orgHurtAudio, Sound.class);
        manager.load(orgAttackAudio, Sound.class);

        manager.finishLoading();
    }
}
