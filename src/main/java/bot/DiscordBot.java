package bot;

import bot.commands.*;
import bot.game.GameBot;
import sx.blah.discord.api.ClientBuilder;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.MessageReceivedEvent;
import sx.blah.discord.handle.impl.events.ReadyEvent;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.util.*;


public class DiscordBot{

    public static final String GUILD_ID = BotParameters.GUILD_ID;
    public static final String NAME = BotParameters.NAME;
    public static final String GAME = BotParameters.GAME;
    public static final String HOME = BotParameters.HOME;

    private static final String TOKEN = "MTkxMzk3NDkzNDE2Nzg3OTY4.CkPSLA.sDU5W0NbZn32gl3eDtFzIhmYV6Q";//Login token

    public static DiscordBot instance;//Main instance of the bot

    public MessageReceivedEvent lastEvent;

    private IDiscordClient client;
    private CommandHandler commandHandler;

    private String home;

    public DiscordBot(IDiscordClient client){
        this.client = client;
    }

    public static void main(String[] args){
        IDiscordClient client;
        try{
            client = login(TOKEN);
        }
        catch(DiscordException e){
            System.err.print(e.getMessage());
            return;
        }

        instance = new DiscordBot(client);
        instance.getClient().getDispatcher().registerListener(instance);

        instance.commandHandler = new CommandHandler(instance);
        instance.commandHandler.registerCommand("test", "Test command", CommandTest.class);
        instance.commandHandler.registerCommand("sound", "Play sounds", CommandSound.class, "s");
        instance.commandHandler.registerCommand("(", "( ͡° ͜ʖ ͡°)", CommandBooty.class);//( ͡° ͜ʖ ͡°)
        instance.commandHandler.registerCommand("attitude", "Display bot attitude towards yourself", CommandAttitude.class);
        instance.commandHandler.registerCommand("leave", "Leave command", CommandLeave.class);
        instance.commandHandler.registerCommand("help", "Show help", CommandHelp.class);

        Thread game = new Thread(() -> {
            GameBot gameBot = new GameBot(client);

            gameBot.getClient().getDispatcher().registerListener(gameBot);

            gameBot.setCommandHandler(new CommandHandler(gameBot));
            gameBot.getCommandHandler().registerCommand("game", "Play a game", CommandGame.class, "play");
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
        return client;
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
        for(IChannel c : getClient().getGuildByID(GUILD_ID).getChannels()){
            if(c.getName().equals(HOME)) return c;
        }
        return null;
    }

    /**
     * Sends a message from this bot in the specified channel
     * @param channel Channel to speak in
     * @param message Message to send
     */
    public void say(IChannel channel, String message){
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
     * Sends a message from this bot in the last channel a user executed</br>
     * a valid command in. Valid commands are commands that are passed by</br>
     * this bot's {@link bot.commands.CommandHandler}
     * @param message Message to send
     */
    public void respond(String message){
        say(lastEvent.getMessage().getChannel(), message);
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

    @EventSubscriber
    public void onReady(ReadyEvent event) throws RateLimitException, DiscordException{
        setAvatar(Image.forUrl("png", "https://cdn3.iconfinder.com/data/icons/fruits-flat-icon-set/256/icon-banana-128.png"));
        setUsername(NAME);
        setGame(GAME);
        setHome(HOME);

        if(getHome() == null){
            System.out.println("No text channel found for bot.");
            System.out.println("Users will be unable to play games and some features will be unavailable");
        }

        System.out.println("Client initialized.");
    }
}


