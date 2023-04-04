package Jobs.Pools;

import JobQueue.FileJob;
import utils.Notifier;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class File {

    public static ExecutorService executorService;
    public File() {
        executorService = Executors.newCachedThreadPool();
    }

    public static void queueFileJob(FileJob fileJob){
        executorService.submit(
                new Jobs.Scanners.File(
                        fileJob
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