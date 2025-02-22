package nx.pingwheel.common.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.MutableComponent;
import nx.pingwheel.common.compat.Component;
import nx.pingwheel.common.helper.LanguageUtils;
import nx.pingwheel.common.screen.SettingsScreen;
import org.apache.logging.log4j.util.TriConsumer;

import java.util.function.Function;

import static nx.pingwheel.common.ClientGlobal.ConfigHandler;
import static nx.pingwheel.common.ClientGlobal.Game;
import static nx.pingwheel.common.config.ClientConfig.MAX_CHANNEL_LENGTH;

public class ClientCommandBuilder {
	private ClientCommandBuilder() {}

	public static <S> LiteralArgumentBuilder<S> build(TriConsumer<CommandContext<S>, Boolean, MutableComponent> responseHandler) {
		var langConfig = LanguageUtils.command("config");
		var langChannel = LanguageUtils.command("channel");

		Function<String, MutableComponent> formatChannel = (channel) -> {
			if (channel.isEmpty()) {
				return langChannel.path("default").get().withStyle(ChatFormatting.YELLOW);
			}

			return LanguageUtils.wrapped(Component.literal(channel).withStyle(ChatFormatting.GOLD), "\"");
		};

		var cmdChannel = LiteralArgumentBuilder.<S>literal("channel")
			.executes((context) -> {
				var currentChannel = ConfigHandler.getConfig().getChannel();

				responseHandler.accept(context, true, langChannel.path("get.response")
					.get(formatChannel.apply(currentChannel)));
				return 1;
			})
			.then(RequiredArgumentBuilder.<S, String>argument("channel_name", StringArgumentType.string()).executes((context) -> {
				var newChannel = context.getArgument("channel_name", String.class);

				if (newChannel.length() > MAX_CHANNEL_LENGTH) {
					responseHandler.accept(context, false, langChannel.path("set.reject").get(MAX_CHANNEL_LENGTH));
					return 0;
				}

				ConfigHandler.getConfig().setChannel(newChannel);
				ConfigHandler.save();

				responseHandler.accept(context, true, langChannel.path("set.response")
					.get(formatChannel.apply(newChannel)));
				return 1;
			}));

		var cmdConfig = LiteralArgumentBuilder.<S>literal("config")
			.executes((context) -> {
				Game.tell(() -> Game.setScreen(new SettingsScreen()));
				return 1;
			});

		Command<S> helpCallback = (context) -> {
			responseHandler.accept(context, true, LanguageUtils.join(
				Component.literal("/pingwheel config"),
				LanguageUtils.wrapped(langConfig.path("description").get()).withStyle(ChatFormatting.GRAY),
				Component.literal("/pingwheel channel"),
				LanguageUtils.wrapped(langChannel.path("get.description").get()).withStyle(ChatFormatting.GRAY),
				Component.literal("/pingwheel channel <channel_name>"),
				LanguageUtils.wrapped(langChannel.path("set.description").get()).withStyle(ChatFormatting.GRAY)
			));
			return 1;
		};

		var cmdHelp = LiteralArgumentBuilder.<S>literal("help")
			.executes(helpCallback);

		return LiteralArgumentBuilder.<S>literal("pingwheel")
			.executes(helpCallback)
			.then(cmdHelp)
			.then(cmdConfig)
			.then(cmdChannel);
	}
}
