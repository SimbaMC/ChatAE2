/* SPDX-License-Identifier: AGPL-3.0 WITH SimbaMC Proxy and SimbaMC Exceptions */
package simba.chatae2.util;

import appeng.api.config.Actionable;
import appeng.api.networking.IGrid;
import appeng.api.networking.IGridNode;
import appeng.api.networking.crafting.ICraftingLink;
import appeng.api.networking.crafting.ICraftingRequester;
import appeng.api.stacks.AEKey;
import appeng.api.storage.StorageHelper;
import appeng.me.helpers.MachineSource;
import com.google.common.collect.ImmutableSet;


public class CraftRequester implements ICraftingRequester {

    ICraftingLink craftingLink;
    final IGrid grid;

    public CraftRequester(IGrid grid) {
        this.craftingLink = null;
        this.grid = grid;
    }

    public void link(ICraftingLink craftingLink) {
        this.craftingLink = craftingLink;
    }

    @Override
    public ImmutableSet<ICraftingLink> getRequestedJobs() {
        return ImmutableSet.of(craftingLink);
    }

    @Override
    public long insertCraftedItems(ICraftingLink link, AEKey what, long amount, Actionable mode) {
        if (link != craftingLink) return 0;
        if (mode != Actionable.SIMULATE) {
            if (grid == null) return 0;
            return StorageHelper.poweredInsert(
                    grid.getEnergyService(),
                    grid.getStorageService().getInventory(),
                    what,
                    amount,
                    new MachineSource(this),
                    Actionable.MODULATE
            );
        }
        return amount;
    }

    @Override
    public void jobStateChange(ICraftingLink link) {
        craftingLink = null;
    }

    @Override
    public IGridNode getActionableNode() {
        return grid.getPivot();
    }

    public boolean alive() {
        return !(
                craftingLink == null || craftingLink.isCanceled() || craftingLink.isDone()
        );
    }
}
