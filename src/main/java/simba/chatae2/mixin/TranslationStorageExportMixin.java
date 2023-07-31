/* SPDX-License-Identifier: AGPL-3.0 WITH SimbaMC Proxy and SimbaMC Exceptions */
package simba.chatae2.mixin;

import net.minecraft.client.resource.language.TranslationStorage;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import simba.chatae2.TranslationStorageInterface;

import java.util.Map;

@Mixin(TranslationStorage.class)
public abstract class TranslationStorageExportMixin implements TranslationStorageInterface {
    @Shadow @Final
    Map<String, String> translations;
    public Map<String, String> getStorage(){
        return translations;
    }
}
