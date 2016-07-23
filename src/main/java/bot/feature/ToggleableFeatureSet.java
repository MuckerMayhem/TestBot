package bot.feature;

public class ToggleableFeatureSet extends FeatureSet implements ToggleableBotFeature{

    public ToggleableFeatureSet(String name, BotFeature... features){
        super(name, features);
    }
    
    @Override
    public boolean defaultEnabled(){
        return false;
    }
}
