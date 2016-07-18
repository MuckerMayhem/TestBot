package gui.window.main.view.info;

import bot.DiscordBot;
import gui.AbstractBotPanel;
import gui.BotGui;
import sx.blah.discord.handle.obj.IGuild;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class InfoPanel extends AbstractBotPanel{
    
    private final JLabel nameLabel;
    private final JLabel regionLabel;

    public InfoPanel(BotGui gui){
        super(gui, new GridLayout(4, 1));

        setBorder(new TitledBorder("Guild info"));
        
        this.nameLabel = new JLabel("Guild name: ");
        add(this.nameLabel);
        
        this.regionLabel = new JLabel("Region: ");
        add(this.regionLabel);
    }

    @Override
    public void onUpdate(){
        IGuild guild = DiscordBot.getClient().getGuildByID(this.gui.getCurrentlySelectedGuild());
        if(guild == null) return;

        DiscordBot bot = DiscordBot.getInstance(guild);
        if(bot == null) return;

        //Update name
        this.nameLabel.setText("Guild name: " + (bot.anonymous() ? guild.getID() : guild.getName()));
        
        //Update region
        this.regionLabel.setText("Region: " + guild.getRegion().getName());
    }
}
