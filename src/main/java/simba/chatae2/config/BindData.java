/* SPDX-License-Identifier: AGPL-3.0 WITH SimbaMC Proxy and SimbaMC Exceptions */
package simba.chatae2.config;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.storage.DimensionDataStorage;
import org.jetbrains.annotations.NotNull;
import simba.chatae2.ChatAE2;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class BindData extends SavedData {

    public Map<String, Tag> Binding;
    public Map<String, String> Bind_Language;

    private static final String NBT_BIND_KEY = "BIND";
    private static final String NBT_LANG_KEY = "LANG";

    public static BindData BindInstance;

    public BindData() {
        Binding = new HashMap<String, Tag>();
        Bind_Language = new HashMap<String, String>();
    }

    public static BindData getServerState(MinecraftServer server) {
        // First we get the persistentStateManager for the OVERWORLD
        DimensionDataStorage persistentStateManager = server
                .getLevel(Level.OVERWORLD).getDataStorage();

        // Calling this reads the file from the disk if it exists, or creates a new one and saves it to the disk
        return persistentStateManager.computeIfAbsent(
                BindData::createFromNbt,
                BindData::new,
                ChatAE2.MODID);
    }

    public void Bind(String BindKey, Tag GridKey) {
        this.Binding.put(BindKey, GridKey);
        if(!this.Bind_Language.containsKey(BindKey)) {
            this.Bind_Language.put(BindKey, ChatAE2.config.getGLOBAL_LANGUAGE());
        }
        this.setDirty();
    }

    public boolean Unbind(String BindKey) {
        if(this.Binding.containsKey(BindKey) || this.Bind_Language.containsKey(BindKey)) {
            this.Binding.remove(BindKey);
            this.Bind_Language.remove(BindKey);
            this.setDirty();
            return true;
        } else {
            return false;
        }
    }

    public Optional<Tag> Query(String BindKey) {
        if (this.Binding.containsKey(BindKey)) {
            return Optional.of(this.Binding.get(BindKey));
        } else {
            return Optional.empty();
        }
    }

    public String getLangOrDefault(String BindKey) {
        if (this.Bind_Language.containsKey(BindKey)) {
            return this.Bind_Language.get(BindKey);
        } else {
            return ChatAE2.config.getGLOBAL_LANGUAGE();
        }
    }

    public static BindData createFromNbt(CompoundTag tag) {
        BindData bindData = new BindData();
        if (tag.contains(NBT_BIND_KEY)) {
            CompoundTag Bind = tag.getCompound(NBT_BIND_KEY);
            for (String nbtKey : Bind.getAllKeys()) {
                bindData.Binding.put(nbtKey, Bind.get(nbtKey));
            }
        }
        if (tag.contains(NBT_LANG_KEY)) {
            CompoundTag Lang = tag.getCompound(NBT_LANG_KEY);
            for (String nbtKey : Lang.getAllKeys()) {
                bindData.Bind_Language.put(nbtKey, Lang.getString(nbtKey));
            }
        }
        return bindData;
    }

    @Override @MethodsReturnNonnullByDefault
    public CompoundTag save(@NotNull CompoundTag nbt) {
        CompoundTag Bind = new CompoundTag();
        for(Map.Entry<String, Tag> entry : Binding.entrySet()) {
            Bind.put(entry.getKey(), entry.getValue());
        }
        CompoundTag Lang = new CompoundTag();
        for(Map.Entry<String, String> entry : Bind_Language.entrySet()) {
            Lang.putString(entry.getKey(), entry.getValue());
        }
        nbt.put(NBT_BIND_KEY, Bind);
        nbt.put(NBT_LANG_KEY, Lang);
        return nbt;
    }
}
