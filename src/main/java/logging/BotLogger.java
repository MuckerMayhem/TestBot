package logging;

import bot.DiscordBot;
import gui.LogTextPane;

import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BotLogger{

    private static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("HH:mm:ss");
    
    private DiscordBot bot;
    
    private List<PrintStream> outputs = new ArrayList<>();
    private List<LogTextPane> panes = new ArrayList<>();
    
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
    
    public void addOutput(LogTextPane pane){
        this.panes.add(pane);
    }
    
    public void logf(Level level, String message, Object... args){
        log(level, String.format(message, args));
    }
    
    public void log(Level level, String message){
        for(PrintStream s : this.outputs) 
            s.printf("%s %s: %s", timestamp(), level.getColorCode() + level.getPrefix(), message + "\n");
        
        for(LogTextPane p : this.panes)
            p.println(String.format("%s %s: %s", timestamp(), level.getPrefix(), message + "\n"));
    }
    
    public enum Level{

        INFO("\u001B[0m", "[INFO]"),
        WARN("\u001B[33m", "[WARN]"),
        ERROR("\u001B[31m", "[ERROR]"),
        DEBUG("\u001B[32m", "[DEBUG]");

        private String colorCode;
        private String prefix;

        Level(String colorCode, String prefix){
            this.colorCode = colorCode;
            this.prefix = prefix;
        }

        private String getColorCode(){
            return this.colorCode;
        }
        
        private String getPrefix(){
            return this.prefix;
        }
    }
}
