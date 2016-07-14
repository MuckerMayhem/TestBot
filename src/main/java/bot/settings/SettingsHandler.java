package bot.settings;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public abstract class SettingsHandler{

    private static ArrayList<Setting> global_settings = new ArrayList<>();

    protected ArrayList<Setting> settings = new ArrayList<>();
    protected File file;

    public SettingsHandler(File file){
        this.file = file;
    }

    /**
     * Gets all registered settings, across all SettingsHandlers
     * @return an {@link java.util.ArrayList} containing all registered {@link bot.settings.Setting}s
     */
    public static ArrayList<Setting> getAllRegisteredSettings(){
        return global_settings;
    }

    public abstract Object getSetting(String key, Setting setting);

    public abstract Settings getSettings(String key);

    public abstract void loadSettings() throws IOException;

    public abstract void saveSettings();

    public abstract void setSetting(String key, Setting setting, Object value);

    public abstract void setSettings(String key, Settings settings);

    public abstract void resetSettings(String key);

    /**
     * Gets all settings registered to this SettingsHandler instance
     * @return an {@link java.util.ArrayList} containing all {@link bot.settings.Setting}s registered to this SettingsHandler
     */
    public ArrayList<Setting> getRegisteredSettings(){
        return this.settings;
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
        if(!global_settings.contains(setting)){
            global_settings.add(setting);
            System.out.println("Registering new setting: " + setting.getName());
        }
    }
}
