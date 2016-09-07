package bot;

import bot.ArgumentParser.Flag;
import bot.ArgumentParser.Value;
import bot.event.BotEventDispatcher;
import bot.event.EventRouter;
import bot.feature.BotFeature;
import bot.feature.FeatureSet;
import bot.feature.ToggleableFeatureSet;
import bot.feature.command.*;
import bot.feature.function.*;
import bot.locale.Locale;
import bot.locale.LocaleHandler;
import bot.settings.*;
import gui.BotGui;
import logging.BotLogger;
import logging.BotLogger.Level;
import org.apache.commons.io.FileUtils;
import sx.blah.discord.api.ClientBuilder;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.impl.events.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.util.*;
import util.DiscordUtil;
import util.Util;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static bot.feature.BotFeature.registerFeature;

@SuppressWarnings({"unused", "ResultOfMethodCallIgnored"})
public class DiscordBot{

    private static final HashMap<IGuild, DiscordBot> instances = new HashMap<>();//All bot instances
    
    private static final UserSettingsHandler userSettingsHandler = new UserSettingsHandler(new File(getGlobalDataFolder() + File.separator + "usersettings.json"));

    //Commands
    //Admin commands
    public static final BotCommand COMMAND_PRUNE    = (BotCommand) registerFeature(new CommandPrune());
    public static final BotCommand COMMAND_LEAVE    = (BotCommand) registerFeature(new CommandLeave());
    public static final BotCommand COMMAND_TYPE     = (BotCommand) registerFeature(new CommandType());
    public static final BotCommand COMMAND_RESTART  = (BotCommand) registerFeature(new CommandRestart());
    public static final BotCommand COMMAND_FEATURE  = (BotCommand) registerFeature(new CommandFeature());

    //Utility commands
    public static final BotCommand COMMAND_HELP     = (BotCommand) registerFeature(new CommandHelp());
    public static final BotCommand COMMAND_TEST     = (BotCommand) registerFeature(new CommandTest());
    public static final BotCommand COMMAND_SETTING  = (BotCommand) registerFeature(new CommandSetting());
    public static final BotCommand COMMAND_RABBIT   = (BotCommand) registerFeature(new CommandRabbit());
    
    //Fun commands
    public static final BotCommand COMMAND_MEME     = (BotCommand) registerFeature(new CommandMeme());
    public static final BotCommand COMMAND_ROLL     = (BotCommand) registerFeature(new CommandDiceRoll());
    public static final BotCommand COMMAND_WAIFU    = (BotCommand) registerFeature(new CommandWaifu());
    public static final BotCommand COMMAND_SOUND    = (BotCommand) registerFeature(new CommandSound());
    public static final BotCommand COMMAND_BOOTY    = (BotCommand) registerFeature(new CommandBooty());
    public static final BotCommand  COMMAND_WOT     = (BotCommand) registerFeature(new CommandWot());

    //Threaded commands
    public static final ThreadedCommand COMMAND_GAME  = (ThreadedCommand) registerFeature(new ThreadedCommandGame());
    public static final ThreadedCommand COMMAND_CLEAR = (ThreadedCommand) registerFeature(new ThreadedCommandClear());
    

    //Functions
    public static final BotFunction FUNCTION_BREAKWALLS = (BotFunction) registerFeature(new FunctionBreakMessages());
    public static final BotFunction FUNCTION_EAT        = (BotFunction) registerFeature(new FunctionEatFood());
    public static final BotFunction FUNCTION_YTTIME     = (BotFunction) registerFeature(new FunctionGetVideoTime());
    public static final BotFunction FUNCTION_WELCOME    = (BotFunction) registerFeature(new FunctionWelcomeBack());


    //Settings
    public static final StringSetting  SETTING_LOCALE     = new StringSetting("locale", "en", true);
    public static final StringSetting  SETTING_HOME       = new StringSetting("bot_home", "", true);
    public static final BooleanSetting SETTING_ANONYMOUS  = new BooleanSetting("anonymous_logging", true, false);
    
