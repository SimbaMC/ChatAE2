/* SPDX-License-Identifier: AGPL-3.0 WITH SimbaMC Proxy and SimbaMC Exceptions */
package simba.chatae2.command;

import appeng.items.tools.powered.WirelessTerminalItem;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import java.util.OptionalLong;

import static simba.chatae2.command.CommandEvent.BIND_KEY;
import static simba.chatae2.config.BindData.BindInstance;

public class BindCommand {

    public static int BindExecute(CommandContext<ServerCommandSource> context) {
        if (context.getSource().isExecutedByPlayer()) {
            ServerPlayerEntity player = context.getSource().getPlayer();
            ItemStack itemStack = player.getMainHandStack();
            Item item = itemStack.getItem();
            if (item instanceof WirelessTerminalItem) {
                OptionalLong grid = ((WirelessTerminalItem)item).getGridKey(itemStack);
                if (grid.isPresent()) {
                    BindInstance.Bind(context.getArgument(BIND_KEY, String.class),
                            grid.getAsLong());
                    context.getSource().sendFeedback(Text.translatable("chat.chatae2.bind.success"), false);
                    return 1;
                }
            }
            context.getSource().sendFeedback(Text.translatable("chat.chatae2.bind.failed"), false);
            return 0;
        } else {
            context.getSource().sendFeedback(Text.literal("Cannot Execute from console"), false);
        }
        return 0;
    }

}
