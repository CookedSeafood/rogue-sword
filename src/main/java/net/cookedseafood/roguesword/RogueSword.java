package net.cookedseafood.roguesword;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import net.cookedseafood.pentamana.component.ManaPreferenceComponentInstance;
import net.cookedseafood.pentamana.component.ServerManaBarComponentInstance;
import net.cookedseafood.roguesword.command.RogueSwordCommand;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.mutable.MutableInt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RogueSword implements ModInitializer {
	public static final String MOD_ID = "rogue-sword";

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static final byte VERSION_MAJOR = 1;
	public static final byte VERSION_MINOR = 3;
	public static final byte VERSION_PATCH = 1;

	public static final String MOD_NAMESPACE = "rogue_sword";
	public static final String ROGUE_SWORD_CUSTOM_ID = Identifier.of(MOD_NAMESPACE, "rogue_sword").toString();

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

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> RogueSwordCommand.register(dispatcher, registryAccess));

		ServerLifecycleEvents.SERVER_STARTED.register(server -> reload());

		UseItemCallback.EVENT.register((player, world, hand) -> {
			if (Hand.OFF_HAND.equals(hand)) {
				return ActionResult.PASS;
			}

			ItemStack stack = player.getMainHandStack();
			if (!ROGUE_SWORD_CUSTOM_ID.equals(stack.getCustomId())) {
				return ActionResult.PASS;
			}

			if (!ManaPreferenceComponentInstance.MANA_PREFERENCE.get(player).isEnabled()) {
				return ActionResult.FAIL;
			}

			if (!ServerManaBarComponentInstance.SERVER_MANA_BAR.get(player).getServerManaBar().consum(manaConsumption)) {
				return ActionResult.FAIL;
			}

			usedBy((ServerPlayerEntity)player);
			return ActionResult.SUCCESS;
		});
	}

	public static void usedBy(LivingEntity entity) {
		entity.addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED, speedDuration, speedAmplifier, speedAmbient, speedShowParticles, speedShowIcon), entity);
	}

	public static int reload() {
		String configString;
		try {
			configString = FileUtils.readFileToString(new File("./config/rogue-sword.json"), StandardCharsets.UTF_8);
		} catch (IOException e) {
			reset();
			return 1;
		}

		JsonObject config = new Gson().fromJson(configString, JsonObject.class);
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
