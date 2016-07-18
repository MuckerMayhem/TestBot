package bot;

import bot.event.BotEventDispatcher;
import bot.feature.BotFeature;
import bot.feature.FeatureSet;
import bot.feature.command.*;
import bot.feature.function.*;
import bot.locale.Locale;
import bot.locale.LocaleHandler;
import bot.settings.*;
import gui.BotGui;
import logging.BotLogger;
import logging.BotLogger.Level;
import sx.blah.discord.api.ClientBuilder;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.impl.events.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.Permissions;
import sx.blah.discord.handle.obj.Status.StatusType;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.MessageBuilder;
import sx.blah.discord.util.MissingPermissionsException;
import sx.blah.discord.util.RateLimitException;
import util.DiscordUtil;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static bot.feature.command.CommandHandler.registerCommand;
import static bot.feature.function.BotFunction.registerFunction;

//TODO: Separate features into sets
//TODO: Don't immediately register every feature
@SuppressWarnings("unused")
public class DiscordBot{

    private static final HashMap<IGuild, DiscordBot> instances = new HashMap<>();//All bot instances
    
    private static final UserSettingsHandler userSettingsHandler = new UserSettingsHandler(new File(getGlobalDataFolder() + File.separator + "usersettings.json"));

    //Commands
    //Admin commands
    public static final BotCommand COMMAND_PRUNE = registerCommand("prune", CommandPrune.class, Permissions.MANAGE_MESSAGES);
    public static final BotCommand COMMAND_LEAVE = registerCommand("leave", CommandLeave.class, Permissions.VOICE_MOVE_MEMBERS);
    public static final BotCommand COMMAND_TYPE = registerCommand("type", CommandType.class, Permissions.CHANGE_NICKNAME);
    public static final BotCommand COMMAND_SHOWHELP = registerCommand("showhelp", CommandShowHelp.class, Permissions.MANAGE_MESSAGES);
    public static final BotCommand COMMAND_RESTART = registerCommand("restart", CommandRestart.class, Permissions.MANAGE_SERVER);

    //Utility commands
    public static final BotCommand COMMAND_HELP = registerCommand("help", CommandHelp.class, Permissions.SEND_MESSAGES);
    public static final BotCommand COMMAND_TEST = registerCommand("test", CommandTest.class, Permissions.SEND_MESSAGES);
    public static final BotCommand COMMAND_SETTING = registerCommand("setting", CommandSetting.class, Permissions.SEND_MESSAGES);
    public static final BotCommand COMMAND_RABBIT = registerCommand("rabbit", CommandRabbit.class, Permissions.SEND_MESSAGES);

    //Fun commands
    public static final BotCommand COMMAND_MEME = registerCommand("meme", CommandMeme.class, Permissions.SEND_MESSAGES);
    public static final BotCommand COMMAND_ROLL = registerCommand("roll", CommandDiceRoll.class, Permissions.SEND_MESSAGES);
    public static final BotCommand COMMAND_SOUND = registerCommand("sound", CommandSound.class, Permissions.VOICE_SPEAK);
    public static final BotCommand COMMAND_BOOTY = registerCommand("(", CommandBooty.class, Permissions.VOICE_SPEAK);//( ͡° ͜ʖ ͡°)
    public static final BotCommand COMMAND_WAIFU = registerCommand("waifu", CommandWaifu.class, Permissions.SEND_MESSAGES);

    //Game command
    public static final BotCommand COMMAND_GAME = registerCommand("game", CommandGame.class, Permissions.SEND_MESSAGES);

    //Clear command
    public static final ThreadedCommand COMMAND_CLEAR = (ThreadedCommand) registerCommand("clear", ThreadedCommandClear.class, Permissions.MANAGE_SERVER);


    //Functions
//    public static final BotFunction FUNCTION_HIGHNOON = registerFunction("highnoon", FunctionHighNoon.class);
    public static final BotFunction FUNCTION_BREAKWALLS = registerFunction("breakwalls", FunctionBreakMessages.class);
    public static final BotFunction FUNCTION_EAT = registerFunction("eat", FunctionEatFood.class);
    public static final BotFunction FUNCTION_YTTIME = registerFunction("yttime", FunctionGetVideoTime.class);
    public static final BotFunction FUNCTION_WELCOME = registerFunction("welcome", FunctionWelcomeBack.class);


