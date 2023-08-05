/* SPDX-License-Identifier: AGPL-3.0 WITH SimbaMC Proxy and SimbaMC Exceptions */
package simba.chatae2.command;

import appeng.api.networking.IGrid;
import appeng.api.networking.crafting.CraftingJobStatus;
import appeng.api.networking.crafting.ICraftingCPU;
import com.google.common.collect.ImmutableSet;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import simba.chatae2.config.I18n;

import static simba.chatae2.command.CommandEvent.BIND_KEY;
import static simba.chatae2.command.CommandEvent.getGridFromContext;

public class QueryCommand {

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
            context.getSource().sendFeedback(Text.literal(
                    I18n.Translate(bindKey, "chat.chatae2.grid.failed")
            ), false);
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
                    CraftingJobStatus job = cpu.getJobStatus();
                    assert (job != null);
                    commandSource.sendFeedback(
                            Text.literal( String.format(
                                    I18n.Translate(bindKey, "chat.chatae2.cpu.crafting"),
                                    I18n.readableSize(cpu.getAvailableStorage()),
                                    I18n.Translate(bindKey, job.crafting().what()),
                                    I18n.readableSize(job.progress()),
                                    I18n.readableSize(job.totalItems())
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
            return 1;
        } else {
            context.getSource().sendFeedback(Text.literal(
                    I18n.Translate(bindKey, "chat.chatae2.grid.failed")
            ), false);
            return 0;
        }
    }


}
