package com.proxis;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.ConfigSection;

import java.util.function.Predicate;

@ConfigGroup("proxis")
public interface ProxisConfig extends Config
{
    @ConfigSection(
            position = 0,
            name = "Prop Hunt",
            description = "Shows configuration for prop hunt."
    )
    String propHuntSection = "propHuntSection";

    @ConfigItem(
            keyName = "game-code",
            name = "Game Code",
            secret = false,
            description = "Put your game code here!",
            position = 1,
            section = propHuntSection
    )
    default String gameCode()
    {
        return "qoopa-rocks";
    }





}