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

    protected DiscordBot bot;

    private HashMap<String, Settings> userSettings = new HashMap<>();
    private ArrayList<Setting> settings = new ArrayList<>();
    private File file;

    public SettingsHandler(DiscordBot bot, File file){
        this.bot = bot;
        this.file = file;
    }

    public SettingsHandler(DiscordBot bot){
        this(bot, DEFAULT_FILE);
    }

    /**
     * Gets all registered settings, across all SettingsHandlers
     * @return an {@link java.util.ArrayList} containing all registered {@link bot.settings.Setting}s
     */
    public static ArrayList<Setting> getAllRegisteredSettings(){
        return global_settings;
    }

    /**
     * Gets all settings registered to this SettingsHandler instance
     * @return an {@link java.util.ArrayList} containing all {@link bot.settings.Setting}s registered to this SettingsHandler
     */
    public ArrayList<Setting> getRegisteredSettings(){
        return this.settings;
    }

    /**
     * Gets the {@link bot.settings.Settings} for a user
     * @param userId User to get settings for
     * @return {@link bot.settings.Settings} containing settings for the user
     */
    public Settings getUserSettings(String userId){
        return userSettings.getOrDefault(userId, Settings.defaults(this));
    }

    /**
     * Gets a registered setting by its name
     * @param name Name of the setting you are looking for
     * @return A {@link bot.settings.Setting} with a matching name, if it exists. Otherwise <i>null</i>
     */
    public Setting getSettingByName(String name){
        for(Setting s : this.settings){
            if(s.getName().equalsIgnoreCase(name)) return s;
        }
        return null;
    }

    /**
     * Gets the value of a user's setting
     * @param userId User to lookup setting for
     * @param setting Setting to lookup
     * @return The value the user has set for the setting
     */
    public Object getUserSetting(String userId, Setting setting){
        Settings settings = userSettings.getOrDefault(userId, Settings.defaults(this));

        return settings.get(setting);
    }

    /**
     * Gets the bot associated with this SettingsHandler
     */
    public DiscordBot getBot(){
        return this.bot;
    }

    /**
     * Gets the file that this SettingsHandler is to store its data in
     * @return The {@link java.io.File} that this SettingsHandler is to store its data in
     */
    public File getFile(){
        return this.file;
    }

    /**
     * Registers a new {@link bot.settings.Setting} to be used by some function of the bot
     * @param setting New {@link bot.settings.Setting} to register
     */
    public void registerNewSetting(Setting setting){
        this.settings.add(setting);
        global_settings.add(setting);
    }

    /**
     * Sets all of a user's settings to the specified {@link bot.settings.Settings} values
     * @param userId User to set settings for
     * @param settings New {@link bot.settings.Settings} for this user
     */
    public void setUserSettings(String userId, Settings settings){
        userSettings.put(userId, settings);
        saveSettings();
    }

    /**
     * Sets all of a user's settings to their default values
     * @param userId User to reset settings for
     */
    public void resetUserSettings(String userId){
        userSettings.put(userId, Settings.defaults(this));
    }

    /**
     * Sets the value of a single setting for a user
     * @param userId User to set setting for
     * @param setting Setting to change
     * @param value Value of the setting you are changing
     */
    public void setUserSetting(String userId, Setting setting, Object value){
        if(!userSettings.containsKey(userId))
            userSettings.put(userId, new Settings(this));

        userSettings.get(userId).set(setting, value);

        saveSettings();
    }

    /**
     * Saves all settings handled by this SettingsHandler to this SettingsHandler's file
     */
    public void saveSettings(){
        JsonArray users = new JsonArray();

        for(String id : userSettings.keySet()){
            JsonObject user = new JsonObject();
            user.addProperty("userId", id);

            JsonArray settings = new JsonArray();
            user.add("settings", settings);

            for(Setting s : this.settings){
                JsonObject setting = new JsonObject();
                setting.addProperty("name", s.getName());
                setting.addProperty("value", s.getValueAsString(userSettings.get(id).get(s)));
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

    /**
     * Loads all settings from this SettingsHandler's file
     * @throws IOException for normal reasons
     */
    public void loadSettings() throws IOException{
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
                userSettings.put(userId, settings);

                for(JsonElement e1 : settingsArray.getAsJsonArray()){
                    if(!e1.isJsonObject())
                        continue;

                    Setting setting = getSettingByName(e1.getAsJsonObject().get("name").getAsString());
                    if(setting == null) continue;

                    settings.set(setting, setting.parse(e1.getAsJsonObject().get("value").getAsString()));
                }
                //For any settings that are registered but were not found in the file, add them with the default value
                this.getRegisteredSettings().stream().filter(s -> !this.userSettings.get(userId).hasValueFor(s)).forEach(s -> {
                    settings.set(s, s.getDefaultValue());
                });
            }
        }
        else file.createNewFile();
    }
}
