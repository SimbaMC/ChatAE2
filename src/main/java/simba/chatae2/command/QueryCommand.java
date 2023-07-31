/* SPDX-License-Identifier: AGPL-3.0 WITH SimbaMC Proxy and SimbaMC Exceptions */
package simba.chatae2.command;

import appeng.api.networking.IGrid;
import appeng.api.networking.crafting.ICraftingCPU;
import appeng.api.networking.security.IActionHost;
import com.google.common.collect.ImmutableSet;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.server.command.ServerCommandSource;
import org.jetbrains.annotations.Nullable;
import simba.chatae2.config.BindData;

import java.util.OptionalLong;

import static appeng.api.features.Locatables.securityStations;
import static net.minecraft.text.Text.literal;
import static simba.chatae2.command.CommandEvent.BIND_KEY;

public class QueryCommand {

    private static @Nullable IGrid getGridFromContext(CommandContext<ServerCommandSource> context) {
        OptionalLong GridKey = BindData.Query(context.getArgument(BIND_KEY, String.class));
        if(GridKey.isPresent()) {
            long key = GridKey.getAsLong();
            IActionHost securityStation = securityStations().get(context.getSource().getWorld(), key);
            return securityStation.getActionableNode().getGrid();
        }
        return null;
    }

    public static int QueryExecute(CommandContext<ServerCommandSource> context) {
        IGrid grid = getGridFromContext(context);
        if (grid != null) {
            context.getSource().sendFeedback(literal(String.valueOf(grid.size())), false);
            return 0;
        } else {
            return 1;
        }
    }

    public static int QueryCPUExecute(CommandContext<ServerCommandSource> context) {
        IGrid grid = getGridFromContext(context);
        ServerCommandSource commandSource = context.getSource();
        if (grid != null) {
            ImmutableSet<ICraftingCPU> cpus = grid.getCraftingService().getCpus();
            for (ICraftingCPU cpu : cpus) {
                commandSource.sendFeedback(literal(String.valueOf(cpu.getAvailableStorage())), false);
                if(cpu.isBusy()) {
                    commandSource.sendFeedback(literal(cpu.getJobStatus().crafting().what().getDisplayName().getString()), false);
                } else {
                    commandSource.sendFeedback(literal("CPU is idle"), false);
                }
            }
            return 0;
        } else {
            return 1;
        }
    }


}