    //Feature sets
    public static final FeatureSet CORE       = (FeatureSet) BotFeature.registerFeature(new FeatureSet("core", COMMAND_HELP, COMMAND_SETTING, COMMAND_FEATURE, COMMAND_RESTART));
    public static final FeatureSet MANAGEMENT = (FeatureSet) BotFeature.registerFeature(new FeatureSet("management", COMMAND_CLEAR, COMMAND_PRUNE));
    public static final FeatureSet MEMES      = (FeatureSet) BotFeature.registerFeature(new ToggleableFeatureSet("memes", COMMAND_MEME, COMMAND_BOOTY));

    private static final long MESSAGE_TIME_SHORT = 3500L;
    private static final long MESSAGE_TIME_LONG  = 6000L;

    private static String username = "TestBot";
    private static String avatarImg;
    private static String gameStatus;
    
    private static IDiscordClient client;
    private static DiscordBot instance;

    public MessageReceivedEvent lastEvent;
    
    private final IGuild guild;
    
    private final ArrayList<BotFeature> features = new ArrayList<>();

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
    
    public static void main(String[] args){
        new ArgumentParser().withArgument("-username", new Value(){
            @Override
            public void handle(Object value){
                DiscordBot.username = (String) value;
            }
        }).withArgument("-avatar", new Value(){
            @Override
            public void handle(Object value){
                DiscordBot.avatarImg = (String) value;
            }
        }).withArgument("-game", new Value(){
            @Override
            public void handle(Object value){
                DiscordBot.gameStatus = (String) value;
            }
        }).withArgument("-token", -1, new Value(){
            @Override
            public void handle(Object value){
                try{
                    System.out.println("Client logging in...");
                    client = login((String) value);
                }
                catch(DiscordException e){
                    System.out.println("Could not log in client, is your token correct?");
                    System.exit(0);
                }
                System.out.println("Client successfully logged in");
            }
        }).withArgument("-nogui", new Flag(){
            @Override
            public void handle(Object value){
                BotGui.disableGui();
            }
        }).parse(args);
        
        if(!BotGui.isDisabled())
            new BotGui(getUsername()).setVisible(true);
        
        if(client == null){
            System.out.println("Please run again with correct arguments... (Missing token)");
            System.exit(0);
        }

        if(!getGlobalDataFolder().exists()){
           System.out.println("Data folder not found. Creating directory...");
            if(getGlobalDataFolder().mkdir())
                System.out.println("Done!");
        }

        client.getDispatcher().registerListener(new EventListener());
        client.getDispatcher().registerListener(new CommandHandler());
        client.getDispatcher().registerListener(new EventRouter());

        instance = new DiscordBot();

        try{
            userSettingsHandler.loadSettings();//All settings should be registered by this time, and can now be loaded
        }
        catch(IOException e){
            getGuildlessInstance().log(e, "Failed to load user settings.");
        }
        
        new Timer().schedule(new TimerTask(){
            @Override
            public void run(){
                if(!getClient().isReady()){
                    System.out.println("Bot is no longer responsive. Restarting...");
                    main(args);
                    this.cancel();
                }
                
            }
        }, 1800000L, 1800000L);
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
        this.serverSettingsHandler.addSetting(SETTING_LOCALE);
        this.serverSettingsHandler.addSetting(SETTING_HOME);
        this.serverSettingsHandler.addSetting(SETTING_ANONYMOUS);

        this.eventDispatcher = new BotEventDispatcher(this);

        loadFeatures();

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
    
    public static String getUsername(){
        return username;
    }
    
    public static String getGameStatus(){
        return gameStatus;
    }
    
    public static Image getAvatarImg(){
        return avatarImg == null ? null : Image.forFile(new File(avatarImg));
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
    
    public IChannel getHome(){
        for(IChannel c : this.guild.getChannels()){
            if(c.getName().equals(this.home)) return c;
        }
        return null;
    }

    public List<BotFeature> getFeatures(){
        return this.features;
    }

    @SuppressWarnings("unchecked")
    public <T extends BotFeature> List<T> getFeaturesOfType(Class<T> type){
        return this.features.stream()
                .filter(f -> type.isAssignableFrom(f.getClass()))
                .map(f -> (T) f)
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
    public void say(IChannel channel, String message){
        say(channel, message, 0L);
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

    /**
     * Enables a feature on this bot. If you need to enable more than one feature<br>
     * at a time, use {@link DiscordBot#enableFeatures(Collection, boolean, boolean)}
     * @param feature Feature to enable
     * @param save If <code>true</code>, immediately save change to file
     * @param reload If <code>true</code>, immediately reload settings
     * @see DiscordBot#enableFeatures(Collection, boolean, boolean) 
     */
    public void enableFeature(BotFeature feature, boolean save, boolean reload){
        this.features.add(feature);
        feature.onEnable(this);

        if(save) saveFeatures();

        if(reload){
            try{
                this.serverSettingsHandler.loadSettings();
            }
            catch(IOException e){
                log(e, "Could not reload server settings");
            }
        }
    }

    /**
     * Enables a feature on this bot. If you need to enable more than<br>
     * one feature at a time, use {@link DiscordBot#enableFeatures(Collection)}<br>
     * <br>
     * <b>This change is not saved to file, and settings are not reloaded</b><br>
     * <i>Use {@link DiscordBot#enableFeature(BotFeature)} to control this</i>
     * @param feature Feature to enable
     * @see DiscordBot#enableFeature(BotFeature) 
     */
    public void enableFeature(BotFeature feature){
        enableFeature(feature, false, false);
    }

    /**
     * Enables a {@link Collection} of features on this bot<br>
     * All the features in the collection are enabled
     * @param features Features to enable
     * @param save If <code>true</code>, immediately save change to file
     * @param reload If <code>true</code>, immediately reload settings
     */
    public void enableFeatures(Collection<BotFeature> features, boolean save, boolean reload){
        features.forEach(f -> {
            this.features.add(f);
            f.onEnable(this);
        });

        if(save) saveFeatures();

        if(reload){
            try{
                this.serverSettingsHandler.loadSettings();
            }
            catch(IOException e){
                log(e, "Could not reload server settings");
            }
        }
    }

    /**
     * Enables a {@link Collection} of features on this bot<br>
     * All the features in the collection are enabled<br>
     * <br>
     * <b>This change is not saved to file, and settings are not reloaded.</b><br>
     * <i>Use {@link DiscordBot#enableFeatures(Collection, boolean, boolean)}</i> to control these 
     * @param features Features to enable
     * @see DiscordBot#enableFeatures(Collection, boolean, boolean) 
     */
    public void enableFeatures(Collection<BotFeature> features){
        enableFeatures(features, false, false);
    }

    /**
     * Disables a feature on this bot. If you need to disable more than one feature<br>
     * at a time, use {@link DiscordBot#disableFeatures(Collection, boolean, boolean)}
     * @param feature Feature to disable
     * @param save If <code>true</code>, immediately save change to file
     * @param reload If <code>true</code>, immediately reload settings
     * @see DiscordBot#disableFeatures(Collection, boolean, boolean) 
     */
    public void disableFeature(BotFeature feature, boolean save, boolean reload){
        this.features.remove(feature);
        feature.onDisable(this);

        if(save) saveFeatures();

        if(reload){
            try{
                this.serverSettingsHandler.loadSettings();
            }
            catch(IOException e){
                log(e, "Could not reload server settings");
            } 
        }
    }

    /**
     * Disables a feature on this bot. If you need to disable more than<br>
     * one feature at a time, use {@link DiscordBot#disableFeatures(Collection)}<br>
     * <br>
     * <b>This change is not saved to file, and settings are not reloaded</b><br>
     * <i>Use {@link DiscordBot#disableFeature(BotFeature, boolean, boolean)} to control these</i>
     * @param feature Feature to disable
     * @see DiscordBot#disableFeatures(Collection)
     * @see DiscordBot#disableFeature(BotFeature, boolean, boolean)
     */
    public void disableFeature(BotFeature feature){
        disableFeature(feature, false, false);
    }

    /**
     * Disables a {@link Collection} of features on this bot<br>
     * All the features in the collection are disabled
     * @param features Features to disable
     * @param save If <code>true</code>, immediately save change to file
     * @param reload If <code>true</code>, immediately reload settings
     */
    public void disableFeatures(Collection<BotFeature> features, boolean save, boolean reload){
        features.forEach(f -> {
            this.features.remove(f);
            f.onDisable(this);
        });

        if(save) saveFeatures();

        if(reload){
            try{
                this.serverSettingsHandler.loadSettings();
            }
            catch(IOException e){
                log(e, "Could not reload server settings");
            } 
        }
    }

    /**
     * Disables a {@link Collection} of features on this bot<br>
     * All the features in the collection are disabled<br>
     * <b>This change is not saved to file, and settings are not reloaded.</b><br>
     * <i>Use {@link DiscordBot#disableFeatures(Collection, boolean, boolean)}</i> to control these 
     * @param features Features to disable
     * @see DiscordBot#disableFeatures(Collection, boolean, boolean)
     */
    public void disableFeatures(Collection<BotFeature> features){
        disableFeatures(features, false, false);
    }

    /**
     * Toggles whether a feature is enabled/disabled on this bot
     * @param feature Feature to toggle
     * @param save If <code>true</code>, immediately save change to file
     * @param reload If <code>true</code>, immediately reload settings
     * @return <code>true</code> if the feature is now enabled, <code>false</code> otherwise
     */
    public boolean toggleFeature(BotFeature feature, boolean save, boolean reload){
        if(featureEnabled(feature)){
            disableFeature(feature, save, reload);
            return false;
        }
        else{
            enableFeature(feature, save, reload);
            return true;
        }
    }

    /**
     * Toggles whether a feature is enabled/disabled on this bot<br>
     * <b>This change is not saved to file, and settings are not reloaded</b><br>
     * <i>Use {@link DiscordBot#toggleFeature(BotFeature, boolean, boolean)} to control these</i>
     * @param feature Feature to toggle
     * @return <code>true</code> if the feature is now enabled, <code>false</code> otherwise
     * @see DiscordBot#toggleFeature(BotFeature, boolean, boolean)
     */
    public boolean toggleFeature(BotFeature feature){
        if(featureEnabled(feature)){
            disableFeature(feature);
            return false;
        }
        else{
            enableFeature(feature);
            return true;
        }
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
    
    private void loadFeatures(){
        File file = getDataFile("features.txt");

        if(!file.exists()){
            try{
                FileUtils.writeLines(file, BotFeature.getAllRegisteredFeatures().stream().map(f -> f.getRegisteredName() + " " + f.defaultEnabled()).collect(Collectors.toList()));
            }
            catch(IOException e){
                log(e, "Could not write lines to file");
                return;
            }
        }
        
        HashMap<String, Boolean> values = new HashMap<>();
        try{
            List<String> lines = FileUtils.readLines(file);
            for(String s : lines){
                String[] split = s.split(" ");
                if(split.length < 2) continue;

                values.put(split[0], Util.parseBoolean(split[1]));
            }
        }
        catch(IOException e){
            log(e, "Could not read lines from file");
            return;
        }
        
        BotFeature.getAllRegisteredFeatures().stream()
                .filter(f -> !values.containsKey(f.getRegisteredName()))
                .forEach(f -> values.put(f.getRegisteredName(), f.defaultEnabled()));

        values.keySet().forEach(k -> {
            BotFeature f = BotFeature.getFeatureByName(k);
            
            boolean v = values.get(k);
            if(f != null && v){
                this.features.add(f);
                f.onEnable(this);
            }
        });
        
        saveFeatures();
        try{
            getServerSettingsHandler().loadSettings();
        }
        catch(IOException e){
            log(e, "Failed to reload server settings.");
        }
    }

    private void saveFeatures(){
        File file = getDataFile("features.txt");
        if(!file.exists()){
            try{
                file.createNewFile();
            }
            catch(IOException e){
                log(e, "Could not create file " + file.getName());
                return;
            }
        }

        try{
            FileUtils.writeLines(file, BotFeature.getAllRegisteredFeatures()
                    .stream().map(f -> f.getRegisteredName() + " " + this.features.contains(f))
                    .collect(Collectors.toList()));
        }
        catch(IOException e){
            log(e, "Could not write lines to file");
        }
    }
}
