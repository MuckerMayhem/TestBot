package bot.locale;

public enum Locale{

    ENGLISH("en", "English (US)"),
    FRENCH("fr", "French"),
    PIRATE("pirate", "Pirate Speak");

    private String code;
    private String name;

    Locale(String code, String name){
        this.code = code;
        this.name = name;
    }

    public String getCode(){
        return this.code;
    }

    public String getName(){
        return this.name;
    }
}
