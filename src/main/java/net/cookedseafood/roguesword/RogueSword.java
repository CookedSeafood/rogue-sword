package net.cookedseafood.roguesword;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import net.cookedseafood.pentamana.command.ManaCommand;
import net.cookedseafood.roguesword.command.RogueSwordCommand;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RogueSword implements ModInitializer {
	public static final String MOD_ID = "roguesword";

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	
	public static final byte VERSION_MAJOR = 1;
	public static final byte VERSION_MINOR = 1;
	public static final byte VERSION_PATCH = 0;

	public static final int MANA_CONSUMPTION = 16777216;
	public static final int STATUS_EFFECT_DURATION = 600;
	public static final int STATUS_EFFECT_AMPLIFIER = 0;
	public static final boolean STATUS_EFFECT_AMBIENT = false;
	public static final boolean STATUS_EFFECT_SHOW_PARTICLES = true;
	public static final boolean STATUS_EFFECT_SHOW_ICON = true;

	public static int manaConsumption;
	public static int statusEffectDuration;
	public static int statusEffectAmplifier;
	public static boolean statusEffectAmbient;
	public static boolean statusEffectShowParticles;
	public static boolean statusEffectShowIcon;

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> RogueSwordCommand.register(dispatcher, registryAccess));

		ServerLifecycleEvents.SERVER_STARTED.register(server -> {
			RogueSwordCommand.executeReload(server.getCommandSource());
		});

		UseItemCallback.EVENT.register((player, world, hand) -> {
			if (Hand.OFF_HAND.equals(hand)) {
				return ActionResult.PASS;
			}

			ItemStack stack = player.getMainHandStack();
			if (!"Rogue Sword".equals(stack.getItemName().getString())) {
				return ActionResult.PASS;
			}
			
			ServerCommandSource source = world.getServer().getPlayerManager().getPlayer(player.getUuid()).getCommandSource();

			try {
				if (ManaCommand.executeGetEnabled(source) != 1) {
					return ActionResult.PASS;
				}

				ManaCommand.executeSetManaConsum(source, manaConsumption);
				if (ManaCommand.executeConsume(source) == 0) {
					return ActionResult.PASS;
				}
			} catch (CommandSyntaxException e) {
				e.printStackTrace();
			}

			player.addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED, statusEffectDuration, statusEffectAmplifier, statusEffectAmbient, statusEffectShowParticles, statusEffectShowIcon), player);
			return ActionResult.PASS;
		});
	}

	public static int reload() {
		String configString;
		try {
			configString = FileUtils.readFileToString(new File("./config/roguesword.json"), StandardCharsets.UTF_8);
		} catch (IOException e) {
            reset();
			return 1;
		}

		JsonObject config = new Gson().fromJson(configString, JsonObject.class);

		manaConsumption =
			config.has("manaConsumption") ?
			config.get("manaConsumption").getAsInt() :
			MANA_CONSUMPTION;
		statusEffectDuration =
			config.has("statusEffectDuration") ?
			config.get("statusEffectDuration").getAsInt() :
			STATUS_EFFECT_DURATION;
		statusEffectAmplifier =
			config.has("statusEffectAmplifier") ?
			config.get("statusEffectAmplifier").getAsInt() :
			STATUS_EFFECT_AMPLIFIER;
		statusEffectAmbient =
			config.has("statusEffectAmbient") ?
			config.get("statusEffectAmbient").getAsBoolean() :
			STATUS_EFFECT_AMBIENT;
		statusEffectShowParticles =
			config.has("statusEffectShowParticles") ?
			config.get("statusEffectShowParticles").getAsBoolean() :
			STATUS_EFFECT_SHOW_PARTICLES;
		statusEffectShowIcon =
			config.has("statusEffectShowIcon") ?
			config.get("statusEffectShowIcon").getAsBoolean() :
			STATUS_EFFECT_SHOW_ICON;
		return 2;
	}

	public static void reset() {
		manaConsumption = MANA_CONSUMPTION;
		statusEffectDuration = STATUS_EFFECT_DURATION;
		statusEffectAmplifier = STATUS_EFFECT_AMPLIFIER;
		statusEffectAmbient = STATUS_EFFECT_AMBIENT;
		statusEffectShowParticles = STATUS_EFFECT_SHOW_PARTICLES;
		statusEffectShowIcon = STATUS_EFFECT_SHOW_ICON;
	}
}
