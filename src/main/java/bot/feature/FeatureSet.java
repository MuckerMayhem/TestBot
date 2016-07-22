package bot.feature;

import bot.DiscordBot;
import bot.locale.Locale;
import bot.locale.LocaleHandler;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;

/**
 * Represents a set of related {@link BotFeature}s. When a FeatureSet is enabled on a bot, its<br>
 * contained features are also enabled for the same bot. Features in a FeatureSet can not be<br>
 * enabled/disabled individually, they are always enables/disabled as a set. If your goal is to<br>
 * keep a list of BotFeatures, use a {@link List}. instead.<br>
 * <br>
 * <b>Registering a FeatureSet:</b><br>
 * Registering a FeatureSet unregisters all of its contained {@link BotFeature}s, effectively<br>
 * taking control of their enabling and disabling—they can only be enabled or disabled when the set is.
 */
public class FeatureSet extends BotFeature implements Iterable<BotFeature>{

    private final List<BotFeature> features;
    
    public FeatureSet(String name, BotFeature... features){
        super(name);
        this.features = Arrays.asList(features);
    }

    @Override
    public String getName(Locale locale){
        return LocaleHandler.get(locale).getLocalizedName(this);
    }
    
    @Override
    public String getTypeName(){
        return "Collection";
    }

    @Override
    public String getDescription(Locale locale){
        return LocaleHandler.get(locale).getLocalizedDescription(this);
    }

    @Override
    public void onRegister(){
        forEach(BotFeature.getAllRegisteredFeatures()::remove);
    }

    @Override
    public void onEnable(DiscordBot bot){
        bot.enableFeatures(this.features);
    }

    @Override
    public void onDisable(DiscordBot bot){
        bot.disableFeatures(this.features);
    }
    
    @Override
    public  Iterator<BotFeature> iterator(){
        return this.features.iterator();
    }

    @Override
    public void forEach(Consumer<? super BotFeature> action){
        this.features.forEach(action);
    }
}
