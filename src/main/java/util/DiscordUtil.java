package util;

import bot.DiscordBot;
import sx.blah.discord.handle.obj.*;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.MissingPermissionsException;
import sx.blah.discord.util.RateLimitException;

import java.util.Optional;
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

    public static IVoiceChannel getVoiceChannel(IGuild guild, IUser user){
        Optional<IVoiceChannel> channel = user.getConnectedVoiceChannels().stream().filter(v -> v.getGuild() == guild).findFirst();
        return channel.orElseGet(null);
    }
    
    public static IChannel getChannelByName(IGuild guild, String name){
        for(IChannel c : guild.getChannels()){
            if(c.getName().equals(name)) return c;
        }
        return null;
    }
    
    public static IUser getUserByMention(IGuild guild, String mention){
        String tag = mention.replaceAll("[<>!]", "");
        if(tag.startsWith("@")){
            return getUserByID(guild, tag.substring(1, tag.length()));
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

    public static int deleteAllMessages(IChannel channel){
        return deleteAllMessages(channel , 0, true, true);
    }

    private static int deleteAllMessages(IChannel channel, int totalDeleted, boolean load, boolean delete){
        int size = channel.getMessages().size();
        int deleted = size > 50 ? 50 : size;

        try{
            channel.getMessages().load(deleted);
            load = false;
            channel.getMessages().deleteFromRange(0, deleted);
            delete = false;
        }
        catch(RateLimitException e){
            try{
                Thread.sleep(e.getRetryDelay());
            }
            catch(InterruptedException e1){
                e1.printStackTrace();
            }
            return deleteAllMessages(channel, totalDeleted, load, delete);
        }
        catch(DiscordException | MissingPermissionsException e){
            DiscordBot.getGuildlessInstance().log(e, "");
            return -1;
        }

        if(size - deleted > 0)
            return deleteAllMessages(channel, deleted + deleted, true, true);
        else
            return deleted;
    }
}
