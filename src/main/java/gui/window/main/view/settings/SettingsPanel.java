package gui.window.main.view.settings;

import bot.DiscordBot;
import bot.settings.Setting;
import bot.settings.SingleSettingsHandler;
import gui.AbstractBotPanel;
import gui.BotGui;
import gui.BotPanel;
import gui.window.message.MessagePanel;
import sx.blah.discord.handle.obj.IGuild;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SettingsPanel extends AbstractBotPanel{
    
    private static String[] headers = {"Setting", "Value"};

    public SettingsPanel(BotGui gui){
        super(gui, new GridLayout(2, 1));
        
        setBorder(new TitledBorder("Guild settings"));
    }

    @Override
    public void onUpdate(){
        IGuild guild = DiscordBot.getClient().getGuildByID(this.gui.getCurrentlySelectedGuild());
        if(guild == null) return;

        DiscordBot bot = DiscordBot.getInstance(guild);
        if(bot == null) return;
        
        removeAll();
        
        //Settings
        SingleSettingsHandler handler = bot.getServerSettingsHandler();
        Object[][] data = new Object[handler.getRegisteredSettings().size()][2];
        int index = 0;
        for(Setting s : handler.getSettings()){
            data[index] = new Object[]{s.getName(), s.getValueAsString(handler.getSetting(s))};
            index++;
        }
        
        JTable table = new JTable(data, headers){
            @Override
            public boolean isCellEditable(int row, int column){
                return false;
            }
        };
        add(new JScrollPane(table));

        BotPanel buttonPanel = new BotPanel(gui);
        add(buttonPanel);
        
        //Show / hide log button
        JButton logButton = new JButton(gui.getLogPanel().isShowing() ? "Hide log" : "Show log");
        logButton.setEnabled(!gui.getLogPanel().getLogPanel(guild).isDetached());
        logButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                gui.getLogPanel().toggleShowing();
                gui.updateLogPanelStatus();
                update();
            }
        });
        buttonPanel.add(logButton);
        
        JButton messageButton = new JButton("Message...");
        messageButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                new MessagePanel(gui, bot).open();
            }
        });
        buttonPanel.add(messageButton);
        
        JButton restartButton = new JButton("Restart");
        restartButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                bot.init();
            }
        });
        buttonPanel.add(restartButton);
    }
}
