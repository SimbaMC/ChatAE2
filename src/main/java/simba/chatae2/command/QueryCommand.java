/* SPDX-License-Identifier: AGPL-3.0 WITH SimbaMC Proxy and SimbaMC Exceptions */
package simba.chatae2.command;

import appeng.api.networking.IGrid;
import appeng.api.networking.crafting.ICraftingCPU;
import appeng.api.networking.security.IActionHost;
import com.google.common.collect.ImmutableSet;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;
import simba.chatae2.config.I18n;

import java.util.OptionalLong;

import static appeng.api.features.Locatables.securityStations;
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
        String bindKey = context.getArgument(BIND_KEY, String.class);
        if (grid != null) {
            context.getSource().sendFeedback(
                Text.literal( String.format(
                                I18n.Translate(bindKey, "chat.chatae2.grid.size"),
                                grid.size()))
            , false);
            return 0;
        } else {
            context.getSource().sendFeedback(Text.translatable( "chat.chatae2.grid.failed"), false);
            return 1;
        }
    }

    public static int QueryCPUExecute(CommandContext<ServerCommandSource> context) {
        IGrid grid = getGridFromContext(context);
        String bindKey = context.getArgument(BIND_KEY, String.class);
        ServerCommandSource commandSource = context.getSource();
        if (grid != null) {
            ImmutableSet<ICraftingCPU> cpus = grid.getCraftingService().getCpus();
            for (ICraftingCPU cpu : cpus) {
                if(cpu.isBusy()) {
                    commandSource.sendFeedback(
                            Text.literal( String.format(
                                    I18n.Translate(bindKey, "chat.chatae2.cpu.crafting"),
                                    I18n.readableSize(cpu.getAvailableStorage()),
                                    I18n.Translate(bindKey, cpu.getJobStatus().crafting().what())
                            ))
                            , false);
                } else {
                    commandSource.sendFeedback(
                            Text.literal( String.format(
                                    I18n.Translate(bindKey, "chat.chatae2.cpu.idle"),
                                    I18n.readableSize(cpu.getAvailableStorage())
                            ))
                            , false);
                }
            }
            return 0;
        } else {
            return 1;
        }
    }


}
