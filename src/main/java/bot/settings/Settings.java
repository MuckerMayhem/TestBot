package bot.settings;

import bot.locale.Locale;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;
import java.util.stream.Collectors;

/**
 * Represents a set of {@link bot.settings.Setting}s and their values
 */
public class Settings implements Iterable<Setting>{

    private static HashMap<SettingsHandler, Settings> defaults = new HashMap<>();

    private TreeMap<Setting, Object> values = new TreeMap<>();

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

    public List<Setting> getSettings(){
        return this.values.keySet().stream().collect(Collectors.toList());
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

    public String toString(Locale locale){

        int leftPadding = 0;
        int rightPadding = 0;

        for(Setting s : this){
            int left = s.getName(locale).length();
            if(left > leftPadding) leftPadding = left;

            int right = s.getValueAsString(get(s)).length();
            if(right > rightPadding) rightPadding = right;
        }

        StringBuilder builder = new StringBuilder("```");

        int index = 1;
        for(Setting s : getSettings()){
            String value = s.getValueAsString(get(s));

            builder.append(index + ". " + s.getName(locale))
                    .append(": ")
                    .append(pad(value, leftPadding - s.getName(locale).length(), rightPadding - value.length()))
                    .append(" | ")
                    .append(s.getDescription(locale))
                    .append(" (Default: ")
                    .append(s.getValueAsString(s.getDefaultValue()))
                    .append(")")
                    .append("\n");

            index++;
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

    private static String formatForType(Setting setting, String value){
        if(setting instanceof StringSetting)
            return '\'' + value + '\'';
        else if(setting instanceof ArraySetting)
            return '[' + String.join(", ", value.split(",")) + ']';
        else return value;
    }

    @Override
    public Iterator<Setting> iterator(){
        return this.values.keySet().iterator();
    }
}
