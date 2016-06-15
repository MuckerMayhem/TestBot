package bot.behavior;

public enum Mood{

    SAD(0.0F, 0.0F),
    HAPPY(1.0F, 0.0F),
    ANNOYED(1.0F, 1.0F),
    OFFENDED(0.0F, 1.0F);

    private float happiness;
    private float annoyance;

    Mood(float happiness, float annoyance){
        this.happiness = happiness;
        this.annoyance = annoyance;
    }

    public static Mood findClosestMood(float happiness, float annoyance){
        float minDiff = 2.0F;
        Mood closest = Mood.ANNOYED;

        for(Mood m : values()){
            float sum = Math.abs(m.happiness - happiness) + Math.abs(m.annoyance - annoyance);
            if(sum < minDiff){
                minDiff = sum;
                closest = m;
            }
        }
        return closest;
    }

    public static Mood findClosestMood(BotAttitude attitude){
        return findClosestMood(attitude.getHappiness(), attitude.getAnnoyance());
    }

    public float getHappiness(){
        return this.happiness;
    }

    public float getAnnoyance(){
        return this.annoyance;
    }
}
