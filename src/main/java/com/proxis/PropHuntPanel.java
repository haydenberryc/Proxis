package com.proxis;

import net.runelite.client.ui.ColorScheme;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import java.awt.*;

@Singleton
public class PropHuntPanel extends JPanel
{
    @Inject
    private PropHuntService propHuntService;
    final Border bottomBorderGold = BorderFactory.createMatteBorder(0, 0, 1, 0, ColorScheme.PROGRESS_INPROGRESS_COLOR);
    final Font cascadiaMono14 = new Font("Cascadia Mono", Font.TRUETYPE_FONT, 14);
    final Font cascadiaMono12 = new Font("Cascadia Mono", Font.TRUETYPE_FONT, 12);
    final Font cascadiaMono10 = new Font("Cascadia Mono", Font.TRUETYPE_FONT, 10);


    private final JButton btnCreateGame = new JButton("Create Game");
    private final JButton btnJoinGame = new JButton("Join Game");
    private final JButton btnStartGame = new JButton("Start Game");
    private final JButton btnLeaveGame = new JButton("Leave Game");
    private final JButton btnRandomModel = new JButton(){
        {
            setText("Random Model");
            setToolTipText("Switch to another random model.");
        }
    };
    private final JButton btnTaunt = new JButton(){
        {
            setText("Taunt");
            setToolTipText("Taunt the hunter(s) by playing a sound clip.");
        }
    };
    private final JButton btnRotateModel = new JButton("Rotate");
    private final JButton btnResizeModel = new JButton("Resize");

    private final JPanel gameOptionsPanel;
    private final JPanel createOrJoinPanel;
    private final JPanel startGamePanel;
    private final JPanel leaveGamePanel;

    private final JPanel propOptionsPanel;
    private final JPanel randomModelPanel;
    private final JPanel tauntPanel;
    private final JPanel rotateOrResizePanel;

    private final JPanel hunterStatsPanel;
    private final JPanel playerLobbyPanel;

    private JList<String> testList = new JList<>();

    @Inject
    public PropHuntPanel(final PropHuntService propHuntService)
    {
        this.propHuntService = propHuntService;

        //panels
        createOrJoinPanel = createSplitButtonsPanel(btnCreateGame, btnJoinGame);
        startGamePanel = createSingleButtonPanel(btnStartGame);
        startGamePanel.setVisible(false);
        leaveGamePanel = createSingleButtonPanel(btnLeaveGame);
        leaveGamePanel.setVisible(false);
        gameOptionsPanel = createOptionsPanel("GAME OPTIONS", new JPanel[]{
            createOrJoinPanel,
            startGamePanel,
            leaveGamePanel
        });

        randomModelPanel = createButtonIntGroupPanel(btnRandomModel, "5");
        tauntPanel = createButtonIntGroupPanel(btnTaunt, "5");
        rotateOrResizePanel = createSplitButtonsPanel(btnRotateModel, btnResizeModel);
        propOptionsPanel = createOptionsPanel("PROP OPTIONS", new JPanel[]{
            randomModelPanel,
            tauntPanel,
            rotateOrResizePanel
        });

        hunterStatsPanel = createOptionsPanel("HUNTER STATS", new JPanel[]{
                createButtonIntGroupPanel("Right Clicks Left", "10", "Amount of right clicks left available."),
                createButtonIntGroupPanel("Props Left To Find", "3", "Amount of props that are still hiding."),
                createButtonIntGroupPanel("Total Props Found", "1", "Amount of props that have been found.")
        });

        playerLobbyPanel = createOptionsPanel("PLAYER'S IN LOBBY", new JPanel[]{});
        playerLobbyPanel.setVisible(false);

        //event listeners
        btnCreateGame.addActionListener(e -> {
            propHuntService.createGame();
            playerLobbyPanel.setVisible(true);
            createOrJoinPanel.setVisible(false);
            startGamePanel.setVisible(true);
            leaveGamePanel.setVisible(true);
            btnStartGame.setEnabled(true);
            btnStartGame.setToolTipText("Start the game.");
        });

        btnJoinGame.addActionListener(e -> {
            propHuntService.joinGame();
            createOrJoinPanel.setVisible(false);
            startGamePanel.setVisible(true);
            leaveGamePanel.setVisible(true);
            btnStartGame.setEnabled(false);
            btnStartGame.setToolTipText("Wait for the host to start the game.");
            playerLobbyPanel.setVisible(true);
        });

        btnLeaveGame.addActionListener(e -> {
            propHuntService.leaveGame();
            playerLobbyPanel.setVisible(false);
            leaveGamePanel.setVisible(false);
            startGamePanel.setVisible(false);
            createOrJoinPanel.setVisible(true);

        });

        propHuntService.Players.addListener(new BackedListListener<String>() {
            @Override
            public void setOnChanged(ListChangeEvent<String> event) {
                if (event.wasAdded()) {
                    event.getChangeList().forEach(player -> {
                        addPlayerToPlayerList(player);
                    });
                }
                if (event.wasRemoved()) {
                    event.getChangeList().forEach(player -> {
                        removePlayerFromPlayerList(player);
                    });
                }
            }
        });

        GridBagConstraints constraints = new GridBagConstraints();
        this.setBackground(ColorScheme.DARKER_GRAY_HOVER_COLOR);
        this.setLayout(new GridBagLayout());
        constraints.insets = new Insets(4,6, 4, 6);
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.weightx = 1.0;

        //game options
        constraints.gridx = 0;
        constraints.gridy = 0;
        this.add(gameOptionsPanel, constraints);

        //current player list
        constraints.gridx = 0;
        constraints.gridy = 1;
        this.add(playerLobbyPanel, constraints);

        //props
        constraints.gridx = 0;
        constraints.gridy = 2;
        this.add(propOptionsPanel, constraints);

        //hunters
        constraints.gridx = 0;
        constraints.gridy = 3;
        this.add(hunterStatsPanel, constraints);
    }

