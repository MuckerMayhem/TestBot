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
}
