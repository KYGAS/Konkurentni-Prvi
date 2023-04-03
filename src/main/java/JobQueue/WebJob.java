package JobQueue;

import Jobs.Scanners.Web;

public class WebJob implements Job{

    public String path;
    public Integer hops;

    public Web parentJob;

    public WebJob(String path, Integer hops, Web parentJob) {
        this.path = path;
        this.hops = hops;
        this.parentJob = parentJob;
    }

    public WebJob() {
    }
}
