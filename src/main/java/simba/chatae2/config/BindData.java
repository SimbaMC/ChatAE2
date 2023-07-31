/* SPDX-License-Identifier: AGPL-3.0 WITH SimbaMC Proxy and SimbaMC Exceptions */
package simba.chatae2.config;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.PersistentState;
import net.minecraft.world.PersistentStateManager;
import net.minecraft.world.World;
import simba.chatae2.ChatAE2;

import java.util.HashMap;
import java.util.Map;
import java.util.OptionalLong;

public class BindData extends PersistentState {

    public Map<String, Long> Binding;

    public static BindData BindInstance;

    public BindData() {
        Binding = new HashMap<String, Long>();
    }

    public static BindData getServerState(MinecraftServer server) {
        // First we get the persistentStateManager for the OVERWORLD
        PersistentStateManager persistentStateManager = server
                .getWorld(World.OVERWORLD).getPersistentStateManager();

        // Calling this reads the file from the disk if it exists, or creates a new one and saves it to the disk
        return persistentStateManager.getOrCreate(
                BindData::createFromNbt,
                BindData::new,
                ChatAE2.MODID);
    }

    public void Bind(String BindKey, Long GridKey) {
        this.Binding.put(BindKey, GridKey);
        this.markDirty();
    }

    public OptionalLong Query(String BindKey) {
        if (this.Binding.containsKey(BindKey)) {
            return OptionalLong.of(this.Binding.get(BindKey));
        } else {
            return OptionalLong.empty();
        }
    }

    public static BindData createFromNbt(NbtCompound tag) {
        BindData bindData = new BindData();
        for (String nbtKey : tag.getKeys()) {
            bindData.Binding.put(nbtKey, tag.getLong(nbtKey));
        }
        return bindData;
    }

    @Override
    public NbtCompound writeNbt(NbtCompound nbt) {
        for(Map.Entry<String, Long> entry : Binding.entrySet()) {
            nbt.putLong(entry.getKey(), entry.getValue());
        }
        return nbt;
    }
}
