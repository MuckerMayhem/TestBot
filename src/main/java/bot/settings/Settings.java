package bot.settings;

import java.util.HashMap;
import java.util.Iterator;

/**
 * Represents a set of {@link bot.settings.Setting}s and their values
 */
public class Settings implements Iterable<Setting>{

    private static HashMap<SettingsHandler, Settings> defaults = new HashMap<>();

    private HashMap<Setting, Object> values = new HashMap<>();

    private boolean canModify;

    public Settings(){
        this.canModify = true;
    }

    public Settings(SettingsHandler handler){
        for(Setting s : handler.getRegisteredSettings()){
            values.put(s, s.getDefaultValue());
        }

        this.canModify = true;
    }

    private Settings(SettingsHandler handler, boolean canModify){
        this(handler);
        this.canModify = canModify;
    }

    public static Settings defaults(SettingsHandler handler){
        if(defaults.containsKey(handler))
            defaults.put(handler, new Settings(handler));

        return defaults.get(handler);
    }

    public Object get(Setting setting){
        return values.getOrDefault(setting, setting.getDefaultValue());
    }

    public void set(Setting setting, Object value){
        if(!this.canModify) return;

        values.put(setting, value);
    }

    @Override
    public String toString(){
        StringBuilder builder = new StringBuilder("```");

        int longest = 0;
        for(Setting s : this.values.keySet()){
            if(s.getName().length() > longest) longest = s.getName().length();
        }
        longest += 3;

        for(Setting s : this.values.keySet()){
            builder.append(String.format("%-" + longest + "s", s.getName() + ":"))//lol
                    .append(get(s))
                    .append(" | ")
                    .append(s.getDescription())
                    .append(" (Default: ")
                    .append(s.getDefaultValue())
                    .append(")")
                    .append("\n");
        }

        return builder.append("```").toString();
    }

    @Override
    public Iterator<Setting> iterator(){
        return this.values.keySet().iterator();
    }
}