    //Settings
    public static final StringSetting SETTING_LOCALE = new StringSetting("locale", "en", true);
    public static final StringSetting SETTING_HOME = new StringSetting("bot_home", "", true);
    public static final BooleanSetting SETTING_ANONYMOUS = new BooleanSetting("anonymous_logging", true, false);
    
    //Feature sets
//    public static final FeatureSet MEME = BotFeature.registerFeature();

    private static final long MESSAGE_TIME_SHORT = 3500L;
    private static final long MESSAGE_TIME_LONG  = 6000L;

    private static IDiscordClient client;
    private static DiscordBot instance;

    private final IGuild guild;
    
    private final ArrayList<BotFeature> features = new ArrayList<>();
    
    public MessageReceivedEvent lastEvent;

    private BotLogger logger;
    
    private SingleSettingsHandler serverSettingsHandler;
    private LocaleHandler localeHandler;
    
    private BotEventDispatcher eventDispatcher;

    private String home;
    
    public DiscordBot(){
        this.guild = null;
    }
    
    public DiscordBot(IGuild guild){
        this.guild = guild;
        instances.put(guild, this);
        init();
    }

    //TODO: Add more arguments
    public static void main(String[] args) throws Exception{
        if(args.length >= 2 && args[1].equals("-nogui")){
            BotGui.disableGui();
        }
        
        if(!BotGui.isDisabled()){
            new BotGui("TestBot").setVisible(true); 
        }
        
        if(args.length == 0){
            System.out.println("Please run again with correct arguments... (Missing token)");
            System.exit(0);
        }

        if(!getGlobalDataFolder().exists()){
           System.out.println("Data folder not found. Creating directory...");
            if(getGlobalDataFolder().mkdir())
                System.out.println("Done!");
        }

        try{
            System.out.println("Client logging in...");
            client = login(args[0]);
        }
        catch(DiscordException e){
            System.err.print(e.getMessage());
            return;
        }

        System.out.println("Client successfully logged in");

        client.getDispatcher().registerListener(new EventListener());
        client.getDispatcher().registerListener(new CommandHandler());
        BotFunction.getAllRegisteredFunctions().forEach(client.getDispatcher()::registerListener);

        instance = new DiscordBot();

        try{
            userSettingsHandler.loadSettings();//All settings should be registered by this time, and can now be loaded
        }
        catch(IOException e){
            getGuildlessInstance().log(e, "Failed to load user settings.");
        }
    }

    public void init(){
        if(!getDataFolder().exists()){
            System.out.println("Creating data folder for guild " + this.guild.getID() + "...");
            if(getDataFolder().mkdir())
                System.out.println("Done!");
        }

        this.logger = new BotLogger(this);
        
        if(BotGui.isDisabled())
            this.logger.addOutput(System.out);
        else
            this.logger.addOutput(BotGui.getGui().getLogPanel().getLogPanel(this.guild));
        
        this.serverSettingsHandler = new SingleSettingsHandler(getDataFile("settings.json"));
        this.serverSettingsHandler.registerNewSetting(SETTING_LOCALE);
        this.serverSettingsHandler.registerNewSetting(SETTING_HOME);
        this.serverSettingsHandler.registerNewSetting(SETTING_ANONYMOUS);

        this.eventDispatcher = new BotEventDispatcher(this);
                
        CommandHandler.getAllRegisteredCommands().forEach(this::enableFeature);
        BotFunction.getAllRegisteredFunctions().forEach(this::enableFeature);

        try{
            getServerSettingsHandler().loadSettings();
        }
        catch(IOException e){
            log(e, "Could not load server settings");
        }

        Locale locale = Locale.getFromCode((String) getServerSettingsHandler().getSetting(SETTING_LOCALE));
        this.localeHandler = LocaleHandler.get(locale == null ? Locale.ENGLISH : locale);

        this.home = (String) getServerSettingsHandler().getSetting(SETTING_HOME);
        
        log(Level.INFO, "Bot initialized.");
    }

