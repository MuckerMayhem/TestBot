package bot.event;

import bot.DiscordBot;
import logging.BotLogger.Level;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class BotEventDispatcher{
    
    private final List<BotEventListener> listeners = new ArrayList<>();
    
    private final DiscordBot bot;
    
    public BotEventDispatcher(DiscordBot bot){
        this.bot = bot;
    }

    public DiscordBot getBot(){
        return bot;
    }

    public void registerListener(BotEventListener listener){
        this.listeners.add(listener);
    }
    
    public void unregisterListener(BotEventListener listener){
        this.listeners.remove(listener);
    }
    
    public void dispatchEvent(BotEvent event){
        for(BotEventListener listener : this.listeners){
            for(Method m : listener.getClass().getMethods()){
                if(m.isAnnotationPresent(BotEventSubscriber.class)){
                    for(Class<?> c : m.getParameterTypes()){
                        if(c == event.getClass()){
                            try{
                                m.invoke(listener, event);
                                return;
                            }
                            catch(IllegalAccessException | InvocationTargetException e){
                                this.bot.logf(Level.ERROR, "Could not dispatch event '%s': %s", event.getClass().getSimpleName(), e.getClass().getSimpleName());
                            }
                        }
                    }
                }
            }
        }
    }
}
