package gui;

import bot.DiscordBot;
import sx.blah.discord.handle.obj.IGuild;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;

public class GuildPanel extends AbstractBotPanel{

    private JList<String> guildList;
    
    public GuildPanel(BotGui gui, LayoutManager layout){
        super(gui, layout);

        setLayout(new GridLayout());
        setBorder(new TitledBorder("Guilds"));

        JList<String> guildList = new JList<>();
        guildList.addListSelectionListener(new GuildListSelectionListener(gui, guildList));
        guildList.setModel(new DefaultListModel<>());
        ((DefaultListModel<String>) guildList.getModel()).addElement("No guilds");
        add(guildList);
        
        this.guildList = guildList;
    }

    public JList<String> getGuildList(){
        return this.guildList;
    }
    
    @Override
    protected void onUpdate(){
        if(this.guildList == null) return;

        this.guildList.clearSelection();
        ((DefaultListModel) this.guildList.getModel()).clear();

        for(IGuild g : DiscordBot.getGuilds()){
            ((DefaultListModel<String>) this.guildList.getModel()).addElement(g.getID());
        }
    }

    public static class GuildListSelectionListener implements ListSelectionListener{

        private BotGui gui;
        private JList guildList;

        public GuildListSelectionListener(BotGui gui, JList guildList){
            this.gui = gui;
            this.guildList = guildList;
        }

        @Override
        public void valueChanged(ListSelectionEvent e){
            if(this.guildList.getSelectedValue() == null) return;
            
            this.gui.selectedGuild = this.guildList.getSelectedValue().toString();
            this.gui.getViewPanel().update();
            this.gui.getLogPanel().update();
        }
    }
}
