package bot.settings;

import java.util.Arrays;

public class ArraySetting extends Setting{

    public ArraySetting(String name, Object defaultValue){
        super(name, defaultValue);
    }

    @Override
    public String[] getDefaultValue(){
        return (String[]) this.defaultValue;
    }

    @Override
    public String[] parse(String value){
        return value.split(",");
    }

    @Override
    public String getValueAsString(Object value){
        return String.join(",", Arrays.asList((String[]) value));
    }
}
