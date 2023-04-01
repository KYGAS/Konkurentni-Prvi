package utils;

public class Notifier {
    public static void notifyObjectStopped(Object object){
        System.out.println("Stopped " + object.getClass().getName() );
    }

    public static void notifyForceStop(Object object){
        System.out.println("Thread < " + object.getClass().getName() + " > has forcefully stopped!");
    }
}