    public static Set<IGuild> getGuilds(){
        return instances.keySet();
    }
    
    public static Collection<DiscordBot> getInstances(){
        return instances.values();
    }
    
    public static DiscordBot getGuildlessInstance(){
        return instance;
    }

    public static DiscordBot getInstance(IGuild guild){
        return instances.get(guild);
    }

    public static DiscordBot getInstance(String guildId){
        return getInstance(client.getGuildByID(guildId));
    }
    
    @Deprecated
    public static IDiscordClient login(String email, String password) throws DiscordException{
        return new ClientBuilder().withLogin(email, password).login();
    }

    public static IDiscordClient login(String token) throws DiscordException{
        return new ClientBuilder().withToken(token).login();
    }

    public static IDiscordClient getClient(){
        return client;
    }

    public static File getGlobalDataFolder(){
        return new File(System.getProperty("user.dir") + File.separator + "data");
    }

    public static UserSettingsHandler getUserSettingsHandler(){
        return userSettingsHandler;
    }

    public Object getUserSetting(String userId, Setting setting){
        return getUserSettingsHandler().getUserSetting(userId, setting);
    }

    public boolean anonymous(){
        return (Boolean) getServerSettingsHandler().getSetting(SETTING_ANONYMOUS);
    }
    
    public boolean checkSetting(String userId, BooleanSetting setting){
        return (Boolean) getUserSetting(userId, setting);
    }

    public File getDataFolder(){
        return new File(getGlobalDataFolder() + File.separator + this.guild.getID());
    }

    public File getDataFile(String name){
        return new File(getDataFolder() + File.separator + name);
    }

    public IGuild getGuild(){
        return this.guild;
    }

    public BotLogger getLogger(){
        return this.logger;
    }
    
    public LocaleHandler getLocaleHandler(){
        return this.localeHandler;
    }

    public SingleSettingsHandler getServerSettingsHandler(){
        return this.serverSettingsHandler;
    }

    public BotEventDispatcher getEventDispatcher(){
        return this.eventDispatcher;
    }
    
    public Locale getLocale(){
        return getLocaleHandler().getLocale();
    }

    public String getUsername(){
        return client.getOurUser().getName();
    }

    public String getGame(){
        if(client.getOurUser().getStatus().getType() == StatusType.GAME)
            return client.getOurUser().getStatus().getStatusMessage();

        return null;
    }

    public IChannel getHome(){
        for(IChannel c : this.guild.getChannels()){
            if(c.getName().equals(this.home)) return c;
        }
        return null;
    }

    public List<BotFeature> getFeatures(){
        return this.features;
    }

    public List<BotCommand> getCommands(){
        return this.features.stream()
                .filter(f -> f instanceof BotCommand)
                .map(c -> (BotCommand) c)
                .collect(Collectors.toList());
    }

    public List<BotFunction> getFunctions(){
        return this.features.stream()
                .filter(f -> f instanceof BotFunction)
                .map(c -> (BotFunction) c)
                .collect(Collectors.toList());
    }

    public boolean featureEnabled(BotFeature feature){
        return this.features.contains(feature);
    }

    public void type(IChannel channel, String message, Long typingTime){
        channel.toggleTypingStatus();

        TimerTask task = new TimerTask(){
            @Override
            public void run(){
                say(channel, message);
            }
        };
        new Timer().schedule(task, typingTime);
    }

    public void type(IChannel channel, String message){
        type(channel, message, message.length() * 100L);
    }

    public void type(String message){
        if(getHome() == null) return;
        type(getHome(), message, message.length() * 100L);
    }

