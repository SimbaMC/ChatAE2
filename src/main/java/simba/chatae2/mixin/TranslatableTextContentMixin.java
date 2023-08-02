/* SPDX-License-Identifier: AGPL-3.0 WITH SimbaMC Proxy and SimbaMC Exceptions */
package simba.chatae2.mixin;

import net.minecraft.text.TranslatableTextContent;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import simba.chatae2.mixinInterface.TranslatableTextContentInterface;

@Mixin(TranslatableTextContent.class)
public abstract class TranslatableTextContentMixin implements TranslatableTextContentInterface {

    @Shadow @Final
    private String key;
    public String getKey() {
        return key;
    }
}
