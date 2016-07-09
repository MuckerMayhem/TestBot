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
        if(!defaults.containsKey(handler))
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

    public boolean hasValueFor(Setting s){
        return this.values.containsKey(s);
    }

    @Override
    public String toString(){

        int leftPadding = 0;
        int rightPadding = 0;

        for(Setting s : this){
            int left = s.getName().length();
            if(left > leftPadding) leftPadding = left;

            int right = s.getValueAsString(get(s)).length();
            if(right > rightPadding) rightPadding = right;
        }

        StringBuilder builder = new StringBuilder("```");

        for(Setting s : this){
            String value = s.getValueAsString(get(s));

            builder.append(s.getName())
                    .append(": ")
                    .append(pad(value, leftPadding - s.getName().length(), rightPadding - value.length()))
                    .append(" | ")
                    .append(s.getDescription())
                    .append(" (Default: ")
                    .append(s.getValueAsString(s.getDefaultValue()))
                    .append(")")
                    .append("\n");
        }

        return builder.append("```").toString();
    }

    private static String pad(String string, int leftTimes, int rightTimes){
        StringBuilder builder = new StringBuilder();

        for(int i = 0;i < leftTimes;i++){
            builder.append(" ");
        }
        builder.append(string);
        for(int i = 0;i < rightTimes;i++){
            builder.append(" ");
        }

        return builder.toString();
    }

    @Override
    public Iterator<Setting> iterator(){
        return this.values.keySet().iterator();
    }
}
