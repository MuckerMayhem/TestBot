package logging;

import bot.DiscordBot;
import bot.feature.command.BotCommand;
import gui.window.main.log.LogTextPane;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;

import java.awt.*;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BotLogger{

    private static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("HH:mm:ss");
    
    private final DiscordBot bot;

    private final List<PrintStream> outputs = new ArrayList<>();
    private final List<LogTextPane> panes = new ArrayList<>();
    
    private String lastUser;
    
    public BotLogger(DiscordBot bot){
        this.bot = bot;
    }
    
    public BotLogger(DiscordBot bot, PrintStream out){
        this.bot = bot;
        this.outputs.add(out);
    }
    
    public BotLogger(DiscordBot bot, LogTextPane pane){
        this.bot = bot;
        this.panes.add(pane);
    }
    
    private static String timestamp(){
        return TIME_FORMAT.format(new Date());
    }
    
    public DiscordBot getBot(){
        return this.bot;
    }
    
    public void addOutput(PrintStream out){
        this.outputs.add(out);
    }
    
    public void removeOutput(PrintStream out){
        this.outputs.remove(out);
    }
    
    public void addOutput(LogTextPane pane){
        this.panes.add(pane);
    }
    
    public void removeOutput(LogTextPane pane){
        this.panes.remove(pane);
    }

    /**
     * Logs a formatted message at the specified logging level.<br>
     * Formats guilds, channels, and users according to the anonymity setting of the bot<br>
     * Has support for a variety of other types and formats them accordingly.<br>
     * This method saves the most recently formatted IUser's ID for reference
     * @param level Severity level
     * @param message Message to log
     * @param args Arguments to be formatted into the message
     */
    //TODO: Find a better way of doing this so this spaghetti can be trashed
    public void logo(Level level, String message, Object... args){
        for(int i = 0;i < args.length;i++){
            Object o = args[i];
            
            if(o instanceof IGuild)
                args[i] = this.bot.anonymous() ? ((IGuild) o).getID() : ((IGuild) o).getName();
            else if(o instanceof IChannel)
                args[i] = ((IChannel) o).getID() + (this.bot.anonymous() ? "" : " (" + ((IChannel) o).getName() + ")");
            else if(o instanceof IUser){
                args[i] = ((IUser) o).getID() + (this.bot.anonymous() ? "" : " (" + ((IUser) o).getName() + ")");
                this.lastUser = ((IUser) o).getID();
                bot.getEventDispatcher().dispatchEvent(new UserLoggedEvent(this.bot, (IUser) o));
            }
            else if(o instanceof IMessage)
                args[i] = ((IMessage) o).getContent();
            else if(o instanceof BotCommand)
                args[i] = ((BotCommand) o).name;
            else if(o instanceof Class<?>)
                args[i] = ((Class<?>) o).getSimpleName();
            else if(o instanceof LogWrapper){
                Object w = ((LogWrapper) o).getObject();
                if(w instanceof IUser){
                    args[i] = this.bot.anonymous() ? ((IUser) w).getID() : ((IUser) w).getName();
                    this.lastUser = ((IUser) w).getID();
                    bot.getEventDispatcher().dispatchEvent(new UserLoggedEvent(this.bot, (IUser) w));
                }
                else if(w instanceof IChannel)
                    args[i] = this.bot.anonymous() ? ((IChannel) w).getID() : ((IChannel) w).getName();
            }
        }
        logf(level, message, args);
    }

    /**
     * Logs a formatted message at the specified level
     * @param level Severity level
     * @param message Message to log
     * @param args Arguments to be formatted into the message
     */
    public void logf(Level level, String message, Object... args){
        log(level, String.format(message, args));
    }
    
    public void log(Exception e, String message){
        logf(Level.ERROR, "%s: %s\nMessage: %s\nGuild ID: %s\nAt: %s", 
                message, e.getClass().getName(), 
                e.getMessage(), 
                this.bot.getGuild().getID(), 
                e.getStackTrace()[0]);
    }
    
    public void log(Exception e){
        log(e, "");
    }
    
    /**
     * Logs a message at the specified level
     * @param level Severity level
     * @param message Message to log
     */
    public void log(Level level, String message){
        for(PrintStream s : this.outputs) 
            s.printf("%s %s: %s", timestamp(), level.getColorCode() + level.getPrefix(), message + "\n");
        
        for(LogTextPane p : this.panes)
            p.println(level.getColor(), String.format("%s %s: %s", timestamp(), level.getPrefix(), message));
    }
    
    public String getLastUser(){
        return this.lastUser;
    }
    
    public enum Level{

        INFO("\u001B[0m", Color.DARK_GRAY, "[INFO]"),
        WARN("\u001B[33m", Color.ORANGE, "[WARN]"),
        ERROR("\u001B[31m", Color.RED, "[ERROR]"),
        DEBUG("\u001B[32m", Color.GREEN, "[DEBUG]");

        private final String colorCode;
        private final Color color;
        private final String prefix;

        Level(String colorCode, Color color, String prefix){
            this.colorCode = colorCode;
            this.color = color;
            this.prefix = prefix;
        }

        private String getColorCode(){
            return this.colorCode;
        }
        
        private Color getColor(){
            return this.color;
        }
        
        private String getPrefix(){
            return this.prefix;
        }
    }
}
