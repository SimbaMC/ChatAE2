/* SPDX-License-Identifier: AGPL-3.0 WITH SimbaMC Proxy and SimbaMC Exceptions */
package simba.chatae2.command;

import appeng.api.networking.IGrid;
import appeng.api.networking.crafting.CraftingJobStatus;
import appeng.api.networking.crafting.ICraftingCPU;
import com.google.common.collect.ImmutableSet;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import simba.chatae2.config.I18n;

import static simba.chatae2.command.CommandEvent.BIND_KEY;
import static simba.chatae2.command.CommandEvent.getGridFromContext;

public class QueryCommand {

    public static int QueryExecute(CommandContext<CommandSourceStack> context) {
        IGrid grid = getGridFromContext(context);
        String bindKey = context.getArgument(BIND_KEY, String.class);
        if (grid != null) {
            context.getSource().sendSuccess(
                    () -> Component.literal( String.format(
                                I18n.Translate(bindKey, "chat.chatae2.grid.size"),
                                grid.size()))
            , false);
            return 0;
        } else {
            context.getSource().sendSuccess(() -> Component.literal(
                    I18n.Translate(bindKey, "chat.chatae2.grid.failed")
            ), false);
            return 1;
        }
    }

    public static int QueryCPUExecute(CommandContext<CommandSourceStack> context) {
        IGrid grid = getGridFromContext(context);
        String bindKey = context.getArgument(BIND_KEY, String.class);
        CommandSourceStack commandSource = context.getSource();
        if (grid != null) {
            ImmutableSet<ICraftingCPU> cpus = grid.getCraftingService().getCpus();
            for (ICraftingCPU cpu : cpus) {
                if(cpu.isBusy()) {
                    CraftingJobStatus job = cpu.getJobStatus();
                    assert (job != null);
                    commandSource.sendSuccess(
                            () -> Component.literal( String.format(
                                    I18n.Translate(bindKey, "chat.chatae2.cpu.crafting"),
                                    I18n.readableSize(cpu.getAvailableStorage()),
                                    I18n.Translate(bindKey, job.crafting().what()),
                                    I18n.readableSize(job.progress()),
                                    I18n.readableSize(job.totalItems())
                            ))
                            , false);
                } else {
                    commandSource.sendSuccess(
                            () -> Component.literal( String.format(
                                    I18n.Translate(bindKey, "chat.chatae2.cpu.idle"),
                                    I18n.readableSize(cpu.getAvailableStorage())
                            ))
                            , false);
                }
            }
            return 1;
        } else {
            commandSource.sendSuccess(() -> Component.literal(
                    I18n.Translate(bindKey, "chat.chatae2.grid.failed")
            ), false);
            return 0;
        }
    }


}
