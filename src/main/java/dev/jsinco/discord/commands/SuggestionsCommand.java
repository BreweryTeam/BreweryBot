package dev.jsinco.discord.commands;

import dev.jsinco.discord.Util;
import dev.jsinco.discord.configuration.Config;
import dev.jsinco.discord.framework.commands.CommandModule;
import dev.jsinco.discord.framework.commands.DiscordCommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.List;

@DiscordCommand(name = "suggestions", description = "Make a suggestion")
public class SuggestionsCommand implements CommandModule {
    @Override
    public void execute(SlashCommandInteractionEvent event) {
        String txt = Util.getOption(event.getOption("suggestion"), OptionType.STRING);
        TextChannel channel = event.getJDA().getTextChannelById(Config.getInstance().getSuggestionChannelId());
        User user = event.getUser();

        createSuggestion(channel, txt, user);
    }

    @Override
    public List<OptionData> getOptions() {
        return List.of(new OptionData(OptionType.STRING, "suggestion", "The suggestion you want to make").setRequired(true));
    }

    private void createSuggestion(TextChannel channel, String txt, User user) {
        if (channel == null) return;

        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("**Suggestion from " + user.getEffectiveName() + "**");
        embedBuilder.setDescription(txt);
        embedBuilder.setColor(16029942);  // This is the color code
        embedBuilder.setThumbnail(user.getEffectiveAvatarUrl());

        Message message = channel.sendMessageEmbeds(embedBuilder.build()).complete();
        message.addReaction(Emoji.fromUnicode("U+2705")).queue();
        message.addReaction(Emoji.fromUnicode("U+274C")).queue();
    }
}
