package bot.commands;

import bot.DiscordBot;
import bot.settings.BooleanSetting;
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

    protected abstract void onRegister();

    protected abstract void onExecute(DiscordBot bot, IMessage message, String[] args) throws RateLimitException, DiscordException, MissingPermissionsException, IOException;

    public Permissions getRequiredPermissions(){
        return this.permissions;
    }

    public String getName(){
        return this.name;
    }

    public String getHandle(){
        return this.commandHandler.getCommandPrefix() + this.name;
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

    /**
     * Check the value of a {@link bot.settings.BooleanSetting}'s value
     * @param userId User to get value of setting for
     * @param setting Setting to check
     * @return
     */
    public boolean checkSetting(String userId, BooleanSetting setting){
        return (Boolean) this.commandHandler.bot.getSettingsHandler().getUserSetting(userId, setting);
    }
}
