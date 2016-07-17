package gui.window.message;

import bot.DiscordBot;
import bot.event.BotEventListener;
import bot.event.BotEventSubscriber;
import gui.AbstractBotPanel;
import gui.BotGui;
import gui.window.message.chat.ChatPanel;
import gui.window.message.input.MessageInputPanel;
import logging.UserLoggedEvent;
import sx.blah.discord.handle.obj.IChannel;

import javax.swing.*;
import java.awt.Dialog.ModalityType;
import java.awt.*;

public class MessagePanel extends AbstractBotPanel implements BotEventListener{
    
    private DiscordBot bot;
    
    private MessageInputPanel inputPanel;
    private ChatPanel chatPanel;
 
    public MessagePanel(BotGui gui, DiscordBot bot){
        super(gui, new GridLayout(2, 1));
        this.bot = bot;
        
        this.inputPanel = new MessageInputPanel(this, bot);
        add(inputPanel);
  
        this.chatPanel = new ChatPanel(this);
        add(chatPanel);
        
        bot.getEventDispatcher().registerListener(this);
        
        inputPanel.update();
    }
    
    public DiscordBot getBot(){
        return this.bot;
    }
    
    public MessageInputPanel getInputPanel(){
        return this.inputPanel;
    }
    
    public ChatPanel getChatPanel(){
        return this.chatPanel;
    }
    
    public IChannel getSelectedChannel(){
        try{
            return bot.getGuild().getChannels().get(getInputPanel().getChannelList().getSelectedIndex()); 
        }
        catch(IndexOutOfBoundsException e){
            return null;
        }
    }
    
    public void open(){
        JDialog dialog = new JDialog(gui, "Send message");
        
        dialog.setModalityType(ModalityType.APPLICATION_MODAL);
        dialog.setResizable(false);
        dialog.add(this);
        
        dialog.pack();
        dialog.setSize(500, 400);
        dialog.setVisible(true);
    }
    
    @Override
    protected void onUpdate(){
        this.inputPanel.update();
        this.chatPanel.update();
    }
    
    @BotEventSubscriber
    public void onUserLogged(UserLoggedEvent event){
        this.getInputPanel().getMentionButton().setEnabled(event.getUser() != null);
    }
}
