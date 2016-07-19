package bot.feature.command;

import bot.DiscordBot;
import bot.feature.BotFeature;
import bot.locale.Locale;
import bot.locale.LocaleHandler;
import bot.locale.Message;
import bot.locale.MessageBuilder;
import bot.settings.BooleanSetting;
import bot.settings.Setting;
import org.apache.commons.lang3.Validate;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.Permissions;

import java.io.File;
import java.util.InputMismatchException;

//TODO: Re-implement detailed descriptions
//TODO: Possibly remake with constructor

/**
 * {@link BotCommand} is the root class of all bot commands. When extending this class, only<br>
 * override the default constructor. Call either {@link BotCommand#BotCommand(String, Permissions)} or<br>
 * {@link BotCommand#BotCommand(String)} inside your default constructor with your command's name—and, if your<br>
 * command requires specific {@link Permissions}, the permissions.<br>
 * <br>
 * Note that the instance created when registering your command is not the<br>
 * one used to invoke {@link BotCommand#onExecute(DiscordBot, IMessage, String[])},<br>
 * a new instance of your command is created using the default constructor every time the command is executed.
 */
public abstract class BotCommand extends BotFeature{

    private Permissions permissions;
    
    private MessageBuilder builder;
    
    private DiscordBot bot;

    /**
     * @apiNote <i>Do not use this constructor. Use the default constructor and call super to this one</i>
     */
    public BotCommand(String name, Permissions permissions){
        super(name);
        Validate.notNull(permissions, "Permissions can not be null");
        this.permissions = permissions;
    }

    /**
     * @apiNote <i>Do not use this constructor. Use the default constructor and call super to this one</i>
     */
     public BotCommand(String name){
        super(name);
        this.permissions = Permissions.SEND_MESSAGES;
    }
    
    /**
     * Called when this command is executed by a user
     * @param bot Bot belonging to the guild the command was executed in
     * @param message {@link IMessage} that executed this command
     * @param args Arguments of the command
     * @throws Exception If an exception is thrown in any implementation of this method.<br>
     *     The exception is logged by the bot's logger
     */
    protected abstract void onExecute(DiscordBot bot, IMessage message, String[] args) throws Exception;

    /**
     * Gets the permissions a user must have in order to execute this command
     * @return Required permissions for this command
     */
    public Permissions getRequiredPermissions(){
        return this.permissions;
    }
    
    /**
     * Gets the name of this Command in the specified locale
     * @return The localized name of this command
     * @param locale Locale to localize the name to
     */
    @Override
    public String getName(Locale locale){
        return LocaleHandler.get(locale).getLocalizedName(this);
    }

    @Override
    public String getTypeName(){
        return "Command";
    }
    
    public String getPrettyName(Locale locale){
        return LocaleHandler.get(locale).getPrettyName(this);
    }
    /**
     * Gets the description of this Command in the specified locale
     * @return The description name of this command
     * @param locale Locale to localize the description to
     */
    @Override
    public String getDescription(Locale locale){
        return LocaleHandler.get(locale).getLocalizedDescription(this);
    }

    /**
     * Gets the handle of this Command in the specified locale
     * @return The localized handle of this command
     * @param locale Locale to localize the handle to
     */
    public String getHandle(Locale locale){
        return CommandHandler.getCommandPrefix() + getName(locale);
    }

    //TODO: Re-implement and localize descriptions
    public String getDetailedDescription(){
        return "No detailed description for this command";
    }
    
    /**
     * @return The localized name of this command
     */
    protected String getName(){
        return getName(getLocale());
    }

    protected String getPrettyName(){
        return getPrettyName(getLocale());
    }
    
    /**
     * @return The localized handle (prefix + name) of this command
     */
    protected String getHandle(){
        return getHandle(getLocale());
    }

    /**
     * @return The localized description of this command
     */
    protected String getDescription(){
        return getDescription(getLocale());
    }

    /**
     * @return The localized arguments of this command
     */
    protected String[] getLocalArgs(){
        return this.bot.getLocaleHandler().getLocalizedArguments(this);
    }

    /**
     * @return The bot belonging to the guild this command was executed in
     */
    protected DiscordBot getBot(){
        return this.bot;
    }

    /**
     * @return The guild this command was executed in
     */
    protected IGuild getGuild(){
        return this.bot.getGuild();
    }

    /**
     * @return The locale set for the guild this command was executed in
     */
    protected Locale getLocale(){
        return this.bot.getLocale();
    }

    /**
     * Gets a data file from the guild's data folder
     * @param name Name of the file
     * @return A File with the specified name, from the data folder
     */
    protected File getDataFile(String name){
        return this.bot.getDataFile(name);
    }

    /**
     * @return A pre-constructed {@link MessageBuilder} for this command
     */
    protected MessageBuilder messageBuilder(){
        return this.builder;
    }

    /**
     * Builds a message in the proper locale. Tokens in the localized message string <i>(Such as $1, $2)</i> are replaced with<br>
     * the arguments in their respective order. {@link Object#toString()} is used on each argument when replacing.
     * @param message {@link Message} to build
     * @param args Objects replacing specific tokens in the string
     * @return The built message
     */
    protected String buildMessage(Message message, Object... args){
        return this.builder.buildMessage(message, args);
    }

    /**
     * Builds a message in the proper locale.
     * @param message {@link Message} to build
     * @return The built message
     */
    protected String buildMessage(Message message){
        return this.builder.buildMessage(message);
    }
    
    protected Object getUserSetting(String userId, Setting setting){
        return this.bot.getUserSetting(userId, setting);
    }
    
    protected boolean checkSetting(String userId, BooleanSetting setting){
        return this.bot.checkSetting(userId, setting);
    }

    /**
     * Creates a new instance of this command and executes it.<br>
     * @param bot DiscordBot with guild matching the guild that the command was executed in
     * @param message IMessage that caused this command to be executed
     * @param args Arguments to execute the command with
     */
    void execute(DiscordBot bot, IMessage message, String[] args) throws Exception{
        if(!bot.getGuild().getID().equals(message.getGuild().getID()))
            throw new InputMismatchException("Guild mismatch between bot and message");
          
        
        BotCommand instance = newInstance(bot);
        if(instance == null) return;
        
        instance.construct(this, bot).onExecute(bot, message, args);
    }

    private BotCommand newInstance(DiscordBot bot){
        try{
            return this.getClass().newInstance();
        }
        catch(IllegalAccessException | InstantiationException e){
            bot.log(e, "Could not create new " + this.getClass().getSimpleName() + " instance");
            return null;
        }
    }
    
    private BotCommand construct(BotCommand base, DiscordBot executor){
        this.bot = executor;
        this.builder = new MessageBuilder(executor.getLocale());
        
        return this;
    }
}
