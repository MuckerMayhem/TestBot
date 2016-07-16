package gui;

import javax.swing.*;
import java.awt.*;

public abstract class AbstractBotPanel extends JPanel{
    
    protected BotGui gui;

    public AbstractBotPanel(BotGui gui) {
        this.gui = gui;
    }
    
    public AbstractBotPanel(BotGui gui, LayoutManager layout, boolean isDoubleBuffered){
        super(layout, isDoubleBuffered);
        this.gui = gui;
    }
    
    public AbstractBotPanel(BotGui gui, boolean isDoubleBuffered){
        super(isDoubleBuffered);
        this.gui = gui;
    }

    public AbstractBotPanel(BotGui gui, LayoutManager layout){
        super(layout);
        this.gui = gui;
    }

    protected abstract void onUpdate();
    
    public void update(){
        this.onUpdate();
        
        for(Component c : getComponents()){
            if(c instanceof AbstractBotPanel){
                ((AbstractBotPanel) c).update();
                break;
            }
        }
        
        revalidate();
        repaint();
    }
    
    @Override
    public Component add(Component c){
        super.add(c);
        if(c instanceof AbstractBotPanel){
            ((AbstractBotPanel) c).gui = this.gui;
        }
        return c;
    }
}
