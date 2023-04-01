package JobDispatcher;

import JobQueue.*;
import Jobs.Pools.File;
import Jobs.Pools.Web;
import utils.Notifier;

import static JobQueue.JobQueue.jobBlockingQueue;

public class JobDispatcher implements Runnable {

    public volatile boolean isRunning;

    @Override
    public void run() {

        innitFilePool();
        innitWebPool();

        isRunning = true;
        while (isRunning) {
            try {
                // Wait for a new job to appear in the queue
                Job job = jobBlockingQueue.take();

                switch (job.getClass().getSimpleName()) {
                    case "FileJob" -> {
                        System.out.println("New File Job!");
                    }
                    case "WebJob" -> {
                        System.out.println("New Web Job!");
                        Web.queueWebJob(((WebJob) job));
                    }
                    default -> {
                        System.out.println("Invalid Job");
                    }
                }
            }
            catch (InterruptedException e) {
                Notifier.notifyForceStop(this);
                return;
            }
        }
        Notifier.notifyObjectStopped(this);
    }

    public void stop(){
        isRunning = false;
    }

    private void innitWebPool(){
        new Web();
    }

    private void innitFilePool(){
        new File();
    }

    public JobDispatcher() {

    }
}
