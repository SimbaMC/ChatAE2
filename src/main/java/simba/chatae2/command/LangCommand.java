/* SPDX-License-Identifier: AGPL-3.0 WITH SimbaMC Proxy and SimbaMC Exceptions */
package simba.chatae2.command;

import com.mojang.brigadier.context.CommandContext;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import simba.chatae2.config.BindData;

import static simba.chatae2.command.CommandEvent.BIND_KEY;
import static simba.chatae2.command.CommandEvent.BIND_LANG;

public class LangCommand {

    public static int LangExecute(CommandContext<ServerCommandSource> context) {
        String bindKey = context.getArgument(BIND_KEY, String.class);
        if(BindData.BindInstance.Bind_Language.containsKey(bindKey)) {
            BindData.BindInstance.Bind_Language.put(bindKey, context.getArgument(BIND_LANG, String.class));
            context.getSource().sendFeedback(Text.translatable("chat.chatae2.bind.success"), false);
            return 1;
        }
        context.getSource().sendFeedback(Text.translatable("chat.chatae2.bind.failed"), false);
        return 0;
    }

}
