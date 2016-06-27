package bot;

import bot.chatter.Mitsuku;
import bot.commands.*;
import bot.function.BotFunction;
import bot.function.FunctionAnnounceNoon;
import bot.function.FunctionEatFood;
import bot.game.GameBot;
import bot.listeners.OnJoinListener;
import bot.listeners.OnLeaveListener;
import sx.blah.discord.api.ClientBuilder;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.MessageReceivedEvent;
import sx.blah.discord.handle.impl.events.ReadyEvent;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.Permissions;
import sx.blah.discord.util.*;

import java.util.ArrayList;

public class DiscordBot{

    public static DiscordBot instance;//Main instance of the bot

    public MessageReceivedEvent lastEvent;

    private IDiscordClient client;
    private CommandHandler commandHandler;

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
//        instance.getClient().getDispatcher().registerListener(new MessageEventListener());
        instance.getClient().getDispatcher().registerListener(new Mitsuku());
        instance.commandHandler = new CommandHandler(instance);
        instance.commandHandler.registerCommand("prune", "Prunes messages matching the specified filter", CommandPrune.class, Permissions.MANAGE_MESSAGES);
        instance.commandHandler.registerCommand("test", "Test command", CommandTest.class, Permissions.SEND_MESSAGES);
        instance.commandHandler.registerCommand("sound", "Play sounds", CommandSound.class, Permissions.VOICE_SPEAK, "s");
        instance.commandHandler.registerCommand("(", "( ͡° ͜ʖ ͡°)", CommandBooty.class, Permissions.VOICE_SPEAK);//( ͡° ͜ʖ ͡°)
        instance.commandHandler.registerCommand("attitude", "Display bot attitude towards yourself", CommandAttitude.class, Permissions.SEND_MESSAGES);
        instance.commandHandler.registerCommand("leave", "Leave command", CommandLeave.class, Permissions.VOICE_MOVE_MEMBERS);
        instance.commandHandler.registerCommand("help", "Show help", CommandHelp.class, Permissions.SEND_MESSAGES);
        instance.commandHandler.registerCommand("gooffline", "Logs out the bot.", CommandGoOffline.class, Permissions.VOICE_MOVE_MEMBERS);
        instance.commandHandler.registerCommand("roll", "Roll a random number or user", CommandDiceRoll.class, Permissions.SEND_MESSAGES, "diceroll", "random");

        instance.addFunction(new FunctionAnnounceNoon());
        instance.addFunction(new FunctionEatFood());

        Thread game = new Thread(() -> {
            GameBot gameBot = new GameBot(client);
            gameBot.setCommandHandler(new CommandHandler(gameBot));
            gameBot.getCommandHandler().registerCommand("game", "Play a game", CommandGame.class, null, "play");
        });

        game.run();
    }

    @Deprecated
    public static IDiscordClient login(String email, String password) throws DiscordException{
        return new ClientBuilder().withLogin(email, password).login();
    }

    public static IDiscordClient login(String token) throws DiscordException{
        return new ClientBuilder().withToken(token).login();
    }

    public IDiscordClient getClient(){
        return this.client;
    }

    public CommandHandler getCommandHandler(){
        return this.commandHandler;
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

    /**
     * Sends a message from this bot in the specified channel
     * @param channel Channel to speak in
     * @param message Message to send
     */
    public void say(IChannel channel, String message){
        if(message == null) return;

        if(channel != null){
            try{
                new MessageBuilder(getClient()).withChannel(channel).withContent(message).build();
            }
            catch(RateLimitException e){
                try{
                    Thread.sleep(10L);//Try again in 10 millis if rate limited
                }
                catch(InterruptedException e1){
                    e1.printStackTrace();
                }
                say(channel, message);
            }
            catch(DiscordException | MissingPermissionsException e){
                System.err.print(e.getMessage());
            }
        }
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
     * by this bot's {@link bot.commands.CommandHandler}
     * @param message Message to send
     */
    public void respond(String message){
        say(lastEvent.getMessage().getChannel(), message);
    }

    public void executeCommand(String name, String[] args){
        getCommandHandler().executeCommand(name, args);
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

    public void addFunction(BotFunction function){
        this.functions.add(function);
        function.bot = this;
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


