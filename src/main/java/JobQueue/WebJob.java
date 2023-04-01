package JobQueue;

public class WebJob implements Job{

    public String path;
    public Integer hops;

    public WebJob(String path, Integer hops) {
        this.path = path;
        this.hops = hops;
    }

    public WebJob() {
    }
}
