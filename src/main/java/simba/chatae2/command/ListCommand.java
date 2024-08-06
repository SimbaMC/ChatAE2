/* SPDX-License-Identifier: AGPL-3.0 WITH SimbaMC Proxy and SimbaMC Exceptions */
package simba.chatae2.command;

import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import simba.chatae2.config.BindData;

import java.util.Map;
import java.util.UUID;

import static simba.chatae2.config.BindData.BindInstance;

public class ListCommand {
    public static int ListExecute(CommandContext<CommandSourceStack> context) {
        for(Map.Entry<String, BindData.Tuple3<Tag, UUID, String>> entry : BindInstance.Bind_data.entrySet()) {
            context.getSource().sendSuccess(() -> Component.literal(
                    entry.getKey() + " " +
                            entry.getValue().FTag + " " +
                            entry.getValue().FUUID + " " +
                            entry.getValue().FLang
            ), false);
        }
        return 0;
    }

}
