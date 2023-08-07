/* SPDX-License-Identifier: AGPL-3.0 WITH SimbaMC Proxy and SimbaMC Exceptions */
package simba.chatae2;

import com.mojang.authlib.GameProfile;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.server.MinecraftServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import simba.chatae2.command.CommandEvent;
import simba.chatae2.config.BindData;
import simba.chatae2.config.Config;
import simba.chatae2.config.I18n;

import java.util.UUID;

public class ChatAE2 implements ModInitializer {

	public static final String MODID = "CHAT_AE2";
	public static final Logger LOGGER = LoggerFactory.getLogger(MODID);

	public static Config config;

	// Copied from AE2 and modified the last UUID number
	public static final GameProfile PROFILE = new GameProfile(UUID.fromString("60C173A5-E1E6-4B87-85B1-272CE424521E"),
			"[ChatAE2]");
	@Override
	public void onInitialize() {
		config = Config.onStart();

		ServerLifecycleEvents.SERVER_STOPPING.register(this::onServerStopping);

		ServerLifecycleEvents.SERVER_STARTED.register( server -> {
			I18n.Initial();
			BindData.BindInstance = BindData.getServerState(server);
		});

		CommandRegistrationCallback.EVENT.register(
				(dispatcher, registryAccess, environment) -> CommandEvent.InitialCommand(dispatcher));
	}

	public void onServerStopping(MinecraftServer server) {
	}
}
