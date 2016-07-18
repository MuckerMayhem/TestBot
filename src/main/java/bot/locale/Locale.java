package bot.locale;

public enum Locale{

    ENGLISH("en", "English (US)"),
    FRENCH("fr", "French"),
    PIRATE("pirate", "Pirate Speak");

    private final String code;
    private final String name;

    Locale(String code, String name){
        this.code = code;
        this.name = name;
    }

    public static Locale getFromCode(String code){
        for(Locale l : values()){
            if(l.code.equals(code)) return l;
        }
        return null;
    }

    public String getCode(){
        return this.code;
    }

    public String getName(){
        return this.name;
    }
}
