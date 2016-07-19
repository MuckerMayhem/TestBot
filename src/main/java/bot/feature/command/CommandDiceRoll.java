package bot.feature.command;

import bot.DiscordBot;
import bot.locale.Message;
import bot.locale.MessageBuilder;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IRole;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.handle.obj.Presences;

import java.util.ArrayList;
import java.util.Random;
import java.util.stream.Collectors;

public class CommandDiceRoll extends BotCommand{

    private static final Random RANDOM = new Random();

    public CommandDiceRoll(){
        super("roll");
    }

    @Override
    public void onRegister() {}

    @Override
    public void onEnable(DiscordBot bot) {}
    
    @Override
    public void onDisable(DiscordBot bot) {}

    @Override
    protected void onExecute(DiscordBot bot, IMessage message, String[] args){
        int value = 6;
        String random = "";
        try{
            value = Integer.parseInt(args[0]);
            random = (RANDOM.nextInt(value) + 1) + "";
        }
        catch(NumberFormatException e){
            IRole role = args[0].equalsIgnoreCase("everyone") ? message.getGuild().getEveryoneRole() : message.getGuild().getRolesByName(args[0]).get(0);
            if(role == null){
                bot.respond(new MessageBuilder(bot.getLocale()).buildMessage(Message.CMD_INVALID_ROLE, args[0]));
            }
            else{
                ArrayList<IUser> users = message.getGuild().getUsers().stream()
                        .filter(u -> u.getRolesForGuild(message.getGuild()).contains(role))
                        .filter(u -> u.getPresence() != Presences.OFFLINE)
                        .collect(Collectors.toCollection(ArrayList::new));
                random = users.get(RANDOM.nextInt(users.size())).getName();
            }
        }
        catch(ArrayIndexOutOfBoundsException e){
            random = (RANDOM.nextInt(value) + 1) + "";
        }

        bot.info(message.getAuthor().mention() + " " + random, true);
    }
}
