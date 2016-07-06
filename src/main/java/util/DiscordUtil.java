package util;

import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IRole;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.handle.obj.Permissions;

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
}
