package bot.locale;

import util.Util;

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

    /**
     * Builds a message in this builder's locale. Tokens in the localized message string <i>(Such as $1, $2)</i> are replaced with<br>
     * the arguments in their respective order. {@link Object#toString()} is used on each argument when replacing.
     * @param message {@link Message} to build
     * @param args Objects replacing specific tokens in the string
     * @return The built message
     */
    public String buildMessage(Message message, Object... args){
        String localized = Util.realNewLines(message.getLocalizedMessage(this.locale));

        int index = 1;
        for(Object o : args){
            localized = localized.replace("$" + index, o.toString());
            index++;
        }

        return localized;
    }

    /**
     * Builds a message in this builder's locale
     * @return The built message
     */
    public String buildMessage(Message message){
        return Util.realNewLines(message.getLocalizedMessage(this.locale));
    }
}
