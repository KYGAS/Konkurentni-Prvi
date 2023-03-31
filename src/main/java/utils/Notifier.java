package utils;

public class Notifier {
    public static void notifyObjectStopped(Object object){
        System.out.println("Stopped " + object.getClass().getName() );
    }
}
