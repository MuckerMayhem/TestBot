package bot.game;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class GameScramble extends Game{

    private int difficulty;

    private int lives;
    private int correct;

    private static String[][] words = {
            {"apple", "cancer", "earth", "hotel", "road"},//Easy
            {"always", "computer", "ocean", "rifle", "under"},//Medium
            {"helicopter", "mechanic", "telephone"},//Hard
            {"crocodile", "underwater", "pneumonia", "datboi"},//Very Hard
            {"conditioning", "distasteful", "hitchhikers"}//Impossible
    };

    private static String[] intros = {
            "Ready to move on to something a little harder? (Reached level 2)",
            "Was that too easy? Well they're about to get harder! (Reached level 3)",
            "Excellent! Think you can handle these ones? (Reached level 4)",
            "You're amazing! But these next words are the hardest of the hard! (Reached level 5)"
    };

    public GameScramble(GameBot bot, int lives){
        super(bot);
        this.lives = lives;
    }

    public GameScramble(GameBot bot){
        super(bot);
        this.lives = 3;
    }

    public static String getInstructions(){
        return "You are given a scrambled word that you\n" +
                "must unscramble to continue. The game has\n" +
                "five difficulty levels. Guess enough words\n" +
                "in one level to move on to the next.";
    }

    @Override
    public boolean isMultiplayer(){
        return false;
    }

    @Override
    public boolean isReplayable(){
        return true;
    }

    @Override
    public void play(){
        this.difficulty = 1;

        Random random = new Random();

        int correct = 0;

        int lives = this.lives;
        while(lives > 0){
            if(correct > 3){
                this.bot.say(intros[this.difficulty - 1]);
                this.difficulty++;
                correct = 0;
            }
            if(this.difficulty > 5){
                win();
                return;
            }

            String[] possibilities = words[this.difficulty - 1];

            String word = possibilities[random.nextInt(possibilities.length)];
            List<String> letters = Arrays.asList(word.split(""));
            Collections.shuffle(letters);
            StringBuilder builder = new StringBuilder();
            letters.forEach(builder::append);
            String scrambled = builder.toString();

            this.bot.say("Unscramble this word: " + scrambled);

            if(this.bot.nextLine().equalsIgnoreCase(word)){
                correct++;
                this.bot.say("Correct! (" + correct + "/" + 4 + ")");
            }
            else{
                lives--;
                this.bot.say("Wrong! The correct answer was \"" + word + "\"! (" + lives + " " + (lives == 1 ? "life" : "lives") + " remaining)");
            }
        }

        lose();
    }

    @Override
    public void quit(){

    }

    private void win(){
        this.bot.say("\nCongratulations! You made it through every difficulty level!");
    }

    private void lose(){
        this.bot.say("\nGame over! You ran out of guesses!\nYou reached level: " + this.difficulty);
    }
}
