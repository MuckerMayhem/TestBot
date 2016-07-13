package util;

import sx.blah.discord.handle.obj.*;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.MissingPermissionsException;
import sx.blah.discord.util.RateLimitException;

import java.util.Timer;
import java.util.TimerTask;

public class DiscordUtil{

    public static boolean userHasPermission(IUser user, IGuild guild, Permissions permission){
        for(IRole r : user.getRolesForGuild(guild)){
            for(Permissions p : r.getPermissions()){
                if(p == permission) return true;
            }
        }
        return false;
    }

    public static IUser getUserByMention(IGuild guild, String mention){
        String tag = mention.replaceAll("[<>!]", "");
        if(tag.startsWith("@")){
            IUser user = getUserByID(guild, tag.substring(1, tag.length()));
            return user;
        }
        return null;
    }

    public static IUser getUserByName(IGuild guild, String name){
        for(IUser u : guild.getUsers()){
            if(u.getName().equals(name)) return u;
        }
        return null;
    }

    public static IUser getUserByID(IGuild guild, String id){
        for(IUser u : guild.getUsers()){
            if(u.getID().equals(id)) return u;
        }
        return null;
    }

    public static void deleteMessage(IMessage message, Long delay){
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

    public static void deleteMessage(IMessage message){
        deleteMessage(message, 0L);
    }
}
