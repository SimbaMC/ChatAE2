/* SPDX-License-Identifier: AGPL-3.0 WITH SimbaMC Proxy and SimbaMC Exceptions */
package simba.chatae2.config;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.StringTag;
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
import java.util.UUID;

public class BindData extends SavedData {

    public Map<String, Tuple3<Tag, UUID, String>> Bind_data;
    public static class Tuple3<TTag, TUUID, TString> {
        public TTag FTag;
        public TUUID FUUID;
        public TString FLang;
        Tuple3(TTag FTag, TUUID FUUID, TString FLang) {
            this.FTag = FTag;
            this.FUUID = FUUID;
            this.FLang = FLang;
        }
    }

    private static final String NBT_BIND_KEY = "BIND";
    private static final String NBT_LANG_KEY = "LANG";
    private static final String NBT_UUID_KEY = "UUID";

    public static BindData BindInstance;

    public BindData() {
        Bind_data = new HashMap<>();
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

    public void Bind(String BindKey, Tag GridKey, UUID playerUUID) {
        Bind_data.put(BindKey, new Tuple3<>(GridKey, playerUUID, ChatAE2.config.getGLOBAL_LANGUAGE()));
        this.setDirty();
    }

    public boolean Unbind(String BindKey) {
        if(this.Bind_data.containsKey(BindKey)) {
            this.Bind_data.remove(BindKey);
            this.setDirty();
            return true;
        } else {
            return false;
        }
    }

    public Optional<Tag> Query(String BindKey) {
        if (this.Bind_data.containsKey(BindKey)) {
            return Optional.of(this.Bind_data.get(BindKey).FTag);
        } else {
            return Optional.empty();
        }
    }

    public String getLangOrDefault(String BindKey) {
        if (this.Bind_data.containsKey(BindKey)) {
            return this.Bind_data.get(BindKey).FLang;
        } else {
            return ChatAE2.config.getGLOBAL_LANGUAGE();
        }
    }

    public static BindData createFromNbt(CompoundTag tag) {
        BindData bindData = new BindData();
        for (String nbtKey : tag.getAllKeys()) {
            CompoundTag keyData = tag.getCompound(nbtKey);
            Tag Bindtag;
            UUID BindUUID;
            String BindLang;
            if (keyData.contains(NBT_BIND_KEY)) {
                Bindtag = keyData.get(NBT_BIND_KEY);
            } else {
                Bindtag = StringTag.valueOf("");
            }
            BindUUID = keyData.getUUID(NBT_UUID_KEY);
            if (tag.contains(NBT_LANG_KEY)) {
                BindLang = keyData.getString(NBT_LANG_KEY);
            } else {
                BindLang = ChatAE2.config.getGLOBAL_LANGUAGE();
            }
            bindData.Bind_data.put(nbtKey, new Tuple3<>(Bindtag, BindUUID, BindLang));
        }
        return bindData;
    }

    @Override @MethodsReturnNonnullByDefault
    public @NotNull CompoundTag save(@NotNull CompoundTag nbt) {
        for(Map.Entry<String, Tuple3<Tag, UUID, String>> entry : Bind_data.entrySet()) {
            CompoundTag keyData = new CompoundTag();
            keyData.put(NBT_BIND_KEY, entry.getValue().FTag);
            keyData.putUUID(NBT_UUID_KEY, entry.getValue().FUUID);
            keyData.putString(NBT_LANG_KEY, entry.getValue().FLang);
            nbt.put(entry.getKey(), keyData);
        }
        return nbt;
    }

}
