package bot.feature.command;

import bot.DiscordBot;
import logging.BotLogger.Level;
import logging.LogWrapper;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IMessage;
import util.DiscordUtil;
import util.Util;

import java.util.ArrayList;
import java.util.List;

public class CommandHandler{

    private static final ArrayList<BotCommand> global_commands = new ArrayList<>();
    
    private static final String[] EMPTY = {};

    private static String commandPrefix = "!";

    private DiscordBot bot;

    private MessageReceivedEvent lastEvent;

    public CommandHandler() {}

    public CommandHandler(DiscordBot bot){
        this.bot = bot;
    }

    /**
     * @return a list containing all registered commands for every CommandHandler instance
     */
    public static List<BotCommand> getAllRegisteredCommands(){
        return global_commands;
    }

    public static BotCommand getCommandByName(String name){
        for(BotCommand c : global_commands){
            if(c.getRegisteredName().equalsIgnoreCase(name)) return c;
        }
        return null;
    }

    /**
     * Gets the command prefix used for executing commands
     * @return - The command prefix that is currently in use
     */
    public static String getCommandPrefix(){
        return commandPrefix;
    }

    /**
     * Sets the prefix used to execute commands. Defaults to "!"
     * @param newPrefix - New prefix to use
     */
    public static void setCommandPrefix(String newPrefix){
        commandPrefix = newPrefix;
    }

    public DiscordBot getBot(){
        return this.bot;
    }

    @EventSubscriber
    public void onMessageReceived(MessageReceivedEvent event){
        DiscordBot bot = DiscordBot.getInstance(event.getMessage().getGuild());
        if(bot == null){
            System.err.println("Could not find bot for guild " + event.getMessage().getGuild().getID());
            return;
        }

        IMessage message = event.getMessage();
        IChannel channel = message.getChannel();
        
        String content = event.getMessage().getContent();

        if(!content.startsWith(commandPrefix)) return;

        DiscordUtil.deleteMessage(message, 2000L);
        
        String command = content.split(" ")[0].substring(1);

        for(BotCommand c : bot.getFeaturesOfType(BotCommand.class)){
            if(command.equalsIgnoreCase(c.getName(bot.getLocale()))){
                if(!DiscordUtil.userHasPermission(message.getAuthor(), message.getGuild(), c.getRequiredPermissions())) return;

                String[] args = EMPTY;
                if(content.contains(" ")){
                    args = Util.parseQuotes(content.substring(content.indexOf(' ') + 1).split(" "));
                }

                bot.lastEvent = event;
                try{
                    c.execute(bot, message, args);
                }
                catch(Exception e){
                    bot.logo(Level.ERROR, "Exception while executing command '%s': %s\nMessage: %s\nChannel ID: %s\nUser ID: %s\nAt: %s", 
                            c, 
                            e.getClass(), 
                            e.getMessage(), 
                            channel, 
                            message.getAuthor(), 
                            e.getStackTrace()[0]);
                    return;
                }
                
                bot.logo(Level.INFO, "%s executed command '%s' in channel %s with arguments: %s", 
                        new LogWrapper(message.getAuthor()), 
                        c.name, 
                        new LogWrapper(channel), 
                        String.join(", ", args));
                return;
            }
        }
    }
}