    private JPanel createPlayerListPanel()
    {
        JPanel playerListPanel = new JPanel();
        playerListPanel.setBorder(BorderFactory.createLineBorder(ColorScheme.DARKER_GRAY_COLOR, 2, true));
        playerListPanel.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.weightx = 1.0;

        JButton btnPlayers = new JButton("CURRENT PLAYERS IN LOBBY");
        btnPlayers.setBorder(BorderFactory.createMatteBorder(0,0,1, 0, ColorScheme.PROGRESS_INPROGRESS_COLOR));
        btnPlayers.setFont(new Font("Cascadia Mono", Font.PLAIN, 12));
        btnPlayers.setEnabled(false);
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.weightx = 0.5;
        playerListPanel.add(btnPlayers, constraints);

        return playerListPanel;
    }

    private void addPlayerToPlayerList(String player)
    {
        GridBagConstraints constraints = new GridBagConstraints();
        JButton btnPlayerName = new JButton(player.toUpperCase());
        //btnPlayerName.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED, ColorScheme.MEDIUM_GRAY_COLOR, ColorScheme.MEDIUM_GRAY_COLOR));
        btnPlayerName.setBorder(BorderFactory.createMatteBorder(1,1,1,1, ColorScheme.MEDIUM_GRAY_COLOR));
        btnPlayerName.setFont(new Font("Cascadia Mono", Font.PLAIN, 12));
        btnPlayerName.setHorizontalAlignment(SwingConstants.CENTER);
        btnPlayerName.setEnabled(false);
        constraints.insets = new Insets(3,6,3,6);
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.weightx = 1.0;
        constraints.gridx = 0;
        constraints.gridy = playerLobbyPanel.getComponentCount() + 1;
        playerLobbyPanel.add(btnPlayerName, constraints);
    }

    private void removePlayerFromPlayerList(String player)
    {
        for (Component component : playerLobbyPanel.getComponents()) {
            if(component instanceof JButton)
                if(((JButton)component).getText() == player.toUpperCase())
                    playerLobbyPanel.remove(component);
        }
    }

