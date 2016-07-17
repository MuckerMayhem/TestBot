package gui;

import gui.window.main.guild.GuildPanel;
import gui.window.main.log.LogPanel;
import gui.window.main.view.ViewPanel;

import javax.swing.*;
import java.awt.*;

public class BotGui extends JFrame{
    
    public String selectedGuild;
    
    private static BotGui gui;
    private static boolean disabled;
    
    private BotPanel mainPanel;
    
    private GuildPanel guildPanel;
    private ViewPanel viewPanel;
    private LogPanel logPanel;
    
    public BotGui(String title){
        super(title);
        
        this.setSize(700, 800);
        
        this.mainPanel = new BotPanel(this, new GridLayout(1, 2));
        add(this.mainPanel);
        
        this.guildPanel = new GuildPanel(this, new GridLayout());
        mainPanel.add(guildPanel);
        
        this.viewPanel = new ViewPanel(this);
        mainPanel.add(viewPanel);
        
        this.logPanel = new LogPanel(this);
        
        gui = this;
    }
    
    public static BotGui getGui(){
        return gui;
    }
    
    public static boolean isDisabled(){
        return disabled;
    }
    
    public static void disableGui(){
        disabled = true;
    }
    
    public BotPanel getMainPanel(){
        return this.mainPanel;
    }
    
    public GuildPanel getGuildPanel(){
        return this.guildPanel;
    }
    
    public ViewPanel getViewPanel(){
        return this.viewPanel;
    }
    
    public LogPanel getLogPanel(){
        return this.logPanel;
    }
    
    public String getCurrentlySelectedGuild(){
        return this.selectedGuild;
    }
    
    public void updateLogPanelStatus(){
        if(this.logPanel.isShowing()){
            this.mainPanel.add(this.logPanel);
            setSize(getWidth() + (int) (getWidth() * 0.33), getHeight());
        }
        else{
            this.mainPanel.remove(this.logPanel);
            setSize(getWidth() - (int) (getWidth() * 0.25), getHeight());
        }
        
        refresh();
    }
    
    public void refresh(){
        revalidate();
        repaint();
    }

    @Override
    public void add(Component c, Object constraints, int index){
        super.add(c, constraints, index);
        if(c instanceof AbstractBotPanel){
            ((AbstractBotPanel) c).gui = this;
        }
    }

    @Override
    public void add(Component c, Object constraints){
        super.add(c, constraints);
        if(c instanceof AbstractBotPanel){
            ((AbstractBotPanel) c).gui = this;
        }
    }
    
    @Override
    public Component add(Component c){
        super.add(c);
        if(c instanceof AbstractBotPanel){
            ((AbstractBotPanel) c).gui = this;
        }
        return c;
    }
    
    public void update(){
        this.guildPanel.update();
        this.viewPanel.update();
        this.logPanel.update();
        updateLogPanelStatus();
    }
}
