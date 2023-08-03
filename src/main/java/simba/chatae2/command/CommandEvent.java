/* SPDX-License-Identifier: AGPL-3.0 WITH SimbaMC Proxy and SimbaMC Exceptions */
package simba.chatae2.command;

import appeng.api.networking.IGrid;
import appeng.api.networking.IGridNode;
import appeng.api.networking.security.IActionHost;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.server.command.ServerCommandSource;
import org.jetbrains.annotations.Nullable;

import java.util.OptionalLong;

import static appeng.api.features.Locatables.securityStations;
import static net.minecraft.server.command.CommandManager.literal;
import static net.minecraft.server.command.CommandManager.argument;
import static simba.chatae2.config.BindData.BindInstance;

public class CommandEvent {

    public static final String BIND_KEY = "bindKey";
    public static final String BIND_LANG = "bindLang";
    public static final String SEARCH_KEY = "searchKey";
    public static void InitialCommand(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(
            literal("chatae2")
                .then(argument( BIND_KEY,
                    StringArgumentType.string())
                    .then(literal("query")
                        .executes(QueryCommand::QueryExecute)
                        .then(literal("cpu")
                                .executes(QueryCommand::QueryCPUExecute)
                        )
                    )
                    .then(literal("bind")
                        .executes(BindCommand::BindExecute)
                    )
                    .then(literal("unbind").requires(source -> source.hasPermissionLevel(2))
                            .executes(UnbindCommand::UnbindExecute)
                    )
                    .then(literal("lang")
                            .then(argument( BIND_LANG,
                                    StringArgumentType.string())
                            .executes(LangCommand::LangExecute)
                            )
                    )
                    .then(literal("search")
                            .then(argument( SEARCH_KEY,
                                    StringArgumentType.string())
                            .executes(SearchCommand::SearchExecute)
                            )
                    )
                )
                .then(literal("list").requires(source -> source.hasPermissionLevel(2))
                    .executes(ListCommand::ListExecute)
                )
        );
    }

    static @Nullable IGrid getGridFromContext(CommandContext<ServerCommandSource> context) {
        OptionalLong GridKey = BindInstance.Query(context.getArgument(BIND_KEY, String.class));
        if(GridKey.isPresent()) {
            long key = GridKey.getAsLong();
            IActionHost securityStation = securityStations().get(context.getSource().getWorld(), key);
            if (securityStation != null) {
                IGridNode actionableNode = securityStation.getActionableNode();
                if (actionableNode != null) {
                    return actionableNode.getGrid();
                }
            }
        }
        return null;
    }

}

