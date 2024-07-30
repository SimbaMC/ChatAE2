/* SPDX-License-Identifier: AGPL-3.0 WITH SimbaMC Proxy and SimbaMC Exceptions */
package simba.chatae2.mixin;

import net.minecraft.client.resources.language.ClientLanguage;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import simba.chatae2.mixinInterface.TranslationStorageInterface;

import java.util.Map;

@Mixin(ClientLanguage.class)
public abstract class TranslationStorageMixin implements TranslationStorageInterface {
    @Shadow @Final
    Map<String, String> storage;
    public Map<String, String> getStorage(){
        return storage;
    }
}
