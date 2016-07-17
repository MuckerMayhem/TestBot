package gui.window.main.view.features;

import bot.DiscordBot;
import bot.feature.BotFeature;
import gui.AbstractBotPanel;
import gui.BotGui;
import sx.blah.discord.handle.obj.IGuild;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.List;

public class FeaturesPanel extends AbstractBotPanel{

    private static String[] headers = {"Name", "Type"};
    
    public FeaturesPanel(BotGui gui){
        super(gui, new GridLayout());
        
        setBorder(new TitledBorder("Guild features"));
    }
    
    @Override
    protected void onUpdate(){
        IGuild guild = DiscordBot.getClient().getGuildByID(this.gui.getCurrentlySelectedGuild());
        if(guild == null) return;

        DiscordBot bot = DiscordBot.getInstance(guild);
        if(bot == null) return;
        
        removeAll();

        List<BotFeature> features = bot.getFeatures();
        Object[][] data = new Object[features.size()][2];
        int index = 0;
        for(BotFeature f : features){
            data[index] = new Object[]{f.getRegisteredName(), f.getClass().getSuperclass().getSimpleName()};
            index++;
        }

        JTable table = new JTable(data, headers){
            {
                getTableHeader().setReorderingAllowed(false);
            }
            
            @Override
            public boolean isCellEditable(int row, int column){
                return false;
            }

            @Override
            public Class<?> getColumnClass(int column){
                if(column == 2) return Boolean.class;
                return super.getColumnClass(column);
            }
        };
        add(new JScrollPane(table));
    }
}
