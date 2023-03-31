package JobDispatcher;

import JobQueue.*;
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
                        System.out.println("New Web Job!");
                    }
                    default -> {
                        System.out.println("Invalid Job");
                    }
                }
            }
            catch (InterruptedException e) {
                System.out.println("JobDispatcherThread was interrupted: " + e.getMessage());
                return;
            }
        }
        Notifier.notifyObjectStopped(this);
    }

    public void stop(){
        isRunning = false;
    }

    public JobDispatcher() {

    }
}
