package bot.feature.command;

import bot.DiscordBot;
import bot.feature.ToggleableBotFeature;
import bot.locale.Message;
import bot.settings.BooleanSetting;
import com.google.gson.*;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;
import util.DiscordUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.Collectors;

//TODO: Optimize
public class CommandWaifu extends BotCommand implements ToggleableBotFeature{

    private static final HashMap<String, WaifuList> waifuLists = new HashMap<>();
    
    //Setting for this command
    private static final BooleanSetting SEE_WAIFU_NOTIFICATIONS = new BooleanSetting("notify_waifu", true);

    public CommandWaifu(){
        super("waifu");
    }

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
            bot.log(e, "Failed to load waifus");
        }
    }

    @Override
    public void onDisable(DiscordBot bot) {}

    @Override
    protected void onExecute(DiscordBot bot, IMessage message, String[] args){
        String userId = message.getAuthor().getID();

        if(args.length == 0){
            bot.info(getDetailedDescription(), true);
            return;
        }

        WaifuList list = waifuLists.get(bot.getGuild().getID());
        if(list == null){
            bot.respond(buildMessage(Message.MSG_ERROR, "List is null"));
            return;
        }

        if(args[0].equalsIgnoreCase(getLocalArgs()[0])){
            IUser target = message.getAuthor();
            if(args.length >= 2){
                IUser user = DiscordUtil.getUserByMention(message.getGuild(), args[1]);
                if(user == null){
                    bot.info("User not found!");
                    return;
                }
                else target = user;
            }

            bot.info("*" + buildMessage(Message.CMD_WAIFU_LIST, target.getDisplayName(getGuild())) + "*\n" + String.join("\n", list.getWaifus(target.getID()).stream()
                    .map(i -> DiscordUtil.getUserByID(getGuild(), i))
                    .filter(u -> u != null)
                    .map(u -> u.getDisplayName(getGuild()))
                    .collect(Collectors.toList())), true);
        }
        else if(args.length < 2){
            bot.info(getDetailedDescription(), true);
            return;
        }

        if(args[0].equalsIgnoreCase(getLocalArgs()[1])){
            IUser user = DiscordUtil.getUserByMention(message.getGuild(), args[1]);
            if(user == null){
                bot.info(buildMessage(Message.CMD_INVALID_USER));
            }
            else if(list.hasAsWaifu(userId, user.getID())){
                bot.info(buildMessage(Message.CMD_WAIFU_ALREADY_ON));
            }
            else{
                list.addWaifu(userId, user.getID());
                bot.info(buildMessage(Message.CMD_WAIFU_ADD, user.getName()));

                if(bot.checkSetting(user.getID(), SEE_WAIFU_NOTIFICATIONS)){
                    bot.say(bot.getHome(), buildMessage(Message.CMD_WAIFU_NOTIFY_ADD, user.mention(), message.getAuthor().getDisplayName(message.getGuild())));
                }
            }
        }
        else if(args[0].equalsIgnoreCase(getLocalArgs()[2])){
            IUser user = DiscordUtil.getUserByMention(message.getGuild(), args[1]);
            if(user == null){
                bot.info(buildMessage(Message.CMD_INVALID_USER));
            }
            else if(!list.hasAsWaifu(userId, user.getID())){
                bot.info(buildMessage(Message.CMD_WAIFU_NOT_ON));
            }
            else{
                list.removeWaifu(userId, user.getID());
                bot.info(buildMessage(Message.CMD_WAIFU_REMOVE, user.getName()));

                if(bot.checkSetting(user.getID(), SEE_WAIFU_NOTIFICATIONS)){
                    bot.say(bot.getHome(), buildMessage(Message.CMD_WAIFU_NOTIFY_REMOVE, user.mention(), message.getAuthor().getDisplayName(message.getGuild())));
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

    private static void saveWaifus(DiscordBot bot){
        File file = bot.getDataFile("waifus.json");

        JsonArray users = new JsonArray();
        
        HashMap<String, ArrayList<String>> waifus = waifuLists.get(bot.getGuild().getID()).waifus;
        for(String s : waifus.keySet()){
            JsonObject user = new JsonObject();
            JsonArray userWaifus = new JsonArray();
            waifus.get(s).forEach(userWaifus::add);
            user.addProperty("userId", s);
            user.add("waifus", userWaifus);
            users.add(user);
        }

        Gson gson = new GsonBuilder().create();
        try{
            FileUtils.writeStringToFile(file, gson.toJson(users));
        }
        catch(IOException e){
            bot.log(e, "Failed to save waifus");
        }
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

        private final HashMap<String, ArrayList<String>> waifus = new HashMap<>();

        private final DiscordBot bot;

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

            saveWaifus(this.bot);
        }

        public void setWaifus(String userId, ArrayList<String> waifus){
            this.waifus.put(userId, waifus);
        }

        public void removeWaifu(String firstUserId, String secondUserId){
            waifus.putIfAbsent(firstUserId, new ArrayList<>());
            waifus.get(firstUserId).remove(secondUserId);

            saveWaifus(this.bot);
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
