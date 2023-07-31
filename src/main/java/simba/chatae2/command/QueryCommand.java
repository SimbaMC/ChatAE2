/* SPDX-License-Identifier: AGPL-3.0 WITH SimbaMC Proxy and SimbaMC Exceptions */
package simba.chatae2.command;

import appeng.api.networking.IGrid;
import appeng.api.networking.crafting.ICraftingCPU;
import appeng.api.networking.security.IActionHost;
import com.google.common.collect.ImmutableSet;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.server.command.ServerCommandSource;
import org.jetbrains.annotations.Nullable;
import java.util.OptionalLong;

import static appeng.api.features.Locatables.securityStations;
import static net.minecraft.text.Text.literal;
import static simba.chatae2.command.CommandEvent.BIND_KEY;
import static simba.chatae2.config.BindData.BindInstance;

public class QueryCommand {

    private static @Nullable IGrid getGridFromContext(CommandContext<ServerCommandSource> context) {
        OptionalLong GridKey = BindInstance.Query(context.getArgument(BIND_KEY, String.class));
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
            context.getSource().sendFeedback(literal("Grid size:" + grid.size()), false);
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
                String Feedback = "CPU Size:" + cpu.getAvailableStorage();
                if(cpu.isBusy()) {
                    Feedback += "; Crafting:" + cpu.getJobStatus().crafting().what().getDisplayName().getString();
                } else {
                    Feedback += "is idle";
                }
                commandSource.sendFeedback(literal(Feedback), false);
            }
            return 0;
        } else {
            return 1;
        }
    }


}
