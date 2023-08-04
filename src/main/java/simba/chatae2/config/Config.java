/* SPDX-License-Identifier: AGPL-3.0 WITH SimbaMC Proxy and SimbaMC Exceptions */
package simba.chatae2.config;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;
import simba.chatae2.ChatAE2;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class Config {

    public String getGLOBAL_LANGUAGE() {
        return GLOBAL_LANGUAGE;
    }

    public void setGLOBAL_LANGUAGE(String GLOBAL_LANGUAGE) {
        this.GLOBAL_LANGUAGE = GLOBAL_LANGUAGE;
    }

    public String GLOBAL_LANGUAGE = "en_us";

    public int getMAX_SEARCH_KEY() {
        return MAX_SEARCH_KEY;
    }

    public void setMAX_SEARCH_KEY(int MAX_SEARCH_KEY) {
        this.MAX_SEARCH_KEY = MAX_SEARCH_KEY;
    }

    public int MAX_SEARCH_KEY = 10;

    public static Config onStart() {
        Path LanguagePath = FabricLoader.getInstance().getConfigDir().resolve("chatae2/chatae2.json");
        try {
            Files.createDirectories(LanguagePath.getParent());
            BufferedReader reader = Files.newBufferedReader(LanguagePath, StandardCharsets.UTF_8);
            Gson gson = new Gson();
            Config config = gson.fromJson(reader, Config.class);
            reader.close();
            return config;
        } catch (IOException e) {
            StringWriter stringWriter = new StringWriter();
            PrintWriter printWriter = new PrintWriter(stringWriter);
            e.printStackTrace(printWriter);
            String stackTraceString = stringWriter.toString();
            ChatAE2.LOGGER.warn(stackTraceString);
            Config config = new Config();
            config.save();
            return config;
        }
    }

    public void save() {
        Path LanguagePath = FabricLoader.getInstance().getConfigDir().resolve("chatae2/chatae2.json");
        try {
            Files.createDirectories(LanguagePath.getParent());
            BufferedWriter writer = Files.newBufferedWriter(LanguagePath, StandardCharsets.UTF_8);
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            gson.toJson(this, writer);
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
