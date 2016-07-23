package gui.window.message.chat;

import bot.DiscordBot;
import gui.AbstractBotPanel;
import gui.window.message.MessagePanel;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import java.awt.*;

public class ChatPanel extends AbstractBotPanel{

    private final MessagePanel messagePanel;
    private final JTextPane chatPane;
            
    public ChatPanel(MessagePanel parent){
        super(parent.getGui(), new GridLayout());
        this.messagePanel = parent;

        setBorder(new TitledBorder("Responses"));
        
        this.chatPane = new JTextPane();
        chatPane.setEditable(false);
        add(new JScrollPane(chatPane));

        DiscordBot.getClient().getDispatcher().registerListener(this);
    }

    public MessagePanel getMessagePanel(){
        return this.messagePanel;
    }
    
    @Override
    protected void onUpdate(){
        this.chatPane.setText("");
        
        IGuild guild = DiscordBot.getClient().getGuildByID(gui.getCurrentlySelectedGuild());
        if(guild == null) return;
        
        DiscordBot bot = DiscordBot.getInstance(guild);
        if(bot == null) return;
        
        IChannel channel = this.messagePanel.getSelectedChannel();
        
        int mentions = 0;
        for(int i = 0;i < channel.getMessages().size();i++){
            IMessage message = channel.getMessages().get(i);
            if(message.getMentions().contains(DiscordBot.getClient().getOurUser())){
                Document doc = this.chatPane.getDocument();
                try{
                    IUser client = DiscordBot.getClient().getOurUser();
                    doc.insertString(doc.getLength(), message.getContent().replaceAll(client.mention(true), "").replaceAll(client.mention(false), "") + "\n", null);
                }
                catch(BadLocationException e){
                    continue;
                }
                mentions++;
            }
            
            if(mentions >= 10) break;
        }
    }
    
    @EventSubscriber
    public void onMessageReceived(MessageReceivedEvent event){
        if(event.getMessage().getChannel() == getMessagePanel().getSelectedChannel()) update();
    }
}
