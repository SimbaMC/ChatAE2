/* SPDX-License-Identifier: AGPL-3.0 WITH SimbaMC Proxy and SimbaMC Exceptions */
package simba.chatae2.command;

import appeng.api.networking.IGrid;
import appeng.items.tools.powered.WirelessTerminalItem;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

import static appeng.api.ids.AEItemIds.WIRELESS_TERMINAL;
import static net.minecraft.commands.Commands.argument;
import static net.minecraft.commands.Commands.literal;
import static simba.chatae2.config.BindData.BindInstance;

public class CommandEvent {

    public static final String BIND_KEY = "bindKey";
    public static final String BIND_LANG = "bindLang";
    public static final String SEARCH_KEY = "searchKey";
    public static final String CRAFT_NUM = "craftNum";
    public static final String CRAFT_KEY = "searchKey";

    static final String TAG_ACCESS_POINT_POS = "accessPoint";

    public static void InitialCommand(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
            literal("chatae2")
                .then(argument( BIND_KEY,
                    StringArgumentType.string())
                    .then(literal("query").requires(source -> source.hasPermission(2))
                        .executes(QueryCommand::QueryExecute)
                        .then(literal("cpu")
                                .executes(QueryCommand::QueryCPUExecute)
                        )
                    )
                    .then(literal("bind")
                        .executes(BindCommand::BindExecute)
                    )
                    .then(literal("unbind").requires(source -> source.hasPermission(2))
                            .executes(UnbindCommand::UnbindExecute)
                    )
                    .then(literal("lang")
                            .then(argument( BIND_LANG,
                                    StringArgumentType.string())
                            .executes(LangCommand::LangExecute)
                            )
                    )
                    .then(literal("search").requires(source -> source.hasPermission(2))
                            .then(argument( SEARCH_KEY,
                                    StringArgumentType.greedyString())
                            .executes(SearchCommand::SearchExecute)
                            )
                    )
                    .then(literal("craft").requires(source -> source.hasPermission(2))
                            .then(argument( CRAFT_NUM,
                                    IntegerArgumentType.integer(1))
                                .then(argument( CRAFT_KEY,
                                        StringArgumentType.greedyString())
                                        .executes(CraftCommand::CraftExecute)
                                )
                            )
                    )
                )
                .then(literal("list").requires(source -> source.hasPermission(2))
                    .executes(ListCommand::ListExecute)
                )
        );
    }

    static @Nullable IGrid getGridFromContext(CommandContext<CommandSourceStack> context) {
        Optional<Tag> GridKey = BindInstance.Query(context.getArgument(BIND_KEY, String.class));
        if(GridKey.isPresent()) {
            WirelessTerminalItem virtualItem = (WirelessTerminalItem) ForgeRegistries.ITEMS.getValue(WIRELESS_TERMINAL);
            assert virtualItem != null;
            ItemStack virtualItemStack = new ItemStack(virtualItem);
            virtualItemStack.getOrCreateTag().put(TAG_ACCESS_POINT_POS, GridKey.get());
            return virtualItem.getLinkedGrid(virtualItemStack, context.getSource().getLevel(), null);
        }
        return null;
    }

}

