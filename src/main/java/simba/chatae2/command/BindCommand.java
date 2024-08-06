/* SPDX-License-Identifier: AGPL-3.0 WITH SimbaMC Proxy and SimbaMC Exceptions */
package simba.chatae2.command;

import appeng.items.tools.powered.WirelessTerminalItem;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import simba.chatae2.config.I18n;

import static simba.chatae2.command.CommandEvent.BIND_KEY;
import static simba.chatae2.command.CommandEvent.TAG_ACCESS_POINT_POS;
import static simba.chatae2.config.BindData.BindInstance;

public class BindCommand {

    public static int BindExecute(CommandContext<CommandSourceStack> context) {
        if (context.getSource().isPlayer()) {
            ServerPlayer player = context.getSource().getPlayer();
            ItemStack itemStack = player.getMainHandItem();
            Item item = itemStack.getItem();
            if (item instanceof WirelessTerminalItem) {
                String bindKey = context.getArgument(BIND_KEY, String.class);
                BindInstance.Bind(bindKey, itemStack.getOrCreateTag().get(TAG_ACCESS_POINT_POS), context.getSource().getPlayer().getUUID());
                context.getSource().sendSuccess(() -> Component.literal(I18n.Translate(bindKey,"chat.chatae2.bind.success")), false);
                return 1;
            }
            context.getSource().sendSuccess(() -> Component.literal(I18n.Translate("","chat.chatae2.bind.failed")), false);
            return 0;
        } else {
            context.getSource().sendSuccess(() -> Component.literal("Cannot Execute from console"), false);
        }
        return 0;
    }

}
