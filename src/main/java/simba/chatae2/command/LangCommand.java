/* SPDX-License-Identifier: AGPL-3.0 WITH SimbaMC Proxy and SimbaMC Exceptions */
package simba.chatae2.command;

import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import simba.chatae2.config.BindData;

import java.util.UUID;

import static simba.chatae2.command.CommandEvent.BIND_KEY;
import static simba.chatae2.command.CommandEvent.BIND_LANG;

public class LangCommand {

    public static int LangExecute(CommandContext<CommandSourceStack> context) {
        String bindKey = context.getArgument(BIND_KEY, String.class);
        if(BindData.BindInstance.Bind_data.containsKey(bindKey)) {
            BindData.BindInstance.setLanguage(bindKey, context.getArgument(BIND_LANG, String.class));
            context.getSource().sendSuccess(() -> Component.translatable("chat.chatae2.bind.success"), false);
            return 1;
        }
        context.getSource().sendSuccess(() -> Component.translatable("chat.chatae2.bind.failed"), false);
        return 0;
    }

}
