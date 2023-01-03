package com.proxis;

import net.runelite.api.Client;
import net.runelite.api.Player;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.swing.*;
import javax.swing.event.EventListenerList;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;

@Singleton
public class PropHuntService
{
    @Inject
    private Client runeLiteClient;

    public String GamePassphrase = "";

    public BackedObservableList<String> Players;

    public PropHuntService()
    {
        Players = new BackedObservableList<>(new ArrayList<>());
    }

    public void createGame(){}
    public void joinGame(){
        Player localPlayer = runeLiteClient.getLocalPlayer();
        if(localPlayer != null)
        {
            Players.add(runeLiteClient.getLocalPlayer().getName());
        }
        else {
            Players.add("hayden");
        }
    }
    public void startGame(){}
    public void leaveGame(){
        Player localPlayer = runeLiteClient.getLocalPlayer();
        if(localPlayer != null)
        {
            Players.add(runeLiteClient.getLocalPlayer().getName());
        }
        else {
            Players.remove("hayden");
            Players.add("weenz");
            Players.add("qoopa");
        }
    }
}
