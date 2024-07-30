/* SPDX-License-Identifier: AGPL-3.0 WITH SimbaMC Proxy and SimbaMC Exceptions */
package simba.chatae2.command;

import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;

import java.util.Map;

import static simba.chatae2.config.BindData.BindInstance;

public class ListCommand {
    public static int ListExecute(CommandContext<CommandSourceStack> context) {
        for(Map.Entry<String, Tag> entry : BindInstance.Binding.entrySet()) {
            context.getSource().sendSuccess(() -> Component.literal(
                    entry.getKey() + " " +
                    entry.getValue() + " " +
                    BindInstance.Bind_Language.get(entry.getKey())), false);
        }
        return 0;
    }

}
