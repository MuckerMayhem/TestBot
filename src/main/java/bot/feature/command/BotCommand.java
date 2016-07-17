package bot.feature.command;

import bot.DiscordBot;
import bot.feature.BotFeature;
import bot.locale.Locale;
import bot.locale.LocaleHandler;
import bot.locale.Message;
import bot.locale.MessageBuilder;
import bot.settings.BooleanSetting;
import bot.settings.Setting;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.Permissions;

import java.io.File;
import java.util.InputMismatchException;
import java.util.List;

public abstract class BotCommand extends BotFeature{

    Permissions permissions;
    
    String description;
    String[] aliases;

    private List<CommandHandler> handlers;
    
    private Class<? extends BotCommand> mainClass;
    
    private MessageBuilder builder;
    
    private DiscordBot bot;
    private IMessage message;
    private String[] args;
    
    private boolean debug;

    protected abstract void onExecute(DiscordBot bot, IMessage message, String[] args) throws Exception;

    public List<CommandHandler> getHandlers(){
        return this.handlers;
    }

    public Permissions getRequiredPermissions(){
        return this.permissions;
    }
    
    /**
     * Gets the name of this Command in the specified locale
     * @return The localized name of this command
     * @param locale Locale to localize the name to
     */
    public String getName(Locale locale){
        return LocaleHandler.get(locale).getLocalizedName(this);
    }

    /**
     * Gets the handle of this Command in the specified locale
     * @return The localized handle of this command
     * @param locale Locale to localize the handle to
     */
    public String getHandle(Locale locale){
        return CommandHandler.getCommandPrefix() + getName(locale);
    }

    /**
     * Gets the description of this Command in the specified locale
     * @return The description name of this command
     * @param locale Locale to localize the description to
     */
    public String getDescription(Locale locale){
        return LocaleHandler.get(locale).getLocalizedDescription(this);
    }

    public String getDetailedDescription(){
        return "No detailed description for this command";
    }

    public String[] getAliases(){
        return this.aliases;
    }

    public Class<? extends BotCommand> getMainClass(){
        return this.mainClass;
    }

    public boolean debug(){
        return this.debug;
    }

    public void setDebug(boolean debug){
        this.debug = debug;
    }

    protected String getName(){
        return getName(getLocale());
    }
    
    protected String getHandle(){
        return getHandle(getLocale());
    }
    
    protected String getDescription(){
        return getDescription(getLocale());
    }
    
    protected String[] getLocalArgs(){
        return this.bot.getLocaleHandler().getLocalizedArguments(this);
    }
    
    protected DiscordBot getBot(){
        return this.bot;
    }

    protected IGuild getGuild(){
        return this.bot.getGuild();
    }
    
    protected IMessage getMessage(){
        return this.message;
    }

    protected String[] getArgs(){
        return this.args;
    }
    
    protected Locale getLocale(){
        return this.bot.getLocale();
    }
    
    protected File getDataFile(String name){
        return this.bot.getDataFile(name);
    }
    
    protected MessageBuilder messageBuilder(){
        return this.builder;
    }
    
    protected String buildMessage(Message message, Object... args){
        return this.builder.buildMessage(message, args);
    }
    
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
     * Executes (and completes the instance of) this command.<br>
     * Executed commands can use all inherited methods
     * @param bot DiscordBot with guild matching the guild that the command was executed in
     * @param message IMessage that caused this command to be executed
     * @param args Arguments to execute the command with
     */
    void execute(DiscordBot bot, IMessage message, String[] args) throws Exception{
        if(!bot.getGuild().getID().equals(message.getGuild().getID()))
            throw new InputMismatchException("Guild mismatch between bot and message");
            
        this.bot = bot;
        this.message = message;
        this.args = args;
        this.builder = new MessageBuilder(bot.getLocale());
        onExecute(bot, message, args);
    }

    BotCommand newInstance(){
        try{
            return this.getClass().newInstance();
        }
        catch(InstantiationException | IllegalAccessException e){
            e.printStackTrace();
        }
        return null;
    }
}
