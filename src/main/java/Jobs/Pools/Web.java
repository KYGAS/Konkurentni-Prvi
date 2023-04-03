package Jobs.Pools;

import JobQueue.JobQueue;
import JobQueue.WebJob;
import utils.Notifier;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Web {

    public static ExecutorService executorService;
    public Web() {
        executorService = Executors.newCachedThreadPool();
    }

    public static void queueWebJob(WebJob webJob){
        executorService.submit(
                new Jobs.Scanners.Web(
                        webJob
                )
        );
    }

    public void stop(){
        try {
            executorService.shutdown();
            Notifier.notifyObjectStopped(new Web());
        }
        catch (Exception ignored){
            Notifier.notifyObjectStopped(new Web());
        }
    }


}