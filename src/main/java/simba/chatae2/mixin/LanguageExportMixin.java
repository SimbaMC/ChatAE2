/* SPDX-License-Identifier: AGPL-3.0 WITH SimbaMC Proxy and SimbaMC Exceptions */
package simba.chatae2.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.locale.Language;
import net.minecraftforge.fml.loading.FMLPaths;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import com.google.gson.Gson;
import simba.chatae2.ChatAE2;
import simba.chatae2.mixinInterface.TranslationStorageInterface;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

@Mixin(value = Language.class, priority = 800)
public class LanguageExportMixin {

    @Inject(method = "inject", at = @At("HEAD"))
    private static void copyInstance(Language language, CallbackInfo ci) {
        Map<String, String> translation = ((TranslationStorageInterface)language).getStorage();
        String currentLanguageCode = Minecraft.getInstance().getLanguageManager().getSelected();
        Path LanguagePath = FMLPaths.CONFIGDIR.get().resolve("chatae2/lang/" + currentLanguageCode + ".json");
        try {
            Files.createDirectories(LanguagePath.getParent());
            BufferedWriter writer = Files.newBufferedWriter(LanguagePath, StandardCharsets.UTF_8);
            Gson gson = new Gson();
            gson.toJson(translation, writer);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            StringWriter stringWriter = new StringWriter();
            PrintWriter printWriter = new PrintWriter(stringWriter);
            e.printStackTrace(printWriter);
            String stackTraceString = stringWriter.toString();
            ChatAE2.LOGGER.warn(stackTraceString);
        }
    }

}
