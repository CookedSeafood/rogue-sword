package net.cookedseafood.roguesword;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.cookedseafood.pentamana.command.ManaCommand;
import net.cookedseafood.roguesword.command.RogueSwordCommand;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RogueSword implements ModInitializer {
	public static final String MOD_ID = "roguesword";

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	
	public static final byte VERSION_MAJOR = 1;
	public static final byte VERSION_MINOR = 0;
	public static final byte VERSION_PATCH = 4;

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> RogueSwordCommand.register(dispatcher, registryAccess));

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

				ManaCommand.executeSetManaConsum(source, 16777216);
				if (ManaCommand.executeConsume(source) == 0) {
					return ActionResult.PASS;
				}
			} catch (CommandSyntaxException e) {
				e.printStackTrace();
			}

			player.addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED, 600, 0), player);
			return ActionResult.PASS;
		});
	}
}
