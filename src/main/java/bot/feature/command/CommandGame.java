package bot.feature.command;

import bot.DiscordBot;
import bot.locale.Message;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IMessage;

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
    protected void onExecute(DiscordBot bot, IMessage message, String[] args){
        IChannel home = bot.getHome();
        
        if(home == null){
            bot.info(buildMessage(Message.CMD_GAME_NO_HOME), true);
            return;
        }
        else if(message.getChannel() != bot.getHome()){
            bot.info(buildMessage(Message.CMD_GAME_NOT_HERE, bot.getHome().getName()), true);
            return;
        }

        /*
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
