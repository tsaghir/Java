package org.foi.nwtis.tsaghir.pomagaci;

/**
 * Singleton klasa za pomoć s dretvama
 * @author tsaghir
 */
public class ThreadHelper {
    private static ThreadHelper instance = null;
    private ThreadHelper(){
    }
    
    public static ThreadHelper getInstance(){
        if(instance == null){
            instance = new ThreadHelper();
        }
        return instance;
    }
    
    public static Thread getThreadByName(String threadName) {
    for (Thread t : Thread.getAllStackTraces().keySet()) {
        if (t.getName().equals(threadName)) return t;
    }
    return null;
}
}
