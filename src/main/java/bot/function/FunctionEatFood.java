package bot.function;

import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.MessageReceivedEvent;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.MissingPermissionsException;
import sx.blah.discord.util.RateLimitException;

public class FunctionEatFood extends BotFunction{

    @Override
    public void activate(){
        this.bot.getClient().getDispatcher().registerListener(this);
    }

    @Override
    public void deactivate(){
        this.bot.getClient().getDispatcher().unregisterListener(this);
    }

    @EventSubscriber
    public void onMessageReceived(MessageReceivedEvent event){
        String content = event.getMessage().getContent().replaceAll(" ", "");
        for(Food f : Food.values()){
            if(content.equalsIgnoreCase(f.getUnicode())){
                try{
                    event.getMessage().delete();
                    bot.say(event.getMessage().getChannel(), f.getEatMessage());
                }
                catch(MissingPermissionsException | RateLimitException | DiscordException e){
                    System.err.print("Bot failed to eat :(\n" + e.getMessage());
                }
                break;
            }
        }
    }

    public enum Food{
        GREEN_APPLE(false, "apple", "\uD83C\uDF4F", ":kissing:"),
        APPLE(false, "apple", "\uD83C\uDF4E", ":yum:"),
        PEAR(false, "pear", "\uD83C\uDF50", ":yum:"),
        TANGERINE(false, "tangerine", "\uD83C\uDF4A", ":kissing_smiling_eyes:"),
        LEMON(false, "lemon", "\uD83C\uDF4B", ":confounded:"),
        BANANA(false, "banana", "\uD83C\uDF4C", ":grinning:"),
        WATERMELON(false, "watermelon", "\uD83C\uDF49", ":yum:"),
        GRAPES(false, "grapes", "\uD83C\uDF47", ":grinning:"),
        STRAWBERRY(false, "strawberry", "\uD83C\uDF53", ":heart_eyes:"),
        MELON(false, "melon", "\uD83C\uDF48", ":yum:"),
        CHERRIES(false, "cherries", "\uD83C\uDF52", ":grimacing:"),
        PEACH(false, "peach", "\uD83C\uDF51", ":yum:"),
        PINEAPPLE(false, "pineapple", "\uD83C\uDF4D", ":open_mouth:"),
        TOMATO(false, "tomato", "\uD83C\uDF45", ":yum:"),
        EGGPLANT(false, "eggplant", "\uD83C\uDF46", ":upside_down:"),
        HOT_PEPPER(false, "hot pepper", "\uD83C\uDF36", ":triumph:"),
//        CORN,
//        SWEET_POTATO,
//        HONEY_POT,
//        BREAD,
//        CHEESE,
//        POULTRY_LEG,
//        MEAT_ON_BONE,
//        FRIED_SHRIMP,
//        EGG,
//        HAMBURGER,
//        FRIES,
//        HOTDOG,
//        PIZZA,
//        SPAGHETTI,
//        TACO,
//        BURRITO,
//        RAMEN,
//        STEW,
//        FISH_CAKE,
//        SUSHI,
//        BENTO,
//        CURRY,
//        RICE_BALL,
//        RICE,
//        RICE_CRACKER,
//        ODEN,
//        DANGO,
//        SHAVED_ICE,
//        ICE_CREAM,
//        CAKE,
//        BIRTHDAY,
//        CUSTARD,
//        CANDY,
//        LOLLIPOP,
//        CHOCOLATE_BAR,
//        POPCORN,
//        DOUGHNUT,
//        COOKIE,
//        BEER,
//        BEERS,
//        WINE_GLASS,
//        COCKTAIL,
//        TROPICAL_DRINK,
//        CHAMPAGNE,
//        SAKE,
//        TEA,
        COFFEE(true, "coffee", "\u2615", ":hugging:"),
        BABY_BOTTLE(false, "baby bottle", "\uD83C\uDF7C", ":baby::skin-tone-1:"),
        FORK_AND_KNIFE(false, "utensils", "\uD83C\uDF74", ":astonished:"),
        DOG(false, "dog", "\uD83D\uDC36", ":thinking:"),
        POOP(false, "poop", "\uD83D\uDCA9", ":scream:");

        private boolean drink;
        private String name;
        private String unicode;
        private String expression;

        Food(boolean drink, String name, String unicode, String expression){
            this.drink = drink;
            this.name = name;
            this.unicode = unicode;
            this.expression = expression;
        }

        public boolean isDrink(){
            return this.drink;
        }

        public String getUnicode(){
            return this.unicode;
        }

        public String getExpression(){
            return this.expression;
        }

        public String getEatMessage(){
            return "*" + (this.drink ? "drinks " : "eats ") + this.name + " " + this.expression + "*";
        }
    }
}
