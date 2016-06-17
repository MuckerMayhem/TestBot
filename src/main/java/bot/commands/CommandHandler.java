package bot.commands;

import bot.DiscordBot;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.MessageReceivedEvent;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.MissingPermissionsException;
import sx.blah.discord.util.RateLimitException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CommandHandler{

    private static ArrayList<Command> global_commands = new ArrayList<>();
    private static final String[] EMPTY = {};

    private ArrayList<Command> commands = new ArrayList<>();
    private String commandPrefix = "!";

    private DiscordBot bot;

    private MessageReceivedEvent lastEvent;

    public CommandHandler(DiscordBot bot){
        this.bot = bot;
        bot.getClient().getDispatcher().registerListener(this);
    }

    /**
     * @return a list containing all registered commands for every CommandHandler instance
     */
    public static List<Command> getAllRegisteredCommands(){
        return global_commands;
    }

    /**
     * Register a new command on a bot. Uses the command prefix set by {@link #setCommandPrefix(String)}
     * @param name Identifier used to execute this command
     * @param description Description of the command. This is shown in the help menu
     * @param mainClass Main class of your command. Should extend {@link bot.commands.Command}
     * @param aliases Aliases your command can be executed with
     * @return true if the command was successfully registered, otherwise false
     */
    public boolean registerCommand(String name, String description, Class<? extends Command> mainClass, String... aliases){
        for(Command c : this.commands){
            if(c.name.equalsIgnoreCase(name)){
                System.out.println("Could not register command '" + name + "': Command with this name has already been registered");
                return false;
            }
            for(String s : c.aliases){
                if(s.equalsIgnoreCase(name)){
                    System.out.println("Could not register command '" + name + "': Conflict with alias of same name for command '" + c.name + "'");
                    return false;
                }
                for(String s1 : aliases){
                    if(s1.equalsIgnoreCase(s)){
                        System.out.println("Could not register command '" + name + "': Duplicate alias '" + s +  "' for command '" + c.name + "'");
                        return false;
                    }
                }
            }
        }

        try{
            Command instance = mainClass.newInstance();
            instance.name = name;
            instance.description = description;
            instance.aliases = aliases;
            instance.commandHandler = this;

            this.commands.add(instance);
            global_commands.add(instance);
        }
        catch(InstantiationException | IllegalAccessException e){
            System.err.print("Failed to register command '" + name + "': " + e.getClass().getSimpleName());
            return false;
        }

        System.out.println("Command '" + name + "' registered with aliases " + Arrays.toString(aliases));

        return true;
    }

    /**
     * @return a list containing all registered commands for this CommandHandler instance
     */
    public List<Command> getRegisteredCommands(){
        return commands;
    }

    /**
     * Gets the command prefix used for executing commands
     * @return - The command prefix that is currently in use
     */
    public String getCommandPrefix(){
        return commandPrefix;
    }

    public void executeCommand(String name, String[] args){
        for(Command c : commands){

        }
    }

    /**
     * Sets the prefix used to execute commands. Defaults to "!"
     * @param newPrefix - New prefix to use
     */
    public void setCommandPrefix(String newPrefix){
        commandPrefix = newPrefix;
    }

    @EventSubscriber
    public void onMessageReceived(MessageReceivedEvent event) throws RateLimitException, DiscordException, MissingPermissionsException{
        String message = event.getMessage().getContent();

        if(!message.startsWith(this.commandPrefix)) return;

        String command = message.split(" ")[0].substring(1);
        String[] args = EMPTY;
        if(message.contains(" ")){
            args = message.substring(message.indexOf(' ') + 1).split(" ");
        }

        for(Command c : commands){
            boolean alias = false;
            for(String s : c.aliases){
                if(command.equalsIgnoreCase(s)){
                    alias = true;
                    break;
                }
            }
            if(alias || command.equalsIgnoreCase(c.getName())){
                this.bot.lastEvent = event;
                c.onExecute(this.bot, event.getMessage(), args);
                System.out.printf("Command '%s' run by user %s with arguments: %s\n", c.name, event.getMessage().getAuthor().getName(), String.join(", ", args));
                return;
            }
        }
    }
}
