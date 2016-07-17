package logging;

/**
 * Wrapper class for objects that have multiple ways of being displayed in the log
 */
public class LogWrapper{
    
    private Object object;
    
    public LogWrapper(Object object){
        this.object = object;
    }
    
    public Object getObject(){
        return this.object;
    }
}
