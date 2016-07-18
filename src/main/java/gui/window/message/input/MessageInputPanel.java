package gui.window.message.input;

import bot.DiscordBot;
import gui.AbstractBotPanel;
import gui.window.message.MessagePanel;
import logging.BotLogger.Level;
import sx.blah.discord.handle.obj.IChannel;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;

public class MessageInputPanel extends AbstractBotPanel{
    
    private final MessagePanel messagePanel;
    private final DiscordBot bot;
    
    private final JTextArea textArea;
    private final JComboBox<String> channelList;
    private final JButton sendButton;
    private final JButton mentionButton;
 
    public MessageInputPanel(MessagePanel parent, DiscordBot bot){
        super(parent.getGui(), new GridLayout(2, 1));
        this.messagePanel = parent;
        this.bot = bot;
        
        setBorder(new TitledBorder("Message"));
        
        //Message typing area
        this.textArea = new JTextArea();
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.getDocument().addDocumentListener(new InputPanelDocumentListener());
        add(new JScrollPane(textArea, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER));

        //Panel to hold the buttons and list
        JPanel buttonPanel = new JPanel(new FlowLayout());
        
        this.channelList = new JComboBox<>();
        channelList.addItemListener(e -> messagePanel.getChatPanel().update());
        buttonPanel.add(channelList);
        
        //Send button
        this.sendButton = new JButton("Send");
        sendButton.setEnabled(false);
        sendButton.addActionListener(e -> {

            String text = textArea.getText();

            IChannel channel = bot.getGuild().getChannels().get(channelList.getSelectedIndex());
            if(channel == null) return;

            bot.say(channel, text);
            bot.log(Level.INFO, "Sent message: " + text);
            textArea.setText("");
        });
        buttonPanel.add(sendButton);
        
        //Mention button
        this.mentionButton = new JButton("Insert mention");
        mentionButton.setEnabled(bot.getLogger().getLastUser() != null);
        mentionButton.addActionListener(e -> textArea.setText(textArea.getText() + "<@!" + bot.getLogger().getLastUser() + ">"));
        buttonPanel.add(mentionButton);
        
        add(buttonPanel);
    }

    public MessagePanel getMessagePanel(){
        return this.messagePanel;
    }
    
    public JTextArea getTextArea(){
        return this.textArea;
    }
    
    public JComboBox<String> getChannelList(){
        return this.channelList;
    }
    
    public JButton getSendButton(){
        return this.sendButton;
    }
    
    public JButton getMentionButton(){
        return this.mentionButton;
    }
    
    @Override
    protected void onUpdate(){
        IChannel home = bot.getHome();
        
        int homeIndex = 0;
        int index = 0;
        for(IChannel c : bot.getGuild().getChannels()){
            if(c == home){
                homeIndex = index;
                channelList.addItem((bot.anonymous() ? c.getID() : c.getName()) + " (Home)");
            }
            else channelList.addItem(bot.anonymous() ? c.getID() : c.getName());
            index++;
        }
        if(home != null) channelList.setSelectedIndex(homeIndex);
    }

    private class InputPanelDocumentListener implements DocumentListener{
        @Override
        public void insertUpdate(DocumentEvent e){
            sendButton.setEnabled(!textArea.getText().trim().isEmpty());
        }

        @Override
        public void removeUpdate(DocumentEvent e){
            sendButton.setEnabled(!textArea.getText().trim().isEmpty());
        }

        @Override
        public void changedUpdate(DocumentEvent e) {}
    }
}
