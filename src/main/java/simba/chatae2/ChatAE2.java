/* SPDX-License-Identifier: AGPL-3.0 WITH SimbaMC Proxy and SimbaMC Exceptions */
package simba.chatae2;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.server.MinecraftServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import simba.chatae2.command.CommandEvent;
import simba.chatae2.config.BindData;
import simba.chatae2.config.Config;

public class ChatAE2 implements ModInitializer {
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final String MODID = "CHAT_AE2";
	public static final Logger LOGGER = LoggerFactory.getLogger(MODID);

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		// LOGGER.info("Hello Fabric world!");
		Config.onStart();
		ServerLifecycleEvents.SERVER_STOPPING.register(this::onServerStopping);

		ServerLifecycleEvents.SERVER_STARTED.register( server -> {
			// You can see we use the function getServer() that's on the player.
			BindData.BindInstance = BindData.getServerState(server);
		});

		CommandRegistrationCallback.EVENT.register(
				(dispatcher, registryAccess, environment) -> CommandEvent.InitialCommand(dispatcher));
	}

	public void onServerStopping(MinecraftServer server) {
		Config.onStop();
	}
}
