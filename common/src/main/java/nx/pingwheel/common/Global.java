package nx.pingwheel.common;

import nx.pingwheel.common.config.ConfigHandler;
import nx.pingwheel.common.config.ServerConfig;
import nx.pingwheel.common.networking.INetworkHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.message.FormattedMessageFactory;
import org.apache.logging.log4j.message.Message;

public class Global {
	private Global() {}

	public static String ModVersion = null;
	public static ConfigHandler<ServerConfig> ServerConfigHandler = null;
	public static INetworkHandler NetHandler = null;

	public static final String MOD_ID = "ping-wheel";
	public static final Logger LOGGER = LogManager.getLogger(MOD_ID,
		new FormattedMessageFactory() {
			@Override
			public Message newMessage(String message) {
				return super.newMessage("[Ping-Wheel] " + message);
			}
		});
}
