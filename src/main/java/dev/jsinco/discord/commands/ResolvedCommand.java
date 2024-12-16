package dev.jsinco.discord.commands;

import dev.jsinco.discord.Util;
import dev.jsinco.discord.framework.commands.CommandModule;
import dev.jsinco.discord.framework.commands.DiscordCommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.channel.concrete.ThreadChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.List;

@DiscordCommand(name = "resolved", permission = Permission.MANAGE_THREADS, description = "Lock this thread")
public class ResolvedCommand implements CommandModule {

    @Override
    public void execute(SlashCommandInteractionEvent event) throws Exception {
        if (!(event.getChannel() instanceof ThreadChannel threadChannel)) {
            event.reply("This command must be used in a ThreadChannel.").queue();
            return;
        }

        String reason = Util.getOption(event.getOption("reason"), OptionType.STRING);
        Boolean archive = Util.getOption(event.getOption("archive"), OptionType.BOOLEAN);
        if (reason == null) {
            reason = "This thread has been locked because the issue was resolved.";
        }


        threadChannel.getManager().setLocked(true).queue();
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("Thread Locked");
        embedBuilder.setDescription("**Reason**\n" + reason);
        embedBuilder.setColor(16029942);

        event.replyEmbeds(embedBuilder.build()).queue();

        if (archive != null && archive) {
            threadChannel.getManager().setArchived(true).queue();
        }
    }

    @Override
    public List<OptionData> getOptions() {
        return List.of(
                new OptionData(OptionType.STRING, "reason", "Reason for locking the thread", false),
                new OptionData(OptionType.BOOLEAN, "archive", "Archive the thread", false)
        );
    }
}
