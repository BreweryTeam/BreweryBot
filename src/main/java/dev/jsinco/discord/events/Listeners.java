package dev.jsinco.discord.events;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dev.jsinco.discord.Util;
import dev.jsinco.discord.configuration.Config;
import dev.jsinco.discord.framework.events.ListenerModule;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.SubscribeEvent;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class Listeners implements ListenerModule {

    @SubscribeEvent
    public void onUserJoinsGuild(GuildMemberJoinEvent event) {
        Role role = event.getGuild().getRoleById(Config.getInstance().getAutoRoleId());
        if (role == null) {
            return;
        }

        event.getGuild().addRoleToMember(event.getMember(), role).queue();
    }

    @SubscribeEvent
    public void onUserUploadsTextFile(MessageReceivedEvent event) {
        if (event.getAuthor().isBot() || event.getMessage().getAttachments().isEmpty()) {
            return;
        }


        HttpClient client = HttpClient.newHttpClient();

        for (Message.Attachment attachment : event.getMessage().getAttachments()) {
            if (attachment.getContentType() == null || !attachment.getContentType().startsWith("text")) {
                continue;
            }

            final String contentType;
            if (Util.equalsAny(attachment.getFileExtension(), "yml", "yaml")) {
                contentType = "text/yaml";
            } else {
                contentType = "text/log"; // Default to text/log
            }

            attachment.getProxy().download().thenAccept(inputStream -> {
                // Do something with the input stream

                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create("https://api.pastes.dev/post"))
                        .header("Content-Type", contentType)
                        .POST(HttpRequest.BodyPublishers.ofInputStream(() -> inputStream))  // Upload InputStream
                        .build();
                try {
                    HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

                    JsonObject jsonResponse = JsonParser.parseString(response.body()).getAsJsonObject();
                    String key = jsonResponse.get("key").getAsString();


                    String pasteUrl = "https://pastes.dev/" + key;

                    event.getMessage().reply("Please use a pasting service when uploading logs or configuration files.\n" +
                            "**Paste:** " + pasteUrl).queue();
                } catch (IOException | InterruptedException e) {
                    throw new RuntimeException(e);
                }
            });
        }
    }
}
