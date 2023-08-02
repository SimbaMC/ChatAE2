/* SPDX-License-Identifier: AGPL-3.0 WITH SimbaMC Proxy and SimbaMC Exceptions */
package simba.chatae2.command;

import com.mojang.brigadier.context.CommandContext;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

import static simba.chatae2.command.CommandEvent.BIND_KEY;
import static simba.chatae2.config.BindData.BindInstance;

public class UnbindCommand {
    public static int UnbindExecute(CommandContext<ServerCommandSource> context) {
        if (BindInstance.Unbind(context.getArgument(BIND_KEY, String.class))) {
            context.getSource().sendFeedback(Text.translatable("chat.chatae2.unbind.success"), false);
            return 1;
        } else {
            context.getSource().sendFeedback(Text.translatable("chat.chatae2.unbind.failed"), false);
            return 0;
        }
    }
}