    /**
     * Sends a message from this bot in the specified channel<br>
     * Message is automatically deleted after the specified amount of time (in millis)<br>
     * If the time is less than or equal to zero, the message will not be deleted
     * @param channel Channel to speak in
     * @param message Message to send
     * @param time Time before this message is deleted
     */
    public IMessage say(IChannel channel, String message, Long time){
        if(channel != null){
            try{
                IMessage botMessage = new MessageBuilder(getClient()).withChannel(channel).withContent(message).build();
                if(time > 0)
                    DiscordUtil.deleteMessage(botMessage, time);
                return botMessage;
            }
            catch(RateLimitException e){
                try{
                    Thread.sleep(e.getRetryDelay());//Try again if rate limited
                }
                catch(InterruptedException e1){
                    log(e1);
                }
                return say(channel, message, time);
            }
            catch(DiscordException | MissingPermissionsException e){
                log(e);
            }
        }
        return null;
    }

    /**
     * Sends a message from this bot in the bot's home channel
     * No message will be sent if the home channel is not found
     * @param message Message to send
     */
    public IMessage say(IChannel channel, String message){
        return say(channel, message, 0L);
    }

    /**
     * Sends a message from this bot in the bot's home channel
     * No message will be sent if the home channel is not found
     * @param message Message to send
     */
    public void say(String message){
        if(getHome() == null) return;
        say(getHome(), message);
    }

    /**
     * Sends a message from this bot in the last channel a user executed<br>
     * a valid command in. Valid commands are commands that are passed<br>
     * by this bot's {@link bot.feature.command.CommandHandler}<br>
     * The message is deleted after the specified time (in millis)
     *
     * @param message Message to send
     * @param time Time before this message is deleted
     */
    public void respond(String message, Long time){
        say(lastEvent.getMessage().getChannel(), message, time);
    }

    /**
     * Sends a message from this bot in the last channel a user executed<br>
     * a valid command in. Valid commands are commands that are passed<br>
     * by this bot's {@link bot.feature.command.CommandHandler}
     * @param message Message to send
     */
    public void respond(String message){
        say(lastEvent.getMessage().getChannel(), message);
    }

    /**
     * Sends a message from this bot in the last channel a user executed<br>
     * a valid command in. Valid commands are commands that are passed<br>
     * by this bot's {@link bot.feature.command.CommandHandler}. Message disappears after a short<br>
     * amount of time (Decided by the <i>longer</i> parameter)
     * @param message Message to send
     * @param longer Whether the message should stay for 6.0 seconds rather than 3.5
     */
    public void info(String message, boolean longer){
        respond(message, longer ? MESSAGE_TIME_LONG : MESSAGE_TIME_SHORT);
    }

    /**
     * Sends a message from this bot in the last channel a user executed<br>
     * a valid command in. Valid commands are commands that are passed<br>
     * by this bot's {@link bot.feature.command.CommandHandler}. Message disappears after 3.5 seconds
     * @param message Message to send
     */
    public void info(String message){
        respond(message, MESSAGE_TIME_SHORT);
    }

    public void setHome(String home){
        this.home = home;
    }

    public void enableFeature(BotFeature feature){
        this.features.add(feature);
        feature.onEnable(this);
    }
    
    public void enableFeatures(FeatureSet features){
        features.forEach(this::enableFeature);
    }

    public void disableFeature(BotFeature feature){
        this.features.remove(feature);
        feature.onDisable(this);
    }
    
    public void disableFeatures(FeatureSet features){
        features.forEach(this::disableFeature);
    }
    
    public void logo(Level level, String message, Object... args){
        if(this.logger == null) return;
        
        this.logger.logo(level, message, args);
    }
    
    public void logf(Level level, String message, Object... args){
        if(this.logger == null) return;

        this.logger.logf(level, message, args);
    }
    
    public void log(Level level, String message){
        if(this.logger == null) return;
        
        this.logger.log(level, message);
    }
    
    public void log(Exception e, String message){
        if(this.logger == null){
            System.err.print(message + ": " + e.getClass().getSimpleName() + "\nMessage: " + e.getMessage());
            return;
        }
        
        this.logger.log(e, message);
    }
    
    public void log(Exception e){
        if(this.logger == null){
            System.err.print(e.getClass().getSimpleName() + "\nMessage: " + e.getMessage());
            return;
        }
        
        this.logger.log(e);
    }
}
