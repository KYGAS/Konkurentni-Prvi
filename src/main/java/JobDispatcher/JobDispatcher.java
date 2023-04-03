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
                        Web.queueWebJob(((WebJob) job));
                    }
                    case "AppStop" -> {
                        System.out.println("Received stop job!");
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
        jobBlockingQueue.clear();
        jobBlockingQueue.offer(new AppStop());
    }

    public Web innitWebPool() { return new Web(); }

    public File innitFilePool(){
        return new File();
    }

    public Jobs.Scanners.Web getWebScanner(){return new Jobs.Scanners.Web();}
    public Jobs.Scanners.File getFileScanner(){return new Jobs.Scanners.File();}

    public JobDispatcher() {

    }
}
