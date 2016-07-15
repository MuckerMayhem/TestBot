package bot.feature.commands;

import bot.DiscordBot;
import bot.locale.Message;
import bot.locale.MessageBuilder;
import bot.settings.BooleanSetting;
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

public class CommandWaifu extends BotCommand{

    //Setting for this command
    private static final BooleanSetting SEE_WAIFU_NOTIFICATIONS = new BooleanSetting("notify_waifu", true);

    private static HashMap<String, WaifuList> waifuLists = new HashMap<>();

    @Override
    public void onRegister(){
        DiscordBot.getUserSettingsHandler().registerNewSetting(SEE_WAIFU_NOTIFICATIONS);
    }

    @Override
    public void onEnable(DiscordBot bot){
        try{
            loadWaifus(bot);
        }
        catch(IOException e){
            bot.reportException(e, "Failed to load waifus");
        }
    }

    @Override
    public void onDisable(DiscordBot bot) {}

    @Override
    protected void onExecute(DiscordBot bot, IMessage message, String[] args) throws RateLimitException, DiscordException, MissingPermissionsException, IOException{
        String userId = message.getAuthor().getID();

        if(args.length == 0){
            bot.info(getDetailedDescription(), true);
            return;
        }

        MessageBuilder builder = new MessageBuilder(bot.getLocale());

        WaifuList list = waifuLists.get(bot.getGuild().getID());
        if(list == null){
            bot.respond(builder.buildMessage(Message.MSG_ERROR, "List is null"));
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

            bot.info("*" + builder.buildMessage(Message.CMD_WAIFU_LIST, target.getDisplayName(message.getGuild())) + "*\n" + String.join("\n", list.getWaifus(target.getID()).stream()
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
                bot.info(builder.buildMessage(Message.CMD_INVALID_USER));
            }
            else if(list.hasAsWaifu(userId, user.getID())){
                bot.info(builder.buildMessage(Message.CMD_WAIFU_ALREADY_ON));
            }
            else{
                list.addWaifu(userId, user.getID());
                bot.info(builder.buildMessage(Message.CMD_WAIFU_ADD, user.getName()));

                if(bot.checkSetting(user.getID(), SEE_WAIFU_NOTIFICATIONS)){
                    bot.say(bot.getHome(), builder.buildMessage(Message.CMD_WAIFU_NOTIFY_ADD, user.mention(), message.getAuthor().getDisplayName(message.getGuild())));
                }
            }
        }
        else if(args[0].equalsIgnoreCase("remove")){
            IUser user = DiscordUtil.getUserByMention(message.getGuild(), args[1]);
            if(user == null){
                bot.info(builder.buildMessage(Message.CMD_INVALID_USER));
            }
            else if(!list.hasAsWaifu(userId, user.getID())){
                bot.info(builder.buildMessage(Message.CMD_WAIFU_NOT_ON));
            }
            else{
                list.removeWaifu(userId, user.getID());
                bot.info(builder.buildMessage(Message.CMD_WAIFU_REMOVE, user.getName()));

                if(bot.checkSetting(user.getID(), SEE_WAIFU_NOTIFICATIONS)){
                    bot.say(bot.getHome(), builder.buildMessage(Message.CMD_WAIFU_NOTIFY_REMOVE, user.mention(), message.getAuthor().getDisplayName(message.getGuild())));
                }
            }
        }
    }

    private static void loadWaifus(DiscordBot bot) throws IOException{
        WaifuList waifus = new WaifuList(bot);

        File file = bot.getDataFile("waifus.json");
        if(file.exists()){
            String content = IOUtils.toString(new FileInputStream(file));

            JsonElement element = new JsonParser().parse(content);
            if(element.isJsonArray()){
                JsonArray users = element.getAsJsonArray();
                for(JsonElement e : users){
                    if(!e.isJsonObject())
                        continue;

                    String userId = e.getAsJsonObject().get("userId").getAsString();
                    JsonArray waifuArray = e.getAsJsonObject().get("waifus").getAsJsonArray();
                    ArrayList<String> waifuList = new ArrayList<>();
                    for(JsonElement e1 : waifuArray){
                        waifuList.add(e1.getAsString());
                        System.out.println(":" + e1.getAsString());
                    }

                    waifus.setWaifus(userId, waifuList);
                }
            }
        }
        else file.createNewFile();

        waifuLists.put(bot.getGuild().getID(), waifus);
    }

    private static void saveWaifus(DiscordBot bot) throws IOException{
        File file = bot.getDataFile("waifus.json");

        JsonArray users = new JsonArray();

        HashMap<String, ArrayList<String>> waifus = new HashMap<>();
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
    /*
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
    */

    public static class WaifuList{

        private static final ArrayList<String> EMPTY = new ArrayList<>();

        private HashMap<String, ArrayList<String>> waifus = new HashMap<>();

        private DiscordBot bot;

        public WaifuList(DiscordBot bot){
            this.bot = bot;
        }

        public HashMap<String, ArrayList<String>> getWaifuMap(){
            return this.waifus;
        }

        public ArrayList<String> getWaifus(String userId){
            return waifus.getOrDefault(userId, EMPTY);
        }

        public void addWaifu(String firstUserID, String secondUserId){
            waifus.putIfAbsent(firstUserID, new ArrayList<>());
            waifus.get(firstUserID).add(secondUserId);

            try{
                saveWaifus(this.bot);
            }
            catch(IOException e){
                e.printStackTrace();
            }
        }

        public void setWaifus(String userId, ArrayList<String> waifus){
            this.waifus.put(userId, waifus);
        }

        public void removeWaifu(String firstUserId, String secondUserId){
            waifus.putIfAbsent(firstUserId, new ArrayList<>());
            waifus.get(firstUserId).remove(secondUserId);

            try{
                saveWaifus(this.bot);
            }
            catch(IOException e){
                e.printStackTrace();
            }
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
    }
}
