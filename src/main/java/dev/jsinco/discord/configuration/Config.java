package dev.jsinco.discord.configuration;

import dev.jsinco.discord.framework.settings.AbstractOkaeriConfig;
import eu.okaeri.configs.annotation.Exclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Config extends AbstractOkaeriConfig {

    @Exclude @Getter
    private static Config instance = createConfig(Config.class);


    private String autoRoleId = "1257256732833615893";

    private String suggestionChannelId = "1200759801693614111";
}
