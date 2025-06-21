package net.cookedseafood.roguesword.command;

import com.mojang.brigadier.CommandDispatcher;
import net.cookedseafood.roguesword.RogueSword;
import net.cookedseafood.roguesword.data.RogueSwordConfig;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

public class RogueSwordCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess) {
        dispatcher.register(
            CommandManager.literal("roguesword")
            .then(
                CommandManager.literal("reload")
                .requires(source -> source.hasPermissionLevel(2))
                .executes(context -> executeReload((ServerCommandSource)context.getSource()))
            )
            .then(
                CommandManager.literal("version")
                .executes(context -> executeVersion((ServerCommandSource)context.getSource()))
            )
        );
    }

    public static int executeReload(ServerCommandSource source) {
        source.sendFeedback(() -> Text.literal("Reloading Rogue Sword!"), true);
        return RogueSwordConfig.reload();
    }

    public static int executeVersion(ServerCommandSource source) {
        source.sendFeedback(() -> Text.literal("Rogue Sword " + RogueSword.VERSION_MAJOR + "." + RogueSword.VERSION_MINOR + "." + RogueSword.VERSION_PATCH), false);
        return 0;
    }
}
