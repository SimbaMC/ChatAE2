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
    public Map<String, String> Bind_Language;

    private static final String NBT_BIND_KEY = "BIND";
    private static final String NBT_LANG_KEY = "LANG";

    public static BindData BindInstance;

    public BindData() {
        Binding = new HashMap<String, Long>();
        Bind_Language = new HashMap<String, String>();
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
        if(!this.Bind_Language.containsKey(BindKey)) {
            this.Bind_Language.put(BindKey, ChatAE2.config.getGLOBAL_LANGUAGE());
        }
        this.markDirty();
    }

    public boolean Unbind(String BindKey) {
        if(this.Binding.containsKey(BindKey) || this.Bind_Language.containsKey(BindKey)) {
            this.Binding.remove(BindKey);
            this.Bind_Language.remove(BindKey);
            this.markDirty();
            return true;
        } else {
            return false;
        }
    }

    public OptionalLong Query(String BindKey) {
        if (this.Binding.containsKey(BindKey)) {
            return OptionalLong.of(this.Binding.get(BindKey));
        } else {
            return OptionalLong.empty();
        }
    }

    public String getLangOrDefault(String BindKey) {
        if (this.Bind_Language.containsKey(BindKey)) {
            return this.Bind_Language.get(BindKey);
        } else {
            return ChatAE2.config.getGLOBAL_LANGUAGE();
        }
    }

    public static BindData createFromNbt(NbtCompound tag) {
        BindData bindData = new BindData();
        if (tag.contains(NBT_BIND_KEY)) {
            NbtCompound Bind = tag.getCompound(NBT_BIND_KEY);
            for (String nbtKey : Bind.getKeys()) {
                bindData.Binding.put(nbtKey, Bind.getLong(nbtKey));
            }
        }
        if (tag.contains(NBT_LANG_KEY)) {
            NbtCompound Lang = tag.getCompound(NBT_LANG_KEY);
            for (String nbtKey : Lang.getKeys()) {
                bindData.Bind_Language.put(nbtKey, Lang.getString(nbtKey));
            }
        }
        return bindData;
    }

    @Override
    public NbtCompound writeNbt(NbtCompound nbt) {
        NbtCompound Bind = new NbtCompound();
        for(Map.Entry<String, Long> entry : Binding.entrySet()) {
            Bind.putLong(entry.getKey(), entry.getValue());
        }
        NbtCompound Lang = new NbtCompound();
        for(Map.Entry<String, String> entry : Bind_Language.entrySet()) {
            Lang.putString(entry.getKey(), entry.getValue());
        }
        nbt.put(NBT_BIND_KEY, Bind);
        nbt.put(NBT_LANG_KEY, Lang);
        return nbt;
    }
}
