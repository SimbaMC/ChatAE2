/* SPDX-License-Identifier: AGPL-3.0 WITH SimbaMC Proxy and SimbaMC Exceptions */
package simba.chatae2.command;

import appeng.api.config.CpuSelectionMode;
import appeng.api.networking.IGrid;
import appeng.api.networking.crafting.*;
import appeng.api.networking.security.IActionSource;
import appeng.api.stacks.AEKey;
import appeng.me.helpers.MachineSource;
import appeng.me.helpers.PlayerSource;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;
import simba.chatae2.ChatAE2;
import simba.chatae2.config.I18n;
import simba.chatae2.util.CraftRequester;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import static simba.chatae2.ChatAE2.config;
import static simba.chatae2.command.CommandEvent.*;

public class CraftCommand {

    public static int CraftExecute(CommandContext<ServerCommandSource> context) {
        IGrid grid = getGridFromContext(context);
        String bindKey = context.getArgument(BIND_KEY, String.class);
        String craftKey = context.getArgument(CRAFT_KEY, String.class);
        int craftNum = context.getArgument(CRAFT_NUM, int.class);
        ServerCommandSource commandSource = context.getSource();
        if (grid == null) {
            commandSource.sendFeedback(Text.literal(
                    I18n.Translate(bindKey, "chat.chatae2.grid.failed")
            ), false);
            return 0;
        }
        Set<AEKey> craftableKey = grid.getCraftingService().getCraftables(
                what -> I18n.Translate(bindKey, what).contains(craftKey)
        );
        if (craftableKey.isEmpty()) {
            commandSource.sendFeedback(Text.literal(
                    I18n.Translate(bindKey, "chat.chatae2.search.nothing")
            ), false);
            return 0;
        }
        if (craftableKey.size() > 1) {
            int match = 0;
            for (AEKey key: craftableKey) {
                String translatedName = I18n.Translate(bindKey, key);
                String printMessage = String.format(
                        I18n.Translate(bindKey, "chat.chatae2.search.craftable"),
                        translatedName
                );
                if( (match ++) >= config.getMAX_SEARCH_KEY() ){
                    commandSource.sendFeedback(Text.literal(String.format(
                            I18n.Translate(bindKey, "chat.chatae2.search.toomore"),
                            craftableKey.size()
                    )), false);
                    return 0;
                }
                commandSource.sendFeedback(Text.literal(printMessage), false);
            }
        }

        assert craftableKey.size() == 1;
        AEKey craftAEKey = craftableKey.iterator().next();
        ICraftingService craftService = grid.getCraftingService();
        ServerWorld craftWorld = commandSource.getWorld();
        ICraftingRequester craftRequester = new CraftRequester(grid);
        IActionSource craftSource = (config.craftSelectionMode == CpuSelectionMode.PLAYER_ONLY) ?
                                        new PlayerSource(
                                                new ServerPlayerEntity(craftWorld.getServer(), craftWorld, ChatAE2.PROFILE, null),
                                                () -> grid.getPivot()) :
                                        new MachineSource(craftRequester);


        Future<ICraftingPlan> craftFuturePlan = craftService.beginCraftingCalculation(
                commandSource.getWorld(),
                () -> craftSource,
                craftAEKey,
                craftNum,
                CalculationStrategy.REPORT_MISSING_ITEMS
        );
        CompletableFuture.supplyAsync(() -> {
            try {
                ICraftingPlan plan = craftFuturePlan.get();
                ICraftingSubmitResult submitResult = grid.getCraftingService().submitJob(plan,
                        (config.craftSelectionMode == CpuSelectionMode.PLAYER_ONLY) ? null : craftRequester,
                        null, false, craftSource);
                @Nullable CraftingSubmitErrorCode errorCode = submitResult.errorCode();
                if (errorCode != null)
                    commandSource.sendFeedback(Text.literal(errorCode.toString()), false);
                return null;
            } catch (InterruptedException | ExecutionException e) {
                StringWriter stringWriter = new StringWriter();
                PrintWriter printWriter = new PrintWriter(stringWriter);
                e.printStackTrace(printWriter);
                String stackTraceString = stringWriter.toString();
                ChatAE2.LOGGER.warn(stackTraceString);
                throw new RuntimeException(e);
            }
        });

        return 1;
    }

}
