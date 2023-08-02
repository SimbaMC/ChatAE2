/* SPDX-License-Identifier: AGPL-3.0 WITH SimbaMC Proxy and SimbaMC Exceptions */
package simba.chatae2.command;

import com.mojang.brigadier.context.CommandContext;
import net.minecraft.server.command.ServerCommandSource;

import java.util.Map;

import static net.minecraft.text.Text.literal;
import static simba.chatae2.config.BindData.BindInstance;

public class ListCommand {
    public static int ListExecute(CommandContext<ServerCommandSource> context) {
        for(Map.Entry<String, Long> entry : BindInstance.Binding.entrySet()) {
            context.getSource().sendFeedback(literal(
                    entry.getKey() + " " +
                    entry.getValue() + " " +
                    BindInstance.Bind_Language.get(entry.getKey())), false);
        }
        return 0;
    }

}
