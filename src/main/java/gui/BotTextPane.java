package gui;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;

public class BotTextPane extends JTextPane{

    public void println(Color color, String x){
        StyleContext context = StyleContext.getDefaultStyleContext();
        AttributeSet attributes = context.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, color);

        try{
            getDocument().insertString(getDocument().getLength(), x + "\n", attributes);
        }
        catch(BadLocationException ignored){}
    }

    public void clear(){
        setText("");
    }
}
