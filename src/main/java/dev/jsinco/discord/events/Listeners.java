package dev.jsinco.discord.events;

import dev.jsinco.discord.configuration.Config;
import dev.jsinco.discord.framework.events.ListenerModule;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.hooks.SubscribeEvent;

public class Listeners implements ListenerModule {

    @SubscribeEvent
    public void onUserJoinsGuild(GuildMemberJoinEvent event) {
        Role role = event.getGuild().getRoleById(Config.getInstance().getAutoRoleId());
        if (role == null) {
            return;
        }

        event.getGuild().addRoleToMember(event.getMember(), role).queue();
    }
}
