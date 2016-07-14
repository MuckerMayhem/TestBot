package bot.feature.commands;

import bot.DiscordBot;
import bot.locale.MessageBuilder;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.MissingPermissionsException;
import sx.blah.discord.util.RateLimitException;

public class CommandGame extends BotCommand{

    @Override
    public void onRegister(){

    }

    @Override
    public void onEnable(DiscordBot bot){

    }

    @Override
    public void onDisable(DiscordBot bot){

    }

    @Override
    protected void onExecute(DiscordBot bot, IMessage message, String[] args) throws RateLimitException, DiscordException, MissingPermissionsException{
        MessageBuilder builder = new MessageBuilder(bot.getLocale());

        /*
        if(message.getChannel() != gameBot.getHome()){
            gameBot.info(builder.buildMessage(Message.CMD_GAME_NOT_HERE, gameBot.getHome().getName()));
            return;
        }

        if(args.length == 0){
            gameBot.say(builder.buildMessage(Message.CMD_GAME_CHOOSE_1) + "\n" +
                    "1. Tic-tac-toe");
            gameBot.say(builder.buildMessage(Message.CMD_GAME_CHOOSE_2) + "\n" +
                        builder.buildMessage(Message.CMD_GAME_CHOOSE_3));

            boolean played = true;
            switch(gameBot.nextLine()){
                case "1":
                    gameBot.say(builder.buildMessage(Message.CMD_GAME_START));
                    gameBot.playGame(new TicTacToe(gameBot, 3));
                    break;
                case "1 instructions":
                    gameBot.say("Here are the instructions for Tic-tac-toe:\n" + TicTacToe.getInstructions());
                    played = false;
                    break;
                case "2":
                    gameBot.say("You found the secret game!");
                    gameBot.playGame(new GameScramble(gameBot));
                    break;
                case "2 instructions":
                    gameBot.say("Here are the instructions for Scramble:\n" + GameScramble.getInstructions());
                    played = false;
                    break;
            }

            if(played)
                gameBot.say(builder.buildMessage(Message.CMD_GAME_THANKS));
        }
        */
    }
}
