package net.cookedseafood.roguesword.data;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.mutable.MutableInt;

public abstract class RogueSwordConfig {
    public static final float MANA_CONSUMPTION = 1;
    public static final int STATUS_EFFECT_DURATION = 600;
    public static final int STATUS_EFFECT_AMPLIFIER = 0;
    public static final boolean STATUS_EFFECT_AMBIENT = false;
    public static final boolean STATUS_EFFECT_SHOW_PARTICLES = true;
    public static final boolean STATUS_EFFECT_SHOW_ICON = true;
    public static float manaConsumption;
    public static int speedDuration;
    public static int speedAmplifier;
    public static boolean speedAmbient;
    public static boolean speedShowParticles;
    public static boolean speedShowIcon;

    public static int reload() {
        String configString;
        try {
            configString = FileUtils.readFileToString(new File("./config/rogue-sword.json"), StandardCharsets.UTF_8);
        } catch (IOException e) {
            reset();
            return 1;
        }

        JsonObject config = new Gson().fromJson(configString, JsonObject.class);
        if (config == null) {
            reset();
            return 1;
        }

        return reload(config);
    }

    public static int reload(JsonObject config) {
        MutableInt counter = new MutableInt(0);

        if (config.has("manaConsumption")) {
            manaConsumption = config.get("manaConsumption").getAsFloat();
            counter.increment();
        } else {
            manaConsumption = MANA_CONSUMPTION;
        }

        if (config.has("speedDuration")) {
            speedDuration = config.get("speedDuration").getAsInt();
            counter.increment();
        } else {
            speedDuration = STATUS_EFFECT_DURATION;
        }

        if (config.has("speedAmplifier")) {
            speedAmplifier = config.get("speedAmplifier").getAsInt();
            counter.increment();
        } else {
            speedAmplifier = STATUS_EFFECT_AMPLIFIER;
        }

        if (config.has("speedAmbient")) {
            speedAmbient = config.get("speedAmbient").getAsBoolean();
            counter.increment();
        } else {
            speedAmbient = STATUS_EFFECT_AMBIENT;
        }

        if (config.has("speedShowParticles")) {
            speedShowParticles = config.get("speedShowParticles").getAsBoolean();
            counter.increment();
        } else {
            speedShowParticles = STATUS_EFFECT_SHOW_PARTICLES;
        }

        if (config.has("speedShowIcon")) {
            speedShowIcon = config.get("speedShowIcon").getAsBoolean();
            counter.increment();
        } else {
            speedShowIcon = STATUS_EFFECT_SHOW_ICON;
        }

        return counter.intValue();
    }

    public static void reset() {
        manaConsumption = MANA_CONSUMPTION;
        speedDuration = STATUS_EFFECT_DURATION;
        speedAmplifier = STATUS_EFFECT_AMPLIFIER;
        speedAmbient = STATUS_EFFECT_AMBIENT;
        speedShowParticles = STATUS_EFFECT_SHOW_PARTICLES;
        speedShowIcon = STATUS_EFFECT_SHOW_ICON;
    }
}
