package bot.settings;

public abstract class Setting{

    protected String name;
    protected String description;
    protected Object defaultValue;

    public Setting(String name, String description, Object defaultValue){
        this.name = name;
        this.description = description;
        this.defaultValue = defaultValue;
    }

    public abstract Object getDefaultValue();

    public abstract Object parse(String value);

    public String getName(){
        return this.name;
    }

    public String getDescription(){
        return this.description;
    }

    @Override
    public String toString(){
        return this.name + ": " + this.description + " (Default: " + this.defaultValue + ")";
    }
}
