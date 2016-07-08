package bot.settings;

import bot.DiscordBot;
import com.google.gson.*;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class SettingsHandler{

    public static final File DEFAULT_FILE = new File(DiscordBot.getDataFolder() + File.separator + "usersettings.json");

    private static ArrayList<Setting> global_settings = new ArrayList<>();
    private static HashMap<String, Settings> userSettings = new HashMap<>();

    protected DiscordBot bot;

    private ArrayList<Setting> settings = new ArrayList<>();
    private File file;

    public SettingsHandler(DiscordBot bot, File file){
        this.bot = bot;
        this.file = file;

        try{
            loadSettings(file);
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }

    public SettingsHandler(DiscordBot bot){
        this(bot, DEFAULT_FILE);
    }

    public Settings getUserSettings(String userId){
        return userSettings.getOrDefault(userId, Settings.DEFAULT);
    }

    public Setting getSettingByName(String name){
        for(Setting s : Setting.values()){
            if(s.getName().equalsIgnoreCase(name)) return s;
        }
        return null;
    }

    public boolean getUserSetting(String userId, Setting setting){
        Settings settings = userSettings.getOrDefault(userId, Settings.DEFAULT);

        return settings.get(setting);
    }

    public DiscordBot getBot(){
        return this.bot;
    }

    public File getFile(){
        return this.file;
    }

    public void registerNewSetting(Setting setting){

    }

    public void setUserSettings(String userId, Settings settings){
        userSettings.put(userId, settings);
        saveSettings(this.file);
    }

    public void setUserSetting(String userId, Setting setting, boolean value){
        if(!userSettings.containsKey(userId))
            userSettings.put(userId, new Settings());

        userSettings.get(userId).set(setting, value);

        saveSettings(this.file);
    }

    public void toggleUserSetting(String userId, Setting setting){
        setUserSetting(userId, setting, !getUserSetting(userId, setting));
    }

    public void saveSettings(File file){
        JsonArray users = new JsonArray();

        for(String id : userSettings.keySet()){
            JsonObject user = new JsonObject();
            user.addProperty("userId", id);

            JsonArray settings = new JsonArray();
            user.add("settings", settings);

            for(Setting s : Setting.values()){
                JsonObject setting = new JsonObject();
                setting.addProperty("name", s.getName());
                setting.addProperty("value", userSettings.get(id).get(s));
                settings.add(setting);
            }
            users.add(user);
        }

        Gson gson = new GsonBuilder().create();
        try{
            FileUtils.writeStringToFile(file, gson.toJson(users));
        }
        catch(IOException e){
            System.err.println("Could not save global settings file: " + e.getMessage());
        }
    }

    public void loadSettings(File file) throws IOException{
        if(file.exists()){
            String content = IOUtils.toString(new FileInputStream(file));

            JsonElement element = new JsonParser().parse(content);
            if(!element.isJsonArray()) return;

            JsonArray users = element.getAsJsonArray();
            for(JsonElement e : users){
                if(!e.isJsonObject())
                    continue;

                String userId = e.getAsJsonObject().get("userId").getAsString();

                JsonElement settingsArray = e.getAsJsonObject().get("settings");
                if(!settingsArray.isJsonArray())
                    continue;


                Settings settings = new Settings();
                for(JsonElement e1 : settingsArray.getAsJsonArray()){
                    if(!e1.isJsonObject())
                        continue;

                    Setting setting = getSettingByName(e1.getAsJsonObject().get("name").getAsString());
                    if(setting == null) continue;

                    settings.set(setting, e1.getAsJsonObject().get("value").getAsBoolean());
                }
                userSettings.put(userId, settings);
            }
        }
        else file.createNewFile();
    }

    public static enum Setting{

        SEE_WELCOME_NOTIFICATIONS("notify_welcome", "Change whether the bot welcomes you back", true),
        SEE_WAIFU_NOTIFICATIONS("notify_waifu", "Change whether you see when someone adds/removes you to your waifu list", true),
        ALLOW_EMOJI_EATING("eat_emoji", "Change whether the bot can eat your emoji", true),
        ALLOW_WALL_BREAKING("break_walls", "Change whether the bot can break up your walls of text", true);

        private String name;
        private String description;
        private boolean defaultSetting;

        Setting(String name, String description, boolean defaultSetting){
            this.name = name;
            this.description = description;
            this.defaultSetting = defaultSetting;
        }

        public String getName(){
            return this.name;
        }

        public String getDescription(){
            return this.description;
        }

        public boolean getDefault(){
            return this.defaultSetting;
        }

        @Override
        public String toString(){
            return this.name + ": " + this.description + " (Default: " + this.defaultSetting + ")";
        }
    }
}
