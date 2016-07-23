package bot.feature.command;

import bot.DiscordBot;
import bot.locale.Message;
import bot.locale.MessageBuilder;
import sx.blah.discord.handle.obj.IMessage;

import java.util.Random;

//TODO: Meme it up
public class CommandMeme extends BotCommand{

    public CommandMeme(){
        super("meme");
    }

    @Override
    public void onRegister() {}

    @Override
    public void onEnable(DiscordBot bot) {}

    @Override
    public void onDisable(DiscordBot bot) {}
    
    @Override
    protected void onExecute(DiscordBot bot, IMessage message, String[] args){
        MessageBuilder builder = new MessageBuilder(bot.getLocale());

        Random random = new Random();

        if(random.nextInt(2) == 0){
            bot.respond(message.getAuthor().mention() + " " + builder.buildMessage(Message.CMD_MEME_SEND) + Meme.values()[random.nextInt(Meme.values().length)].getImgUrl());
        }
        else{
            bot.respond(message.getAuthor().mention() + " " + builder.buildMessage(Message.CMD_MEME_DANK) + "\n" + DankMeme.values()[random.nextInt(DankMeme.values().length)].getImgUrl());
        }
    }

    public enum Meme{
        DATBOI("https://66.media.tumblr.com/3446c4a8a6f25de2dded665b7d731c20/tumblr_o6mg36XCOQ1tgbyedo1_540.gif");

        private final String imgUrl;

        Meme(String imgUrl){
           this.imgUrl = imgUrl;
        }

        public String getImgUrl(){
            return this.imgUrl;
        }
    }

    public enum DankMeme{
        DATBOI("https://66.media.tumblr.com/3446c4a8a6f25de2dded665b7d731c20/tumblr_o6mg36XCOQ1tgbyedo1_540.gif"),
        SPICYBOI("http://i0.kym-cdn.com/photos/images/original/001/148/668/49a.png");

        private final String imgUrl;

        DankMeme(String imgUrl){
            this.imgUrl = imgUrl;
        }

        public String getImgUrl(){
            return this.imgUrl;
        }
    }
}
