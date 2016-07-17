package gui.window.main.log;

import bot.DiscordBot;
import gui.AbstractBotPanel;
import gui.BotGui;
import sx.blah.discord.handle.obj.IGuild;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

/**
 * Represents the right panel in the BotGui
 * Contains two panels:<ul>
 *     <li>An info panel that shows the information of the currently selected guild</li>
 *     <li>A settings panel that shows the settings of the currently selected guild</li>
 * </ul>
 */
public class LogPanel extends AbstractBotPanel{
    
    private static HashMap<String, LogTextPane> textPanes = new HashMap<>();
    
    private boolean showing;

    public LogPanel(BotGui gui){
        super(gui, new GridBagLayout());
        
        this.setBorder(new TitledBorder("Log"));
    }
    
    public LogTextPane getLogPanel(IGuild guild){
        if(!textPanes.containsKey(guild.getID()))
            textPanes.put(guild.getID(), new LogTextPane(this, guild));

        return textPanes.get(guild.getID());
    }
    
    public boolean isShowing(){
        return this.showing;
    }
    
    public void setShowing(boolean showing){
        this.showing = showing;
    }
    
    public void toggleShowing(){
        this.showing = !showing;
    }
    
    @Override
    public void onUpdate(){
        DiscordBot bot = DiscordBot.getInstance(this.gui.getCurrentlySelectedGuild());
        if(bot == null) return;
        
        removeAll();
        
        //Add log
        add(new JScrollPane(getLogPanel(bot.getGuild())), new GridBagConstraints(0, 0, 1, 9, 1, 1, GridBagConstraints.CENTER, 1, new Insets(0, 0, 0, 0), 0, 0));
        
        //Add detach button
        JButton detachButton = new JButton("Detach log window");
        detachButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                JFrame popup = getLogPanel(bot.getGuild()).detach();
                popup.pack();
                popup.setSize(500, 900);
                popup.setVisible(true);
                
                toggleShowing();
                gui.updateLogPanelStatus();
                gui.getViewPanel().getSettingsPanel().update();
            }
        });
        add(detachButton, new GridBagConstraints(0, 10, 1, 1, 0.05, 0.05, GridBagConstraints.PAGE_END, 1, new Insets(0, 0, 0, 0), 0, 0));
    }
}
