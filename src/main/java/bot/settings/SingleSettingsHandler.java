package bot.settings;

import com.google.gson.*;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class SingleSettingsHandler extends SettingsHandler{

    private static final String DEFAULT = "_default_";

    private Settings values;

    public SingleSettingsHandler(File file){
        super(file);
    }

    @Override
    public Object getSetting(String key, Setting setting){
        return getSetting(setting);
    }

    @Override
    public Settings getSettings(String key){
        return getSettings();
    }

    @Override
    public void setSetting(String key, Setting setting, Object value){
        setSetting(setting, value);
    }

    @Override
    public void setSettings(String key, Settings settings){
        setSettings(settings);
    }

    @Override
    public void resetSettings(String key){
        resetSettings();
    }

    public Object getSetting(Setting setting){
        return getSettings().get(setting);
    }

    public Settings getSettings(){
        if(this.values == null)
            this.values = Settings.defaults(this);

        return this.values;
    }

    public void setSetting(Setting setting, Object value){
        getSettings().set(setting, value);
        saveSettings();
    }

    public void setSettings(Settings settings){
        this.values = settings;
        saveSettings();
    }

    public void resetSettings(){
        this.values = Settings.defaults(this);
    }

    public void loadSettings() throws IOException{
        if(this.file.exists()){
            String content = IOUtils.toString(new FileInputStream(file));

            JsonElement element = new JsonParser().parse(content);
            if(!element.isJsonArray()) return;

            JsonArray settingsArray = element.getAsJsonArray();
            this.values = new Settings();

            for(JsonElement e1 : settingsArray){
                if(!e1.isJsonObject())
                    continue;

                Setting setting = getSettingByName(e1.getAsJsonObject().get("name").getAsString());
                if(setting == null) continue;

                this.values.set(setting, setting.parse(e1.getAsJsonObject().get("value").getAsString()));
            }
            //For any settings that are registered but were not found in the file, add them with the default value
            this.getAddedSettings().stream().filter(s -> !this.values.hasValueFor(s)).forEach(s -> this.values.set(s, s.getDefaultValue()));
        }
        else file.createNewFile();
    }

    public void saveSettings(){
        JsonArray settings = new JsonArray();

        for(Setting s : this.settings){
            JsonObject setting = new JsonObject();
            setting.addProperty("name", s.getName());
            setting.addProperty("value", s.getValueAsString(this.values.get(s)));
            settings.add(setting);
        }

        Gson gson = new GsonBuilder().create();
        try{
            FileUtils.writeStringToFile(file, gson.toJson(settings));
        }
        catch(IOException e){
            System.err.println("Could not save global settings file: " + e.getMessage());
        }
    }
}
