package bot.feature;

import bot.DiscordBot;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;

public class FeatureSet extends BotFeature implements Iterable<BotFeature>{

    private final List<BotFeature> features;
    
    public FeatureSet(BotFeature... features){
        this.features = Arrays.asList(features);
    }

    @Override
    public void onRegister(){
        forEach(f -> BotFeature.registerFeature());
    }

    @Override
    public void onEnable(DiscordBot bot){
        forEach(bot::enableFeature);
    }

    @Override
    public void onDisable(DiscordBot bot){
        forEach(bot::disableFeature);
    }
    
    @Override
    public Iterator<BotFeature> iterator(){
        return this.features.iterator();
    }

    @Override
    public void forEach(Consumer<? super BotFeature> action){
        this.features.forEach(action);
    }
}
