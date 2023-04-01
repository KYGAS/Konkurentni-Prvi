package JobQueue;

public class FileJob implements Job{

    public String path;

    public FileJob(String path) {
        this.path = path;
    }

    public FileJob() {
    }
}
