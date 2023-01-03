package com.proxis;

import com.google.inject.Provides;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.events.GameStateChanged;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.ClientToolbar;
import net.runelite.client.ui.NavigationButton;
import net.runelite.client.game.ItemManager;
import java.awt.image.BufferedImage;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.game.SpriteManager;
import net.runelite.client.util.ImageUtil;

@Slf4j
@PluginDescriptor(
        name = "Proxis"
)
public class ProxisPlugin extends Plugin
{
    @Inject
    private Client client;

    @Inject
    private SpriteManager spriteManager;

    @Inject
    private ItemManager itemManager;

    @Inject
    private ProxisConfig config;

    @Inject
    private ClientToolbar clientToolbar;

    @Inject
    private ClientThread clientThread;

    private ProxisPanel panel;
    private NavigationButton panelButton;


    @Override
    protected void startUp() throws Exception
    {
        log.info("proxis started!");
        panel = injector.getInstance(ProxisPanel.class);

        final BufferedImage icon = ImageUtil.loadImageResource(ProxisPlugin.class, "/proxis-pickaxe.png");

        panelButton = NavigationButton.builder()
                .icon(icon)
                .tooltip("Proxis")
                .priority(1)
                .panel(panel)
                .build();

        clientToolbar.addNavigation(panelButton);
    }

    @Override
    protected void shutDown() throws Exception
    {
        log.info("proxis stopped!");
    }

    @Subscribe
    public void onGameStateChanged(GameStateChanged gameStateChanged)
    {
        if (gameStateChanged.getGameState() == GameState.LOGGED_IN)
        {
//            client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "Proxis says " + config.greeting(), null);
        }
    }

    @Provides
    ProxisConfig provideConfig(ConfigManager configManager)
    {
        return configManager.getConfig(ProxisConfig.class);
    }
}
