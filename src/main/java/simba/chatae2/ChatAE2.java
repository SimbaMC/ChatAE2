/* SPDX-License-Identifier: AGPL-3.0 WITH SimbaMC Proxy and SimbaMC Exceptions */
package simba.chatae2;

import com.mojang.authlib.GameProfile;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import simba.chatae2.command.CommandEvent;
import simba.chatae2.config.BindData;
import simba.chatae2.config.Config;
import simba.chatae2.config.I18n;

import java.util.UUID;

@Mod("chatae2")
public class ChatAE2 {

	public static final String MODID = "CHAT_AE2";
	public static final Logger LOGGER = LoggerFactory.getLogger(MODID);

	public static Config config;

	// Copied from AE2 and modified the last UUID number
	public static final GameProfile PROFILE = new GameProfile(UUID.fromString("60C173A5-E1E6-4B87-85B1-272CE424521E"),
			"[ChatAE2]");
	public ChatAE2() {
		MinecraftForge.EVENT_BUS.addListener(this::onServerStarting);
		MinecraftForge.EVENT_BUS.addListener(this::onRegisterCommands);
	}

	public void onServerStarting(ServerStartedEvent event) {
		// This method will be called when the server is starting
		config = Config.onStart();
		I18n.Initial();
		BindData.BindInstance = BindData.getServerState(event.getServer());
	}

	public void onRegisterCommands(RegisterCommandsEvent event) {
		// This method will be called when commands are being registered
		CommandEvent.InitialCommand(event.getDispatcher());
	}
}
