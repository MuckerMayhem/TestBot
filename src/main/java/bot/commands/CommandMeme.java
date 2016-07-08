package bot.commands;

import bot.DiscordBot;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.MissingPermissionsException;
import sx.blah.discord.util.RateLimitException;

import java.util.Random;

public class CommandMeme extends Command{

    @Override
    protected void onRegister(){

    }

    @Override
    protected void onExecute(DiscordBot bot, IMessage message, String[] args) throws RateLimitException, DiscordException, MissingPermissionsException{
        Random random = new Random();

        if(random.nextInt(2) == 0){
            bot.respond(message.getAuthor().mention() +" " + Meme.values()[random.nextInt(Meme.values().length)].getImgUrl());
        }
        else{
            bot.respond(message.getAuthor().mention() + " Here's a dank meme for you m8:\n" + DankMeme.values()[random.nextInt(DankMeme.values().length)].getImgUrl());
        }
    }

    public enum Meme{
        MEME("https://66.media.tumblr.com/3446c4a8a6f25de2dded665b7d731c20/tumblr_o6mg36XCOQ1tgbyedo1_540.gif");

        private String imgUrl;

        Meme(String imgUrl){
           this.imgUrl = imgUrl;
        }

        public String getImgUrl(){
            return this.imgUrl;
        }
    }

    public enum DankMeme{
        MEME("https://66.media.tumblr.com/3446c4a8a6f25de2dded665b7d731c20/tumblr_o6mg36XCOQ1tgbyedo1_540.gif");

        private String imgUrl;

        DankMeme(String imgUrl){
            this.imgUrl = imgUrl;
        }

        public String getImgUrl(){
            return this.imgUrl;
        }
    }
}
