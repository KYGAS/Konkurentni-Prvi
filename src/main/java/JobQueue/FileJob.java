package JobQueue;

import Jobs.Scanners.Web;

public class FileJob implements Job{

    public String path;

    public Web parentJob;

    public FileJob(String path) {
        this.path = path;
    }

    public FileJob() {
    }
}
