package bot.commands;

import bot.DiscordBot;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IRole;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.handle.obj.Presences;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.MissingPermissionsException;
import sx.blah.discord.util.RateLimitException;

import java.util.ArrayList;
import java.util.Random;
import java.util.stream.Collectors;

public class CommandDiceRoll extends Command{

    private static final Random RANDOM = new Random();

    @Override
    void onExecute(DiscordBot bot, IMessage message, String[] args) throws RateLimitException, DiscordException, MissingPermissionsException{
        int value = 6;
        String random = "";
        try{
            value = Integer.parseInt(args[0]);
            random = (RANDOM.nextInt(value) + 1) + "";
        }
        catch(NumberFormatException e){
            IRole role = args[0].equalsIgnoreCase("everyone") ? message.getGuild().getEveryoneRole() : message.getGuild().getRolesByName(args[0]).get(0);
            if(role == null){
                bot.respond("Invalid role '" + args[0] + "'");
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

        bot.respond(message.getAuthor().mention() + " " + random);
    }
}
