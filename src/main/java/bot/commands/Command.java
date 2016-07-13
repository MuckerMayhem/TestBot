package bot.commands;

import bot.DiscordBot;
import bot.locale.Locale;
import bot.locale.LocaleHandler;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.Permissions;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.MissingPermissionsException;
import sx.blah.discord.util.RateLimitException;

import java.io.IOException;
import java.util.List;

public abstract class Command{

    Permissions permissions;

    String name;
    String description;
    String[] aliases;

    List<CommandHandler> handlers;

    Class<? extends Command> mainClass;

    private boolean debug;

    public abstract void onRegister();

    public abstract void onEnable(DiscordBot bot);

    public abstract void onDisable(DiscordBot bot);

    protected abstract void onExecute(DiscordBot bot, IMessage message, String[] args) throws RateLimitException, DiscordException, MissingPermissionsException, IOException;

    public List<CommandHandler> getHandlers(){
        return this.handlers;
    }

    /*
    public DiscordBot getBot(){
        return getHandler().getBot();
    }
    */

    public Permissions getRequiredPermissions(){
        return this.permissions;
    }

    /**
     * Gets the name this command was registered under
     * @return The name this command was registered under
     */
    public String getRegisteredName(){
        return this.name;
    }

    /**
     * Gets the name of this Command. Note that the name is localized—it will give the<br>
     * name in the currently set language (Defined by the language the bot of the<br>
     * {@link bot.commands.CommandHandler} this Command is registered to.)
     * @return The localized name of this command
     */
    public String getName(Locale locale){
        return LocaleHandler.get(locale).getLocalizedName(this);
    }

    public String getHandle(Locale locale){
        return CommandHandler.getCommandPrefix() + getName(locale);
    }

    public String getDescription(Locale locale){
        return LocaleHandler.get(locale).getLocalizedDescription(this);
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

    /*
    public UserSettingsHandler getUserSettingsHandler(){
        return getBot().getUserSettingsHandler();
    }

    public SingleSettingsHandler getServerSettingsHandler(){
        return getBot().getServerSettingsHandler();
    }

    public Object getUserSetting(String userId, Setting setting){
        return getUserSettingsHandler().getUserSetting(userId, setting);
    }
    */

    /**
     * Check the value of a {@link bot.settings.BooleanSetting}'s value
     * @param userId User to get value of setting for
     * @param setting Setting to check
     * @return
     */
    /*
    public boolean checkSetting(String userId, BooleanSetting setting){
        return (Boolean) getUserSetting(userId, setting);
    }
    */
}
