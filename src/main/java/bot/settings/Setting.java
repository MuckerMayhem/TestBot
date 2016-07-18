package bot.settings;

import bot.locale.Locale;
import bot.locale.LocaleHandler;

import javax.annotation.Nonnull;

public abstract class Setting implements Comparable<Setting>{

    protected final String name;
    protected final Object defaultValue;
    protected final boolean requiresRestart;

    public Setting(String name, Object defaultValue, boolean requiresRestart){
        this.name = name;
        this.defaultValue = defaultValue;
        this.requiresRestart = requiresRestart;
    }
    
    public Setting(String name, Object defaultValue){
        this(name, defaultValue, false);
    }

    public abstract Object getDefaultValue();

    public abstract Object parse(String value);

    public String getName(){
        return this.name;
    }
    
    public boolean requiresRestart(){
        return this.requiresRestart;
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
    public int compareTo(@Nonnull Setting other){
        return this.name.compareTo(other.name);
    }
}
