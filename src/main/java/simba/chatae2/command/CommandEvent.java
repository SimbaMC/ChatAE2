/* SPDX-License-Identifier: AGPL-3.0 WITH SimbaMC Proxy and SimbaMC Exceptions */
package simba.chatae2.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.server.command.ServerCommandSource;
import static net.minecraft.server.command.CommandManager.literal;
import static net.minecraft.server.command.CommandManager.argument;

public class CommandEvent {

    public static final String BIND_KEY = "bindKey";
    public static void InitialCommand(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(
            literal("chatae2")
                .then(argument( BIND_KEY,
                        StringArgumentType.string())
                    .then( literal("query")
                            .executes(QueryCommand::QueryExecute)
                            .then(literal("cpu")
                                    .executes(QueryCommand::QueryCPUExecute)
                            )
                    )
                    .then( literal("bind")
                            .executes(BindCommand::BindExecute))
                    )
        );
    }
}

