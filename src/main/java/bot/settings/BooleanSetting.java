package bot.settings;

public class BooleanSetting extends Setting{

    public BooleanSetting(String name, String description, boolean defaultValue){
        super(name, description, defaultValue);
    }

    @Override
    public Boolean getDefaultValue(){
        return (Boolean) this.defaultValue;
    }

    @Override
    public Boolean parse(String value){
        if(!value.equalsIgnoreCase("true") && !value.equalsIgnoreCase("false")) return null;
        return Boolean.parseBoolean(value);
    }
}