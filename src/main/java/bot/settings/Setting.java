package bot.settings;

import bot.locale.Locale;
import bot.locale.LocaleHandler;

public abstract class Setting implements Comparable<Setting>{

    protected String name;
    protected Object defaultValue;

    public Setting(String name, Object defaultValue){
        this.name = name;
        this.defaultValue = defaultValue;
    }

    public abstract Object getDefaultValue();

    public abstract Object parse(String value);

    public String getName(){
        return this.name;
    }

    public String getName(Locale locale){
        return LocaleHandler.get(locale).getLocalizedName(this);
    }

    public String getDescription(Locale locale){
        return LocaleHandler.get(locale).getLocalizedDescription(this);
    }

    public String getValueAsString(Object value){
        return value.toString();
    }

    @Override
    public int compareTo(Setting other){
        return this.name.compareTo(other.name);
    }
}
