package gui;

import sx.blah.discord.handle.obj.IGuild;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class LogTextPane extends JTextPane{
    
    private BotGui gui;
    
    private LogPanel logPanel;
    private IGuild guild;
    
    private boolean detached;
    
    public LogTextPane(LogPanel parent, IGuild guild){
        this.setEditable(false);
        this.logPanel = parent;
        this.guild = guild;
        this.gui = parent.gui;
    }
 
    public JFrame detach(){
        this.detached = true;
        
        JFrame logWindow = new JFrame(this.guild.getID());
        logWindow.addWindowListener(new LogPanelPopoutListener(this.guild.getID()));
        logWindow.setResizable(false);

        JPanel panel = new JPanel(new GridLayout());
        logWindow.add(panel);
        panel.setBorder(new TitledBorder("Log"));
        panel.add(new JScrollPane(this));
        
        return logWindow;
    }
    
    public LogPanel getLogPanel(){
        return this.logPanel;
    }
    
    public IGuild getGuild(){
        return this.guild;
    }
    
    public boolean isDetached(){
        return this.detached;
    }
    
    public void println(String x){
        this.setText(getText() + x);
    }

    public class LogPanelPopoutListener implements WindowListener{

        private String guildId;

        public LogPanelPopoutListener(String guildId){
            this.guildId = guildId;
        }

        @Override
        public void windowOpened(WindowEvent e){

        }

        @Override
        public void windowClosing(WindowEvent e){
            detached = false;
            
            if(this.guildId.equals(gui.getCurrentlySelectedGuild())){
                gui.getLogPanel().setShowing(true);
                gui.update();
            }
        }

        @Override
        public void windowClosed(WindowEvent e) {}

        @Override
        public void windowIconified(WindowEvent e) {}

        @Override
        public void windowDeiconified(WindowEvent e) {}

        @Override
        public void windowActivated(WindowEvent e) {}

        @Override
        public void windowDeactivated(WindowEvent e) {}
    }
}
