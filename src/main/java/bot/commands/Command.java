package bot.commands;

import bot.DiscordBot;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.Permissions;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.MissingPermissionsException;
import sx.blah.discord.util.RateLimitException;

import java.io.IOException;

public abstract class Command{

    Permissions permissions;

    String name;
    String description;
    String[] aliases;

    CommandHandler commandHandler;

    Class<? extends Command> mainClass;

    private boolean debug;

    abstract void onExecute(DiscordBot bot, IMessage message, String[] args) throws RateLimitException, DiscordException, MissingPermissionsException, IOException;

    public Permissions getRequiredPermissions(){
        return this.permissions;
    }

    public String getName(){
        return this.name;
    }

    public String getDescription(){
        return this.description;
    }

    public String getDetailedDescription(){
        return "No detailed description for this command";
    }

    public String[] getAliases(){
        return this.aliases;
    }

    public Class<? extends Command> getMainClass(){
        return this.mainClass;
    }

    public boolean debug(){
        return this.debug;
    }

    public void setDebug(boolean debug){
        this.debug = debug;
    }
}
