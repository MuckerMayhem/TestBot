package bot.feature.function;

import bot.feature.BotFeature;
import bot.locale.Locale;
import bot.locale.LocaleHandler;

public abstract class BotFunction extends BotFeature{
    
    public BotFunction(String name){
        super(name);
    }

    /**
     * Gets the name of this Function in the specified locale
     * @return The localized name of this feature
     * @param locale Locale to localize the name to
     */
    @Override
    public String getName(Locale locale){
        return LocaleHandler.get(locale).getLocalizedName(this);
    }

    @Override
    public String getTypeName(){
        return "Function";
    }
    
    /**
     * Gets the description of this Function in the specified locale
     * @return The localized description of this function
     * @param locale Locale to localize the name to
     */
    @Override
    public String getDescription(Locale locale){
        return LocaleHandler.get(locale).getLocalizedDescription(this);
    }
}
