/* SPDX-License-Identifier: AGPL-3.0 WITH SimbaMC Proxy and SimbaMC Exceptions */
package simba.chatae2.config;

import appeng.api.stacks.AEKey;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import net.fabricmc.loader.api.FabricLoader;
import simba.chatae2.ChatAE2;
import simba.chatae2.mixinInterface.TranslatableTextContentInterface;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

public class I18n {

    public Map<String, String> translation;

    public static Map<String, I18n> I18N_INSTANCE;
    public static void Initial() {
        File LanguagePath = FabricLoader.getInstance().getConfigDir().resolve("chatae2/lang").toFile();
        I18N_INSTANCE = new HashMap<String, I18n>();
        FilenameFilter jsonFilter = (dir, name) -> {
            String lowercaseName = name.toLowerCase();
            return (lowercaseName.endsWith(".json"));
        };
        try {
            File[] filesList = LanguagePath.listFiles(jsonFilter);
            for (File langFile: filesList) {
                BufferedReader reader =  new BufferedReader(new FileReader(langFile, StandardCharsets.UTF_8));
                Gson gson = new Gson();
                TypeToken<Map<String, String>> typeToken = new TypeToken<Map<String, String>>() {};
                I18n i18n = new I18n();
                i18n.translation = gson.fromJson(reader, typeToken.getType());
                reader.close();
                I18N_INSTANCE.put(langFile.getName().toLowerCase().replace(".json" ,""), i18n);
            }
        } catch (IOException e) {
            StringWriter stringWriter = new StringWriter();
            PrintWriter printWriter = new PrintWriter(stringWriter);
            e.printStackTrace(printWriter);
            String stackTraceString = stringWriter.toString();
            ChatAE2.LOGGER.info(stackTraceString);
        }
    }

    public static String Translate(String BindKey, AEKey aeKey) {
        String lang = BindData.BindInstance.getLangOrDefault(BindKey);
        if (I18N_INSTANCE.containsKey(lang)) {
            String key = ((TranslatableTextContentInterface) (aeKey.getDisplayName().getContent())).getKey();
            if (I18N_INSTANCE.get(lang).translation.containsKey(key)) {
                return I18N_INSTANCE.get(lang).translation.get(key);
            }
        }
        return aeKey.getDisplayName().getString();
    }

    public static String Translate(String BindKey, String key) {
        String lang = BindData.BindInstance.getLangOrDefault(BindKey);
        if (I18N_INSTANCE.containsKey(lang)) {
            if (I18N_INSTANCE.get(lang).translation.containsKey(key)) {
                return I18N_INSTANCE.get(lang).translation.get(key);
            }
        }
        return key;
    }

    public static String readableSize(long size) {
        if(size <= 0) return "0";
        final String[] units = new String[] { "", "k", "M", "G", "T", "E" };
        int digitGroups = (int) (Math.log10(size)/Math.log10(1024));
        return new DecimalFormat("###0.#").format(size/Math.pow(1024, digitGroups)) + units[digitGroups];
    }

}
