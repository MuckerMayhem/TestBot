package bot.feature.commands;

import bot.DiscordBot;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.Permissions;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.MissingPermissionsException;
import sx.blah.discord.util.RateLimitException;
import util.DiscordUtil;
import util.Util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CommandHandler{

    private static final String[] EMPTY = {};

    private static String commandPrefix = "!";
    private static ArrayList<BotCommand> global_commands = new ArrayList<>();

    private DiscordBot bot;

    private MessageReceivedEvent lastEvent;

    public CommandHandler() {}

    public CommandHandler(DiscordBot bot){
        this.bot = bot;
    }

    /**
     * @return a list containing all registered commands for every CommandHandler instance
     */
    public static List<BotCommand> getAllRegisteredCommands(){
        return global_commands;
    }

    public static BotCommand getCommandByName(String name){
        for(BotCommand c : global_commands){
            if(c.getRegisteredName().equalsIgnoreCase(name)) return c;
        }
        return null;
    }

    public static BotCommand registerCommand(boolean debug, String name, Class<? extends BotCommand> mainClass, Permissions permissions, String... aliases){
        for(BotCommand c : global_commands){

            if(c.name.equalsIgnoreCase(name)){
                System.out.println("Could not register command '" + name + "': Command with this name has already been registered");
                return null;
            }
            for(String s : c.aliases){
                if(s.equalsIgnoreCase(name)){
                    System.out.println("Could not register command '" + name + "': Conflict with alias of same name for command '" + c.name + "'");
                    return null;
                }
                for(String s1 : aliases){
                    if(s1.equalsIgnoreCase(s)){
                        System.out.println("Could not register command '" + name + "': Duplicate alias '" + s +  "' for command '" + c.name + "'");
                        return null;
                    }
                }
            }
        }

        BotCommand instance;
        try{
            instance = mainClass.newInstance();
            instance.permissions = permissions == null ? Permissions.SEND_MESSAGES : permissions;
            instance.name = name;
            instance.aliases = aliases;

            instance.setDebug(debug);

            instance.onRegister();

            global_commands.add(instance);
        }
        catch(InstantiationException | IllegalAccessException e){
            System.err.print("Failed to register command '" + name + "': " + e.getClass().getSimpleName());
            return null;
        }

        System.out.println("Command '" + name + "' registered with aliases " + Arrays.toString(aliases));

        return instance;
    }

    /**
     * Register a new command on a bot. Uses the command prefix set by {@link #setCommandPrefix(String)}
     * @param name Identifier used to execute this command
     * @param mainClass Main class of your command. Should extend {@link BotCommand}
     * @param aliases Aliases your command can be executed with
     * @return true if the command was successfully registered, otherwise false
     */
    public static BotCommand registerCommand(String name, Class<? extends BotCommand> mainClass, Permissions permissions, String... aliases){
        return registerCommand(false, name, mainClass, permissions, aliases);
    }

    /**
     * Gets the command prefix used for executing commands
     * @return - The command prefix that is currently in use
     */
    public static String getCommandPrefix(){
        return commandPrefix;
    }

    /**
     * Sets the prefix used to execute commands. Defaults to "!"
     * @param newPrefix - New prefix to use
     */
    public static void setCommandPrefix(String newPrefix){
        commandPrefix = newPrefix;
    }

    public DiscordBot getBot(){
        return this.bot;
    }

    @EventSubscriber
    public void onMessageReceived(MessageReceivedEvent event){
        DiscordBot bot = DiscordBot.getInstance(event.getMessage().getGuild());
        if(bot == null){
            System.err.println("Could not find bot for guild " + event.getMessage().getGuild().getID());
            return;
        }

        IMessage message = event.getMessage();
        String content = event.getMessage().getContent();

        if(!content.startsWith(commandPrefix)) return;

        String command = content.split(" ")[0].substring(1);

        for(BotCommand c : bot.getCommands()){
            boolean alias = false;
            for(String s : c.aliases){
                if(command.equalsIgnoreCase(s)){
                    alias = true;
                    break;
                }
            }
            if(alias || command.equalsIgnoreCase(c.getName(bot.getLocale()))){
                if(!DiscordUtil.userHasPermission(message.getAuthor(), message.getGuild(), c.getRequiredPermissions())) return;
                if(!c.debug()){
                    DiscordUtil.deleteMessage(message, 2000L);
                }

                String[] args = EMPTY;
                if(content.contains(" ")){
                    args = Util.parseQuotes(content.substring(content.indexOf(' ') + 1).split(" "));
                }

                bot.lastEvent = event;
                try{
                    c.execute(bot, message, args);
                }
                catch(DiscordException | RateLimitException | MissingPermissionsException | IOException e){
                    bot.reportException(e, "Exception occurred while executing command '" + c.getRegisteredName() + "'");
                    return;
                }

                System.out.printf("(%s) Command '%s' run by user %s with arguments: %s\n", message.getGuild().getName(), c.name, message.getAuthor().getName(), String.join(", ", args));

                return;
            }
        }
    }
}