    private JPanel createOptionsPanel(String titleText, JPanel[] options)
    {
        JPanel optionsPanel = new JPanel();
        optionsPanel.setLayout(new GridBagLayout());
        optionsPanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED, ColorScheme.DARKER_GRAY_COLOR, ColorScheme.DARKER_GRAY_COLOR));
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.weightx = 1.0;
        constraints.gridx = 0;
        optionsPanel.add(new JLabel(titleText){
            {
                setHorizontalAlignment(SwingConstants.CENTER);
                setBorder(bottomBorderGold);
                setFont(cascadiaMono14);
            }
        }, constraints);

        constraints.insets = new Insets(4,6,4,6);
        for (int i = 1; i <= options.length; i++) {
            constraints.gridy = i;
            optionsPanel.add(options[i-1], constraints);
        }

        return optionsPanel;
    }

    private JPanel createButtonIntGroupPanel(String btnLeftText, String btnRightText, String btnLeftToolTip)
    {
        return new JPanel(){
            {
                setLayout(new GridBagLayout());
                GridBagConstraints c = new GridBagConstraints();
                c.fill = GridBagConstraints.BOTH;

                JButton btnLeft = new JButton(btnLeftText);
                btnLeft.setBorder(BorderFactory.createMatteBorder(1,1,1,0, ColorScheme.MEDIUM_GRAY_COLOR));
                btnLeft.setToolTipText(btnLeftToolTip);
                btnLeft.setFocusPainted(false);
                btnLeft.setEnabled(false);
                btnLeft.setFont(cascadiaMono12);

                c.gridx = 0;
                c.gridy = 0;
                c.weightx = 1.0;
                add(btnLeft, c);

                JButton btnRight = new JButton(btnRightText);
                btnRight.setBorder(BorderFactory.createMatteBorder(1,0,1,1, ColorScheme.MEDIUM_GRAY_COLOR));
                btnRight.setFont(cascadiaMono12);
                btnRight.setHorizontalTextPosition(SwingConstants.CENTER);
                btnRight.setVerticalTextPosition(SwingConstants.CENTER);
                btnRight.setPreferredSize(new Dimension(33,23));
                btnRight.setEnabled(false);

                c.gridx = 1;
                c.gridy = 0;
                c.weightx = 0.0;
                add(btnRight, c);
            }
        };
    }

    private JPanel createButtonIntGroupPanel(JButton btnLeft, String btnRightText)
    {
        return new JPanel(){
            {
                setLayout(new GridBagLayout());
                GridBagConstraints c = new GridBagConstraints();
                c.fill = GridBagConstraints.BOTH;

                btnLeft.setBorder(BorderFactory.createMatteBorder(1,1,1,0, ColorScheme.MEDIUM_GRAY_COLOR));
                btnLeft.setFocusPainted(false);
                btnLeft.setFont(cascadiaMono12);

                c.gridx = 0;
                c.gridy = 0;
                c.weightx = 1.0;
                add(btnLeft, c);

                JButton btnRight = new JButton(btnRightText);
                btnRight.setBorder(BorderFactory.createMatteBorder(1,0,1,1, ColorScheme.MEDIUM_GRAY_COLOR));
                btnRight.setFont(cascadiaMono12);
                btnRight.setHorizontalTextPosition(SwingConstants.CENTER);
                btnRight.setVerticalTextPosition(SwingConstants.CENTER);
                btnRight.setPreferredSize(new Dimension(33,23));
                btnRight.setEnabled(false);

                c.gridx = 1;
                c.gridy = 0;
                c.weightx = 0.0;
                add(btnRight, c);
            }
        };
    }

    private JPanel createSplitButtonsPanel(JButton leftButton, JButton rightButton)
    {
        return new JPanel() {
            {
                setLayout(new GridBagLayout());
                GridBagConstraints c = new GridBagConstraints();
                c.fill = GridBagConstraints.BOTH;

                leftButton.setPreferredSize(new Dimension(10, 23));
                leftButton.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, ColorScheme.MEDIUM_GRAY_COLOR));
                leftButton.setFocusPainted(false);
                leftButton.setFont(cascadiaMono12);

                c.gridx = 0;
                c.gridy = 0;
                c.weightx = 1.0;
                c.insets = new Insets(0, 0, 2, 4);
                add(leftButton, c);

                rightButton.setPreferredSize(new Dimension(10, 23));
                rightButton.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, ColorScheme.MEDIUM_GRAY_COLOR));
                rightButton.setFocusPainted(false);
                rightButton.setFont(cascadiaMono12);

                c.gridx = 1;
                c.gridy = 0;
                c.weightx = 1.0;
                c.insets = new Insets(0, 4, 2, 0);
                add(rightButton, c);
            }
        };
    }

    private JPanel createSingleButtonPanel(JButton btn)
    {
        return new JPanel(){
            {
                this.setLayout(new GridBagLayout());
                GridBagConstraints c = new GridBagConstraints();
                c.fill = GridBagConstraints.BOTH;

                btn.setPreferredSize(new Dimension(10, 23));
                btn.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, ColorScheme.MEDIUM_GRAY_COLOR));
                btn.setFocusPainted(false);
                btn.setFont(cascadiaMono12);

                c.weightx = 1.0;
                add(btn, c);
            }
        };
    }

}
