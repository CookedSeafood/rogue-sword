package net.cookedseafood.roguesword;

import net.cookedseafood.roguesword.command.RogueSwordCommand;
import net.cookedseafood.roguesword.data.RogueSwordConfig;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RogueSword implements ModInitializer {
    public static final String MOD_ID = "rogue-sword";

    // This logger is used to write text to the console and the log file.
    // It is considered best practice to use your mod id as the logger's name.
    // That way, it's clear which mod wrote info, warnings, and errors.
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static final byte VERSION_MAJOR = 1;
    public static final byte VERSION_MINOR = 4;
    public static final byte VERSION_PATCH = 0;

    public static final String MOD_NAMESPACE = "rogue_sword";
    public static final String ROGUE_SWORD_CUSTOM_ID = Identifier.of(MOD_NAMESPACE, "rogue_sword").toString();

    @Override
    public void onInitialize() {
        // This code runs as soon as Minecraft is in a mod-load-ready state.
        // However, some things (like resources) may still be uninitialized.
        // Proceed with mild caution.

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> RogueSwordCommand.register(dispatcher, registryAccess));

        ServerLifecycleEvents.SERVER_STARTED.register(server -> RogueSwordConfig.reload());

        UseItemCallback.EVENT.register((player, world, hand) -> {
            if (Hand.OFF_HAND.equals(hand)) {
                return ActionResult.PASS;
            }

            ItemStack stack = player.getMainHandStack();
            if (!ROGUE_SWORD_CUSTOM_ID.equals(stack.getCustomId())) {
                return ActionResult.PASS;
            }

            if (!player.consumMana(RogueSwordConfig.manaConsumption)) {
                return ActionResult.FAIL;
            }

            usedBy((ServerPlayerEntity)player);
            return ActionResult.SUCCESS;
        });
    }

    public static void usedBy(LivingEntity entity) {
        entity.addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED, RogueSwordConfig.speedDuration, RogueSwordConfig.speedAmplifier, RogueSwordConfig.speedAmbient, RogueSwordConfig.speedShowParticles, RogueSwordConfig.speedShowIcon), entity);
    }
}
