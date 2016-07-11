package bot;

import bot.chatter.Mitsuku;
import bot.commands.*;
import bot.function.*;
import bot.game.GameBot;
import bot.listeners.OnJoinListener;
import bot.listeners.OnLeaveListener;
import bot.settings.SingleSettingsHandler;
import bot.settings.UserSettingsHandler;
import sx.blah.discord.api.ClientBuilder;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.MessageReceivedEvent;
import sx.blah.discord.handle.impl.events.ReadyEvent;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.Permissions;
import sx.blah.discord.util.*;

import java.io.File;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class DiscordBot{

    public static DiscordBot instance;//Main instance of the bot

    private static final long MESSAGE_TIME_SHORT = 3500L;
    private static final long MESSAGE_TIME_LONG  = 6000L;

    private static final File SETTINGS_FILE = new File(getDataFolder() + File.separator + "settings.json");
    private static final SingleSettingsHandler GLOBAL_SETTINGS = new SingleSettingsHandler(SETTINGS_FILE);

    public MessageReceivedEvent lastEvent;

    private IDiscordClient client;

    private CommandHandler commandHandler;
    private UserSettingsHandler settingsHandler;

    private ArrayList<BotFunction> functions = new ArrayList<BotFunction>();

    private String home;

    /**
     * Creates a new DiscordBot wrapper for an {@link sx.blah.discord.api.IDiscordClient}<br>
     * @param client IDiscordClient holding login for the bot
     */
    public DiscordBot(IDiscordClient client){
        this.client = client;
    }

    public static void main(String[] args) throws Exception{
        if(!getDataFolder().exists()){
           System.out.println("Data folder not found. Creating directory...");
            if(getDataFolder().mkdir())
                System.out.println("Done!");
        }

        IDiscordClient client;
        try{
            System.out.println("Client logging in...");
            client = login(BotParameters.TOKEN);
        }
        catch(DiscordException e){
            System.err.print(e.getMessage());
            return;
        }

        System.out.println("Client successfully logged in");

        instance = new DiscordBot(client);
        instance.getClient().getDispatcher().registerListener(instance);
        instance.getClient().getDispatcher().registerListener(new OnLeaveListener());
        instance.getClient().getDispatcher().registerListener(new OnJoinListener());
        instance.getClient().getDispatcher().registerListener(new Mitsuku());

        instance.commandHandler = new CommandHandler(instance);
        instance.settingsHandler = new UserSettingsHandler();

        //Admin commands
        instance.commandHandler.registerCommand("prune", "Prunes messages matching the specified filter", CommandPrune.class, Permissions.MANAGE_MESSAGES);
        instance.commandHandler.registerCommand("leave", "Leave command", CommandLeave.class, Permissions.VOICE_MOVE_MEMBERS);
        instance.commandHandler.registerCommand("gooffline", "Logs out the bot.", CommandGoOffline.class, Permissions.MANAGE_SERVER);
        instance.commandHandler.registerCommand("type", "Make the bot type a message", CommandType.class, Permissions.CHANGE_NICKNAME);
        instance.commandHandler.registerCommand("showhelp", "Show the detailed description of a command", CommandShowHelp.class, Permissions.MANAGE_MESSAGES);

        //Utility commands
        instance.commandHandler.registerCommand("help", "Show help", CommandHelp.class, Permissions.SEND_MESSAGES);
        instance.commandHandler.registerCommand("test", "Test command", CommandTest.class, Permissions.SEND_MESSAGES);
        instance.commandHandler.registerCommand("attitude", "Display bot attitude towards yourself", CommandAttitude.class, Permissions.SEND_MESSAGES);
        instance.commandHandler.registerCommand("setting", "Change user settings", CommandSetting.class, Permissions.SEND_MESSAGES);
        instance.commandHandler.registerCommand("rabbit", "Show your rabb.it room", CommandRabbit.class, Permissions.SEND_MESSAGES);

        //Fun commands
        instance.commandHandler.registerCommand("meme", "meme", CommandMeme.class, Permissions.SEND_MESSAGES);
        instance.commandHandler.registerCommand("roll", "Roll a random number or user", CommandDiceRoll.class, Permissions.SEND_MESSAGES, "diceroll", "random");
        instance.commandHandler.registerCommand("sound", "Play sounds", CommandSound.class, Permissions.VOICE_SPEAK, "s");
        instance.commandHandler.registerCommand("(", "( ͡° ͜ʖ ͡°)", CommandBooty.class, Permissions.VOICE_SPEAK);//( ͡° ͜ʖ ͡°)
        instance.commandHandler.registerCommand("waifu", "Manage your waifu list", CommandWaifu.class, Permissions.SEND_MESSAGES);

        //Functions
        instance.addFunction(new FunctionAnnounceNoon());
        instance.addFunction(new FunctionEatFood());
        instance.addFunction(new FunctionWelcomeBack());
        instance.addFunction(new FunctionBreakMessages());
        instance.addFunction(new FunctionGetVideoTime());

        instance.settingsHandler.loadSettings();//All settings should be registered by this time, and can now be loaded

        Thread game = new Thread(() -> {
            GameBot gameBot = new GameBot(client);
            gameBot.setCommandHandler(new CommandHandler(gameBot));
            gameBot.getCommandHandler().registerCommand("game", "Play a game", CommandGame.class, Permissions.SEND_MESSAGES, "play");
        }, "GameBot");
        game.run();

        Thread input = new Thread(() -> {
            InputBot inputBot = new InputBot(client);
            inputBot.setCommandHandler(new CommandHandler(inputBot));
            inputBot.getCommandHandler().registerCommand("clear", "Clear messages", CommandClear.class, Permissions.MANAGE_SERVER);
        }, "InputBot");
        input.run();

        GLOBAL_SETTINGS.loadSettings();
    }

    @Deprecated
    public static IDiscordClient login(String email, String password) throws DiscordException{
        return new ClientBuilder().withLogin(email, password).login();
    }

    public static IDiscordClient login(String token) throws DiscordException{
        return new ClientBuilder().withToken(token).login();
    }

    public static File getDataFolder(){
        return new File(System.getProperty("user.dir") + File.separator + "data");
    }

    public static SingleSettingsHandler getGlobalSettingsHandler(){
        return GLOBAL_SETTINGS;
    }

    public IDiscordClient getClient(){
        return this.client;
    }

    public CommandHandler getCommandHandler(){
        return this.commandHandler;
    }

    public UserSettingsHandler getUserSettingsHandler(){
        return this.settingsHandler;
    }

    public String getUsername(){
        return this.client.getOurUser().getName();
    }

    public String getGame(){
        if(this.client.getOurUser().getGame().isPresent()) return this.client.getOurUser().getGame().get();
        return null;
    }

    public IChannel getHome(){
        for(IChannel c : getClient().getGuildByID(BotParameters.GUILD_ID).getChannels()){
            if(c.getName().equals(BotParameters.HOME)) return c;
        }
        return null;
    }

    public ArrayList<BotFunction> getFunctions(){
        return this.functions;
    }

    public void deleteMessage(IMessage message, Long delay){
        TimerTask task = new TimerTask(){
            @Override
            public void run(){
                try{
                    message.delete();
                }
                catch(MissingPermissionsException | RateLimitException | DiscordException e){
                    e.printStackTrace();
                }
            }
        };
        new Timer().schedule(task, delay);
    }

    public void deleteMessage(IMessage message){
        deleteMessage(message, 0L);
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
                    deleteMessage(botMessage, time);
                return botMessage;
            }
            catch(RateLimitException e){
                try{
                    Thread.sleep(10L);//Try again in 10 millis if rate limited
                }
                catch(InterruptedException e1){
                    e1.printStackTrace();
                }
                return say(channel, message, time);
            }
            catch(DiscordException | MissingPermissionsException e){
                System.err.print(e.getMessage());
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
        say(getHome(), message);
    }

    /**
     * Sends a message from this bot in the last channel a user executed<br>
     * a valid command in. Valid commands are commands that are passed<br>
     * by this bot's {@link bot.commands.CommandHandler}<br>
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
     * by this bot's {@link bot.commands.CommandHandler}
     * @param message Message to send
     */
    public void respond(String message){
        say(lastEvent.getMessage().getChannel(), message);
    }

    /**
     * Sends a message from this bot in the last channel a user executed<br>
     * a valid command in. Valid commands are commands that are passed<br>
     * by this bot's {@link bot.commands.CommandHandler}. Message disappears after a short<br>
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
     * by this bot's {@link bot.commands.CommandHandler}. Message disappears after 3.5 seconds
     * @param message Message to send
     */
    public void info(String message){
        respond(message, MESSAGE_TIME_SHORT);
    }

    /**
     * Sets username of the client attached to this bot.</br>
     * This affects all bot instances
     * @param username New username of the bot
     */
    public void setUsername(String username){
        try{
            this.client.changeUsername(username);
        }
        catch(DiscordException | RateLimitException e){
            System.err.print(e.getMessage());
        }
    }

    public void setAvatar(Image image){
        try{
            this.client.changeAvatar(image);
        }
        catch(DiscordException | RateLimitException e){
            System.err.print(e.getMessage());
        }
    }

    public void setGame(String game){
        this.client.changeGameStatus(game);
    }

    public void setHome(String home){
        this.home = home;
    }

    public void setCommandHandler(CommandHandler commandHandler){
        this.commandHandler = commandHandler;
    }

    public void setSettingsHandler(UserSettingsHandler settingsHandler){
        this.settingsHandler = settingsHandler;
    }

    public void addFunction(BotFunction function){
        this.functions.add(function);
        function.bot = this;
        function.init();
    }

    @EventSubscriber
    public void onReady(ReadyEvent event){
        setUsername(BotParameters.getName());
        setAvatar(BotParameters.IMAGE);
        setGame(BotParameters.GAME);
        setHome(BotParameters.HOME);

        if(getHome() == null){
            System.out.println("No text channel found for bot.");
            System.out.println("Users will be unable to play games and some features will be unavailable");
        }

        System.out.println("Bot initialized.");

        this.functions.forEach(BotFunction::activate);
    }

    /*Maybe used for logging later?
    public enum Level{

        INFO("\u001B[0m"),
        WARN("\u001B[33m"),
        ERROR("\u001B[31m"),
        DEBUG("\u001B[32m");

        private String prefix;

        Level(String prefix){
            this.prefix = prefix;
        }

        private String getPrefix(){
            return this.prefix;
        }
    }
    */
}


