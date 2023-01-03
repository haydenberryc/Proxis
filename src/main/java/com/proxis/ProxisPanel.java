package com.proxis;

import com.google.inject.Inject;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.Border;

import net.runelite.client.callback.ClientThread;
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.PluginPanel;

public class ProxisPanel extends PluginPanel
{
    private final ProxisPlugin proxisPlugin;
    private final ProxisConfig proxisConfig;
    private final ImageIcon PROXIS_CARET_DOWN = new ImageIcon(
            new ImageIcon(getClass().getResource("/proxis-caret-down.png"))
                    .getImage()
                    .getScaledInstance(12,12, Image.SCALE_SMOOTH)
    );

    private final ImageIcon PROXIS_CARET_UP = new ImageIcon(
            new ImageIcon(getClass().getResource("/proxis-caret-up.png"))
                    .getImage()
                    .getScaledInstance(12,12, Image.SCALE_SMOOTH)
    );
    private final JPanel proxisPanel;
    private final JButton btnPropHuntPanel;
    private final JPanel propHuntPanel;
//    private final JButton btnTestPanel;
//    private final JPanel testPanel;

    @Inject
    public ProxisPanel(final ClientThread clientThread, final ProxisPlugin plugin, final ProxisConfig config, final PropHuntPanel propHuntPanelBuilder)
    {
        proxisPlugin = plugin;
        proxisConfig = config;

        //Proxis Panel
        proxisPanel = new JPanel();
        proxisPanel.setLayout(new GridBagLayout());

        //Prop Hunt Panel
        btnPropHuntPanel = new JButton("Prop Hunt");
        propHuntPanel = propHuntPanelBuilder;
        addPanelButton(btnPropHuntPanel, propHuntPanel, 0, 0);
        addPanel(propHuntPanel, 0, 1);

//        //Test Panel
//        btnTestPanel = new JButton("Some Test Panel");
//        testPanel = propHuntPanelBuilder.buildPropHuntPanel(ColorScheme.MEDIUM_GRAY_COLOR);
//        addPanelButton(btnTestPanel, testPanel, 0, 2);
//        addPanel(testPanel, 0, 3);

        add(proxisPanel, BorderLayout.NORTH);
    }

    private void addPanelButton(JButton btn, JPanel panelToLinkTo, int gridx, int gridy)
    {
        btn.setText(btn.getText() + new String(new char[22 - btn.getText().length()]).replace('\0', ' '));
        btn.setIcon(PROXIS_CARET_DOWN);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Cascadia Mono", Font.TRUETYPE_FONT, 14));
        btn.setHorizontalAlignment(SwingConstants.CENTER);
        btn.setHorizontalTextPosition(SwingConstants.LEFT);
        panelToLinkTo.setVisible(false);

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.insets = new Insets(8,2,0,2);
        constraints.gridx = gridx;
        constraints.gridy = gridy;
        constraints.weightx = 1.0;

        btn.addActionListener(e ->
        {
            final Border activeBorder = BorderFactory.createLineBorder(ColorScheme.LIGHT_GRAY_COLOR, 2);
            final Border inactiveBorder = BorderFactory.createLineBorder(ColorScheme.DARK_GRAY_COLOR, 2);

            btn.setIcon(!panelToLinkTo.isVisible() ? PROXIS_CARET_UP : PROXIS_CARET_DOWN);
            //btn.setBorder(!panelToLinkTo.isVisible() ? activeBorder: inactiveBorder);
            panelToLinkTo.setVisible(!panelToLinkTo.isVisible());
        });

        proxisPanel.add(btn, constraints);
    }

    private void addPanel(JPanel panel, int gridx, int gridy)
    {
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.insets = new Insets(0,2,0,2);
        constraints.gridx = gridx;
        constraints.gridy = gridy;
        constraints.weightx = 1.0;
        constraints.gridheight = 1;
        proxisPanel.add(panel, constraints);
    }
}
