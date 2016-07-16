package gui;

import java.awt.*;

public class BotPanel extends AbstractBotPanel{

    public BotPanel(BotGui gui) {
        super(gui);
    }
    
    public BotPanel(BotGui gui, LayoutManager layout, boolean isDoubleBuffered){
        super(gui, layout, isDoubleBuffered);
    }
    
    public BotPanel(BotGui gui, boolean isDoubleBuffered){
        super(gui, isDoubleBuffered);
    }

    public BotPanel(BotGui gui, LayoutManager layout){
        super(gui, layout);
    }

    @Override
    protected void onUpdate() {}
}
