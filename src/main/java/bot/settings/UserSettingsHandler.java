package bot.settings;

import com.google.gson.*;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;

public class UserSettingsHandler extends SettingsHandler{

    protected HashMap<String, Settings> userSettings = new HashMap<>();

    public UserSettingsHandler(File file){
        super(file);
    }

    @Override
    public Object getSetting(String key, Setting setting){
        return getUserSetting(key, setting);
    }

    @Override
    public Settings getSettings(String key){
        return getUserSettings(key);
    }

    @Override
    public void setSetting(String key, Setting setting, Object value){
        setUserSetting(key, setting, value);
    }

    @Override
    public void setSettings(String key, Settings settings){
        setUserSettings(key, settings);
    }

    @Override
    public void resetSettings(String key){
        resetUserSettings(key);
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
            userSettings.put( userId, new Settings(this));

        userSettings.get(userId).set(setting, value);

        saveSettings();
    }

    public void loadSettings() throws IOException{
        if(this.file.exists()){
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
}
