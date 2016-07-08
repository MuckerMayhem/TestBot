package bot.commands;

import bot.DiscordBot;
import bot.settings.SettingsHandler.Setting;
import com.google.gson.*;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.MissingPermissionsException;
import sx.blah.discord.util.RateLimitException;
import util.DiscordUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.Collectors;

public class CommandWaifu extends Command{

    private static final File DEFAULT_FILE = new File(DiscordBot.getDataFolder() + File.separator + "waifus.json");

    private static HashMap<String, ArrayList<String>> waifus = new HashMap<>();

    @Override
    protected void onRegister(){
        try{
            loadWaifus(DEFAULT_FILE);
        }
        catch(IOException e){
            e.printStackTrace();
        }

        if(!hasAsWaifu("195313570127282176", "188803847458652162")){
            addWaifu("195313570127282176", "188803847458652162");
        }
    }

    @Override
    protected void onExecute(DiscordBot bot, IMessage message, String[] args) throws RateLimitException, DiscordException, MissingPermissionsException, IOException{
        String userId = message.getAuthor().getID();

        if(args.length == 0){
            bot.info(getDetailedDescription(), true);
            return;
        }

        if(args[0].equalsIgnoreCase("list")){
            IUser target = message.getAuthor();
            if(args.length >= 2){
                IUser user = DiscordUtil.getUserByMention(message.getGuild(), args[1]);
                if(user == null){
                    bot.info("User not found!");
                    return;
                }
                else target = user;
            }

            bot.info("*" + target.getDisplayName(message.getGuild()) + "'s waifus:*\n" + String.join("\n", waifus.get(target.getID()).stream()
            .filter(i -> DiscordUtil.getUserByID(message.getGuild(), i) != null)
            .map(i -> DiscordUtil.getUserByID(message.getGuild(), i).getDisplayName(message.getGuild()))
            .collect(Collectors.toList())), true);
        }
        else if(args.length < 2){
            bot.info(getDetailedDescription(), true);
            return;
        }

        if(args[0].equalsIgnoreCase("add")){
            IUser user = DiscordUtil.getUserByMention(message.getGuild(), args[1]);
            if(user == null){
                bot.info("User not found!");
            }
            else if(user == bot.getClient().getOurUser() && !userId.equals("188803847458652162")){
                bot.info("Silly thing! I can't be your waifu! Hehe \uD83D\uDE17");
            }
            else if(hasAsWaifu(userId, user.getID())){
                bot.info("That person is already on your waifu list!");
            }
            else{
                addWaifu(userId, user.getID());
                bot.info("User '" + user.getName() + "' added to your waifu list");

                if(checkSetting(user.getID(), Setting.SEE_WAIFU_NOTIFICATIONS)){
                    bot.say(bot.getHome(), "Hey, " + user.mention() + "! " + message.getAuthor().getDisplayName(message.getGuild()) + " has added you to their waifu list!");
                }
            }
        }
        else if(args[0].equalsIgnoreCase("remove")){
            IUser user = DiscordUtil.getUserByMention(message.getGuild(), args[1]);
            if(user == null){
                bot.info("User not found!");
            }
            else if(!hasAsWaifu(userId, user.getID())){
                bot.info("That person is not on your waifu list!");
            }
            else{
                removeWaifu(userId, user.getID());
                bot.info("User '" + user.getName() + "' removed from your waifu list");

                if(checkSetting(user.getID(), Setting.SEE_WAIFU_NOTIFICATIONS)){
                    bot.say(bot.getHome(), user.mention() + ", " + message.getAuthor().getDisplayName(message.getGuild()) + " has removed you from their waifu list! b-baka!");
                }
            }
        }
    }

    @Override
    public String getDetailedDescription(){
        return "Manage your waifus!\n" +
                "Usage:\n" +
                " " + this.getHandle() + " list\n" +
                "  *Lists users you have on your waifu list*\n" +
                " " + this.getHandle() + " add <user>\n" +
                "  *Add a user to your waifu list*\n" +
                " " + this.getHandle() + " remove <user>\n" +
                "  *Remove a user from your waifu list*";
    }

    private static void loadWaifus(File file) throws IOException{
        if(file.exists()){
            String content = IOUtils.toString(new FileInputStream(file));

            JsonElement element = new JsonParser().parse(content);
            if(!element.isJsonArray()) return;

            JsonArray users = element.getAsJsonArray();
            for(JsonElement e : users){
                if(!e.isJsonObject())
                    continue;

                String userId = e.getAsJsonObject().get("userId").getAsString();
                JsonArray waifuArray = e.getAsJsonObject().get("waifus").getAsJsonArray();
                ArrayList<String> waifuList = new ArrayList<>();
                for(JsonElement e1 : waifuArray){
                    waifuList.add(e1.getAsString());
                }

                waifus.put(userId, waifuList);
            }
        }
        else file.createNewFile();
    }

    private static void saveWaifus(File file) throws IOException{
        JsonArray users = new JsonArray();
        for(String s : waifus.keySet()){
            JsonObject user = new JsonObject();
            JsonArray userWaifus = new JsonArray();
            waifus.get(s).forEach(userWaifus::add);
            user.addProperty("userId", s);
            user.add("waifus", userWaifus);
            users.add(user);
        }

        Gson gson = new GsonBuilder().create();
        FileUtils.writeStringToFile(file, gson.toJson(users));
    }

    private boolean hasAsWaifu(String firstUserId, String secondUserId){
        if(waifus.containsKey(firstUserId) && waifus.get(firstUserId) != null){
            return waifus.get(firstUserId).contains(secondUserId);
        }

        return false;
    }

    private boolean hasAsWaifu(IUser firstUser, IUser secondUser){
        return hasAsWaifu(firstUser.getID(), secondUser.getID());
    }

    private void addWaifu(String firstUserID, String secondUserId){
        waifus.putIfAbsent(firstUserID, new ArrayList<>());
        waifus.get(firstUserID).add(secondUserId);

        try{
            saveWaifus(DEFAULT_FILE);
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }

    private void removeWaifu(String firstUserId, String secondUserId){
        waifus.putIfAbsent(firstUserId, new ArrayList<>());
        waifus.get(firstUserId).remove(secondUserId);

        try{
            saveWaifus(DEFAULT_FILE);
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }
}
