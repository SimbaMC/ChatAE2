/* SPDX-License-Identifier: AGPL-3.0 WITH SimbaMC Proxy and SimbaMC Exceptions */
package simba.chatae2.command;

import appeng.api.networking.IGrid;
import appeng.api.stacks.AEKey;
import appeng.api.stacks.KeyCounter;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import simba.chatae2.config.I18n;

import java.util.HashSet;
import java.util.Set;

import static appeng.api.stacks.AmountFormat.SLOT;
import static simba.chatae2.ChatAE2.config;
import static simba.chatae2.command.CommandEvent.*;

public class SearchCommand {
    public static int SearchExecute(CommandContext<CommandSourceStack> context) {
        IGrid grid = getGridFromContext(context);
        String bindKey = context.getArgument(BIND_KEY, String.class);
        String searchKey = context.getArgument(SEARCH_KEY, String.class);
        CommandSourceStack commandSource = context.getSource();
        if (grid == null) return 0;
        KeyCounter cachedInventory = grid.getStorageService().getCachedInventory();
        Set<AEKey> storedKey = cachedInventory.keySet();
        Set<AEKey> craftableKey = grid.getCraftingService().getCraftables(
                what -> I18n.Translate(bindKey, what).contains(searchKey)
        );
        Set<String> storedName = new HashSet<>();
        for (AEKey key : storedKey) {
            String translatedName = I18n.Translate(bindKey, key);
            if(translatedName.contains(searchKey)) {
                if(craftableKey.contains(key)) {
                    storedName.add(
                            String.format(
                                    I18n.Translate(bindKey, "chat.chatae2.search.stored_craftable"),
                                    translatedName,
                                    key.formatAmount(cachedInventory.get(key), SLOT)
                            ));
                    craftableKey.remove(key);
                } else {
                    storedName.add(
                            String.format(
                                    I18n.Translate(bindKey, "chat.chatae2.search.stored"),
                                    translatedName,
                                    key.formatAmount(cachedInventory.get(key), SLOT)
                            )
                    );
                }
            }
        }
        for (AEKey key : craftableKey) {
            String translatedName = I18n.Translate(bindKey, key);
            storedName.add(
                    String.format(
                            I18n.Translate(bindKey, "chat.chatae2.search.craftable"),
                            translatedName
                    )
            );
        }
        int match = 0;
        for (String name: storedName) {
            if( (match ++) >= config.getMAX_SEARCH_KEY() ){
                commandSource.sendSuccess(() -> Component.literal(String.format(
                        I18n.Translate(bindKey, "chat.chatae2.search.toomore"),
                        storedName.size()
                )), false);
                return storedName.size();
            }
            commandSource.sendSuccess(() -> Component.literal(name), false);
        }
        if (match == 0) {
            commandSource.sendSuccess(() -> Component.literal(
                    I18n.Translate(bindKey, "chat.chatae2.search.nothing")
            ), false);
        }
        return match;
    }
}
