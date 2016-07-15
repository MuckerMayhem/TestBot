package bot.locale;

public class MessageBuilder{

    private Message message;
    private Locale locale;
    private String[] args;

    public MessageBuilder(Locale locale){
        this.locale = locale;
    }

    public MessageBuilder withLocale(Locale locale){
        this.locale = locale;
        return this;
    }

    public String buildMessage(Message message, Object... args){
        String localized = message.getLocalizedMessage(this.locale);

        int index = 1;
        for(Object o : args){
            localized = localized.replaceAll("\\$" + index, o.toString());
            index++;
        }

        return localized;
    }

    public String buildMessage(Message message){
        return message.getLocalizedMessage(this.locale);
    }
}
