package bot.settings;

public class StringSetting extends Setting{

    private String[] validValues;

    public StringSetting(String name, String description, String defaultValue){
        super(name, description, defaultValue);
    }

    @Override
    public String getDefaultValue(){
        return (String) this.defaultValue;
    }

    @Override
    public String parse(String value){
        if(validValues == null) return value;
        else{
            for(String s : this.validValues){
                if(value.equalsIgnoreCase(s)) return s;
            }
        }
        return null;
    }

    public void setValidValues(String... values){
        this.validValues = values;
    }

    @Override
    public String toString(){
        return this.name + ": " + this.description + " (Default: '" + this.defaultValue + "')";
    }
}
