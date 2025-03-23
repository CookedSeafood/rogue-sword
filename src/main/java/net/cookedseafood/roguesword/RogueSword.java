package net.cookedseafood.roguesword;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import net.cookedseafood.pentamana.Pentamana;
import net.cookedseafood.pentamana.component.ManaPreferenceComponentImpl;
import net.cookedseafood.pentamana.component.ServerManaBarComponentImpl;
import net.cookedseafood.roguesword.command.RogueSwordCommand;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RogueSword implements ModInitializer {
	public static final String MOD_ID = "rogue-sword";

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	
	public static final byte VERSION_MAJOR = 1;
	public static final byte VERSION_MINOR = 1;
	public static final byte VERSION_PATCH = 7;

	public static final int MANA_POINT_CONSUMPTION = 1;
	public static final int STATUS_EFFECT_DURATION = 600;
	public static final int STATUS_EFFECT_AMPLIFIER = 0;
	public static final boolean STATUS_EFFECT_AMBIENT = false;
	public static final boolean STATUS_EFFECT_SHOW_PARTICLES = true;
	public static final boolean STATUS_EFFECT_SHOW_ICON = true;

	public static int manaPointConsumption;
	public static int speedDuration;
	public static int speedAmplifier;
	public static boolean speedAmbient;
	public static boolean speedShowParticles;
	public static boolean speedShowIcon;

	public static int manaConsumption;

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> RogueSwordCommand.register(dispatcher, registryAccess));

		ServerLifecycleEvents.SERVER_STARTED.register(server -> {
			RogueSword.reload();
		});

		UseItemCallback.EVENT.register((player, world, hand) -> {
			if (Hand.OFF_HAND.equals(hand)) {
				return ActionResult.PASS;
			}

			ItemStack stack = player.getMainHandStack();
			if (!"Rogue Sword".equals(stack.getItemName().getString())) {
				return ActionResult.PASS;
			}

			if (!ManaPreferenceComponentImpl.MANA_PREFERENCE.get(player).isEnabled()) {
				return ActionResult.PASS;
			}

			if (!ServerManaBarComponentImpl.SERVER_MANA_BAR.get(player).getServerManaBar().consum(manaConsumption)) {
				return ActionResult.PASS;
			}

			player.addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED, speedDuration, speedAmplifier, speedAmbient, speedShowParticles, speedShowIcon), player);
			return ActionResult.PASS;
		});
	}

	public static int reload() {
		String configString;
		try {
			configString = FileUtils.readFileToString(new File("./config/rogue-sword.json"), StandardCharsets.UTF_8);
		} catch (IOException e) {
            reset();
			reCalc();
			return 1;
		}

		JsonObject config = new Gson().fromJson(configString, JsonObject.class);

		manaPointConsumption =
			config.has("manaConsumption") ?
			config.get("manaConsumption").getAsInt() :
			MANA_POINT_CONSUMPTION;
		speedDuration =
			config.has("speedDuration") ?
			config.get("speedDuration").getAsInt() :
			STATUS_EFFECT_DURATION;
		speedAmplifier =
			config.has("speedAmplifier") ?
			config.get("speedAmplifier").getAsInt() :
			STATUS_EFFECT_AMPLIFIER;
		speedAmbient =
			config.has("speedAmbient") ?
			config.get("speedAmbient").getAsBoolean() :
			STATUS_EFFECT_AMBIENT;
		speedShowParticles =
			config.has("speedShowParticles") ?
			config.get("speedShowParticles").getAsBoolean() :
			STATUS_EFFECT_SHOW_PARTICLES;
		speedShowIcon =
			config.has("speedShowIcon") ?
			config.get("speedShowIcon").getAsBoolean() :
			STATUS_EFFECT_SHOW_ICON;

		reCalc();
		return 2;
	}

	public static void reset() {
		manaPointConsumption = MANA_POINT_CONSUMPTION;
		speedDuration = STATUS_EFFECT_DURATION;
		speedAmplifier = STATUS_EFFECT_AMPLIFIER;
		speedAmbient = STATUS_EFFECT_AMBIENT;
		speedShowParticles = STATUS_EFFECT_SHOW_PARTICLES;
		speedShowIcon = STATUS_EFFECT_SHOW_ICON;
	}

	public static void reCalc() {
		manaConsumption = Pentamana.manaPerPoint * manaPointConsumption;
	}
}
