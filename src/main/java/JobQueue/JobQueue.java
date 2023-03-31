package JobQueue;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class JobQueue {
    public static BlockingQueue<Job> jobBlockingQueue = new LinkedBlockingQueue<>();

    public static void addJob(Job job){
        jobBlockingQueue.add(job);
    }
}